package com.jmoraes.componentizationsample.basic.components

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import com.jmoraes.componentizationsample.basic.eventTypes.ScreenStateEvent
import com.jmoraes.componentizationsample.basic.components.uiViews.ErrorView
import com.jmoraes.componentizationsample.basic.eventTypes.UserInteractionEvent
import com.netflix.componentizationV1.EventBusFactory
import com.netflix.componentizationV1.UIComponent
import io.reactivex.Observable

@SuppressLint("CheckResult")
open class ErrorComponent(container: ViewGroup, private val bus: EventBusFactory) : UIComponent<UserInteractionEvent> {
    override fun getContainerId(): Int {
        return uiView.containerId
    }

    override fun getUserInteractionEvents(): Observable<UserInteractionEvent> {
        return bus.getSafeManagedObservable(UserInteractionEvent::class.java)
    }

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
