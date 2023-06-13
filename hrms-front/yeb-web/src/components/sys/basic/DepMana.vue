<template>
  <div style="width: 800px">
    <div>
      <el-input
        size="small"
        class="addDepInput"
        placeholder="添加根部门..."
        prefix-icon="el-icon-plus"
        @keydown.enter.native="addDept"
        v-model="dept.name">
      </el-input>
      <el-button icon="el-icon-plus" size="small" type="primary" @click="addDept">添加</el-button>
    </div>
    <div class="depManaMain">
      <el-table
        :data="department"
        style="width: 100%"
        row-key="id"
        border
        lazy
        :load="initDeps"
        :tree-props="{children: 'children', hasChildren: 'hasChildren'}">
    <el-table-column
      prop="name"
      label="部门名称"
      width="180">
    </el-table-column>
    <el-table-column
      prop="createTime"
      label="创建时间"
      width="180">
    </el-table-column>
    <el-table-column
      label="是否启用"
      width="150">
      <template slot-scope="scope">
        <el-switch
          v-model="scope.row.enabled"
          active-color="#13ce66"
          inactive-color="#ff4949"
          active-value="0"
          inactive-value="1"
          v-if="!scope.row.parentId == 0"
          @change="handleEnabledChange(scope.row)"
        ></el-switch>
      </template>
    </el-table-column>
    <el-table-column
      label="操作"
      >
          <template slot-scope="scope">
              <el-button
              size="mini"
              type="primary"
              @click="handleEditDep(scope.row)">编辑</el-button>
              <el-button
              size="mini"
              type="danger"
              @click="handleDeleteDep(scope.row.id)">删除</el-button>
              <el-button
              size="mini"
              type="success"
              v-if="scope.row.parentId == 0"
              @click="handleAddDep(scope.row.id)">新增部门</el-button>
          </template>
      </el-table-column>
  </el-table>
    </div>
    <div>
      <el-dialog
          :title="title"
          :visible.sync="dialogVisible"
          width="40%"
          :before-close="handleClose">
          <el-form ref="depFrom" :model="dep" label-width="100px" :rules="rules">
              <el-form-item label="部门名称:" prop="name">
                  <el-input v-model="dep.name" placeholder="请输部门名称"></el-input>
              </el-form-item>
              <el-form-item label="是否启用:" prop="enabled" v-if="dep.parentId != 0">
                <div>
                <el-radio-group v-model="dep.enabled">
                  <el-radio label="0">启用</el-radio>
                  <el-radio label="1">停用</el-radio>
                </el-radio-group>
                </div>
              </el-form-item>
          </el-form>
          <span slot="footer" class="dialog-footer">
              <el-button @click="handleClose">取 消</el-button>
              <el-button type="primary" @click="doAddDep">确 定</el-button>
          </span>
      </el-dialog>
    </div>
  </div>
</template>

<script>
  export default {
    name: "DepMana",
    data() {
      return {
        department:[],
        dept: {
          name: ''
        },
        dep: {
          name: '',
          enabled: ''
        },
        dialogVisible: false,
        title:'',
        rules: {
          name: [{
              required: true, message: '请输入部门名称', trigger:
              'blur'
          }]
        },
        id: null
      }
    },
    mounted() {
      this.initDeps();
    },
    methods: {
      initDeps(){
        this.getRequest('/system/basic/department/').then(resp=> {
          if(resp){
            this.department = resp.obj;
          } 
        })
      },
      handleEnabledChange(row){
        let text = row.enabled === "0" ? "启用" : "停用";
        this.$confirm('确认要' + text + '"' + row.name + '"吗？').then(() =>{
            this.dep = row;
            this.putRequest('/system/basic/department/', this.dep).then(resp => {
              if (resp) {
                this.initDeps();
              }
            })
          }  
        ).catch(function() {
          row.enabled = row.enabled === "0" ? "1" : "0";
        });
      },
      changeDepEnabled(id,enabled){
        this.dep.id = id;
        this.dep.enabled = enabled;
        this.doAddDep();
      },
      addDept() {
        //添加根部门
        if (this.dept.name) {
          this.postRequest('/system/basic/department/', this.dept).then(resp => {
            if (resp) {
              this.initDeps();
              this.dept.name = '';
            }
          })
        } else {
          this.$message.error("根部门名称不可以为空！");
        }
      },
      handleAddDep(data){
        this.title = '新增';
        this.dialogVisible = true;
        this.dep = {
          name: '',
          enabled: '0'
        };
        this.dep.parentId = data;
      },
      doAddDep(){
        if(this.dep.id){
          this.$refs['depFrom'].validate(valid => {
            if (valid) {
              this.putRequest('/system/basic/department/', this.dep).then(resp => {
                if (resp) {
                this.dialogVisible = false;
                this.initDeps();
                }
              })
            }
          })
        } else {
          this.$refs['depFrom'].validate(valid => {
            if (valid) {
              this.postRequest('/system/basic/department/', this.dep).then(resp => {
                if (resp) {
                  this.dialogVisible = false;
                  this.initDeps();
                }
              })
            }
          })
        }
      },
      handleEditDep(data){
        this.title = '编辑';
        this.dialogVisible = true;
        this.dep = Object.assign({}, data)
        // this.dep = data;
      },
      handleClose(){
        this.$refs['depFrom'].resetFields();
        this.dialogVisible = false;
        
      },
      handleDeleteDep(data){
        this.id = data;
        
        this.$confirm('确认删除该信息,该操作不可逆,是否继续?', '确定删除', {
                'confirmButtonText': '确定',
                'cancelButtonText': '取消',
            }).then(() => {
              this.deleteRequest('/system/basic/department/'+ this.id).then(resp => {
                if(resp){
                  this.initDeps();
                }
              })
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '已取消删除'
                });
            })
      }
    }
  }
</script>

<style>
  
  .addDepInput {
    width: 300px;
    margin-right: 8px;
  }

  .depManaMain {
    margin-top: 10px;
  }
</style>
