<template>
    <div>
        <div style="display: flex;justify-content: space-between">
            <div>
                <el-input prefix-icon="el-icon-search" placeholder="请输入员工名进行搜索..." v-model="name" clearable
                    @clear="initEmpAttendance" style="width: 300px;margin-right: 10px"></el-input>
                <el-date-picker v-model="month" value-format="yyyyMM" type="month" placeholder="选择月份">
                </el-date-picker>
                <el-button type="primary" icon="el-icon-search" @click="initEmpAttendance">搜索
                </el-button>
                <el-button icon="el-icon-refresh-right" @click="resetSearch">重置
                </el-button>
            </div>
            <div>
                <el-upload action="/personnel/attendance/import" :before-upload="beforeUpload" :headers="headers"
                    :on-success="onSuccess" :on-error="onError" :disabled="importDataDisabled"
                    style="display: inline-flex;margin-right: 8px" :show-file-list="false">
                    <el-button :disabled="importDataDisabled" type="success" :icon="importDataBtnIcon">
                        {{ importDataBtnText }}
                    </el-button>
                </el-upload>
                <el-button type="warning" icon="el-icon-download" @click="exportData">
                    导出考勤
                </el-button>
            </div>
        </div>
        <div style="margin-top: 10px">
            <el-table :data="table.tableData" border stripe row-key="id" :header-cell-style="{
                background: '#eef1f6', color: '#606266',
                textAlign: 'center', fontWeight: 'bold', borderWidth: '3px'
            }"  v-loading="loading" element-loading-text="拼命加载中..."
        element-loading-spinner="el-icon-loading" element-loading-background="rgba(0, 0, 0, 0.8)">
                <el-table-column prop="workId" label="工号" min-width="125" fixed />
                <el-table-column prop="name" label="姓名" min-width="125" fixed />
                <el-table-column prop="depName" label="部门" min-width="125" fixed />
                <el-table-column v-for="index in dayNum" :label="index + '日'" :key="index" min-width="60">
                    <template slot-scope="scope">
                        <div @click="changeStatus(scope.row, index - 1)">
                            <el-tag :type="table.tagType[scope.row.statusList[index - 1]]">
                                {{ scope.row.statusList[index - 1] }}
                            </el-tag>
                        </div>
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
            <el-dialog title="考勤状态" :visible.sync="dialog.isShow">
                <el-radio-group v-model="dialog.data.status" style="position: center">
                    <el-radio v-for="(item, index) in dialog.statusList" :key="index" :label="item" border />
                </el-radio-group>
                <div slot="footer" class="dialog-footer">
                    <el-button @click="dialog.isShow = false">取消</el-button>
                    <el-button type="primary" @click="confirm">确定</el-button>
                </div>
            </el-dialog>
        </div>
    </div>
</template>

<script>
export default {
    name: "PerAtt",
    data() {
        return {
            dialog: {
                isShow: false,
                data: {},
                statusList: ['正常', '迟到', '早退', '旷工', '休息', '请假']
            },
            table: {
                tagType: {
                    "正常": "success",
                    "迟到": "warning",
                    "早退": "warning",
                    "旷工": "danger",
                    "休息": "info",
                    "请假": ""
                },
                tableData: []
            },
            loading: false,
            headers: {
                Authorization: window.sessionStorage.getItem('tokenStr')
            },
            importDataBtnText: '导入考勤',
            importDataBtnIcon: 'el-icon-upload2',
            importDataDisabled: false,
            total: 0,
            currentPage: 1,
            size: 5,
            dayNum: 0,
            month: '',
            name: ''
        }
    },
    mounted() {
        this.initEmpAttendance();
    },
    methods: {
        initEmpAttendance() {
            this.loading = true;
            let url = '/personnel/attendance/?currentPage=' + this.currentPage + '&size=' + this.size;
            if (this.name) {
                url += '&name=' + this.name;
            }
            if (this.month) {
                url += '&month=' + this.month;
            }
            this.getRequest(url).then(resp => {
                this.loading = false
                this.table.tableData = resp.obj.list;
                this.total = resp.obj.total;
                this.dayNum = resp.obj.dayNum
                this.table.tableData.forEach(item => {
                    item.statusList = item.attendanceList.map(i =>
                        i.status
                    )
                })
            })
            
        },
        changeStatus(row, i) {
            this.dialog.isShow = true
            this.dialog.data = row.attendanceList[i]
        },
        resetSearch() {
            this.name = '';
            this.month = '';
            this.initEmpAttendance();
            this.month = '';
        },
        sizeChange(currentSize) {
            this.size = currentSize;
            this.initEmpAttendance();
        },
        currentChange(currentPage) {
            this.currentPage = currentPage;
            this.initEmpAttendance();
        },
        confirm() {
            this.putRequest('/personnel/attendance/edit', this.dialog.data).then(resp => {
                if (resp) {
                    this.initEmpAttendance();
                    this.dialog.isShow = false;
                }
            })
        },
        onError() {
            this.importDataBtnText = '导入考勤';
            this.importDataBtnIcon = 'el-icon-upload2';
            this.importDataDisabled = false;
        },
        onSuccess(response, file, fileList) {
            this.importDataBtnText = '导入考勤';
            this.importDataBtnIcon = 'el-icon-upload2';
            this.importDataDisabled = false;
            this.initEmpAttendance();
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
            if (this.month == '' || this.month == null) {
                this.$message({
                    type: 'error',
                    message: '请先选择导出考勤月份'
                })
            } else {
                let url = '/personnel/attendance/export?month=' + this.month;
                if (this.name) {
                    url += '&name=' + this.name;
                }
                this.downloadRequest(url);
            }
        },
    }
}
</script>

<style scoped></style>