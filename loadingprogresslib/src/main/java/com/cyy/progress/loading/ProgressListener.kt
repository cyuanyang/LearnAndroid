package com.cyy.progress.loading

/**
 * Created by study on 17/10/11.
 * event
 */


/**
 * 进度条事件
 */
interface ProgressListener{
    fun onProgressStartListener(progress: LoadingProgress) //开始旋转
    fun onProgressRepeatListener(progress: LoadingProgress) //每个周期结束的时候调用
    fun onProgressResetListener(progress: LoadingProgress) //重置的回调事件
    fun onProgressEndListener(progress: LoadingProgress ) //动画结束回调
}

abstract class SimpleProgressListener : ProgressListener{
    override fun onProgressStartListener(progress: LoadingProgress) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onProgressRepeatListener(progress: LoadingProgress) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onProgressResetListener(progress: LoadingProgress) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onProgressEndListener(progress: LoadingProgress) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
