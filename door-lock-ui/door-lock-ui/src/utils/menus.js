//因为在js环境下所以不能this.,要引入
import {getRequest} from "./api";


export const initMenu=(router,store)=>{
	//如果store中有路由，则直接返回
	if (store.state.routes.length>0){
		return;
	}

	//如果没有发起请求
	getRequest('/system/menu/getMenuBycurrentUser').then(data=>{
		//数据存在格式化router
		if (data){
			//格式化好的Router
			let fmtRoutes=formatRoutes(data);
			//添加到router
			router.addRoutes(fmtRoutes);
			//将数据存入vuex
			store.commit('initRoutes',fmtRoutes);
		}
	})
}
//格式化router
export const formatRoutes=(routers)=>{
	let fmtRoutes=[];
	routers.forEach(router=>{
		let {
			path,
			component,
			name,
			iconCls,
			children,
		}=router;
		if (children && children instanceof Array){
			//递归
			children=formatRoutes(children);
		}
		//格式化
		let fmRouter={
			path:path,
			name:name,
			iconCls:iconCls,
			children:children,
			component(resolve){
			//	es5语法
				if (component.startsWith('Home')){
					require(['../views/'+component+'.vue'],resolve);
				} else if (component.startsWith('Sys')) {
					require(['../views/sys/' + component + '.vue'], resolve);
				}else if (component.startsWith('Doorlock')){
					require(['../views/doorlock/' + component + '.vue'], resolve);
				}
			}
		}
		//格式化好的放入数组
		fmtRoutes.push(fmRouter)
	});
	return fmtRoutes;
}