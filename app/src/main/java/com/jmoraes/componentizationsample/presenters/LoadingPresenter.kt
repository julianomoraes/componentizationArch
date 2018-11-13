package com.jmoraes.componentizationsample.presenters

import android.annotation.SuppressLint
import android.view.ViewGroup
import com.jmoraes.componentizationsample.eventTypes.ScreenStateEvent
import com.jmoraes.componentizationsample.views.LoadingView
import com.netflix.arch.EventBusFactory
import io.reactivex.rxkotlin.subscribeBy

@SuppressLint("CheckResult")
class LoadingPresenter(container: ViewGroup, bus: EventBusFactory) {
    private val uiView = LoadingView(container)

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
