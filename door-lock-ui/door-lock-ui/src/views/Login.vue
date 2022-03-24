<template>
	<div>
		<el-form :rules="rules"
		         v-loading="loading"
		         element-loading-text="拼命加载中"
		         element-loading-spinner="el-icon-loading"
		         element-loading-background="rgba(0, 0, 0, 0.5)"
		         ref="loginForm"
		         :model="loginForm"
		         class="loginContainer" @keydown.enter.native="submitLogin">
			<h3 class="loginTitle"> 环网柜门禁管理系统</h3>
			<!--只有这样才能有验证规则-->
			<el-form-item prop="username">
				<el-input prefix-icon="el-icon-user-solid" type="text" auto-complete="false" v-model="loginForm.username"
				          placeholder="请输入用户名"></el-input>
			</el-form-item>
			<el-form-item prop="password">
				<el-input prefix-icon="el-icon-lock" type="password" show-password auto-complete="false" v-model="loginForm.password" placeholder="请输入密码"></el-input>
			</el-form-item>
			<el-form-item prop="code">
				<el-input prefix-icon="el-icon-discount" type="text" auto-complete="false" v-model="loginForm.code" placeholder="点击图片更换验证码" style="width: 250px;margin-right: 5px"></el-input>
				<img :src="captchaUrl" @click="updateCaptcha">
			</el-form-item>
			<el-checkbox v-model="checked" class="loginRemember">记住我</el-checkbox>
			<el-button type="primary" style="width: 100%"  @click="submitLogin">登录</el-button>
		</el-form>

	</div>

</template>

<script>


import {initMenu} from "@/utils/menus";
import router from "@/router";
import store from "@/store";

export default {
	name: "Login",
	data() {
		return {
			//time保证验证码能正常刷新
			captchaUrl:"/captcha?time"+new Date(),
			loginForm: {
				username: 'admin',
				password: '123456',
				code: ''
			},
			checked:true,
			loading:false,
			//规则
			rules:{
				//用户名需要，提示信息请输入用户名，时区焦点的时候触发
				username: [{required:true,message:'请输入用户名',trigger:'blur'}],
				password: [{required:true,message:'请输入密码',trigger:'blur'}],
				code: [{required:true,message:'请输入验证码',trigger:'blur'}]
			}
		}
	},
	mounted() {
		this.notLogin();
	},
	methods:{
		updateCaptcha(){
			// 确保验证码能正确刷新
			this.captchaUrl="/captcha?time"+new Date();
		},
		submitLogin(){
			this.$refs.loginForm.validate((valid) => {
				if (valid) {
					//加载界面
					this.loading=true;
					this.postRequest('/login',this.loginForm).then(resp=>{
						if (resp){
							this.loading=false;
							//replace是替换界面不可以使用浏览器后退键来后退
							//而push是可以通过浏览器的后退键来后退的
							const tokenStr = resp.data.tokenHead+" "+resp.data.token;
							//存储用户token
							window.localStorage.setItem('tokenStr',tokenStr);
							// 跳转路由（要拦截用户直接输入）
							// this.$router.replace('/home');

							initMenu(router, store);
							let path=this.$route.query.redirect;
							this.$router.push((path==='/' || path===undefined)?'/home':path);

						}
						this.loading=false;
					});
				} else {
					this.$message.error("请输入填写完整信息")
					return false;
				}
			});
		},
		notLogin(){
			if(window.localStorage.getItem('tokenStr')){
				this.postRequest('/').then(resp=>{
					if (resp){
						this.$router.push("/home");
					}
				})
			}
		}
	}

}
</script>

<style>
.loginContainer{
	border-radius: 15px;
	background-clip: padding-box;
	margin: 180px auto;
	width: 350px;
	padding: 15px 35px 15px 35px;
	background: #fff;
	border: 1px solid #eaeaea;
	box-shadow: 0 0 25px #cac6c6;
}
.loginTitle{
	margin: 0px auto 40px auto;
	text-align: center;
}
.loginRemember{
	text-align: left;
	margin: 0px 0px 15px 0px;
}
.el-form-item__content{
	display: flex;
	align-items: center;
}
</style>