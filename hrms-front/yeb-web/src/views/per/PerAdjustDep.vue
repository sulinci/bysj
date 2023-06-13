<template>
    <div>
        <div style="display: flex;justify-content: space-between">
            <div>
                <el-input prefix-icon="el-icon-search" placeholder="请输入员工名进行搜索..." v-model="empName"
                    @keydown.enter.native="initEmpDepAdjusts" clearable @clear="initEmpDepAdjusts"
                    style="width: 300px;margin-right: 10px"></el-input>
                <el-button type="primary" icon="el-icon-search" @click="initEmpDepAdjusts">搜索
                </el-button>
            </div>
            <div>
                <el-button type="warning" icon="el-icon-download" @click="exportData">导出数据
                </el-button>
                <el-button type="danger" icon="el-icon-delete" @click="handleDeleteEmpDepAdjust('批量', null)">
                    批量删除
                </el-button>
                <el-button type="success" icon="el-icon-plus" @click="showAddEmpDepAdjusts">
                    添加调动
                </el-button>
            </div>
        </div>
        <div style="margin-top: 10px"  v-loading="loading" element-loading-text="拼命加载中..."
        element-loading-spinner="el-icon-loading" element-loading-background="rgba(0, 0, 0, 0.8)">
            <el-table :data="empDepAdjusts" stripe border @selection-change="handleSelectionChange" style="width: 100%">
                <el-table-column type="selection" prop="id" width="40">
                </el-table-column>
                <el-table-column prop="empWorkID" label="员工工号" align="center" width="100">
                </el-table-column>
                <el-table-column prop="empName" label="员工姓名" align="center" width="100">
                </el-table-column>
                <el-table-column prop="beforeDep" label="调动前部门" align="center" width="100">
                </el-table-column>
                <el-table-column prop="afterDep" label="调动后部门" align="center" width="100">
                </el-table-column>
                <el-table-column prop="adjustDate" label="调动日期" align="center" width="100">
                </el-table-column>
                <el-table-column prop="reason" label="调动原因" align="center">
                </el-table-column>
                <el-table-column label="操作" align="center" width="150">
                    <template slot-scope="scope">
                        <el-button size="mini" type="primary" @click="handleEditEmpDepAdjust(scope.row)">编辑</el-button>
                        <el-button size="mini" type="danger"
                            @click="handleDeleteEmpDepAdjust('单删', scope.row.id)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
        </div>
        <div style="display: flex;justify-content: flex-end">
            <el-pagination background @current-change="currentChange" @size-change="sizeChange"
                :page-sizes="[5, 10, 30, 50]" :page-size="size" layout="sizes,prev,pager,next,jumper,->,total,slot"
                :total="total"></el-pagination>
        </div>
        <div>
            <el-dialog :title="title" :visible.sync="dialogVisible" width="30%" :before-close="handleClose">
                <el-form ref="depAdjustFrom" :model="depAdjust" label-width="100px" :rules="rules">
                    <el-form-item label="员工工号:" prop="empWorkID">
                        <el-input v-model="depAdjust.empWorkID" placeholder="请输入员工工号" :disabled="disabled"></el-input>
                    </el-form-item>
                    <el-form-item label="员工姓名:" prop="empName">
                        <el-input v-model="depAdjust.empName" placeholder="请输入员工姓名" :disabled="disabled"></el-input>
                    </el-form-item>
                    <el-form-item label="调动后部门:" prop="afterDepId">
                        <el-select v-model="depAdjust.afterDepId" placeholder="请选择调动后部门" style="width:100%">
                            <el-option-group v-for="dep in departments" :key="dep.id" :label="dep.name">
                                <el-option v-for="item in dep.children" :key="item.id" :label="item.name" :value="item.id"
                                    :disabled="item.enabled == 1 ? true : false">
                                </el-option>
                            </el-option-group>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="调动日期:" prop="adjustDate">
                        <el-col :span="24">
                            <el-date-picker type="date" placeholder="请选择调动日期" v-model="depAdjust.adjustDate"
                                style="width: 100%;"></el-date-picker>
                        </el-col>
                    </el-form-item>
                    <el-form-item label="调动原因:" prop="reason">
                        <el-input v-model="depAdjust.reason" placeholder="请输入调动原因"></el-input>
                    </el-form-item>
                </el-form>
                <span slot="footer" class="dialog-footer">
                    <el-button @click="handleClose">取 消</el-button>
                    <el-button type="primary" @click="doAddDepAdjust">确 定</el-button>
                </span>
            </el-dialog>
        </div>
    </div>
</template>

