#### OkHttpSimple

#### Overview Version:OkHttp 3.8.1
本人学习OkHttp的学习笔记和学习的代码

#### http 第一次同步请求流程详细分析

 请求代码
```
//单利
 val client:OkHttpClient = OkHttpClient()
                .newBuilder().build()


 var request = Request.Builder()
         .url("http://www.baidu.com")
         .build()

 Thread(Runnable {
     client.newCall(request).execute().use {
         print(" body == ${it.body()!!.string()}")
     }
 }).start()

```

##### 第一步 构建一个Request
通过建造者模式构建一个Request。Request包含了HttpUrl，请求方式，端口，参数等信息

HttpUrl包含scheme host port 等信息

##### 新建一个Call并同步执行

###### 1.client.newCall(request)
       ```
       @Override public Call newCall(Request request) {
           return new RealCall(this, request, false /* for web socket */);
         }
       ```
       RealCall的构造方法,在构造RealCall的时候，会构造一个RetryAndFollowUpInterceptor类，这个类主要负责处理重试和重定向的
       eventListener3.8.1还没有实现。
       ```
       RealCall(OkHttpClient client, Request originalRequest, boolean forWebSocket) {
           final EventListener.Factory eventListenerFactory = client.eventListenerFactory();

           this.client = client;
           this.originalRequest = originalRequest;
           this.forWebSocket = forWebSocket;
           this.retryAndFollowUpInterceptor = new RetryAndFollowUpInterceptor(client, forWebSocket);

           // TODO(jwilson): this is unsafe publication and not threadsafe.
           this.eventListener = eventListenerFactory.create(this);
         }
       ```

********
##### 2.RealCall.execute()
    这里面同步执行请求，主要是getResponseWithInterceptorChain()。

    ```
    Response getResponseWithInterceptorChain() throws IOException {
        // Build a full stack of interceptors.
        List<Interceptor> interceptors = new ArrayList<>();
        interceptors.addAll(client.interceptors());
        interceptors.add(retryAndFollowUpInterceptor);
        interceptors.add(new BridgeInterceptor(client.cookieJar()));
        interceptors.add(new CacheInterceptor(client.internalCache()));
        interceptors.add(new ConnectInterceptor(client));
        if (!forWebSocket) {
          interceptors.addAll(client.networkInterceptors());
        }
        interceptors.add(new CallServerInterceptor(forWebSocket));

        Interceptor.Chain chain = new RealInterceptorChain(
            interceptors, null, null, null, 0, originalRequest);
        return chain.proceed(originalRequest);
      }
    ```
    在 chain.proceed()的时候会用一种递归的方式来执行所有拦截器的intercept()方法。

    假定client.interceptors().size() 为0.这段代码的执行chain.proceed的时候执行循序为
    RetryAndFollowUpInterceptor>BridgeInterceptor>CacheInterceptor>ConnectInterceptor>CallServerInterceptor

*******
##### 3.RetryAndFollowUpInterceptor.intercept()
    ```
    streamAllocation = new StreamAllocation(
                   client.connectionPool(), createAddress(request.url()), callStackTrace);
    ```
    创建Address，并用它来创建StreamAllocation。StreamAllocation组织Connections，Streams和Calls的关系。
    然后执行
    ```
    response = ((RealInterceptorChain) chain).proceed(request, streamAllocation, null, null);
    ```
    这个会将创建好的streamAllocation传递给BridgeInterceptor的intercept()方法

*******
##### 4.BridgeInterceptor.intercept()
    这个拦截器的主要功能是：
    > 处理请求头（header），将自定义的头和协议必须的头合在一起，
    如果有自定义使用自定义的，没有就生成默认头
    > 处理cookie

    头部信息处理完毕后 执行
    ```
    Response networkResponse = chain.proceed(requestBuilder.build());
    ```
    将处理后的新Request传递给下一个拦截器

*******
##### 5.CacheInterceptor.intercept()

     本拦截器主要是处理缓存的。

     执行

    ```
    networkResponse = chain.proceed(networkRequest);
    ```
    将处理后的新Request传递给下一个拦截器

##### 6.ConnectInterceptor.intercept()





