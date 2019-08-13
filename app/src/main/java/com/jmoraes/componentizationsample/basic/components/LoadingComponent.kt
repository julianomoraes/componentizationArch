package com.jmoraes.componentizationsample.basic.components

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import com.jmoraes.componentizationsample.basic.components.uiViews.LoadingView
import com.jmoraes.componentizationsample.basic.eventTypes.ScreenStateEvent
import com.netflix.componentizationV1.EventBusFactory
import com.netflix.componentizationV1.UIComponent
import io.reactivex.Observable

@SuppressLint("CheckResult")
open class LoadingComponent(container: ViewGroup, bus: EventBusFactory) : UIComponent<Unit> {
    override fun getContainerId(): Int {
        return uiView.containerId
    }

    override fun getUserInteractionEvents(): Observable<Unit> {
        return Observable.empty()
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val uiView = initView(container)

    open fun initView(container: ViewGroup): LoadingView {
        return LoadingView(container)
    }

    init {
        bus.getSafeManagedObservable(ScreenStateEvent::class.java)
            .subscribe {
                when (it) {
                    ScreenStateEvent.Loading -> {
                        uiView.show()
                    }
                    ScreenStateEvent.Loaded -> {
                        uiView.hide()
                    }
                    ScreenStateEvent.Error -> {
                        uiView.hide()
                    }
                }
            }
    }
}
