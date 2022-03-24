<template>
	<div>
		<el-divider></el-divider>
		<div class="permissionManaTool">
			<el-input size="small" placeholder="请输入角色英文名" v-model="role.name">
				<template size="small" slot="prepend">ROLE</template>
			</el-input>
			<el-input size="small" placeholder="请输入角色中文名" v-model="role.nameZh"></el-input>
			<el-button size="small" type="primary" icon="el-icon-plus" @click="doAddRole">添加角色</el-button>
		</div>
		<div class="permissionManaMain">
			<el-collapse v-model="activeName" accordion @change="change">
				<el-collapse-item :title="r.nameZh" :name="r.id" v-for="(r,index) in roles" :key="index">
					<el-card class="box-card">
						<div slot="header" class="clearfix">
							<span>可访问资源</span>
							<el-button style="float: right; padding: 3px;color: #ff0000"  type="text" icon="el-icon-delete" @click="doDeleteRole(r)"></el-button>
						</div>
						<div>
							<el-tree
								show-checkbox
								:data="allMenus"
								:props="defaultProps"
								ref="tree"
								:key="index"
								:default-checked-keys="selectedMenus"
								node-key="id"></el-tree>
							<div style="display: flex;justify-content: flex-end">
								<el-button size="mini" @click="cancelUpdate">取消修改</el-button>
								<el-button size="mini" type="primary" @click="doUpdate(r.id,index)">确认修改</el-button>
							</div>
						</div>
					</el-card>
				</el-collapse-item>
			</el-collapse>
		</div>
	</div>
</template>

<script>


export default {
	name: "SysPermiss",
	data(){
		return {
			role:{
				name:'',
				nameZh:''
			},
			roles:[],
			allMenus:[],
			defaultProps: {
				children: 'children',
				label: 'name'
			},
			selectedMenus:[],
			//默认-1全部面板折叠
			activeName:-1
		}
	},
	mounted() {
		//钩子单进入页面时候去请求
		this.initRoles();
	},
	methods:{
		doDeleteRole(role){
			this.$confirm('此操作将永久删除名称为[' + role.nameZh + ']的角色，是否继续?','提示',{
				confirmButtonText:'确定',
				cancelButtonText:'取消',
				type:'warning'
			}).then(() => {
				this.deleteRequest( '/system/permiss/deleteRole/' + role.id).then(resp => {
					if (resp) {
						this.initRoles();
					}
				})
			}).catch(() => {
				this.$message({
					type: 'info' ,
					message: '已取消删除'});
			});

		},
		doAddRole(){
			if (this.role.name&&this.role.nameZh){
				this.postRequest("/system/permiss/addRole",this.role).then(resp=>{
					if (resp){
						this.initRoles();
						this.role.name='';
						this.role.nameZh='';
					}
				})
			}else {
				this.$message.error("所有字段不能为空");
			}
		},
		//取消更新
		cancelUpdate(){
			this.activeName=-1;
		},
		//更新角色菜单
		doUpdate(rid,index){
			//依据循环中的index获取tree，tree被ref引用
			let tree =this.$refs.tree[index];
			// true为根结点不需要被选中
			let selectedKeys = tree.getCheckedKeys();
			let url='/system/permiss/updateMenuRole/?rid='+rid;
			//拼接mids
			selectedKeys.forEach(key=>{
				url+='&mids='+key;
			})
			console.log(url)
			//发送请求
			this.putRequest(url).then(resp=>{
				if (resp){
					//初始化角色
					this.initSelectedMenus(rid);
					this.activeName=-1;
				}
			})

		},
		//（初始化）获取已有权限（菜单）
		initSelectedMenus(rid){
			this.getRequest("/system/permiss/getMidByRid/"+rid).then(resp=>{
				if (resp) {
					this.selectedMenus =resp;
				}
			})
		},
		//改变列表时重新获取权限（菜单）
		change(rid){
			if (rid) {
				this.initSelectedMenus(rid);
				this.initAllMenu();
			}
		},
		//(初始化)获取菜单（权限）
		initAllMenu(){
			this.getRequest('/system/permiss/getAllMenus').then(resp=>{
				if (resp){
					this.allMenus=resp;
				}
			})
		},
		//（初始化）获取角色
		initRoles(){
			this.getRequest('/system/permiss/getAllRoles').then(resp=>{
				if (resp){
					this.roles=resp;

				}
			})
		}
	}
}
</script>

<style>
.permissionManaTool{
	display: flex;
	margin-top: 20px;
}
.permissionManaTool .el-input{
	width: 300px;
	margin-right: 6px;
}
.permissionManaMain{
	margin-top: 15px;
	width: 700px;
}

</style>