package com.cyy.okhttpsimple.Interceptor

/**
 * Created by study on 17/8/22.
 *
 */


interface Interceptor{

    fun intercept(chain:Chain) : Response
    
    interface Chain{

        fun proceed(request:Request):Response
    }

    interface ChainCallback{
        fun proceedBefore(info:String)
        fun proceedAfter(info:String)
    }
}


class Response{
    var body:String? = null
    var requets:Request? = null

    var param1:String?=null
    var param2:String?=null

    override fun toString(): String {
        return with(this){
            return "body = $body param1=$param1 param2=$param2"
        }
    }
}

class Request{
    var url:String? = null
    var body:String? = null

    var param1:String?=null
    var param2:String?=null

    override fun toString(): String {
        return with(this){
            return "body = $body param1=$param1 param2=$param2"
        }
    }
}