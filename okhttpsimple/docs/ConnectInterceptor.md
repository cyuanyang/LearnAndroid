#### ConnectInterceptor
这个拦截器就是一个功能，就是与目标服务器建立连接然后传给下一个拦截器

为了建立连接，OK做了非常复杂的操作。 主要的操作都在StreamAllocation类中

大概有三步操作

1.寻找Route
2.从连接池当中寻找连接，没有就new一个，然后放入连接池
3.根据得到的连接建立连接

[优秀的分析连接建立的文章](https://www.wolfcstech.com/2016/10/27/OkHttp3%E8%BF%9E%E6%8E%A5%E5%BB%BA%E7%AB%8B%E8%BF%87%E7%A8%8B%E5%88%86%E6%9E%90/)

#### 寻找Route


