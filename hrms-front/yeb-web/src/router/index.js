import Vue from 'vue'
import Router from 'vue-router'
import Login from '../views/Login'
import Home from '../views/Home';
import FriendChat from "../views/chat/FriendChat";
import AdminInfo from '../views/AdminInfo';
import Index from '../views/Index';

const originalReplace = Router.prototype.replace
Router.prototype.replace = function replace(location) {
  return originalReplace.call(this, location).catch(err => err);
}


Vue.use(Router)

export default new Router({
  // 去掉url中的#
  // mode: 'history',
  routes: [
    {
      path: '/login',
      name: 'Login',
      component: Login,
      hidden: true
    },
    // {
    //   path: '*',
    //   name: '404',
    //   component: () => import('@/views/error/404'),
    //   hidden: true
    // },
    {
      path: '/',
      name: 'Home',
      component: Home,
      redirect: 'index',
      children:[
        // {
        //   path: '/chat',
        //   name: 'friendChat',
        //   component:FriendChat,
        // },
        {
          path: '/userinfo',
          name: '个人中心',
          component:AdminInfo,
        },
        {
          path: '/index',
          name: '首页',
          component:Index,
        }
      ]
    }

  ]
})