<script>
export default {
    name: "PerAdjustDep",
    data() {
        return {
            empDepAdjusts: [],
            departments: [],
            loading: false,
            total: 0,
            currentPage: 1,
            size: 5,
            empName: '',
            ids: [],
            dialogVisible: false,
            title: '',
            depAdjust: {
                empWorkID: '',
                empName: '',
                afterDepId: '',
                adjustDate: '',
                reason: '',
            },
            disabled: false,
            rules: {
                empWorkID: [{
                    required: true, message: '请输入员工工号', trigger:
                        'blur'
                }],
                empName: [{
                    required: true, message: '请输入员工姓名', trigger:
                        'blur'
                }],
                afterDepId: [{
                    required: true, message: '请选择调动后部门', trigger:
                        'blur'
                }],
                adjustDate: [{
                    required: true, message: '请选择调动日期', trigger:
                        'blur'
                }],
                reason: [{
                    required: true, message: '请输入调动原因', trigger:
                        'blur'
                }]
            }
        }
    },
    mounted() {
        this.initEmpDepAdjusts();
        this.initDepition();
    },
    methods: {
        handleSelectionChange(val) {
            let checkArr = []
            val.forEach((n) => {
                checkArr.push(n.id)
            })
            this.ids = checkArr
        },
        //初始化调动信息
        initEmpDepAdjusts() {
            this.loading = true;
            let url = '/personnel/dep-adjust/?currentPage=' + this.currentPage + '&size=' + this.size;
            if (this.empName) {
                url += '&empName=' + this.empName;
            }
            this.getRequest(url).then(resp => {
                this.loading = false;
                this.total = resp.obj.total;
                this.empDepAdjusts = resp.obj.data;
            })
        },
        initDepition() {
            this.getRequest('/personnel/dep-adjust/department').then(resp => {
                this.departments = resp.obj;
            })
        },
        sizeChange(currentSize) {
            this.size = currentSize;
            this.initEmpDepAdjusts();
        },
        currentChange(currentPage) {
            this.currentPage = currentPage;
            this.initEmpDepAdjusts();
        },
        handleDeleteEmpDepAdjust(type, id) {
            if (type === '批量' && id === null) {
                if (this.ids.length === 0) {
                    return this.$message.error('请勾选删除对象')
                }
            }
            this.$confirm('确认删除该信息,该操作不可逆,是否继续?', '确定删除', {
                'confirmButtonText': '确定',
                'cancelButtonText': '取消',
                type: 'warning'
            }).then(() => {
                this.deleteRequest("/personnel/dep-adjust/delete?ids=" + (type === '批量' ? this.ids.join(',') : id)).then(resp => {
                    if (resp) {
                        var totalPage;
                        if (type === '批量') {
                            totalPage = Math.ceil((this.total - this.ids.length) / this.size);
                        } else {
                            //总页数（每次删除计算总页数）
                            totalPage = Math.ceil((this.total - 1) / this.size);
                        }
                        //当前页大于总页数代表已经删除最后一条数据 将总页数赋值给当前页 会跳转到上一页
                        this.currentPage = this.currentPage > totalPage ? totalPage : this.currentPage
                        //当页数小于为0 就给它为1页 否则就还是当前页
                        this.currentPage = this.currentPage < 1 ? 1 : this.currentPage
                        this.initEmpDepAdjusts();
                    }
                })
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '已取消删除'
                });
            })

        },
        showAddEmpDepAdjusts() {
            this.title = '添加调动';
            this.disabled = false;
            this.depAdjust = {
                empWorkID: '',
                empName: '',
                afterDepId: '',
                adjustDate: '',
                reason: '',
            };
            this.dialogVisible = true;
        },
        handleEditEmpDepAdjust(data) {
            this.title = '编辑调动信息';
            // this.depAdjust = data;
            this.depAdjust = Object.assign({}, data)
            this.disabled = true;
            this.dialogVisible = true;
        },
        doAddDepAdjust() {
            if (this.depAdjust.id) {
                this.$refs['depAdjustFrom'].validate(valid => {
                    if (valid) {
                        this.putRequest('/personnel/dep-adjust/', this.depAdjust).then(resp => {
                            if (resp) {
                                this.dialogVisible = false;
                                this.initEmpDepAdjusts();
                            }
                        })
                    }
                })
            } else {
                this.$refs['depAdjustFrom'].validate(valid => {
                    if (valid) {
                        this.postRequest('/personnel/dep-adjust/', this.depAdjust).then(resp => {
                            if (resp) {
                                this.dialogVisible = false;
                                this.initEmpDepAdjusts();
                            }
                        })
                    }
                })
            }
        },
        handleClose() {
            this.$refs['depAdjustFrom'].resetFields();
            this.dialogVisible = false;
            this.$message({
                type: 'info',
                message: '取消操作'
            });
        },
        exportData() {
            let url = '/personnel/dep-adjust/export';
            if (this.empName) {
                url += '?empName=' + this.empName;
            }
            this.downloadRequest(url);
        }
    }
}
</script>

<style scoped></style>