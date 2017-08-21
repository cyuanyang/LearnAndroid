#### RetryAndFollowUpInterceptor 实现原理和功能分析

##### Overview

    该拦截器功能有
    * 创建Address
    * 创建StreamAllocation
    * 处理响应结果，例如重定向或者重试

    这个类最核心的方法就是它的
    ```java
    @Override public Response intercept(Chain chain) throws IOException
    ```
    这个Chain是在RealCall中的 getResponseWithInterceptorChain()构造并传入的。

    下面是该方法的实现

********
##### 1.创建Address和StreamAllocation

    ```java
    private Address createAddress(HttpUrl url) {
        SSLSocketFactory sslSocketFactory = null;
        HostnameVerifier hostnameVerifier = null;
        CertificatePinner certificatePinner = null;
        if (url.isHttps()) {
          sslSocketFactory = client.sslSocketFactory();
          hostnameVerifier = client.hostnameVerifier();
          certificatePinner = client.certificatePinner();
        }

        return new Address(url.host(), url.port(), client.dns(), client.socketFactory(),
            sslSocketFactory, hostnameVerifier, certificatePinner, client.proxyAuthenticator(),
            client.proxy(), client.protocols(), client.connectionSpecs(), client.proxySelector());
      }
     ```

     通过此代码创建一个Address，然后创建一个StreamAllocation，这个类算是OkHttp的核心类，创建Socket和连接Socket都在这个类当中。


##### 2.执行chain.proceed()

    ```
    response = ((RealInterceptorChain) chain).proceed(request, streamAllocation, null, null);
    releaseConnection = false;
    ```

    将Request和构建好的streamAllocation传递给后面的interceptor,并返回Response

##### 3.处理响应结果，例如重定向或者重试

    得到Response，根据响应状态做响应的处理

**********
#### 怎么实现重定向和重试的？

    这是其 public Response intercept(Chain chain) 的一段大致流程代码，
    这段代码不是源码，是我在源码的基础上认为比较重要的复制出来拼在一起的
    很佩服作者的思路，真的很巧妙。
    用一个死循环来做的，重定向是根据结果返回的的来决定是否需要跳出死循环，
    重试是根据抛出的IOException的时候判断是将异常抛出还是继续执行循环来做的

    ```
     while (true){

        //proceed
        try {

            response = chain.proceed(request, streamAllocation, null, null)

        }catch (IOException e) {
             boolean requestSendStarted = !(e instanceof ConnectionShutdownException);
        //这是其中的一个异常处理 ，recover是判断时候需要重试，若需要重试继续循环，不需要则抛出异常退出循环
             if (!recover(e, requestSendStarted, request)) throw e;
             releaseConnection = false;
             continue;
        }
        //根据response判断是否需要重定向 ，不要返回null 需要返回重定向的Request
        Request followUp = followUpRequest(response)

        //不需要重定向 返回response
        if (followUp == null){
            return response
        }

        //是否达到最大允许的重定向次数
        if (++followUpCount > MAX_FOLLOW_UPS) {
            streamAllocation.release()
            throw ProtocolException("Too many follow-up requests: " + followUpCount)
        }

        //重新构建streamAllocation
        if (!sameConnection(response, followUp.url())) {
            streamAllocation.release();
            streamAllocation = new StreamAllocation(
                    client.connectionPool(), createAddress(followUp.url()), callStackTrace);
        } else if (streamAllocation.codec() != null) {
            throw new IllegalStateException("Closing the body of " + response
                    + " didn't close its backing stream. Bad interceptor?");
        }

        接着循环
    }
    ```

#### End 到此这个类的功能原理就很清楚了


