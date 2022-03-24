<template>
	<div>
		<el-divider></el-divider>

		<div style="display: flex;justify-content: space-between">
			<div>
				<el-input style="width: 300px;margin-right: 10px"
				          prefix-icon="el-icon-search"
				          v-model="nickName"
				          @keydown.enter.native="initUsers"
				          clearable
				          @clear="initUsers"
				          placeholder="请输入员工姓名或昵称搜索..."></el-input>
				<el-button type="primary" icon="el-icon-search" @click="initUsers">搜索</el-button>
			</div>

			<div>
				<el-button type="success" icon="el-icon-plus" size="small" @click="showAddUserView">添加用户</el-button>
			</div>
		</div>
		<div>
			<el-table
				:data="users"
				border
				stripe
				style="width: 100%;margin-top: 10px">
				<el-table-column
					type="selection"
					width="55">
				</el-table-column>
				<el-table-column
					prop="username"
					label="用户名"
					width="180">
				</el-table-column>
				<el-table-column
					prop="nickname"
					label="昵称"
					width="180">
				</el-table-column>
				<el-table-column
					prop="enabled"
					label="是否启用"
					width="100">
					<template slot-scope="scope">
						<el-tag type="success" v-if="scope.row.enabled">已启用</el-tag>
						<el-tag type="danger" v-else>未启用</el-tag>
					</template>
				</el-table-column>
				<el-table-column
					prop="roles"
					label="角色">
					<template slot-scope="scope">
						<el-tag style="margin-right: 4px;" type="success" v-for="(role,index) in scope.row.roles"
						        :key="index">
							{{ role.nameZh }}
						</el-tag>
						<!-- 16、更新操作员角色 弹出框、选择器、 -->
						<!-- 20、@show="showPop(admin)" -->
						<!-- 24、@hide="hidePop(admin)" hide 隐藏时触发-->
						<!--						<el-popover-->
						<!--							placement="right"-->
						<!--							title="角色列表"-->
						<!--							width="200"-->
						<!--							@show="showPop(admin)"-->
						<!--							@hide="hidePop(admin)"-->
						<!--							trigger="click">-->
						<!--							&lt;!&ndash; 17、更新操作员角色 下拉框 &ndash;&gt;-->
						<!--							&lt;!&ndash; 22、v-model="selectedRoles" 存的是1个角色id，multiple 多选，显示已有角色 &ndash;&gt;-->
						<!--							-->
						<!--							&lt;!&ndash; 3个点按钮 ... &ndash;&gt;-->
						<!--							<el-button slot="reference" type="text" icon="el-icon-more"></el-button>-->
						<!--						</el-popover>-->
					</template>
				</el-table-column>
				<el-table-column
					fixed="right"
					label="操作"
					width="200">
					<template slot-scope="scope">
						<el-button type="primary" @click="showEditUser(scope.row)">编辑</el-button>
						<el-button type="danger" @click="deleteUser(scope.row)">删除</el-button>
					</template>
				</el-table-column>

			</el-table>
			<div style="display: flex;justify-content: flex-start;margin-top: 10px">
				<el-pagination
					background
					@current-change="currentChange"
					@size-change="sizeChange"
					:page-sizes="[1,5,10,15,20]"
					layout="prev, pager, next,sizes, jumper, ->, total"
					:total=total>
				</el-pagination>
			</div>

		</div>
		<el-dialog
			:title="title"
			:before-close="canceladd"
			:visible.sync="dialogVisible"
			width="40%">
			<div>
				<el-form ref="adduserForm"
				         :rules="rules"
				         :model="user">

					<el-row :gutter="20">
						<el-col :span="10">
							<el-form-item label="用户名:" prop="username">
								<el-input size="mini" style="width: 150px" type="text" v-model="user.username"
								          placeholder="请输入用户名" prefix-icon="el-icon-edit"></el-input>
							</el-form-item>
						</el-col>
						<el-col :span="10">
							<el-form-item label="昵称/姓名:" prop="nickname">
								<el-input size="mini" style="width: 150px" type="text" v-model="user.nickname"
								          placeholder="请输入姓名或昵称" prefix-icon="el-icon-edit"></el-input>
							</el-form-item>
						</el-col>
						<el-col :span="10">
							<el-form-item label="密 码:" prop="password">
								&nbsp;&nbsp;<el-input size="mini" style="width: 150px" type="password"
								                      v-model="user.password"
								                      placeholder="请输入密码" prefix-icon="el-icon-edit"></el-input>
							</el-form-item>
						</el-col>
						<el-col :span="10">
							<el-form-item label="性别:" prop="gender">

								<el-radio-group style="margin-top: 8px" v-model="user.gender">
									<el-radio label="男">男</el-radio>
									<el-radio label="女">女</el-radio>
								</el-radio-group>
							</el-form-item>

						</el-col>
						<el-col :span="10">
							<el-form-item label="是否启用:" prop="enable">
								<el-tooltip :content="'Switch value: ' + user.enabled" placement="top" style="margin-top: 8px">
									<el-switch
										v-model="user.enabled"
										active-color="#13ce66"
										inactive-color="#ff4949"
									>
									</el-switch>
								</el-tooltip>
							</el-form-item>
						</el-col>
						<el-col :span="10">
							<el-form-item label="用户角色:" prop="roles">
								<div>
									<el-tag type="success" style="margin-right: 4px;margin-top: 5px;"
									        v-for="(role,indexj) in roles" :key="indexj">
										{{ role.nameZh }}
									</el-tag>
									<el-popover
										placement="bottom"
										title="用户角色"
										width="200"
										@show="showPop(roles)"
										trigger="click">
										<el-select v-model="selectRoles" multiple placeholder="请选择">
											<el-option
												v-for="(r,index) in allRoles"
												:key="index"
												:label="r.nameZh"
												:value="r.id">
											</el-option>
										</el-select>
										<el-button slot="reference" type="text" icon="el-icon-more"></el-button>
									</el-popover>

								</div>
							</el-form-item>
						</el-col>
					</el-row>
				</el-form>
			</div>
			<span slot="footer" class="dialog-footer">
			    <el-button @click="canceladd">取 消</el-button>
			    <el-button type="primary" @click="doAdduser">确 定</el-button>
			  </span>
		</el-dialog>
	</div>

