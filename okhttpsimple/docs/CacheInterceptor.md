#### CacheInterceptor

该拦截器主要功能对Response做缓存，缓存的主要操作放在Cache类中
> proceed之前
根据Request检查缓存


> proceed之后
根据Request的请求头的设置来决定是否调用

具体的实现方案在Cache.java这个类当中


