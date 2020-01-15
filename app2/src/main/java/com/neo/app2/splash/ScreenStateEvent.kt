package com.neo.app2.splash

import com.netflix.componentizationV1.ComponentEvent

sealed class ScreenStateEvent : ComponentEvent() {
    object Loading : ScreenStateEvent()
    object Loaded : ScreenStateEvent()
    object Error : ScreenStateEvent()
}