</template>

<script>
export default {
	name: "SysUser",
	data() {
		return {
			//标题
			title: '',
			//所有用户
			users: [],
			total: 0,
			currentPage: 1,
			pageSize: 10,
			nickName: '',
			dialogVisible: false,
			//单个用户
			user: {
				enabled: false,
				gender: "",
				id: 0,
				nickname: "",
				password: null,
				username: ""
			},
			//角色
			roles: {
				id: "",
				name: "",
				nameZh: ""
			},
			//所有角色
			allRoles:[],
			//所选中的角色
			selectRoles:[],
			//规则
			rules: {
				//用户名需要，提示信息请输入用户名，时区焦点的时候触发
				username: [{required: true, message: '请输入用户名', trigger: 'blur'}],
				password: [{required: true, message: '请输入密码', trigger: 'blur'}],
				nickname: [{required: true, message: '请输入昵称或姓名', trigger: 'blur'}],
				gengder: [{required: true, message: '请选择性别', trigger: 'change'}]
			}
		}
	},
	mounted() {
		this.initUsers();
	},
	methods: {
		showPop(roles){
			this.initAllRoles();
			this.selectRoles=[];
			roles.forEach(r=>{
				this.selectRoles.push(r.id);
			})
		},
		//初始化所有角色
		initAllRoles(){
			this.getRequest('/system/sysuser/roles').then(resp=>{
				if (resp){
					this.allRoles=resp;
				}
			})
		},
		//弹出编辑用户
		showEditUser(data) {
			this.title = '编辑用户';
			//用数据拷贝
			Object.assign(this.user, data);
			this.roles = data.roles;
			this.roles.forEach(r=>{
				this.selectRoles.push(r.id);
			})
			/*this.user=data;//不用数据双向绑定*/
			this.user.enabled = data.enabled;
			this.dialogVisible = true;

		},
		//删除用户
		deleteUser(data) {
			this.$confirm('此操作将永久删除昵称为【' + data.nickname + '】的用户，是否继续?', '提示', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning'
			}).then(() => {
				this.deleteRequest('/system/sysuser/deleteUser/' + data.id).then(resp => {
					if (resp) {
						this.initUsers();
					}
				})
			}).catch(() => {
				this.$message({
					type: 'info',
					message: '已取消删除'
				});
			});
		},
		//取消添加用户
		canceladd() {
			this.dialogVisible = false;
			this.resetForm('adduserForm');
		},
		//重置表单
		resetForm(formName) {
			this.$refs[formName].resetFields();
			this.user = {
				enabled: false,
				gender: "",
				id: 0,
				nickname: "",
				password: null,
				username: "",
			}
			this.roles = null;
		},
		//确认添加用户
		doAdduser() {
			//更新用户信息
			if (this.user.id) {
				/*对表单进行部分校验*/
				let fieldToValidate = [
					'username',
					'nickname',
					'gender'
				];
				//单个进行校验
				Promise.all(fieldToValidate.map(item => {
					let p = new Promise((resolve, reject) => {
						this.$refs.adduserForm.validateField(item, valid => {
							resolve(valid)
						})
					})
					return p;
				})).then(results => {
					results = results.filter(item => item)
					/*当results为空数组时表明校验通过*/
					if (!results.length) {
						this.postRequest('/system/sysuser/updateUser', this.user).then(resp => {
							if (resp) {
								//更新用户信息后，更新用户角色
								//拼接url
								let url='/system/sysuser/userRole?userId='+this.user.id;
								this.selectRoles.forEach(sr=>{
									url+='&rids='+sr;
								});
								//发送更新用户角色请求
								this.putRequest(url).then(respo=>{
									if (respo){
										this.initUsers();
										this.selectRoles=[];
									}

								})
								this.dialogVisible = false;
								this.initUsers();
								this.resetForm('adduserForm');
							}
						})
					} else {
						this.$message.error("请输入填写完整信息")
						return false;
					}
				})

				/*this.$refs.adduserForm.validate((valid)=>{
					if (!valid) {
						this.postRequest('/system/sysuser/updateUser', this.user).then(resp => {
							if (resp) {
								this.dialogVisible = false;
								this.initUsers();
								this.resetForm('adduserForm')
							}
						})
					}/!*else{
						this.$message.error("请输入填写完整信息")
						return false;
					}*!/
				})*/
			} else {//添加用户

				this.$refs.adduserForm.validate((valid) => {
					if (valid) {
						this.postRequest('/system/sysuser/addUser', this.user).then(resp => {
							if (resp) {
								//更新用户信息后，更新用户角色，通过返回的id获取用户
								if (resp.data){
									//拼接url
									let url='/system/sysuser/userRole?userId='+resp.data;
									this.selectRoles.forEach(sr=>{
										url+='&rids='+sr;
									});
									//发送更新用户角色请求
									this.putRequest(url).then(respo=>{
										if (respo){
											this.initUsers();
											this.selectRoles=[];
										}

									})
								}
								this.dialogVisible = false;
								this.initUsers();
								this.resetForm('adduserForm')
							}
						})
					} else {
						this.$message.error("请输入填写完整信息")
						return false;
					}
				})
			}
		},
		//每页显示数量改变
		sizeChange(pageSize) {
			this.pageSize = pageSize;
			this.initUsers();
		},
		//改变当前页
		currentChange(currentPage) {
			this.currentPage = currentPage;
			this.initUsers();
		},
		//显示添加用户界面
		showAddUserView() {
			this.title = '添加用户';
			this.dialogVisible = true;
			this.resetForm('adduserForm');
		},
		//初始化用户类别
		initUsers() {
			this.getRequest('/system/sysuser/getAllUsers/?currentPage=' + this.currentPage + '&pageSize=' + this.pageSize + '&keyWords=' + this.nickName).then(resp => {
				if (resp) {
					this.users = resp.records;
					this.total = resp.total;
				}
			})
		}
	}
}
</script>

<style scoped>

</style>