<template>
	<div>
		<el-divider></el-divider>
		<!--卡片-->
		<div class="user-container">
			<el-card class="user-card">
				<div slot="header" class="clearfix">
					<span>个人信息</span>
					<el-button style="float: right; padding: 3px;color: cornflowerblue" type="text" icon="el-icon-lock"
					           @click="showUpdatePassword">修改密码
					</el-button>
					<el-button style="float: right;  padding: 3px;color: #67C23A" type="text" icon="el-icon-edit"
					           @click="showEditUserInfo">编辑信息
					</el-button>
				</div>
				<div  class="userInfo-container" v-model="userinfo">
					<div>
						账户：
						<el-tag type="" >{{userinfo.username}}</el-tag>
					</div>
					<div>
						昵称：
						<el-tag type="" >{{userinfo.nickname}}</el-tag>
					</div>
					<div>
						性别：
						<el-tag type="" >{{userinfo.gender}}</el-tag>
					</div>
				</div>
			</el-card>
		</div>

		<el-dialog
			title="编辑信息"
			:visible.sync="dialogVisible"
			width="35%">
			<div>
				<el-form ref="adduserForm"
				         :rules="rules"
				         :model="eduserinfo">

					<el-row :gutter="20">
						<el-col :span="10">
							<el-form-item label="用户名:" prop="username">
								<el-input size="mini" style="width: 150px" type="text" v-model="eduserinfo.username"
								          placeholder="请输入用户名" prefix-icon="el-icon-edit"></el-input>
							</el-form-item>
						</el-col>
						<el-col :span="10">
							<el-form-item label="昵称/姓名:" prop="nickname">
								<el-input size="mini" style="width: 150px" type="text" v-model="eduserinfo.nickname"
								          placeholder="请输入姓名或昵称" prefix-icon="el-icon-edit"></el-input>
							</el-form-item>
						</el-col>
						<el-col :span="10">
							<el-form-item label="性 别:" prop="gender">
								<el-radio-group style="margin-top: 8px;margin-left: 10px" v-model="eduserinfo.gender">
									<el-radio label="男">男</el-radio>
									<el-radio label="女">女</el-radio>
								</el-radio-group>
							</el-form-item>

						</el-col>
					</el-row>
				</el-form>
			</div>
			<span slot="footer" class="dialog-footer">
			    <el-button @click="dialogVisible=false">取 消</el-button>
			    <el-button type="primary" @click="editUserInfo">确 定</el-button>
			  </span>
		</el-dialog>

		<el-dialog
			title="修改密码"
			:visible.sync="passwordDialogVisible"
			width="30%">
			<div>
				<el-form :model="ruleForm" status-icon :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">

					<el-form-item label="请输入旧密码" prop="oldPass">
						<el-input type="password" v-model="ruleForm.oldPass" autocomplete="off"></el-input>
					</el-form-item>
					<el-form-item label="请输入新密码" prop="pass">
						<el-input type="password" v-model="ruleForm.pass" autocomplete="off"></el-input>
					</el-form-item>
					<el-form-item label="请确认新密码" prop="checkPass">
						<el-input type="password" v-model="ruleForm.checkPass" autocomplete="off"></el-input>
					</el-form-item>
					<el-form-item>
						<el-button type="primary" @click="submitForm('ruleForm')">提交</el-button>
						<el-button @click="resetForm('ruleForm')">重置</el-button>
					</el-form-item>
				</el-form>
			</div>
<!--			<span slot="footer" class="dialog-footer">
			    <el-button @click="passwordDialogVisible=false">取 消</el-button>
			    <el-button type="primary" @click="passwordDialogVisible=false">确 定</el-button>
			  </span>-->
		</el-dialog>
	</div>
</template>

<script>
import {getRequest} from "@/utils/api";
import store from "@/store";

