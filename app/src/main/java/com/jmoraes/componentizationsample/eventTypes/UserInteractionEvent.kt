package com.jmoraes.componentizationsample.eventTypes

import com.netflix.arch.ComponentEvent

/**
 * List of all events Views can emit
 */
sealed class UserInteractionEvent : ComponentEvent() {
    object IntentTapRetry : UserInteractionEvent()
}