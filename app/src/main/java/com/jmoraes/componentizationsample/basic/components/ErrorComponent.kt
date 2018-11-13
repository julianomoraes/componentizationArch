package com.jmoraes.componentizationsample.basic.components

import android.annotation.SuppressLint
import android.support.annotation.VisibleForTesting
import android.view.ViewGroup
import com.jmoraes.componentizationsample.basic.eventTypes.ScreenStateEvent
import com.jmoraes.componentizationsample.basic.components.uiViews.ErrorView
import com.netflix.arch.EventBusFactory

@SuppressLint("CheckResult")
open class ErrorComponent(container: ViewGroup, bus: EventBusFactory) {
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val uiView = initView(container, bus)

    open fun initView(container: ViewGroup, bus: EventBusFactory): ErrorView {
        return ErrorView(container, bus)
    }

    init {
        bus.getSafeManagedObservable(ScreenStateEvent::class.java)
            .subscribe {
                when (it) {
                    ScreenStateEvent.Loading -> {
                        uiView.hide()
                    }
                    ScreenStateEvent.Loaded -> {
                        uiView.hide()
                    }
                    ScreenStateEvent.Error -> {
                        uiView.show()
                    }
                }
            }
    }
}
