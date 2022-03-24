import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from "@/views/Login";
import UserInfo from "@/views/UserInfo";
import Home from "@/views/Home";


//安装路由
Vue.use(VueRouter);
//配置路由
const routes = [
	{
		//跳转路径
		path: '/',
		//路由名称
		name: 'Login',
		//跳转组件
		component: Login,
		//隐藏不被菜单扫描到
		hidden:true
	},
	{
		//跳转路径
		path: '/home',
		//路由名称
		name: 'Home',
		component: Home,
		children:[{
			//跳转路径
			path: '/userinfo',
			//路由名称
			name: '个人信息',
			component: UserInfo,
		}]
	}

]

//new 一个VueRouter实例
const router = new VueRouter({
	routes
})

//导出，让其他地方可以导入路由
export default router;
