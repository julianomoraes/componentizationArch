package com.jmoraes.componentizationsample.player.components

import android.annotation.SuppressLint
import android.support.annotation.VisibleForTesting
import android.view.ViewGroup
import com.netflix.arch.ComponentEvent
import com.netflix.arch.EventBusFactory
import com.netflix.arch.UIComponent
import com.netflix.elfo.components.PlayerUserInteractionEvents
import com.netflix.elfo.components.PrimaryControlsUIView
import io.reactivex.Observable

@SuppressLint("CheckResult")
open class PrimaryControlsComponent(container: ViewGroup, private val bus: EventBusFactory) : UIComponent<PlayerUserInteractionEvents> {
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val uiView = initView(container, bus)

    open fun initView(container: ViewGroup, bus: EventBusFactory): PrimaryControlsUIView {
        return PrimaryControlsUIView(container, bus)
    }

    override fun getContainerId() = uiView.containerId

    override fun getUserInteractionEvents(): Observable<PlayerUserInteractionEvents> {
        return bus.getSafeManagedObservable(PlayerUserInteractionEvents::class.java)
    }

    init {
        bus.getSafeManagedObservable(PlayerEvents::class.java)
            .subscribe {
                when (it) {
                    PlayerEvents.Buffering -> {
                        uiView.hide()
                    }
                    PlayerEvents.PlayStarted -> {
                        uiView.show()
                        uiView.setPlayPauseImageResource(true)
                    }
                    PlayerEvents.Paused -> {
                        uiView.show()
                        uiView.setPlayPauseImageResource(false)
                    }
                }
            }
    }
}

sealed class PlayerEvents : ComponentEvent() {
    object Buffering : PlayerEvents()
    object PlayStarted : PlayerEvents()
    object Paused : PlayerEvents()
}