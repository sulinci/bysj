<template>
  <div>
    <div style="display: flex;justify-content: space-between">
      <div>
        <el-input prefix-icon="el-icon-search" placeholder="请输入员工名进行搜索..." v-model="empName" clearable
          style="width: 300px;margin-right: 10px"></el-input>
        <el-button type="primary" icon="el-icon-search" @click="initEmps">搜索
        </el-button>
      </div>
      <div>
        <el-upload action="/personnel/emp/import" :before-upload="beforeUpload" :headers="headers" :on-success="onSuccess"
          :on-error="onError" :disabled="importDataDisabled" style="display: inline-flex;margin-right: 8px"
          :show-file-list="false">
          <el-button :disabled="importDataDisabled" type="success" :icon="importDataBtnIcon">
            {{ importDataBtnText }}
          </el-button>
        </el-upload>
        <el-button type="warning" icon="el-icon-download" @click="exportData">
          导出数据
        </el-button>
        <el-button type="success" icon="el-icon-plus" @click="showAddEmp">
          添加员工
        </el-button>
      </div>
    </div>
    <div style="margin-top: 10px">
      <el-table :data="emps" style="width: 100%" v-loading="loading" element-loading-text="拼命加载中..."
        element-loading-spinner="el-icon-loading" element-loading-background="rgba(0, 0, 0, 0.8)" >
        <el-table-column fixed prop="workId" label="员工工号" width="150">
        </el-table-column>
        <el-table-column prop="name" label="姓名" width="120">
        </el-table-column>
        <el-table-column prop="gender" label="性别" width="120">
          <template slot-scope="scope">
            {{ scope.row.gender == 0 ? '男' : '女' }}
          </template>

        </el-table-column>
        <el-table-column prop="birthday" label="出生日期" width="150">
        </el-table-column>
        <el-table-column prop="email" label="邮箱" width="200">
        </el-table-column>
        <el-table-column prop="phone" label="手机号" width="150">
        </el-table-column>
        <el-table-column prop="depName" label="所属部门" width="120">
        </el-table-column>
        <el-table-column prop="posName" label="职位" width="150">
        </el-table-column>
        <el-table-column fixed="right" label="操作" width="100">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="handleEditEmp(scope.row)">编辑</el-button>
            <el-button @click="handleDeleteEmp(scope.row.id)" type="text" size="small">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div>
      <el-dialog :title="title" :visible.sync="dialogVisible" width="30%" :before-close="handleClose">
        <el-form ref="empFrom" :model="emp" label-width="100px" :rules="rules">
          <el-form-item label="员工姓名:" prop="name">
            <el-input v-model="emp.name" placeholder="请输入员工姓名"></el-input>
          </el-form-item>
          <el-form-item label="性别:" prop="gender">
            <div>
              <el-radio-group v-model="emp.gender">
                <el-radio label="0">男</el-radio>
                <el-radio label="1">女</el-radio>
              </el-radio-group>
            </div>
          </el-form-item>
          <el-form-item label="出生日期:" prop="birthday">
            <el-date-picker v-model="emp.birthday" type="date" placeholder="选择日期" style="width:100%">
            </el-date-picker>
          </el-form-item>
          <el-form-item label="邮箱:" prop="email">
            <el-input v-model="emp.email" placeholder="请输入邮箱"></el-input>
          </el-form-item>
          <el-form-item label="手机号:" prop="phone">
            <el-input v-model="emp.phone" placeholder="请输入手机号"></el-input>
          </el-form-item>
          <el-form-item label="部门:" prop="depId">
            <el-select v-model="emp.depId" placeholder="请选择部门" :disabled="disabled" style="width:100%">
              <el-option-group v-for="dep in departments" :key="dep.id" :label="dep.name">
                <el-option v-for="item in dep.children" :key="item.id" :label="item.name" :value="item.id"
                  :disabled="item.enabled == 1 ? true : false">
                </el-option>
              </el-option-group>
            </el-select>
          </el-form-item>
          <el-form-item label="职位:" prop="posId">
            <el-select v-model="emp.posId" placeholder="请选择职位" :disabled="disabled" style="width:100%">
              <el-option v-for="item in positions" :key="item.id" :label="item.name" :value="item.id"></el-option>
            </el-select>
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button @click="handleClose">取 消</el-button>
          <el-button type="primary" @click="doAddEmp">确 定</el-button>
        </span>
      </el-dialog>
    </div>
    <div style="display: flex;justify-content: flex-end">
      <el-pagination background @current-change="currentChange" @size-change="sizeChange" :page-sizes="[5, 10, 30, 50]"
        :page-size="size" layout="sizes,prev,pager,next,jumper,->,total,slot" :total="total"></el-pagination>
    </div>
  </div>
</template>

