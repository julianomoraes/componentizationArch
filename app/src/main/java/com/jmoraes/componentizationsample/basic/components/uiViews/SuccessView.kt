package com.jmoraes.componentizationsample.basic.components.uiViews

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jmoraes.componentizationsample.R
import com.jmoraes.componentizationsample.basic.eventTypes.UserInteractionEvent
import com.netflix.arch.EventBusFactory
import com.netflix.arch.UIView

class SuccessView(container: ViewGroup, eventBusFactory: EventBusFactory) :
    UIView<UserInteractionEvent>(container) {
    private val view: View =
        LayoutInflater.from(container.context).inflate(R.layout.success, container, true)
            .findViewById(R.id.success_tv)

    override val containerId: Int = view.id

    override fun show() {
        view.visibility = View.VISIBLE
    }

    override fun hide() {
        view.visibility = View.GONE
    }
}
