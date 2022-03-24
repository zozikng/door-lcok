<template>
	<div>
		<el-divider></el-divider>
		<div style="display: flex;justify-content: space-between">

			<div>
				<el-select v-model="station" placeholder="请选择站" value-key="stationIp" @change="initCabinet()">
					<el-option
						v-for="(st,index) in stations"
						:key="index"
						:label="st.stationName"
						:value="st">
					</el-option>
				</el-select>
			</div>
			<div>
				<el-button type="primary" icon="el-icon-plus" size="small" @click="showAddCabinetView">添加柜门</el-button>
			</div>
		</div>
		<!--卡片-->
		<div class="cabinet-container">
			<el-card class="cabinet-card" v-for="(cabinet,index) in cabinets" :key="index">
				<div slot="header" class="clearfix">
					<span>{{ cabinet.cabinetName }}</span>
					<el-button style="float: right; padding: 3px;color: #ff0000" type="text" icon="el-icon-delete"
					           @click="doDeleteCabinet(cabinet)">删除
					</el-button>
					<el-button style="float: right;  padding: 3px;color: cornflowerblue" type="text" icon="el-icon-edit"
					           @click="showEditCabinet(cabinet)">编辑
					</el-button>

				</div>
				<div class="cabinetInfo-container">
					<div>
						前门状态：
						<el-tag type="danger" v-if="cabinet.state.fontDoorState==='true'">打开</el-tag>
						<el-tag type="success" v-else-if="cabinet.state.fontDoorState==='false'">关闭</el-tag>
						<el-tag type="warning" v-else>未初始化连接</el-tag>
					</div>
					<div>
						后门状态：
						<el-tag type="danger" v-if="cabinet.state.backDoorState==='true'">打开</el-tag>
						<el-tag type="success" v-else-if="cabinet.state.backDoorState==='false'">关闭</el-tag>
						<el-tag type="warning" v-else>未初始化连接</el-tag>
					</div>
					<div>
						前门是否异常：
						<el-tag type="danger" v-if="cabinet.state.fontDoorExce==='true'">异常</el-tag>
						<el-tag type="success" v-else-if="cabinet.state.fontDoorExce==='false'">正常</el-tag>
						<el-tag type="warning" v-else>未初始化连接</el-tag>
					</div>
					<div>
						后门是否异常：
						<el-tag type="danger" v-if="cabinet.state.backDoorExce==='true'">异常</el-tag>
						<el-tag type="success" v-else-if="cabinet.state.backDoorExce==='false'">正常</el-tag>
						<el-tag type="warning" v-else>未初始化连接</el-tag>
					</div>
					<el-divider></el-divider>
					<div>
						<div>
							操作前门：
							<el-button type="primary" size="mini" style="margin-top: 10px"
							           @click="openDoor(cabinet.cabinetId,4)">打开
							</el-button>
							<el-button type="warning" size="mini" style="margin-top: 10px"
							           @click="closeDoor(cabinet.cabinetId,4)">关闭
							</el-button>
						</div>
						<div>
							操作后门：
							<el-button type="primary" size="mini" style="margin-top: 10px"
							           @click="openDoor(cabinet.cabinetId,3)">打开
							</el-button>
							<el-button type="warning" size="mini" style="margin-top: 10px"
							           @click="closeDoor(cabinet.cabinetId,3)">关闭
							</el-button>
						</div>
						<div>
							查看日志：
							<el-button type="primary" size="mini" style="margin-top: 10px"
							           @click="showLogCabine(cabinet)">查看
							</el-button>
						</div>
					</div>
				</div>

			</el-card>
		</div>
		<!--弹框-->
		<el-dialog
			:title="title"
			:visible.sync="dialogVisible"
			width="38%">
			<div>
				<el-form ref="addCabinetForm"
				         :rules="rules"
				         :model="cabinet">
					<el-row :gutter="20">
						<el-col :span="10">
							<el-form-item label="柜门id:" prop="cabinetId">
								<el-input size="mini" style="width: 150px" type="text" v-model="cabinet.cabinetId"
								          placeholder="请输入柜门id" prefix-icon="el-icon-edit"></el-input>
							</el-form-item>
						</el-col>
						<el-col :span="10">
							<el-form-item label="柜门名称:" prop="cabinetName">
								<el-input size="mini" style="width: 150px" type="text" v-model="cabinet.cabinetName"
								          placeholder="请输入柜门名称" prefix-icon="el-icon-edit"></el-input>
							</el-form-item>
						</el-col>
					</el-row>
				</el-form>
			</div>
			<span slot="footer" class="dialog-footer">
				<el-button @click="dialogVisible = false">取 消</el-button>
		        <el-button type="primary" @click="doAddOrUpdateCabinet">确 定</el-button>
			</span>
		</el-dialog>
		<!--		//日志弹窗-->
		<el-dialog
			:title="title"
			:visible.sync="cabinetLogDialogVisible"
			width="70%">
			<div>
				<div>
					<!--					<div style="margin-top: 20px;display: flex;justify-content: space-around;">-->
					<el-form ref="CabinetLogForm"
					         :rules="rules"
					         :model="cabinet"
					         style="margin-top: 20px;display: flex;justify-content: space-around;">
						<el-form-item prop="radioDay">
							<el-radio-group v-model="radioDay" size="small">
								<el-radio-button label="1天"></el-radio-button>
								<el-radio-button label="2天"></el-radio-button>
								<el-radio-button label="7天"></el-radio-button>
								<el-radio-button label="30天"></el-radio-button>
							</el-radio-group>
						</el-form-item>
						<el-form-item prop="doorStateSelectValue">
							<el-cascader
								v-model="doorStateSelectValue"
								:options="doorStateSelect"></el-cascader>
						</el-form-item>
						<el-form-item>
							<el-button type="primary" size="small"
							           @click="showLogChar">查询
							</el-button>
						</el-form-item>
					</el-form>

					<!--					</div>-->
					<div ref="testLine" id="testLine" style="width:100%; height:350px;margin-top: 15px"></div>
					<div class="block">

					</div>
				</div>
			</div>
			<span slot="footer" class="dialog-footer">
				<el-button @click="cabinetLogDialogVisible = false">取 消</el-button>
				<!--		        <el-button type="primary" @click="cabinetLogDialogVisible=false">确 定</el-button>-->
			</span>
		</el-dialog>

	</div>
