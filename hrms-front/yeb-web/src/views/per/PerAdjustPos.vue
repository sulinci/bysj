<template>
    <div>
        <div style="display: flex;justify-content: space-between">
            <div>
                <el-input prefix-icon="el-icon-search" placeholder="请输入员工名进行搜索..." v-model="empName"
                    @keydown.enter.native="initEmpPosAdjusts" clearable @clear="initEmpPosAdjusts"
                    style="width: 300px;margin-right: 10px"></el-input>
                <el-button type="primary" icon="el-icon-search" @click="initEmpPosAdjusts">搜索
                </el-button>
            </div>
            <div>
                <el-button type="warning" icon="el-icon-download" @click="exportData">导出数据
                </el-button>
                <el-button type="danger" icon="el-icon-delete" @click="handleDeleteEmpPosAdjust('批量', null)">
                    批量删除
                </el-button>
                <el-button type="success" icon="el-icon-plus" @click="showAddEmpPosAdjusts">
                    添加调动
                </el-button>
            </div>
        </div>
        <div style="margin-top: 10px" v-loading="loading" element-loading-text="拼命加载中..."
        element-loading-spinner="el-icon-loading" element-loading-background="rgba(0, 0, 0, 0.8)">
            <el-table :data="empPosAdjusts" stripe border @selection-change="handleSelectionChange" style="width: 100%">
                <el-table-column type="selection" prop="id" width="40">
                </el-table-column>
                <el-table-column prop="empWorkID" label="员工工号" align="center" width="100">
                </el-table-column>
                <el-table-column prop="empName" label="员工姓名" align="center" width="100">
                </el-table-column>
                <el-table-column prop="beforePos" label="调动前职位" align="center" width="100">
                </el-table-column>
                <el-table-column prop="afterPos" label="调动后职位" align="center" width="100">
                </el-table-column>
                <el-table-column prop="adjustDate" label="调动日期" align="center" width="100">
                </el-table-column>
                <el-table-column prop="reason" label="调动原因" align="center">
                </el-table-column>
                <el-table-column label="操作" align="center" width="150">
                    <template slot-scope="scope">
                        <el-button size="mini" type="primary" @click="handleEditEmpPosAdjust(scope.row)">编辑</el-button>
                        <el-button size="mini" type="danger"
                            @click="handleDeleteEmpPosAdjust('单删', scope.row.id)">删除</el-button>
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
                <el-form ref="posAdjustFrom" :model="posAdjust" label-width="100px" :rules="rules">
                    <el-form-item label="员工工号:" prop="empWorkID">
                        <el-input v-model="posAdjust.empWorkID" placeholder="请输入员工工号" :disabled="disabled"></el-input>
                    </el-form-item>
                    <el-form-item label="员工姓名:" prop="empName">
                        <el-input v-model="posAdjust.empName" placeholder="请输入员工姓名" :disabled="disabled"></el-input>
                    </el-form-item>
                    <el-form-item label="调动后职位:" prop="afterPosId">
                        <el-select v-model="posAdjust.afterPosId" placeholder="请选择调动后职位" style="width:100%">
                            <el-option v-for="item in positions" :key="item.id" :label="item.name"
                                :value="item.id"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="调动日期:" prop="adjustDate">
                        <el-col :span="24">
                            <el-date-picker type="date" placeholder="请选择调动日期" v-model="posAdjust.adjustDate"
                                style="width: 100%;"></el-date-picker>
                        </el-col>
                    </el-form-item>
                    <el-form-item label="调动原因:" prop="reason">
                        <el-input v-model="posAdjust.reason" placeholder="请输入调动原因"></el-input>
                    </el-form-item>
                </el-form>
                <span slot="footer" class="dialog-footer">
                    <el-button @click="handleClose">取 消</el-button>
                    <el-button type="primary" @click="doAddposAdjust">确 定</el-button>
                </span>
            </el-dialog>
        </div>
    </div>
</template>

<script>
export default {
    name: "PerAdjustPos",
    data() {
        return {
            empPosAdjusts: [],
            positions: [],
            loading: false,
            total: 0,
            currentPage: 1,
            size: 5,
            empName: '',
            ids: [],
            dialogVisible: false,
            title: '',
            posAdjust: {
                empWorkID: '',
                empName: '',
                afterPosId: '',
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
                afterPosId: [{
                    required: true, message: '请选择调动后职位', trigger:
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
        this.initEmpPosAdjusts();
        this.initPosition();
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
        initEmpPosAdjusts() {
            this.loading = true;
            let url = '/personnel/pos-adjust/?currentPage=' + this.currentPage + '&size=' + this.size;
            if (this.empName) {
                url += '&empName=' + this.empName;
            }
            this.getRequest(url).then(resp => {
                this.loading = false;
                this.total = resp.obj.total;
                this.empPosAdjusts = resp.obj.data;
            })
        },
        initPosition() {
            this.getRequest('/personnel/pos-adjust/position').then(resp => {
                this.positions = resp.obj;
            })
        },
        sizeChange(currentSize) {
            this.size = currentSize;
            this.initEmpPosAdjusts();
        },
        currentChange(currentPage) {
            this.currentPage = currentPage;
            this.initEmpPosAdjusts();
        },
        handleDeleteEmpPosAdjust(type, id) {
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
                this.deleteRequest("/personnel/pos-adjust/delete?ids=" + (type === '批量' ? this.ids.join(',') : id)).then(resp => {
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
                        this.initEmpPosAdjusts();
                    }
                })
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '已取消删除'
                });
            })

        },
        showAddEmpPosAdjusts() {
            this.title = '添加调动';
            this.disabled = false;
            this.posAdjust = {
                empWorkID: '',
                empName: '',
                afterPosId: '',
                adjustDate: '',
                reason: '',
            };
            this.dialogVisible = true;
        },
        handleEditEmpPosAdjust(data) {
            this.title = '编辑调动信息';
            // this.posAdjust = data;
            this.posAdjust = Object.assign({}, data);
            this.disabled = true;
            this.dialogVisible = true;
        },
        doAddposAdjust() {
            if (this.posAdjust.id) {
                this.$refs['posAdjustFrom'].validate(valid => {
                    if (valid) {
                        this.putRequest('/personnel/pos-adjust/', this.posAdjust).then(resp => {
                            if (resp) {
                                this.dialogVisible = false;
                                this.initEmpPosAdjusts();
                            }
                        })
                    }
                })
            } else {
                this.$refs['posAdjustFrom'].validate(valid => {
                    if (valid) {
                        this.postRequest('/personnel/pos-adjust/', this.posAdjust).then(resp => {
                            if (resp) {
                                this.dialogVisible = false;
                                this.initEmpPosAdjusts();
                            }
                        })
                    }
                })
            }
        },
        handleClose() {
            this.$refs['posAdjustFrom'].resetFields();
            this.dialogVisible = false;
            this.$message({
                type: 'info',
                message: '取消操作'
            });
        },
        exportData() {
            let url = '/personnel/pos-adjust/export';
            if (this.empName) {
                url += '?empName=' + this.empName;
            }
            this.downloadRequest(url);
        }
    }
}
</script>

<style scoped></style>