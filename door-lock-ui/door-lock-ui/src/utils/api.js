import axios from 'axios'
import {Message} from 'element-ui'
import router from '../router'

// 请求拦截器
axios.interceptors.request.use(config => {
// Do something before request is sent
	if(window.localStorage.getItem('tokenStr')){
		config.headers['Authorization'] = window.localStorage.getItem('tokenStr')
	}
	return config;
},error => {
	console.log(error)
// Do something with request <error></error>
	return Promise.reject(error);
});



// 响应拦截器
axios.interceptors.response.use(success=>{
	// 成功掉到接口
	if(success.status && success.status === 200){
		// 业务逻辑错误
		if(success.data.code === 404||success.data.code === 10002||success.data.code === 10005||success.data.code === 10006||success.data.code === 70001||success.data.code === 90002||success.data.code === -999){
			Message.error({message:success.data.msg})
			return;
		}
		if(success.data.msg){
			Message.success({message:success.data.msg})
		}
	}
	return success.data
},error=>{
	if(error.response.code===504||error.response.code === 404){
		Message.error({message:'服务器被吃了，'})
	}else if(error.response.code === 403){
		Message.error({message:'权限不足，请联系管理员'})
	}else if(error.response.code === 401){
		Message.error({message:'尚未登录，请登录'})
		router.replace('/')
	}else{
		if(error.response.data.msg){
			Message.error({message:error.response.data.message})
		}else {
			Message.error({message:'未知错误'})
		}
	}
})
// post请求
let base = ''
export const postRequest = (url,params)=>{
	return axios({
		method:'post',
		url:`${base}${url}`,
		data:params
	})
}

// get请求
export const getRequest = (url,params)=>{
	return axios({
		method:'get',
		url:`${base}${url}`,
		data:params
	})
}

// put请求
export const putRequest = (url,params)=>{
	return axios({
		method:'put',
		url:`${base}${url}`,
		data:params
	})
}
// delete请求
export const deleteRequest = (url,params)=>{
	return axios({
		method:'delete',
		url:`${base}${url}`,
		data:params
	})
}