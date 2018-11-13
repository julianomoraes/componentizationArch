package com.jmoraes.componentizationsample.presenters

import android.annotation.SuppressLint
import android.view.ViewGroup
import com.jmoraes.componentizationsample.eventTypes.ScreenStateEvent
import com.jmoraes.componentizationsample.views.ErrorView
import com.netflix.arch.EventBusFactory

@SuppressLint("CheckResult")
class ErrorPresenter(container: ViewGroup, bus: EventBusFactory) {
    private val uiView = ErrorView(container, bus)

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
