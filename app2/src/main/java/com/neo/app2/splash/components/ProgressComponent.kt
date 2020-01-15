package com.neo.app2.splash.components

import android.view.ViewGroup
import com.neo.app2.splash.ScreenStateEvent
import com.neo.app2.splash.components.views.LoadingView
import com.netflix.componentizationV1.EventBusFactory
import com.netflix.componentizationV1.UIComponent
import io.reactivex.Observable

open class ProgressComponent(container: ViewGroup, bus: EventBusFactory) : UIComponent<Unit> {
    override fun getContainerId(): Int {
        return uiView.containerId
    }

    override fun getUserInteractionEvents(): Observable<Unit> {
        return Observable.empty()
    }

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
