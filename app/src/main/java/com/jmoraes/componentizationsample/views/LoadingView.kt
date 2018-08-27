package com.jmoraes.componentizationsample.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jmoraes.componentizationsample.R
import com.jmoraes.componentizationsample.arch.UIView
import com.jmoraes.componentizationsample.eventTypes.UserInteractionEvent

class LoadingView(container: ViewGroup) : UIView<UserInteractionEvent>(container) {
    override val view: View = LayoutInflater.from(container.context).inflate(R.layout.loading, container, false)
    init {
        container.addView(view)
    }
}
