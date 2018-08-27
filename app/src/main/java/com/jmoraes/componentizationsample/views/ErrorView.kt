package com.jmoraes.componentizationsample.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.jmoraes.componentizationsample.R
import com.jmoraes.componentizationsample.arch.UIView
import com.jmoraes.componentizationsample.eventTypes.UserInteractionEvent

class ErrorView(container: ViewGroup) : UIView<UserInteractionEvent>(container) {
    override val view: View = LayoutInflater.from(container.context).inflate(R.layout.error, container, false)

    init {
        container.addView(view)
        view.findViewById<Button>(R.id.button)
            .setOnClickListener {
                userInteractionEvents.onNext(UserInteractionEvent.IntentTapRetry)
            }
    }
}