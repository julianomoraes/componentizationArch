package com.jmoraes.componentizationsample.eventTypes

import com.netflix.arch.ComponentEvent

/**
 * List of all events this Screen can emit
 */
sealed class ScreenStateEvent : ComponentEvent() {
    object Loading : ScreenStateEvent()
    object Loaded : ScreenStateEvent()
    object Error : ScreenStateEvent()
}