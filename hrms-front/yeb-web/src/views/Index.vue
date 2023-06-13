<template>
  <el-row class="home" :gutter="20">
    <el-col :span="8" style="margin-top: 20px">
      <el-card>
        <div style="display: flex;">
          <div>
            <img :src="getImage(user.userFace)" style="height: 150px;width: 150px;border-radius: 75px;" alt="" />
          </div>
          <div style="margin-left: 20px;margin-top: 40px;">
            <p class="name">{{ user.name }}</p>
            <p style="margin-bottom: 15px;font-weight: bold;">{{ currentDateInfo }}</p>
          </div>
        </div>
        <el-divider></el-divider>
        <div class="login-info">
          <p>电话：<span>{{ user.phone }}</span></p>
          <p>地址：<span>{{ user.address }}</span></p>
        </div>
      </el-card>
      <el-card style="margin-top: 40px;height:500px">
        <el-calendar v-model="value">
        </el-calendar>
      </el-card>
    </el-col>
    <el-col :span="16">
      <div class="num">
        <el-card v-for="(item, index) in countData" :key="index" :body-style="{ display: 'flex', padding: 0 }"
          style="margin-top: 20px">
          <i class="icon" :class="'el-icon-' + item.icon" :style="{ background: item.color }" />
          <div class="detail" style="width: 150px;">
            <p class="txt" style=" margin-bottom: 5px">{{ item.name }}</p>
            <p class="num">{{ item.value }}</p>
          </div>
        </el-card>
      </div>
      <el-card style="height: 300px;margin-top: 20px;">
        <div style="height: 280px;width: 820px;" ref="soc"></div>
      </el-card>
      <div style="margin-top: 20px;display: flex;justify-content: space-between;">
        <el-card style="height: 300px">
          <div style="height: 280px;width: 370px;" ref="emp"></div>
        </el-card>
        <el-card style="height: 300px">
          <div style="height: 280px;width: 370px;" ref="department"></div>
        </el-card>
      </div>
    </el-col>
  </el-row>
</template>

<script>
import * as echarts from 'echarts';