</template>

<script>
import doorlockStation from "@/views/doorlock/DoorlockStation";
import dayjs from "dayjs";

export default {
	name: "DoorlockCabinet",
	data() {
		return {
			stations: [],
			station: {
				id: null,
				stationName: null,
				stationIp: null,
				stationAddress: null,
				stationStatus: null,
			},
			cabinets: [],
			//使用websocket需要安装npm install sockjs-client 和 npm install stompjs ，安装完后需要去vue.config.js去配置
			cabinet: {
				id: null,
				cabinetId: null,
				cabinetName: null,
				stationId: null,
				fontDoorState: null,
				backDoorState: null,
				fontDoorExce: null,
				backDoorExce: null,
			},
			state: {
				fontDoorState: null,
				backDoorState: null,
				fontDoorExce: null,
				backDoorExce: null,
			},
			//使用websocket需要安装npm install sockjs-client 和 npm install stompjs ，安装完后需要去vue.config.js去配置
			path: "ws://192.168.1.2:8081/ws/",
			ws: {},
			//编辑窗口
			dialogVisible: false,
			//日志窗口
			cabinetLogDialogVisible: false,
			//标题
			title: "",
			radioDay: "",
			doorStateSelectValue: null,
			doorStateSelect: [{
				value: 'fontDoorState',
				label: '前门状态',
				children: [{
					value: 'true',
					label: '打开'
				}, {
					value: 'false',
					label: '关闭'
				}]
			}, {
				value: 'backDoorState',
				label: '后门状态',
				children: [{
					value: 'true',
					label: '打开'
				}, {
					value: 'false',
					label: '关闭'
				}]
			}, {
				value: 'fontDoorExce',
				label: '前门异常状态',
				children: [{
					value: 'true',
					label: '异常'
				}, {
					value: 'false',
					label: '正常'
				}]
			}, {
				value: 'backDoorExce',
				label: '后门异常状态',
				children: [{
					value: 'true',
					label: '异常'
				}, {
					value: 'false',
					label: '正常'
				}]
			}
			],
			//规则
			rules: {
				//用户名需要，提示信息请输入用户名，时区焦点的时候触发
				cabinetId: [{required: true, message: '请输入柜门id', trigger: 'blur'}],
				cabinetName: [{required: true, message: '请输入柜门名称', trigger: 'blur'}],
				radioDay: [{required: true, message: '请选择查询的日期', trigger: 'blur'}],
				doorStateSelectValue: [{required: true, message: '请选择查询状态', trigger: 'blur'}],

			},
			myChart: null,
			option: {
				title: {
					text: "日志分布图"
				},
				xAxis: {
					type: 'category',
					data: []
				},
				yAxis: {
					type: 'value'
				},
				series: [{
					data: [],
					type: 'line',
					smooth: true,
				}]
			}
		}

	},
	mounted() {
		this.initStations();
	},
	destroyed() {
		if (this.ws.readyState === 1 || this.ws.readyState === 2) {
			// 离开页面还有websocket连接就关闭
			this.ws.onclose();
		}
	},
	methods: {
		//重置表单
		resetLogForm() {
			this.option.xAxis.data=[];
			this.option.series[0].data=[];
		},

		//显示图表
		showLogChar() {
			this.resetLogForm();
//两张引入方式都可以，这里我用ref,注意一定要节点 初始化完成
// 			this.myChart = this.$echarts.init(document.getElementById('testLine'))
			this.myChart = this.$echarts.init(this.$refs.testLine)
			this.initChart();
			if (this.doorStateSelectValue!==null&&this.doorStateSelectValue!==[]&&this.radioDay!==""&&this.radioDay!==null){
				var star;
				var end;
				var type;
				if (this.radioDay==="1天"){
					end=dayjs().add(1,"day").format("YYYY-MM-DD");
					star=dayjs().subtract(8,"hour").format("YYYY-MM-DD");
					type=2;
				}else if (this.radioDay==="2天"){
					end=dayjs().subtract(8,"hour").add(1,"day").format("YYYY-MM-DD");
					star=dayjs().subtract(32,"hour").format("YYYY-MM-DD");
					type=2;
				} else if (this.radioDay==="7天"){
					end=dayjs().subtract(8).add(1,"day").format("YYYY-MM-DD");
					star=dayjs().subtract(8,"hour").subtract(1,"week").format("YYYY-MM-DD");
					type=3;
				} else if (this.radioDay==="30天"){
					end=dayjs().subtract(8,"hour").add(1,"day").format("YYYY-MM-DD");
					star=dayjs().subtract(8,"hour").subtract(1,"month").format("YYYY-MM-DD");
					type=3;
				}

				this.postRequest("cabinet/getCabinetStateLog/"+this.cabinet.cabinetId+ "/" +this.station.stationIp+ "/" +star+ "/" +end+ "/"
					+ this.doorStateSelectValue[0] + "/" + this.doorStateSelectValue[1] + "/"+type+"").then(resp=>{
					this.$refs.CabinetLogForm.clearValidate();
						if (resp){

							// 两张引入方式都可以，这里我用ref,注意一定要节点 初始化完成
							this.option.xAxis.data=resp.xdata;
							this.option.series[0].data=resp.seriess;
							// this.myChart = this.$echarts.init(document.getElementById('testLine'))
							this.myChart = this.$echarts.init(this.$refs.testLine);
							this.initChart();
						}
				})



			}else {
				this.$refs.CabinetLogForm.validate();
			}




		},
		//初始化表
		initChart() {
			const {
				myChart,
				option
			} = this
			myChart.setOption(option)
		},
		//显示日志窗口
		showLogCabine(cabinet) {
			this.radioDay="";
			this.doorStateSelectValue=null;
			this.resetLogForm();
			//用数据拷贝
			Object.assign(this.cabinet, cabinet);
			this.cabinetLogDialogVisible = true;
			this.myChart = this.$echarts.init(this.$refs.testLine);
			this.initChart();
		},
		//显示编辑
		showEditCabinet(cabinet) {
			this.title = '编辑柜门';
			//用数据拷贝
			Object.assign(this.cabinet, cabinet);
			this.dialogVisible = true;
		},
		//添加或更新柜门
		doAddOrUpdateCabinet() {
			if (this.cabinet.id) {
				//更新
				this.$refs.addCabinetForm.validate((valid) => {
					if (valid) {
						this.cabinet.stationId = this.station.id;
						this.postRequest('/cabinet/updateCabinet', this.cabinet).then(resp => {
							if (resp) {
								this.dialogVisible = false;
								this.initWS(this.station.stationIp);
								this.initCabinet();
								this.resetForm();
							}
						})
					}
				})
			} else {
				//添加
				this.$refs.addCabinetForm.validate((valid) => {
					if (valid) {
						this.cabinet.stationId = this.station.id;
						this.postRequest('/cabinet/addCabinet', this.cabinet).then(resp => {
							if (resp) {
								this.dialogVisible = false;
								this.initWS(this.station.stationIp);
								this.initCabinet();
								this.resetForm();
							}
						})
					}
				})
			}
		},
		//显示添加柜门
		showAddCabinetView() {
			if (this.station.id !== null) {

				this.title = "添加柜门";
				this.dialogVisible = true;
				this.resetForm();
			} else {
				this.$message.error("请选择站,再添加柜门！！！");
			}
		},
		//重置表单
		resetForm() {
			this.cabinet = {
				id: null,
				cabinetId: null,
				cabinetName: null,
				stationId: null,
				fontDoorState: null,
				backDoorState: null,
				fontDoorExce: null,
				backDoorExce: null,
			}
		},
		doDeleteCabinet(cabinet) {
			this.$confirm('此操作将永久删除名称为【' + cabinet.cabinetName + '】的柜门，是否继续?', '提示', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning'
			}).then(() => {
				this.deleteRequest('/cabinet/deleteCabinet/' + cabinet.id).then(resp => {
					if (resp) {
						this.initWS(this.station.stationIp);
						this.initCabinet();
					}

				})
			}).catch(() => {
				this.$message({
					type: 'info',
					message: '已取消删除'
				});
			});
		},
		//开门
		openDoor(cabinetId, door) {
			this.putRequest('/operateDoor/' + this.station.stationIp + '/' + cabinetId + '/' + door + '/' + 1).then(resp => {
				if (resp) {
				}
			})
		},
		//关门
		closeDoor(cabinetId, door) {
			this.putRequest('/operateDoor/' + this.station.stationIp + '/' + cabinetId + '/' + door + '/' + 0).then(resp => {
				if (resp) {
				}
			})
		},
		//初始化站
		initStations() {
			this.getRequest('/station/getAllStation').then(resp => {
				if (resp) {
					this.stations = resp;
				}
			})
		},
		//初始化柜门
		initCabinet() {
			this.getRequest('/cabinet/findCabinetByStationIp?stationIp=' + this.station.stationIp).then(resp => {
				if (resp) {
					if (this.ws.readyState === 1) {
						this.ws.onclose();
					}
					this.cabinets = resp.data;
					for (let i = 0; i < this.cabinets.length; i++) {
						this.$set(this.cabinets[i], "state", this.state);
					}
					this.initWS(this.station.stationIp);
				}
			})
		},
		//websocket
		//init函数可在页面加载的时候就进行初始化或者根据自己的业务需求在需要打开通讯的时候在进行初始化
		initWS(stationIp) {
			// 实例化socket，这里的实例化直接赋值给this.ws是为了后面可以在其它的函数中也能调用websocket方法，例如：this.ws.close(); 完成通信后关闭WebSocket连接
			this.ws = new WebSocket(this.path + stationIp)

			//监听是否连接成功
			this.ws.onopen = () => {
				console.log('ws连接状态：' + this.ws.readyState);
				//连接成功则发送一个数据
				this.ws.send('连接成功');
			}

			//接听服务器发回的信息并处理展示
			this.ws.onmessage = (data) => {
				console.log('接收到来自服务器的消息：');
				let da = JSON.parse(data.data);
				if (da.code === 404) {
					this.$message.error(da.msg);
					this.ws.onclose()
				} else {
					for (let i = 0; i < this.cabinets.length; i++) {
						// Object.assign(this.cabinets[i],da[i]);
						// this.cabinets[i] = Object.assign({}, this.cabinets[i], da[i]);
						this.$set(this.cabinets[i], "state", da[i]);
						// this.cabinets[i]=[...da[i]];
					}
				}
			}

			//监听连接关闭事件
			this.ws.onclose = () => {
				//监听整个过程中websocket的状态
				this.ws.close();
				console.log('ws连接状态：' + this.ws.readyState);
			}

			//监听并处理error事件
			this.ws.onerror = function (error) {
				console.log(error);
			}
		}
	}

}
</script>

<style scoped>
.cabinet-container {
	display: flex;
	justify-content: space-around;
	flex-wrap: wrap;
	margin-top: 20px;
}

.cabinet-card {
	width: 370px;
	margin-bottom: 20px;
}

.cabinetInfo-container {
	font-size: 14px;
	line-height: initial;
	color: #409eff;
}

.cabinetInfo-container div {
	padding-top: 3px;
}
</style>