export default {
	name: "UserInfo",
	data(){
		var validatePass = (rule, value, callback) => {
			if (value === '') {
				callback(new Error('请输入密码'));
			} else {
				if (this.ruleForm.checkPass !== '') {
					this.$refs.ruleForm.validateField('checkPass');
				}
				callback();
			}
		};
		var validatePass2 = (rule, value, callback) => {
			if (value === '') {
				callback(new Error('请再次输入密码'));
			} else if (value !== this.ruleForm.pass) {
				callback(new Error('两次输入密码不一致!'));
			} else {
				callback();
			}
		};
		return{
			userinfo:[],
			dialogVisible:false,
			passwordDialogVisible:false,
			eduserinfo:{
				userId:null,
				username:"",
				password:"",
				nickname:"",
				gender:""
			},
			ruleForm: {
				pass: '',
				checkPass: '',
				age: '',
				oldPass:''
			},
			//规则
			rules: {
				//用户名需要，提示信息请输入用户名，时区焦点的时候触发
				username: [{required: true, message: '请输入用户名', trigger: 'blur'}],
				nickname: [{required: true, message: '请输入昵称或姓名', trigger: 'blur'}],
				gengder: [{required: true, message: '请选择性别', trigger: 'change'}],
				pass: [
					{ validator: validatePass, trigger: 'blur' }
				],
				checkPass: [
					{ validator: validatePass2, trigger: 'blur' }
				],
				oldPass: [
					{ validator: validatePass, trigger: 'blur' }
				]
			}
		}
	},
	mounted() {
		this.initUserInfo();
	},
	methods:{
		submitForm(formName) {
			this.$refs[formName].validate((valid) => {
				if (valid) {
					this.userinfo.password=this.ruleForm.oldPass;
					this.putRequest('/system/sysuser/currentUserUpdateInfo/'+this.userinfo.id+'/'+this.ruleForm.oldPass+'/'+this.ruleForm.pass).then(resp=>{
						if(resp){
							this.$store.commit('initRoutes',[]);
							this.$store.commit('INITUSER',[]);
							window.localStorage.removeItem('tokenStr');
							window.localStorage.removeItem('user');
							this.$router.replace("/");
						}
					})
					this.passwordDialogVisible=false;

				} else {
					console.log('error submit!!');
					return false;
				}
			});
		},
		resetForm(formName) {
			this.$refs[formName].resetFields();
		},
		//显示修改密码编辑框
		showUpdatePassword(){
			this.passwordDialogVisible=true;
			Object.assign(this.eduserinfo,this.userinfo);
		},
		//编辑信息
		editUserInfo(){
			this.$refs.adduserForm.validate((valid) => {
				if (valid) {
					this.postRequest('/system/sysuser/updateUser', this.eduserinfo).then(resp => {
						if (resp) {
							getRequest('/user/info').then(resp=>{
								if (resp){
									//更新用户信息后，更新用户角色，通过返回的id获取用户
									window.localStorage.setItem('user',JSON.stringify(resp.data));
									this.$store.commit('INITUSER',resp.data);
									this.dialogVisible = false;
									this.initUserInfo();
								}
							});

						}
					})
				} else {
					this.$message.error("请输入填写完整信息")
					return false;
				}
			})
		},
		//显示用户信息编辑
		showEditUserInfo(){
			this.dialogVisible=true;
			Object.assign(this.eduserinfo,this.userinfo);
		},
		//初始化用户信息
		initUserInfo(){
			this.userinfo=JSON.parse(window.localStorage.getItem('user'));
		}

	}

}
</script>

<style scoped>
.user-container {
	display: flex;
	justify-content: space-around;
	flex-wrap: wrap;
	margin-top: 20px;
}

.user-card {
	width: 500px;
	margin-bottom: 20px;
}

.userInfo-container{
	font-size: 14px;
	line-height: initial;
	color: #409eff;
}
.userInfo-container div{
	font-size: 14px;
	line-height: initial;
	color: #409eff;
	margin-top: 20px;

	margin-left:  100px;
	margin-bottom: 40px;

}
</style>