export default {
  data() {
    return {
      user: JSON.parse(window.sessionStorage.getItem("user")),
      value: new Date(),
      dayOfWeek: ['星期天', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六'],
      countData: [
        {
          name: '员工总数',
          value: 0,
          icon: 'user',
          color: '#409EFF'
        },
        {
          name: '状态正常',
          value: 0,
          icon: 'star-on',
          color: '#67C23A'
        },
        {
          name: '今日请假',
          value: 0,
          icon: 's-goods',
          color: '#409EFF'
        },
        {
          name: '今日迟到',
          value: 0,
          icon: 'timer',
          color: '#E6A23C'
        },
        {
          name: '今日早退',
          value: 0,
          icon: 'watch',
          color: '#E6A23C'
        }
        , {
          name: '今日旷工',
          value: 0,
          icon: 'lock',
          color: '#F56C6C'
        }
      ],
      socOption: {
        title: {
          text: '部分部门五险一金比例'
        },
        legend: {
          // orient: 'vertical',
          left: 'right'
        },
        tooltip: {
          trigger: 'item',
          formatter: function (params) {
            return "<a>" + params.name + "</a></a>" + "<br /><a>" + params.seriesName + ": " + params.data[params.seriesIndex + 1] * 100 + "%</a>"
          },
          textStyle: {
            fontWeight: 'bold',
          },
        },
        dataset: {
          source: []
        },
        xAxis: { type: 'category' },
        yAxis: {
          type: 'value',
          name: '百分比',
          axisLabel: {
            formatter: function (val) {
              return val * 100 + "%"
            }
          }
        },
        series: [{ type: 'line' }, { type: 'line' }, { type: 'line' }, { type: 'line' }, { type: 'line' }, { type: 'line' }]
      },
      empOption: {
        title: {
          text: new Date().getFullYear() + '年员工入职情况',
          left: 'center'
        },
        tooltip: {
          trigger: 'item',
          formatter: '{b}: {c}人',
          textStyle: {
            fontWeight: 'bold',
          },
        },
        xAxis: {
          type: 'category',
          data: []
        },
        yAxis: {
          type: 'value',
          name: '人数',
        },
        series: [
          {
            data: [],
            type: 'line'
          },
          {
            data: [],
            type: 'bar'
          }
        ]
      },
      departmentOption: {
        title: {
          text: '员工分布情况',
          left: 'center'
        },
        legend: {
          orient: 'vertical',
          left: 'left'
        },
        tooltip: {
          trigger: 'item',
          formatter: '{b}: {c}人',
          textStyle: {
            fontWeight: 'bold',
          },
        },
        toolbox: {
          show: true,
          feature: {
            mark: { show: true },
            dataView: { show: true, readOnly: false },
            restore: { show: true },
            saveAsImage: { show: true }
          },
        },
        series: [
          {
            name: 'Nightingale Chart',
            type: 'pie',
            radius: [10, 120],
            center: ['50%', '55%'],
            roseType: 'area',
            label: {            //饼图图形上的文本标签
              normal: {
                show: true,
                position: 'inner', //标签的位置
                textStyle: {
                  fontWeight: 'bold',
                },
                formatter: '{d}%'
              }
            },
            itemStyle: {
              borderRadius: 3
            },
            data: []
          }
        ]
      }
    }
  },
  created() {
  },
  mounted() {
    this.getCountData();
    this.getSocData();
    this.getEmpData();
    this.getDepartmentData();
  },
  computed: {
    currentDateInfo() {
      let date = new Date()
      let year = date.getFullYear()
      let month = date.getMonth() + 1
      let currentDate = date.getDate()
      let day = date.getDay()
      return year + "年" + month + "月" + currentDate + "日" + " " + this.dayOfWeek[day]
    }
  },
  methods: {
    //获取图片
    getImage(image) {
      return `/common/download?name=${image}`
    },
    getCountData() {
      this.getRequest('/common/count').then(resp => {
        if (resp) {
          this.countData[0].value = resp.obj.totalNum
          this.countData[1].value = resp.obj.normalNum
          this.countData[2].value = resp.obj.leaveCount
          this.countData[3].value = resp.obj.lateCount
          this.countData[4].value = resp.obj.leaveEarlyCount
          this.countData[5].value = resp.obj.absenteeismCount
        }
      })
    },
    getSocData() {
      this.getRequest('/common/soc').then(resp => {
        if (resp) {
          let socData = resp.obj.map(item => [item.name, item.comPensionRate, item.comMedicalRate, item.comUnemploymentRate, item.comMaternityRate, item.comInjuryRate, item.comHouseRate])
          socData.unshift(['product', '养老保险', '医疗保险', '失业保险', '生育保险', '工伤保险', '公积金'])
          this.socOption.dataset.source = socData
          const socChart = echarts.init(this.$refs.soc)
          socChart.setOption(this.socOption)
        }
      })
    },
    getEmpData() {
      this.getRequest('/common/emp').then(resp => {
        if (resp) {
          const quarters = ['一季度', '二季度', '三季度', '四季度']
          this.empOption.xAxis.data = quarters
          this.empOption.series.forEach(item => {
            item.data = resp.obj
          })
          const empChart = echarts.init(this.$refs.emp);
          empChart.setOption(this.empOption);
        }
      })
    },
    getDepartmentData() {
      this.getRequest('/common/dep').then(resp => {
        this.departmentOption.series[0].data = resp.obj
        const departmentChart = echarts.init(this.$refs.department)
        departmentChart.setOption(this.departmentOption)
      })
    }
  }
}
</script>

<style>
.el-calendar-table .el-calendar-day {
  height: 50px;
}

.num {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
}

.icon {
  font-size: 30px;
  width: 80px;
  height: 80px;
  text-align: center;
  line-height: 80px;
  color: #fff;
}

.detail {
  margin-left: 15px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.txt {
  font-size: 14px;
  color: #999999;
}

.home .num .detail .num {
  font-size: 30px;
  margin-bottom: 10px;
}
</style>