<template>
    <div>
        <div style="display: flex;justify-content: space-between">
            <div></div>
            <div>
                <el-button type="success" icon="el-icon-plus" @click="showAddSocDep">
                    添加五险一金
                </el-button>
            </div>
        </div>
        <div style="margin-top: 10px">
            <el-table :data="socDeps" stripe style="width: 100%" border v-loading="loading" element-loading-text="拼命加载中..."
        element-loading-spinner="el-icon-loading" element-loading-background="rgba(0, 0, 0, 0.8)">
                <el-table-column type="selection" width="55">
                </el-table-column>
                <el-table-column prop="name" label="部门名称" width="90" fixed="left">
                </el-table-column>
                <el-table-column label="养老保险缴费比例" width="180">
                    <el-table-column prop="perPensionRate" label="个人" width="90">
                    </el-table-column>
                    <el-table-column prop="comPensionRate" label="企业" width="90">
                    </el-table-column>
                </el-table-column>
                <el-table-column label="医疗保险缴费比例" width="180">
                    <el-table-column prop="perMedicalRate" label="个人" width="90">
                    </el-table-column>
                    <el-table-column prop="comMedicalRate" label="企业" width="90">
                    </el-table-column>
                </el-table-column>
                <el-table-column label="失业保险缴费比例" width="180">
                    <el-table-column prop="perUnemploymentRate" label="个人" width="90">
                    </el-table-column>
                    <el-table-column prop="comUnemploymentRate" label="企业" width="90">
                    </el-table-column>
                </el-table-column>
                <el-table-column prop="comMaternityRate" label="生育保险企业缴费比例" width="180">
                </el-table-column>
                <el-table-column prop="comInjuryRate" label="工伤保险企业缴费比例" width="180">
                </el-table-column>
                <el-table-column label="公积金缴费比例" width="180">
                    <el-table-column prop="perHouseRate" label="个人" width="90">
                    </el-table-column>
                    <el-table-column prop="comHouseRate" label="企业" width="90">
                    </el-table-column>
                </el-table-column>
                <el-table-column fixed="right" label="操作" width="180">
                    <template slot-scope="scope">
                        <el-button type="text" size="small" @click="handleEditSocDep(scope.row)">编辑</el-button>
                        <el-button @click="handleDeleteSocDep(scope.row.id)" type="text" size="small">删除</el-button>
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
            <el-dialog :title="title" :visible.sync="dialogVisible" width="50%" :before-close="handleClose">
                <el-form ref="socDepFrom" :model="socDep" label-width="170px" :rules="rules" size="mini">
                    <el-form-item label="部门名称:" prop="name">
                        <el-input v-model="socDep.name" placeholder="请输入部门名称" style="width:50%"></el-input>
                    </el-form-item>
                    <el-form-item label="养老保险个人缴费比例:" prop="perPensionRate">
                        <el-input-number v-model="socDep.perPensionRate" controls-position="right" :min="0.08" :max="0.12"
                            placeholder="请输入养老保险个人缴费比例" :step="0.001" :precision="3" style="width:50%"></el-input-number>
                        （比例范围：0.08 ~ 0.12）
                    </el-form-item>
                    <el-form-item label="养老保险企业缴费比例:" prop="comPensionRate">
                        <el-input-number v-model="socDep.comPensionRate" controls-position="right" :min="0.12" :max="0.2"
                            placeholder="请输入养老保险企业缴费比例" :step="0.001" :precision="3" style="width:50%"></el-input-number>
                        （比例范围：0.12 ~ 0.20）
                    </el-form-item>
                    <el-form-item label="医疗保险个人缴费比例:" prop="perMedicalRate">
                        <el-input-number v-model="socDep.perMedicalRate" controls-position="right" :min="0.01" :max="0.03"
                            placeholder="请输入医疗保险个人缴费比例" :step="0.001" :precision="3" style="width:50%"></el-input-number>
                        （比例范围：0.01 ~ 0.03）
                    </el-form-item>
                    <el-form-item label="医疗保险企业缴费比例:" prop="comMedicalRate">
                        <el-input-number v-model="socDep.comMedicalRate" controls-position="right" :min="0.08" :max="0.10"
                            placeholder="请输入医疗保险企业缴费比例" :step="0.001" :precision="3" style="width:50%"></el-input-number>
                        （比例范围：0.08 ~ 0.10）
                    </el-form-item>
                    <el-form-item label="失业保险个人缴费比例:" prop="perUnemploymentRate">
                        <el-input-number v-model="socDep.perUnemploymentRate" controls-position="right" :min="0.005"
                            :max="0.012" placeholder="请输入失业保险个人缴费比例" :step="0.001" :precision="3"
                            style="width:50%"></el-input-number>
                        （比例范围：0.005 ~ 0.012）
                    </el-form-item>
                    <el-form-item label="失业保险企业缴费比例:" prop="comUnemploymentRate">
                        <el-input-number v-model="socDep.comUnemploymentRate" controls-position="right" :min="0.015"
                            :max="0.025" placeholder="请输入失业保险企业缴费比例" :step="0.001" :precision="3"
                            style="width:50%"></el-input-number>
                        （比例范围：0.015 ~ 0.025）
                    </el-form-item>
                    <el-form-item label="生育保险企业缴费比例:" prop="comMaternityRate">
                        <el-input-number v-model="socDep.comMaternityRate" controls-position="right" :min="0.005"
                            :max="0.01" placeholder="请输入生育保险企业缴费比例" :step="0.001" :precision="3"
                            style="width:50%"></el-input-number>
                        （比例范围：0.005 ~ 0.01）
                    </el-form-item>
                    <el-form-item label="工伤保险企业缴费比例:" prop="comInjuryRate">
                        <el-input-number v-model="socDep.comInjuryRate" controls-position="right" :min="0.002" :max="0.019"
                            placeholder="请输入工伤保险企业缴费比例" :step="0.001" :precision="3" style="width:50%"></el-input-number>
                        （比例范围：0.002 ~ 0.019，推荐0.019）
                    </el-form-item>
                    <el-form-item label="公积金个人缴费比例:" prop="perHouseRate">
                        <el-input-number v-model="socDep.perHouseRate" controls-position="right" :min="0.05" :max="0.12"
                            placeholder="请输入公积金个人缴费比例" :step="0.001" :precision="3" style="width:50%"></el-input-number>
                        （比例范围：0.05 ~ 0.12，推荐0.12）
                    </el-form-item>
                    <el-form-item label="公积金企业缴费比例:" prop="comHouseRate">
                        <el-input-number v-model="socDep.comHouseRate" controls-position="right" :min="0.05" :max="0.12"
                            placeholder="请输入公积金企业缴费比例" :step="0.001" :precision="3" style="width:50%"></el-input-number>
                        （比例范围：0.05 ~ 0.12，推荐0.12）
                    </el-form-item>
                </el-form>
                <span slot="footer" class="dialog-footer">
                    <el-button @click="handleClose">取 消</el-button>
                    <el-button type="primary" @click="doAddSocDep">确 定</el-button>
                </span>
            </el-dialog>
        </div>
    </div>
