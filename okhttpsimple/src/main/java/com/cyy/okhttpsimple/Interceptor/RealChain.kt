package com.cyy.okhttpsimple.Interceptor

import java.util.*

/**
 * Created by study on 17/8/22.
 *
 */

class RealChain(interceptors:MutableList<Interceptor> , index:Int , request: Request?) : Interceptor.Chain{

    //这里面存储所有的拦截器
    var interceptors:MutableList<Interceptor> = interceptors
    //当前拦截器的index
    var index:Int = index
    var reqeust:Request? = request

    override fun proceed(request: Request):Response {
        //在这里递归调用每个拦截器
        return with(RealChain(interceptors , index+1 , request)){

            return interceptors[this@RealChain.index].intercept(this)
        }
    }

}