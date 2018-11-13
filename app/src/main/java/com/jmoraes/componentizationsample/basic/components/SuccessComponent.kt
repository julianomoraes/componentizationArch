package com.jmoraes.componentizationsample.basic.components

import android.annotation.SuppressLint
import android.view.ViewGroup
import com.jmoraes.componentizationsample.basic.eventTypes.ScreenStateEvent
import com.jmoraes.componentizationsample.basic.components.uiViews.SuccessView
import com.netflix.arch.EventBusFactory

@SuppressLint("CheckResult")
open class SuccessComponent(container: ViewGroup, bus: EventBusFactory) {
    val uiView = initView(container, bus)

    open fun initView(container: ViewGroup, bus: EventBusFactory): SuccessView {
        return SuccessView(container, bus)
    }

    init {
        bus.getSafeManagedObservable(ScreenStateEvent::class.java)
            .subscribe {
                when (it) {
                    ScreenStateEvent.Loading -> {
                        uiView.hide()
                    }
                    ScreenStateEvent.Loaded -> {
                        uiView.show()
                    }
                    ScreenStateEvent.Error -> {
                        uiView.hide()
                    }
                }
            }
    }
}