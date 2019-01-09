package com.ryanyu.basecore.listener

/**
 * Created by Ryan Yu on 17/12/2018.
 */
interface RyObserverSingleStatusListener<T> {
    fun onNext(t: T)
    fun onError()
}
