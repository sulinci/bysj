<template>
    <div>
        <div style="display: flex;justify-content: space-between">
            <div>
                <el-input prefix-icon="el-icon-search" placeholder="请输入员工名进行搜索..." v-model="empName"
                    @keydown.enter.native="initEmpDepAdjusts" clearable @clear="initSalStand"
                    style="width: 300px;margin-right: 10px"></el-input>
                <el-button type="primary" icon="el-icon-search" @click="initSalStand">搜索
                </el-button>
            </div>
            <div>
                <el-button type="warning" icon="el-icon-download" @click="exportData">导出数据
                </el-button>
            </div>
        </div>
        <div style="margin-top: 10px" v-loading="loading" element-loading-text="拼命加载中..."
        element-loading-spinner="el-icon-loading" element-loading-background="rgba(0, 0, 0, 0.8)">
            <el-table :data="salstands" stripe style="width: 100%" border>
                <el-table-column prop="workId" label="员工工号" width="120" fixed="left">
                </el-table-column>
                <el-table-column prop="name" label="姓名" width="120" fixed="left">
                </el-table-column>
                <el-table-column prop="depName" label="部门名称" width="120" fixed="left">
                </el-table-column>
                <el-table-column prop="socName" label="五险一金名称" width="120" fixed="left">
                </el-table-column>
                <el-table-column prop="baseSalary" label="基础工资" width="120">
                </el-table-column>
                <el-table-column prop="subsidy" label="生活补贴" width="120">
                </el-table-column>
                <el-table-column prop="bonus" label="奖金" width="120">
                </el-table-column>
                <el-table-column prop="socialBase" label="社保缴纳基数" width="150">
                </el-table-column>
                <el-table-column prop="houseBase" label="公积金缴纳基数" width="150">
                </el-table-column>
                <el-table-column fixed="right" label="操作" >
                    <template slot-scope="scope">
                        <el-button type="text" size="small" @click="handleEditSalStand(scope.row)">编辑</el-button>
                    </template>
                </el-table-column>
            </el-table>
        </div>
        <div style="display: flex;justify-content: flex-end">
            <el-pagination background @current-change="currentChange" @size-change="sizeChange" :page-sizes="[5, 10, 30, 50]"
            :page-size="size" layout="sizes,prev,pager,next,jumper,->,total,slot" :total="total"></el-pagination>
        </div>
        <div>
            <el-dialog title="薪资标准" :visible.sync="dialogVisible" width="40%" :before-close="handleClose">
                <el-form ref="salStandFrom" :model="salStand" label-width="120px" :rules="rules" size="mini">
                    <el-form-item label="基本工资:" prop="baseSalary">
                        <el-input-number v-model="salStand.baseSalary" :min="0" :max="30000" placeholder="请输入基本工资"
                            :precision="2" style="width:50%"></el-input-number>
                    </el-form-item>
                    <el-form-item label="生活补贴:" prop="subsidy">
                        <el-input-number v-model="salStand.subsidy" :min="0" :max="2000" placeholder="请输入生活补贴"
                            :precision="2" style="width:50%"></el-input-number>
                    </el-form-item>
                    <el-form-item label="奖金:" prop="bonus">
                        <el-input-number v-model="salStand.bonus" :min="0" :max="2000" placeholder="奖金" :precision="2"
                            style="width:50%"></el-input-number>
                    </el-form-item>
                    <el-form-item label="五险一金:" prop="socId">
                        <el-select v-model="salStand.socId" placeholder="请选择五险一金">
                            <el-option v-for="item in socs" :key="item.id" :label="item.name" :value="item.id"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="社保缴纳基数:" prop="socialBase">
                        <el-input-number v-model="salStand.socialBase" :min="3000" :max="12000" placeholder="社保基数"
                            :precision="2" style="width:50%"></el-input-number>
                        （基数范围：3000 ~ 12000）
                    </el-form-item>

                    <el-form-item label="公积金缴纳基数:" prop="houseBase">
                        <el-input-number v-model="salStand.houseBase" :min="3000" :max="12000" placeholder="公积金基数"
                            :precision="2" style="width:50%"></el-input-number>
                        （基数范围：3000 ~ 12000）
                    </el-form-item>
                </el-form>
                <span slot="footer" class="dialog-footer">
                    <el-button @click="handleClose">取 消</el-button>
                    <el-button type="primary" @click="setSalaryStandard">确 定</el-button>
                </span>
            </el-dialog>
        </div>
    </div>
</template>

<script>
export default {
    name: "SalStand",
    data() {
        return {
            salstands: [],
            loading: false,
            socs: [],
            soc: '',
            empName: '',
            currentPage: 1,
            size: 5,
            total: 0,
            name: '',
            dialogVisible: false,
            salStand: '',
            rules: {
                name: [{
                    required: true, message: '请输入部门名称', trigger:
                        'blur'
                }]
            }
        }
    },
    mounted() {
        this.initSalStand();
        this.initSoc();
    },

    methods: {
        initSalStand() {
            this.loading = true;
            let url = '/salary/standard/?currentPage=' + this.currentPage + '&size=' + this.size;
            if (this.empName) {
                url += '&empName=' + this.empName;
            }
            this.getRequest(url).then(resp => {
                this.loading = false;
                this.total = resp.obj.total;
                this.salstands = resp.obj.records;
            })
        },
        initSoc() {
            this.getRequest('/salary/standard/soc').then(resp => {
                this.socs = resp.obj;
            })
        },
        handleEditSalStand(data) {
            this.dialogVisible = true;
            this.soc = '';
            this.salStand = '';
            this.salStand = Object.assign({}, data);
        },
        handleClose() {
            this.dialogVisible = false;
        },
        setSalaryStandard() {
            this.$refs['salStandFrom'].validate(valid => {
                if (valid) {
                    this.postRequest('/salary/standard/', this.salStand).then(resp => {
                        if (resp) {
                            this.dialogVisible = false;
                            this.initSalStand();
                        }
                    })
                }
            })
        },
        currentChange(val){
            this.currentPage = val;
            this.initSalStand();
        },
        sizeChange(val){
            this.size = val;
            this.initSalStand();
        },
        exportData(){
            let url = '/salary/standard/export';
            if (this.empName) {
                url += '?name=' + this.empName;
            }
            this.downloadRequest(url);
        }
    }
}
</script>

<style scoped></style>
