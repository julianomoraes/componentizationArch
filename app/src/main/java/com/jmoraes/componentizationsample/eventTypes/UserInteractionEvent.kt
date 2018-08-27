package com.jmoraes.componentizationsample.eventTypes

/**
 * List of all events Views can emit
 */
sealed class UserInteractionEvent {
    object IntentTapRetry : UserInteractionEvent()
}