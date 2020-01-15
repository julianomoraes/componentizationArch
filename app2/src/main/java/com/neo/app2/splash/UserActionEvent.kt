package com.neo.app2.splash

import com.netflix.componentizationV1.ComponentEvent

sealed class UserActionEvent : ComponentEvent() {
    object Retry : UserActionEvent()
}
