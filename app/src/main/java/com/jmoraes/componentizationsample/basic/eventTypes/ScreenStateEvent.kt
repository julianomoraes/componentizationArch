package com.jmoraes.componentizationsample.basic.eventTypes

import com.netflix.componentizationV1.ComponentEvent

/**
 * List of all events this Screen can emit
 */
sealed class ScreenStateEvent : ComponentEvent() {
    object Loading : ScreenStateEvent()
    object Loaded : ScreenStateEvent()
    object Error : ScreenStateEvent()
}