</template>

<script>
export default {
    name: "SalSocDep",
    data() {
        return {
            socDeps: [],
            loading: false,
            currentPage: 1,
            size: 5,
            total: 0,
            dialogVisible: false,
            title: '',
            socDep: '',
            rules: {
                name: [{
                    required: true, message: '请输入部门名称', trigger: 'blur'
                }],
                perPensionRate: [
                    {
                        required: true, message: '请输入养老保险个人缴费比例', trigger: 'blur'
                    }
                ],
                comPensionRate: [
                    {
                        required: true, message: '请输入养老保险企业缴费比例', trigger: 'blur'
                    }
                ],
                perMedicalRate: [
                    {
                        required: true, message: '请输入医疗保险个人缴费比例', trigger: 'blur'
                    }
                ],
                comMedicalRate: [
                    {
                        required: true, message: '请输入医疗保险企业缴费比例', trigger: 'blur'
                    }
                ],
                perUnemploymentRate: [
                    {
                        required: true, message: '请输入失业保险个人缴费比例', trigger: 'blur'
                    }
                ],
                comUnemploymentRate: [
                    {
                        required: true, message: '请输入失业保险企业缴费比例', trigger: 'blur'
                    }
                ],
                comMaternityRate: [
                    {
                        required: true, message: '请输入生育保险企业缴费比例', trigger: 'blur'
                    }
                ],
                comInjuryRate: [
                    {
                        required: true, message: '请输入工伤保险企业缴费比例', trigger: 'blur'
                    }
                ],
                perHouseRate: [
                    {
                        required: true, message: '请输入公积金个人缴费比例', trigger: 'blur'
                    }
                ],
                comHouseRate: [
                    {
                        required: true, message: '请输入公积金企业缴费比例', trigger: 'blur'
                    }
                ]
            },

        }
    },
    mounted() {
        this.initSocDep();
    },
    methods: {
        initSocDep() {
            this.loading = true;
            let url = '/salary/soc-dep/?currentPage=' + this.currentPage + '&size=' + this.size;
            this.getRequest(url).then(resp => {
                this.loading = false;
                this.total = resp.obj.total;
                this.socDeps = resp.obj.records;
            })
        },
        handleEditSocDep(data) {
            this.dialogVisible = true;
            this.title = '编辑';
            this.socDep = Object.assign({}, data)
        },
        showAddSocDep() {
            this.dialogVisible = true;
            this.title = '新增';
            this.socDep = {};
        },
        handleDeleteSocDep(id) {
            this.$confirm('确认删除该信息,该操作不可逆,是否继续?', '确定删除', {
                'confirmButtonText': '确定',
                'cancelButtonText': '取消',
                type: 'warning'
            }).then(() => {
                this.deleteRequest("/salary/soc-dep/" + id).then(resp => {
                    if (resp) {
                        //总页数（每次删除计算总页数）
                        const totalPage = Math.ceil((this.total - 1) / this.size);
                        //当前页大于总页数代表已经删除最后一条数据 将总页数赋值给当前页 会跳转到上一页
                        this.currentPage = this.currentPage > totalPage ? totalPage : this.currentPage
                        //当页数小于为0 就给它为1页 否则就还是当前页
                        this.currentPage = this.currentPage < 1 ? 1 : this.currentPage
                        this.initSocDep();
                    }
                })
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '已取消删除'
                });
            })
        },
        handleClose() {
            this.$refs['socDepFrom'].resetFields();
            this.dialogVisible = false;
        },
        doAddSocDep() {
            if (this.socDep.id) {
                this.$refs['socDepFrom'].validate(valid => {
                    if (valid) {
                        this.putRequest('/salary/soc-dep/', this.socDep).then(resp => {
                            if (resp) {
                                this.dialogVisible = false;
                                this.initSocDep();
                            }
                        })
                    }
                })
            } else {
                this.$refs['socDepFrom'].validate(valid => {
                    if (valid) {
                        this.postRequest('/salary/soc-dep/', this.socDep).then(resp => {
                            if (resp) {
                                this.dialogVisible = false;
                                this.initSocDep();
                            }
                        })
                    }
                })
            }
        },
        currentChange(val) {
            this.currentPage = val;
            this.initSocDep();
        },
        sizeChange(val) {
            this.size = val;
            this.initSocDep();
        }

    }
}
</script>

<style scoped></style>