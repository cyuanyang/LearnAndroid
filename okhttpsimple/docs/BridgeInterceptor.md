#### BridgeInterceptor

该拦截器主要功能
> proceed之前
* 处理请求头 最开始我们构建的Request是不符合Http的Request的规范的，该拦截器会讲参数补充完成
* 处理Cookie 如果曾在cookie会加载到Request中

> proceed之后
* 保存cookie
* Response设置对应的Request
* proceed之前如果有压缩，则会处理压缩
* 之后返回处理好的Response

这个拦截器很简单 比较好的是它的Cookie的处理很巧妙

CookieJar是一个接口实现saveFromResponse()存储cookie 实现loadForRequest()读取co0kie

CookieJar这个对象为OkHttpClient持有，在构建BridgeInterceptor()传入的