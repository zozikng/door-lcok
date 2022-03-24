<template>
	<div>
		<el-container>
			<el-header class="homeHeader">
				<div class="title"> 环网柜门禁管理系统</div>
				<el-dropdown class="userInfo" @command="commandHandler">
				  <span class="el-dropdown-link">
				    {{user.nickname}}<i><img :src="user.avatar"></i>
				  </span>
					<el-dropdown-menu slot="dropdown">
						<el-dropdown-item command="userinfo">个人中心</el-dropdown-item>
						<el-dropdown-item command="logout">退出</el-dropdown-item>
					</el-dropdown-menu>
				</el-dropdown>
			</el-header>
			<el-container>
				<el-aside width="200px">
					<!--不用菜单激活回调@select="menuClick"，用router启动路由模式-->
					<el-menu router unique-opened>
						<!--v-for循环出路由，一开始的循环会循环整个路由，!item.hidden就是判断是否是login路由-->
						<!-- 有vuex之后可以不从this.$router.options.routes，直接从vuex中获取-->
						<el-submenu
							:index="index +''"
							v-for="(item,index) in routes"
							:key="index"
							v-if="!item.hidden">
							<template slot="title">
								<!--图标-->
								<i :class="item.iconCls"></i>
								<span>{{ item.name }}</span>
							</template>
							<!--绑定index的路由，做二次循环-->
							<el-menu-item
								:index="children.path"
								v-for="(children,indexj) in item.children"
								:key="indexj">
								<i :class="children.iconCls"></i>
								<span>{{ children.name }}</span>
							</el-menu-item>
						</el-submenu>
					</el-menu>
				</el-aside>
				<el-main>
					<el-breadcrumb separator-class="el-icon-arrow-right" v-if="this.$router.currentRoute.path!=='/home'">
						<el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
						<el-breadcrumb-item>{{this.$router.currentRoute.name}}</el-breadcrumb-item>
					</el-breadcrumb>
					<div class="homeWelcome" v-if="this.$router.currentRoute.path==='/home'">
						欢迎来到环网柜门禁管理系统
					</div>
					<router-view/>
				</el-main>
			</el-container>
		</el-container>
	</div>
</template>

<script>

export default {
	name: "Home",
	//计算属性
	computed: {
		routes() {
			return this.$store.state.routes;
		},
		user(){
			return this.$store.state.currentAdmin;
		}
	},
	data() {
		return {
			// user: JSON.parse(window.localStorage.getItem('user'))
		}
	},
	methods:{
		// routes() {
		// 	return this.$store.state.routes;
		// },
		commandHandler(command){
			if (command==='logout'){
				this.$confirm('此操作将注销登录, 是否继续?', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					//注销登录
					this.postRequest('/logout');
					//清空用户信息
					// 清空vuex
					this.$store.commit('initRoutes',[]);
					this.$store.commit('INITUSER',[]);
					window.localStorage.removeItem('tokenStr');
					window.localStorage.removeItem('user');

					//返回登录界面
					this.$router.replace('/');
				}).catch(() => {
					this.$message({
						type: 'info',
						message: '已取消操作'
					});
				});

			}else if(command==='userinfo'){
				this.$router.replace('/userinfo');
			}
		}
	}
}
</script>

<style scoped>
.homeHeader {
	background-color: #409eff;
	display: flex;
	align-items: center;
	justify-content: space-between;
	padding: 0px 15px;
	box-sizing: border-box;
}

.title {
	font-size: 20px;
	font-family: 微软雅黑;
	font-weight: bold;
	color: cornsilk;
}
.userInfo{
	cursor: pointer;
	align-items: center;
}
.el-dropdown-link img{
	width: 48px;
	height: 48px;
	border-radius: 24px;
	margin-left: 15px;
}
.homeWelcome{
	text-align: center;
	font-size: 30px;
	font-family: 微软雅黑;
	font-weight: bold;
	color: #409eff;
	padding-top: 50px;
}
</style>