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
