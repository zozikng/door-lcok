//代理对象
let proxyObj={}

proxyObj['/']={
    //websocket
    ws:false,
    //目标地址//域名
    target: 'http://localhost:8081',
    //发送请求头host会被设置target//是否来气跨区
    changeOrigin: true,
    //不重写请求地址//替换/api；不替换
    pathRewrite: {
        '^/':'/'
    }

}

proxyObj['/ws']={
    //是websocket
    ws:true,
    //目标地址//域名
    target: 'ws://localhost:8081',
    //发送请求头host会被设置target//是否来气跨区
    changeOrigin: true,
    //不重写请求地址//替换/api；不替换
    pathRewrite: {
        '^/':'/'
    }

}


//访问的默认路径端口
module.exports = {
    lintOnSave: false,
    devServer: {
      host: '192.168.1.2',//访问的是localhost
      port: 8080,//访问的端口
      proxy: proxyObj
    }
  }