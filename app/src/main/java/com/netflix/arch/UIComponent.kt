package com.netflix.arch

import io.reactivex.Observable

interface UIComponent<T> {
    fun getContainerId(): Int
    fun getUserInteractionEvents(): Observable<T>
}