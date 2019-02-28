package com.seta.common.mvp

import retrofit2.Call
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

/**
 * Created by SETA_WORK on 2017/7/4.
 */
interface Presenter<in V : MvpView?> {
    fun attachView(mvpView: V) //参数泛型，逆变，in，允许传入子类型参数
    fun detachView()
}

open class BasePresenter<T : MvpView?> : Presenter<T> {

    var mvpView: T? = null
    private val callList = ArrayList<Call<Any>>()
    private val mCompositeSubscription by lazy { CompositeSubscription() }

    override fun attachView(mvpView: T) {
        this.mvpView = mvpView
    }

    override fun detachView() {
        this.mvpView = null
        onUnsubscribe()
        callCancel()
    }

    fun isViewAttached() = (mvpView == null)

    fun onUnsubscribe() = mCompositeSubscription.unsubscribe()

    fun callCancel() = callList.forEach { it.cancel() }

    fun <R> Observable<R>.doSubscribe(subscriber: Subscriber<R>): Observable<R> {
        mCompositeSubscription.add(this
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.computation())
                .subscribe(subscriber))
        return this
    }

    //进一步简化，省略
//    fun <R> Observable<R>.doSubscribe(onLoad: (R) -> Unit) {
//        this.doSubscribe(object : Subscriber<R>() {
//            override fun onNext(t: R) {
//                onLoad.invoke(t)
//            }
//
//            override fun onCompleted() {
//            }
//
//            override fun onError(e: Throwable?) {
//                mvpView?.onLoadFail(e)
//            }
//        })
//    }

//    fun addSubscription(observable: Observable<Any>, subscriber: Subscriber<Any>) {
//        mCompositeSubscription.add(observable
//                .observeOn(AndroidSchedulers.mainThread())
//                .unsubscribeOn(Schedulers.io())
//                .subscribeOn(Schedulers.computation())
//                .subscribe(subscriber))
//    }

    fun addCall(call: Call<Any>) = callList.add(call)
}
