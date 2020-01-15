package com.neo.app2.splash.components

import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import com.neo.app2.splash.ScreenStateEvent
import com.neo.app2.splash.UserActionEvent
import com.neo.app2.splash.components.views.ErrorView
import com.neo.app2.splash.components.views.LoadingView
import com.netflix.componentizationV1.EventBusFactory
import com.netflix.componentizationV1.UIComponent
import io.reactivex.Observable

open class ErrorComponent(container: ViewGroup, private val bus: EventBusFactory) : UIComponent<UserActionEvent> {
    override fun getContainerId(): Int {
        return uiView.containerId
    }

    override fun getUserInteractionEvents(): Observable<UserActionEvent> {
        return bus.getSafeManagedObservable(UserActionEvent::class.java)
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