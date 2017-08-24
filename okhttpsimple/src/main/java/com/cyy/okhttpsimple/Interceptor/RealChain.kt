package com.cyy.okhttpsimple.Interceptor

import java.util.*

/**
 * Created by study on 17/8/22.
 *
 */

class RealChain(interceptors:MutableList<Interceptor> , index:Int ,
                request: Request? , callBack:Interceptor.ChainCallback) : Interceptor.Chain{

    //这里面存储所有的拦截器
    var interceptors:MutableList<Interceptor> = interceptors
    //当前拦截器的index
    var index:Int = index
    var reqeust:Request? = request
    var callback:Interceptor.ChainCallback = callBack

    override fun proceed(request: Request):Response {
        //在这里递归调用每个拦截器
        with(RealChain(interceptors , index+1 , request , callback)){

            val interceptor = interceptors[this@RealChain.index]
            callback.proceedBefore("interceptor name is ${interceptor::class.java.simpleName}")
            val response = interceptor.intercept(this)
            callback.proceedAfter("interceptor name is ${interceptor::class.java.simpleName}")

            return response
        }
    }

}