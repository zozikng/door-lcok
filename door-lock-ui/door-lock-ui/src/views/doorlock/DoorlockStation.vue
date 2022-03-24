<template>
	<div>

		<el-divider></el-divider>
		<div style="display: flex;justify-content: space-between">
			<div>
				<el-button type="success" icon="el-icon-plus" size="small" @click="showAddStation">添加站</el-button>
			</div>
		</div>

		<div>
			<el-table
				:data="stations"
				border
				stripe
				style="width: 100%;margin-top: 10px">
				<el-table-column
					type="selection"
					width="55">
				</el-table-column>
				<el-table-column
					prop="stationName"
					label="站名"
					width="200">
				</el-table-column>
				<el-table-column
					prop="stationAddress"
					label="站地址"
					width="350">
				</el-table-column>
				<el-table-column
					prop="stationIp"
					label="站IP"
					width="200">
				</el-table-column>
				<el-table-column
					prop="stationStatus"
					label="是否在线"
					width="250">
					<template slot-scope="scope">
						<el-tag type="success" v-if="scope.row.stationStatus">在线</el-tag>
						<el-tag type="danger" v-else>未在线</el-tag>
					</template>
				</el-table-column>
				<el-table-column
					fixed="right"
					label="操作"
					width="200">
					<template slot-scope="scope">
						<el-button type="primary" @click="showEditStation(scope.row)">编辑</el-button>
						<el-button type="danger" @click="doDeleteStation(scope.row)">删除</el-button>
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
		<!--弹框-->
		<el-dialog
			:title="title"
			:visible.sync="dialogVisible"
			width="38%">
			<div>
				<el-form ref="addStationForm"
				         :rules="rules"
				         :model="station">
					<el-row :gutter="20">
						<el-col :span="10">
							<el-form-item label="站名称:" prop="stationName">
								<el-input size="mini" style="width: 150px" type="text" v-model="station.stationName"
								          placeholder="请输入站名称" prefix-icon="el-icon-edit"></el-input>
							</el-form-item>
						</el-col>
						<el-col :span="10">
							<el-form-item label="站地址:" prop="stationAddress">
								<el-input size="mini" style="width: 150px" type="text" v-model="station.stationAddress"
								          placeholder="请输入站地址" prefix-icon="el-icon-edit"></el-input>
							</el-form-item>
						</el-col>
						<el-col :span="10">
							<el-form-item label="站IP:   " prop="stationIp">
								<el-input size="mini" style="width: 150px;margin-left: 15px" type="text" v-model="station.stationIp"
								          placeholder="请输入站IP" prefix-icon="el-icon-edit"></el-input>
							</el-form-item>
						</el-col>
						<el-col :span="10" v-if="title==='编辑站'">
							<el-form-item label="是否启用:" prop="stationStatus">
								<el-tooltip :content="'Switch value: ' +station.stationStatus" placement="top" style="margin-top: 8px">
<!--									绑定number时需要加':'-->
									<el-switch
										v-model="station.stationStatus"
										active-color="#13ce66"
										inactive-color="#ff4949"
										:active-value=1
										:inactive-value=0>
									</el-switch>
								</el-tooltip>
							</el-form-item>
						</el-col>
					</el-row>

				</el-form>
			</div>
			<span slot="footer" class="dialog-footer">
				<el-button @click="dialogVisible = false">取 消</el-button>
		        <el-button type="primary" @click="doAddOrUpdateStation">确 定</el-button>
			</span>
		</el-dialog>


	</div>
</template>

<script>
export default {
	name: "DoorlockStation",
	data() {
		return {
			//标题
			title: '',
			//所有站
			stations: [],
			total: 0,
			currentPage: 1,
			pageSize: 10,
			dialogVisible: false,
			//单个站
			station: {
				id: null,
				stationName: "",
				stationIp: "",
				stationAddress: "",
				stationStatus: 1
			},
			//规则
			rules: {
				//用户名需要，提示信息请输入用户名，时区焦点的时候触发
				stationName: [{required: true, message: '请输入站名称', trigger: 'blur'}],
				stationAddress: [{required: true, message: '请输入站地址', trigger: 'blur'}],
				stationIp: [{required: true, message: '请输入站IP', trigger: 'blur'}],
			}
		}
	},
	mounted() {
		this.initStation();
	},
	methods: {
		showEditStation(station){

			this.title = '编辑站';
			//用数据拷贝
			Object.assign(this.station, station);
			this.dialogVisible = true;
		},
		//重置表单
		resetForm() {
			this.station = {
				id: null,
				stationName: "",
				stationIp: "",
				stationAddress: "",
				stationStatus: 1,
			}
		},
		//添加或更新柜门
		doAddOrUpdateStation() {
			if (this.station.id) {
				//更新
				this.$refs.addStationForm.validate((valid) => {
					if (valid) {
						this.postRequest('/station/updateStation', this.station).then(resp => {
							if (resp) {
								this.dialogVisible = false;
								this.initStation();
								this.resetForm();
							}
						})
					}
				})
			} else {
				//添加
				this.$refs.addStationForm.validate((valid) => {
					if (valid) {
						this.postRequest('/station/addStation', this.station).then(resp => {
							if (resp) {
								this.dialogVisible = false;
								this.initStation();
								this.resetForm();
							}
						})
					}
				})
			}
		},
		//显示添加
		showAddStation() {
			this.title = '添加站';
			this.dialogVisible = true;
			this.resetForm();
		},
		//删除
		doDeleteStation(station) {
			this.$confirm('此操作将永久删除名称为【' + station.stationName + '】的站，是否继续?', '提示', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning'
			}).then(() => {
				this.deleteRequest('/station/deleteStation/' + station.id).then(resp => {
					if (resp){
						this.initStation();
					}

				})
			}).catch(() => {
				this.$message({
					type: 'info',
					message: '已取消删除'
				});
			});
		},
		//每页显示数量改变
		sizeChange(pageSize) {
			this.pageSize = pageSize;
			this.initStation();
		},
		//改变当前页
		currentChange(currentPage) {
			this.currentPage = currentPage;
			this.initStation();
		},
		//初始化站
		initStation() {
			this.getRequest('/station/getAllStationPage/?currentPage=' + this.currentPage + '&pageSize=' + this.pageSize).then(resp => {
				if (resp) {
					this.stations = resp.records;
					this.total = resp.total;
				}
			})
		}
	}
}
</script>

<style scoped>

</style>