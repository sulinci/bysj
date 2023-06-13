<template>
  <el-container style="height: 100%">
    <el-aside width="200px" style="box-shadow: 2px 0 6px rgb(0 21 41 / 35%)">
      <el-menu router unique-opened text-color="#fff" background-color="#545c64" active-text-color="#ffd04b">
        <div style="height: 60px; line-height: 60px; text-align: center">
          <b style="color: #fff; font-weight: bold">
            {{ '人事管理系统' }}
          </b>
        </div>
        <el-menu-item index="/index">
          <i class="el-icon-s-home"></i>
          <span>首页</span>
        </el-menu-item>
        <el-submenu :index="index + ''" v-for="(item, index) in routes" v-if="!item.hidden" :key="index">
          <template slot="title">
            <i :class="item.iconCls"></i>
            <span>{{ item.name }}</span>
          </template>
          <el-menu-item :index="children.path" v-for="(children, indexj) in item.children"
            :key="indexj">
            <span>{{ children.name }}</span>
          </el-menu-item>
        </el-submenu>
      </el-menu>
    </el-aside>

    <el-container>
      <el-header class="homeHeader">
        <div></div>
        <div>
          <!-- <el-button icon="el-icon-bell" type="text" style="margin-right: 10px;color: black;" size="normal"
            @click="goChat"></el-button> -->
          <el-dropdown class="userInfo" @command="commandHandler">
            <span class="el-dropdown-link">{{ user.name }}<i><img :src="getImage(user.userFace)"></i></span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="userinfo">个人中心</el-dropdown-item>
              <el-dropdown-item command="logout">注销</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </el-header>

      <el-main style="height: 660px;">
        <el-breadcrumb separator-class="el-icon-arrow-right">
          <el-breadcrumb-item :to="{ path: '/index' }">首页</el-breadcrumb-item>
          <el-breadcrumb-item
            v-if="this.$router.currentRoute.path != '/index'">{{ this.$router.currentRoute.name }}</el-breadcrumb-item>
        </el-breadcrumb>
        <router-view class="homeRouterView" />
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
export default {
  name: "Home",
  data() {
    return {
      user: JSON.parse(window.sessionStorage.getItem("user"))
    }
  },
  computed: {
    routes() {
      return this.$store.state.routes;
    }
  },
  methods: {
    // goChat() {
    //   this.$router.push('/chat');
    // },
    commandHandler(cmd) {
      if (cmd == 'logout') {
        this.$confirm('此操作将注销登录，是否继续？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          //注销
          this.postRequest("/logout");
          //清除用户信息
          window.sessionStorage.removeItem("user");
          window.sessionStorage.removeItem("tokenStr");
          //清空菜单
          this.$store.commit('initRoutes', []);
          //跳转登录页
          this.$router.replace("/login")
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消操作'
          })
        })
      }
      if (cmd == 'userinfo') {
        this.$router.push('/userinfo')
      }
    },
    getImage(image) {
      return `/common/download?name=${image}`
    }
  }
}
</script>

<style>
* {
  margin: 0;
}

.el-menu {
  height: 100%;
  border: none;
  /* 无边框 */
}

.homeHeader {
  background-color: #409eff;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0px 15px;
  box-sizing: border-box;

}

.homeHeader .title {
  font-size: 30px;
  font-family: 华文行楷;
  color: #ffffff;
}

.homeHeader .userInfo {
  cursor: pointer;
}

.homeWelcome {
  text-align: center;
  font-size: 30px;
  font-family: 华文行楷;
  color: #409eff;
  padding-top: 50px;
}

.homeRouterView {
  margin-top: 10px;
}


.el-dropdown-link img {
  width: 48px;
  height: 48px;
  border-radius: 24px;
  margin-left: 8px;
}

.el-dropdown-link {
  display: flex;
  align-items: center;
}
</style>