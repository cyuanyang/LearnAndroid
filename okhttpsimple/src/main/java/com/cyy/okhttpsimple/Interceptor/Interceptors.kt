package com.cyy.okhttpsimple.Interceptor

/**
 * Created by study on 17/8/22.
 *
 */


//第一个拦截器
class FirstInterceptor : Interceptor{

    override fun intercept(chain: Interceptor.Chain): Response {
        val realChain = chain as RealChain
        val request = realChain.reqeust

        //处理reqeust
        request?.param1 = "FirstInterceptor"
        //必须调用proceed
        val response = chain.proceed(request!!)

        return response.apply {
            param1 = "FirstInterceptor"
        }
    }
}

//第一个拦截器
class SecondInterceptor : Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {

        val realChain = chain as RealChain
        val request = realChain.reqeust

        //处理reqeust
        request?.param2 = "SecondInterceptor"
        //必须调用proceed
        val response = chain.proceed(request!!)

        return response.apply {
            param2 = "SecondInterceptor"
        }
    }

}

//第一个拦截器
class LastInterceptor : Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {

        val realChain = chain as RealChain
        val request = realChain.reqeust

        //最后一个不能调用proceed

        //组装Response

        return Response().apply {
            body = "Response body"
            requets = request
            //...
        }

    }

}