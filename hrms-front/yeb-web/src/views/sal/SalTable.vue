<template>
    <div>
        <div>
            <!-- @clear="initEmpAttendance"  -->
            <el-input prefix-icon="el-icon-search" placeholder="请输入员工名进行搜索..." v-model="empName" clearable
                style="width: 300px;margin-right: 10px"></el-input>
            <el-date-picker v-model="month" value-format="yyyyMM" type="month" placeholder="请选择月份进行搜索...">
            </el-date-picker>
            <el-button type="primary" icon="el-icon-search" @click="initSalarys">搜索
            </el-button>
            <el-date-picker v-model="countMonth" value-format="yyyyMM" type="month" placeholder="选择结算月份">
            </el-date-picker>
            <el-button type="primary" @click="countSalary">结算该月工资
            </el-button>
            <el-button icon="el-icon-refresh-right" @click="resetSearch">重置
            </el-button>
        </div>
        <div style="margin-top: 10px">
            <el-date-picker v-model="exportMonth" value-format="yyyyMM" type="month" placeholder="请选择导出工资月份">
            </el-date-picker>
            <el-button type="warning" icon="el-icon-download" @click="exportData">导出
            </el-button>
        </div>
        <div style="margin-top: 10px">
            <el-table :data="salarys" stripe border style="width: 100%" v-loading="loading" element-loading-text="拼命加载中..."
        element-loading-spinner="el-icon-loading" element-loading-background="rgba(0, 0, 0, 0.8)">
                <!-- <el-table-column type="selection" prop="id" width="40">
                </el-table-column> -->
                <el-table-column prop="empWorkId" label="员工工号" align="center" width="100" fixed="left">
                </el-table-column>
                <el-table-column prop="empName" label="员工姓名" align="center" width="100" fixed="left">
                </el-table-column>
                <el-table-column prop="depName" label="部门" align="center" width="100" fixed="left">
                </el-table-column>
                <el-table-column prop="month" label="月份" align="center" width="100" fixed="left">
                </el-table-column>
                <el-table-column prop="basicSalary" label="基础工资" align="center" width="100">
                </el-table-column>
                <el-table-column prop="subsidy" label="生活补贴" align="center">
                </el-table-column>
                <el-table-column prop="bonus" label="奖金" align="center">
                </el-table-column>
                <el-table-column label="扣款" align="center" width="500">
                    <el-table-column prop="lateDeduct" label="迟到(50元/次)" align="center" width="125">
                    </el-table-column>
                    <el-table-column prop="leaveEarlyDeduct" label="早退(50元/次)" align="center" width="125">
                    </el-table-column>
                    <el-table-column prop="absenteeismDeduct" label="旷工(100元/次)" align="center" width="125">
                    </el-table-column>
                    <el-table-column prop="leaveDeduct" label="请假(50元/次)" align="center" width="125">
                    </el-table-column>
                </el-table-column>
                <el-table-column label="公积金缴纳费用" align="center" width="200">
                    <el-table-column prop="perHousePay" label="个人" align="center" width="100">
                    </el-table-column>
                    <el-table-column prop="comHousePay" label="企业" align="center" width="100">
                    </el-table-column>
                </el-table-column>
                <el-table-column label="社保缴纳费用" align="center" width="200">
                    <el-table-column prop="perSocialPay" label="个人" align="center" width="100">
                    </el-table-column>
                    <el-table-column prop="comSocialPay" label="企业" align="center" width="100">
                    </el-table-column>
                </el-table-column>
                <el-table-column prop="allSalary" label="应发工资" align="center" width="100" fixed="right">
                </el-table-column>
            </el-table>
        </div>
        <div style="display: flex;justify-content: flex-end">
            <el-pagination background @current-change="currentChange" @size-change="sizeChange" :page-sizes="[5, 10, 30, 50]"
            :page-size="size" layout="sizes,prev,pager,next,jumper,->,total,slot" :total="total"></el-pagination>
        </div>
    </div>
</template>

<script>
export default {
    name: "SalTable",
    data() {
        return {
            salarys: [],
            loading: false,
            currentPage: 0,
            size: 5,
            empName: '',
            month: '',
            countMonth: '',
            total: 0,
            exportMonth:''
        }
    },
    mounted() {
        this.initSalarys();
    },
    methods: {
        initSalarys() {
            this.loading = true;
            let url = '/salary/salary/?currentPage=' + this.currentPage + '&size=' + this.size;
            if (this.empName) {
                url += '&name=' + this.empName;
            }
            if (this.month) {
                url += '&month=' + this.month;
            }
            this.getRequest(url).then(resp => {
                this.loading = false;
                this.total = resp.obj.total;
                this.salarys = resp.obj.records;
            })
        },
        countSalary() {
            if (this.countMonth == '') {
                this.$message({
                    type: 'error',
                    message: '请先选择结算月份'
                });
            } else {
                let url = '/salary/salary/?month=' + this.countMonth;
                this.postRequest(url).then(resp => {
                    if (resp) {
                        this.initSalarys();
                    }
                })
            }
        },
        resetSearch() {
            this.empName = '';
            this.month = '';
            this.countMonth = '';
            this.initSalarys();
        },
        currentChange(val){
            this.currentPage = val;
            this.initSalarys();
        },
        sizeChange(val){
            this.size = val;
            this.initSalarys();
        },
        exportData(){
            if (this.exportMonth == '') {
                this.$message({
                    type: 'error',
                    message: '请先选择导出工资月份'
                });
            } else {
                let url = '/salary/salary/export?month=' + this.exportMonth;
                this.downloadRequest(url);
            }
        }
    }
}
</script>

<style scoped></style>
