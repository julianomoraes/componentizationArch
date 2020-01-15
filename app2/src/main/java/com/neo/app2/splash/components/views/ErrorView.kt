package com.neo.app2.splash.components.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.neo.app2.R
import com.neo.app2.splash.UserActionEvent
import com.netflix.componentizationV1.EventBusFactory
import com.netflix.componentizationV1.UIView

class ErrorView(container: ViewGroup, eventBusFactory: EventBusFactory) :
        UIView<UserActionEvent>(container) {
    private val view: View =
            LayoutInflater.from(container.context).inflate(R.layout.view_error, container, true)
                    .findViewById(R.id.error_container)

    override val containerId: Int = 0

    init {
        view.findViewById<Button>(R.id.button)
                .setOnClickListener {
                    eventBusFactory.emit(
                            UserActionEvent::class.java,
                            UserActionEvent.Retry
                    )
                }
    }

    override fun show() {
        view.visibility = View.VISIBLE
    }

    override fun hide() {
        view.visibility = View.GONE
    }
}