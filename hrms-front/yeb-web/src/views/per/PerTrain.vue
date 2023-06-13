<template>
    <div>
        <div style="display: flex;justify-content: space-between">
            <div>
                <el-input prefix-icon="el-icon-search" placeholder="请输入员工名进行搜索..." v-model="empName"
                    @keydown.enter.native="initEmpTrains" clearable @clear="initEmpTrains"
                    style="width: 300px;margin-right: 10px"></el-input>
                <el-button type="primary" icon="el-icon-search" @click="initEmpTrains">搜索
                </el-button>
            </div>
            <div>
                <el-button type="warning" icon="el-icon-download" @click="exportData">导出信息
                </el-button>
                <el-button type="danger" icon="el-icon-delete" @click="handleDeleteEmpTrain('批量', null)">
                    批量删除
                </el-button>
                <el-button type="success" icon="el-icon-plus" @click="showAddEmpTrains">
                    添加培训
                </el-button>
            </div>
        </div>
        <div style="margin-top: 10px" v-loading="loading" element-loading-text="拼命加载中..."
        element-loading-spinner="el-icon-loading" element-loading-background="rgba(0, 0, 0, 0.8)">
            <el-table :data="empTrains" stripe border @selection-change="handleSelectionChange" style="width: 100%">
                <el-table-column type="selection" prop="id" width="40">
                </el-table-column>
                <el-table-column prop="empWorkID" label="员工工号" align="center" width="100">
                </el-table-column>
                <el-table-column prop="empName" label="员工姓名" align="center" width="100">
                </el-table-column>
                <el-table-column prop="trainDate" label="培训日期" align="center" width="100">
                </el-table-column>
                <el-table-column prop="endDate" label="结束日期" align="center" width="100">
                </el-table-column>
                <el-table-column prop="trainContent" label="培训内容" align="center">
                </el-table-column>
                <el-table-column prop="remark" label="备注" align="center" width="150">
                </el-table-column>
                <el-table-column label="操作" align="center" width="200">
                    <template slot-scope="scope">
                        <el-button size="mini" type="primary" @click="handleEditEmpTrain(scope.row)">编辑</el-button>
                        <el-button size="mini" type="danger"
                            @click="handleDeleteEmpTrain('单删', scope.row.id)">删除</el-button>
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
                <el-form ref="trainFrom" :model="train" label-width="100px" :rules="rules">
                    <el-form-item label="员工工号:" prop="empWorkID">
                        <el-input v-model="train.empWorkID" placeholder="请输入员工工号" :disabled="disabled"></el-input>
                    </el-form-item>
                    <el-form-item label="员工姓名:" prop="empName">
                        <el-input v-model="train.empName" placeholder="请输入员工姓名" :disabled="disabled"></el-input>
                    </el-form-item>
                    <el-form-item label="培训日期:" prop="trainDate">
                        <el-col :span="24">
                            <el-date-picker type="date" placeholder="请选择培训日期" v-model="train.trainDate"
                                style="width: 100%;"></el-date-picker>
                        </el-col>
                    </el-form-item>
                    <el-form-item label="结束日期:" prop="endDate">
                        <el-col :span="24">
                            <el-date-picker type="date" placeholder="请选择结束日期" v-model="train.endDate"
                                style="width: 100%;"></el-date-picker>
                        </el-col>
                    </el-form-item>
                    <el-form-item label="培训内容:" prop="trainContent">
                        <el-input v-model="train.trainContent" placeholder="请输入培训内容"></el-input>
                    </el-form-item>
                    <el-form-item label="备注:" prop="remark">
                        <el-input v-model="train.remark" placeholder="请输入备注"></el-input>
                    </el-form-item>
                </el-form>
                <span slot="footer" class="dialog-footer">
                    <el-button @click="handleClose">取 消</el-button>
                    <el-button type="primary" @click="doAddTrain">确 定</el-button>
                </span>
            </el-dialog>
        </div>
    </div>
</template>

<script>
export default {
    name: "PerTrain",
    data() {
        return {
            empTrains: [],
            loading: false,
            total: 0,
            currentPage: 1,
            size: 5,
            empName: '',
            ids: [],
            dialogVisible: false,
            title: '',
            train: {
                empWorkID: '',
                empName: '',
                trainDate: '',
                endDate: '',
                trainContent: '',
                remark: ''
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
                trainDate: [{
                    required: true, message: '请输入培训日期', trigger:
                        'blur'
                }],
                endDate: [{
                    required: true, message: '请输入结束日期', trigger:
                        'blur'
                }],
                trainContent: [{
                    required: true, message: '请输入培训内容', trigger:
                        'blur'
                }],
                remark: [{
                    required: true, message: '请输入备注', trigger:
                        'blur'
                }]
            }
        }
    },
    mounted() {
        this.initEmpTrains();
    },
    methods: {
        handleSelectionChange(val) {
            let checkArr = []
            val.forEach((n) => {
                checkArr.push(n.id)
            })
            this.ids = checkArr
        },
        //初始化培训信息
        initEmpTrains() {
            this.loading = true;
            let url = '/personnel/train/?currentPage=' + this.currentPage + '&size=' + this.size;
            if (this.empName) {
                url += '&empName=' + this.empName;
            }
            this.getRequest(url).then(resp => {
                this.loading = false;
                this.total = resp.obj.total;
                this.empTrains = resp.obj.data;
            })
        },
        sizeChange(currentSize) {
            this.size = currentSize;
            this.initEmpTrains();
        },
        currentChange(currentPage) {
            this.currentPage = currentPage;
            this.initEmpTrains();
        },
        //删除培训信息
        handleDeleteEmpTrain(type, id) {
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
                this.deleteRequest("/personnel/train/delete?ids=" + (type === '批量' ? this.ids.join(',') : id)).then(resp => {
                    if (resp) {
                        let totalPage;
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
                        this.initEmpTrains();
                    }
                })
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '已取消删除'
                });
            })

        },
        showAddEmpTrains() {
            this.title = '添加培训';
            this.disabled = false;
            this.train = {
                empWorkID: '',
                empName: '',
                trainDate: '',
                endDate: '',
                trainContent: '',
                remark: ''
            };
            this.dialogVisible = true;
        },
        handleEditEmpTrain(data) {
            this.title = '编辑培训信息';
            // this.train = data;
            this.train = Object.assign({}, data);
            this.disabled = true;
            this.dialogVisible = true;
        },
        doAddTrain() {
            if (this.train.id) {
                this.$refs['trainFrom'].validate(valid => {
                    if (valid) {
                        this.putRequest('/personnel/train/', this.train).then(resp => {
                            if (resp) {
                                this.dialogVisible = false;
                                this.initEmpTrains();
                            }
                        })
                    }
                })
            } else {
                this.$refs['trainFrom'].validate(valid => {
                    if (valid) {
                        this.postRequest('/personnel/train/', this.train).then(resp => {
                            if (resp) {
                                this.dialogVisible = false;
                                this.initEmpTrains();
                            }
                        })
                    }
                })
            }
        },
        handleClose() {
            this.$refs['trainFrom'].resetFields();
            this.dialogVisible = false;
            this.$message({
                type: 'info',
                message: '取消操作'
            });
        },
        exportData() {
            let url = '/personnel/train/export';
            if (this.empName) {
                url += '?empName=' + this.empName;
            }
            this.downloadRequest(url);
        }
    }
}
</script>

<style scoped></style>
