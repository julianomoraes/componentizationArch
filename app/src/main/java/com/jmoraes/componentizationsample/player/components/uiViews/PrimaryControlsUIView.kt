package com.netflix.elfo.components

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import com.jmoraes.componentizationsample.R
import com.jmoraes.componentizationsample.basic.eventTypes.UserInteractionEvent
import com.netflix.arch.ComponentEvent
import com.netflix.arch.EventBusFactory
import com.netflix.arch.UIView

class PrimaryControlsUIView(container: ViewGroup, eventBusFactory: EventBusFactory) :
    UIView<UserInteractionEvent>(container) {
    private val uiView: View =
        LayoutInflater.from(container.context).inflate(R.layout.primary_controls, container, false)

    private val playPauseBtn: ImageButton = uiView.findViewById(R.id.pause_btn)
    private val rwBtn: ImageButton = uiView.findViewById(R.id.rw_btn)
    private val fwBtn: ImageButton = uiView.findViewById(R.id.fw_btn)

    init {
        container.addView(uiView)

        playPauseBtn.setOnClickListener {
            eventBusFactory.emit(
                PlayerUserInteractionEvents::class.java,
                PlayerUserInteractionEvents.IntentPlayPauseClicked
            )
        }

        rwBtn.setOnClickListener {
            eventBusFactory.emit(
                PlayerUserInteractionEvents::class.java,
                PlayerUserInteractionEvents.IntentRwClicked
            )
        }

        fwBtn.setOnClickListener {
            eventBusFactory.emit(
                PlayerUserInteractionEvents::class.java,
                PlayerUserInteractionEvents.IntentFwClicked
            )
        }
    }

    override val containerId: Int = uiView.id

    override fun show() {
        uiView.visibility = View.VISIBLE
    }

    override fun hide() {
        uiView.visibility = View.GONE
    }

    fun setPlayPauseImageResource(pauseImage: Boolean) {
        if (pauseImage) {
            playPauseBtn.setImageResource(R.drawable.ic_player_pause)
        } else {
            playPauseBtn.setImageResource(R.drawable.ic_player_play)
        }
    }
}

sealed class PlayerUserInteractionEvents : ComponentEvent() {
    object IntentPlayPauseClicked : PlayerUserInteractionEvents()
    object IntentRwClicked : PlayerUserInteractionEvents()
    object IntentFwClicked : PlayerUserInteractionEvents()
}
