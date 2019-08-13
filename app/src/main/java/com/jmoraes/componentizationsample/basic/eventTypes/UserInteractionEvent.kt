package com.jmoraes.componentizationsample.basic.eventTypes

import com.netflix.componentizationV1.ComponentEvent

/**
 * List of all events Views can emit
 */
sealed class UserInteractionEvent : ComponentEvent() {
    object IntentTapRetry : UserInteractionEvent()
}