<script>
export default {
  name: "PerEmp",
  data() {
    return {
      emps: [],
      loading: false,
      currentPage: 1,
      size: 5,
      total: 0,
      empName: '',
      emp: {
        name: ''
      },
      headers: {
        Authorization: window.sessionStorage.getItem('tokenStr')
      },
      importDataBtnText: '导入数据',
      importDataBtnIcon: 'el-icon-upload2',
      importDataDisabled: false,
      dialogVisible: false,
      title: '',
      departments: [],
      positions: [],
      rules: {
        name: [{
          required: true, message: '请输入名字', trigger:
            'blur'
        }],
        gender: [{
          required: true, message: '请选择性别', trigger:
            'blur'
        }],
        birthday: [{
          required: true, message: '请选择出生日期', trigger:
            'blur'
        }],
        email: [
          {
            required: true, message: '请输入邮箱', trigger:
              'blur'
          },
          {
            pattern: /^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(.[a-zA-Z0-9_-]+)+$/,
            message: '邮箱格式错误,请检查后输入',
            trigger: 'blur'
          }
        ],
        phone: [
          {
            required: true, message: '请输入手机号', trigger:
              'blur'
          },
          {
            pattern: /^1(3|4|5|7|8)\d{9}$/,
            message: '手机号格式错误,请检查后输入',
            trigger: 'blur'
          }
        ],
        depId: [{
          required: true, message: '请选择部门', trigger:
            'blur'
        }],
        posId: [{
          required: true, message: '请选择职位', trigger:
            'blur'
        }]
      },
      disabled: false
    }
  },
  mounted() {
    this.initEmps();
    this.initDeps();
    this.initPoss();
  },
  methods: {
    initEmps() {
      this.loading = true;
      let url = '/personnel/emp/?currentPage=' + this.currentPage + '&size=' + this.size;
      if (this.empName) {
        url += '&name=' + this.empName;
      }
      this.getRequest(url).then(resp => {
        this.loading = false;
        this.total = resp.obj.total;
        this.emps = resp.obj.data;
      })
    },
    initDeps() {
      this.getRequest('/personnel/emp/deps').then(resp => {
        this.departments = resp.obj;
      })
    },
    initPoss() {
      this.getRequest('/personnel/emp/position').then(resp => {
        this.positions = resp;
      })
    },
    handleEditEmp(data) {
      this.dialogVisible = true;
      this.title = '编辑';
      this.disabled = true;
      this.emp = Object.assign({}, data)
    },
    handleClose() {
      this.$refs['empFrom'].resetFields();
      this.dialogVisible = false;
      this.$message({
        type: 'info',
        message: '取消操作'
      });
    },
    doAddEmp() {
      if (this.emp.id) {
        this.$refs['empFrom'].validate(valid => {
          if (valid) {
            this.putRequest('/personnel/emp/', this.emp).then(resp => {
              if (resp) {
                this.dialogVisible = false;
                this.initEmps();
              }
            })
          }
        })
      } else {
        this.$refs['empFrom'].validate(valid => {
          if (valid) {
            this.postRequest('/personnel/emp/', this.emp).then(resp => {
              if (resp) {
                this.dialogVisible = false;
                this.initEmps();
              }
            })
          }
        })
      }
    },
    showAddEmp() {
      this.dialogVisible = true;
      this.title = '新增员工';
      this.disabled = false;
      this.emp = {
        name: '',
        gender: '',
        birthday: '',
        email: '',
        phone: '',
        depId: '',
        posId: ''
      };
    },
    handleDeleteEmp(data) {
      this.$confirm('确认删除该信息,该操作不可逆,是否继续?', '确定删除', {
        'confirmButtonText': '确定',
        'cancelButtonText': '取消',
        type: 'warning'
      }).then(() => {
        this.deleteRequest('/personnel/emp/' + data).then(resp => {
          if (resp) {
            //总页数（每次删除计算总页数）
            const totalPage = Math.ceil((this.total - 1) / this.size);
            //当前页大于总页数代表已经删除最后一条数据 将总页数赋值给当前页 会跳转到上一页
            this.currentPage = this.currentPage > totalPage ? totalPage : this.currentPage
            //当页数小于为0 就给它为1页 否则就还是当前页
            this.currentPage = this.currentPage < 1 ? 1 : this.currentPage
            this.initEmps();
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      })
    },
    onError() {
      this.importDataBtnText = '导入数据';
      this.importDataBtnIcon = 'el-icon-upload2';
      this.importDataDisabled = false;
    },
    onSuccess(response, file, fileList) {
      this.importDataBtnText = '导入数据';
      this.importDataBtnIcon = 'el-icon-upload2';
      this.importDataDisabled = false;
      this.initEmps();
      if(response.code == 200){
                this.$message({
                    type: 'success',
                    message: response.message
                })
            }else{
                this.$message({
                    type: 'error',
                    message: response.message
                })
            }
    },
    beforeUpload() {
      this.importDataBtnText = '正在导入';
      this.importDataBtnIcon = 'el-icon-loading';
      this.importDataDisabled = true;
    },
    exportData() {
      let url = '/personnel/emp/export/';
      if (this.empName) {
        url += '?name=' + this.empName;
      }
      this.downloadRequest(url);
      // this.downloadRequest('/personnel/emp/export')
    },
    currentChange(val) {
      this.currentPage = val;
      this.initEmps();
    },
    sizeChange(val) {
      this.size = val;
      this.initEmps();
    }
  }
}
</script>

<style scoped></style>
