import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  //全局对象用于保存组件公共的数据
  state: {
    routes:[],
    currentAdmin: JSON.parse(window.localStorage.getItem( 'user'))
},
  //可以改变state中值的方法，同步执行
  mutations: {
    INITUSER(state,user){
      state.currentAdmin=user;
    },
    //初始化路由
    initRoutes(state,data){
      state.routes = data;
    }
  },

  /*actions: {
    
  },
  modules: {
  }*/
})
