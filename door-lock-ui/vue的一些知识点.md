```vue
<div id="nav">
  <!--相当于href跳转都响应路由-->
  <router-link to="/">Home</router-link> |
  <router-link to="/about">About</router-link>
</div>
```



```vue
//导出组件
export default {
  name: 'Home',
  components: {
    //定义了helloworld子组件
    HelloWorld
  }
}
```



```vue
,
{
  path: '/about',
  name: 'About',
  // route level code-splitting
  // this generates a separate chunk (about.[hash].js) for this route
  // which is lazy-loaded when the route is visited.
  //懒加载，不提前导入，等使用到的时候再导入
  component: () => import(/* webpackChunkName: "about" */ '../views/About.vue')
}
```

