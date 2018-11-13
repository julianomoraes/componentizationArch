package com.jmoraes.componentizationsample.basic.components

import android.annotation.SuppressLint
import android.support.annotation.VisibleForTesting
import android.view.ViewGroup
import com.jmoraes.componentizationsample.basic.components.uiViews.LoadingView
import com.jmoraes.componentizationsample.basic.eventTypes.ScreenStateEvent
import com.netflix.arch.EventBusFactory

@SuppressLint("CheckResult")
open class LoadingComponent(container: ViewGroup, bus: EventBusFactory) {
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
