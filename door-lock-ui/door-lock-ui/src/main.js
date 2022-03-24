import Vue from 'vue'
import store from "./store";
import App from './App.vue'
import router from './router'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import {postRequest, getRequest, putRequest, deleteRequest} from './utils/api'
import {initMenu} from "./utils/menus";
import * as echarts from 'echarts';
import dayjs from "dayjs";

//import是ES6的语法打包后会变成require
//控制台的提示
Vue.config.productionTip = false
//引入elementUI后安装它
Vue.use(ElementUI,{size: 'small'});
Vue.prototype.$echarts = echarts
Vue.use(echarts);
Vue.use(dayjs);

// 全局插件引入
Vue.prototype.postRequest = postRequest
Vue.prototype.putRequest = putRequest
Vue.prototype.getRequest = getRequest
Vue.prototype.deleteRequest = deleteRequest

//全局路由前置守卫
router.beforeEach((to, from, next) => {
	//如果用户登录就获取菜单
	if (window.localStorage.getItem('tokenStr')) {
		initMenu(router, store);
		if (!window.localStorage.getItem('user')){
			//判断用户信息是否存在
			return getRequest('/user/info').then(resp=>{
				if (resp){
					//存入用户信息
					window.localStorage.setItem('user',JSON.stringify(resp.data));
					store.commit('INITUSER',resp.data);
					next();
				}
			});
		}

		next();


	} else {
		if (to.path==='/') {
			next();
		}else{
			next('/?redirect='+to.path);
		}
	}
	/*//跳到哪个路由
	console.log(to);
	// 从哪个路由跳转
	console.log(from);
	//有next才是真正的跳转
	//属性是false的话可以中断to，回到from，也可以自定义导航'/',也有error
	next();*/
})

new Vue({
	//router组件
	router,
	store,
	//渲染App
	render: h => h(App)
	//手动挂载app
}).$mount('#app')
