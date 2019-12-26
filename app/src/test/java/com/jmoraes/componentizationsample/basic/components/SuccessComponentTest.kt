package com.jmoraes.componentizationsample.basic.components

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.jmoraes.componentizationsample.basic.eventTypes.ScreenStateEvent
import com.jmoraes.componentizationsample.basic.components.uiViews.SuccessView
import com.netflix.componentizationV1.EventBusFactory
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class SuccessComponentTest {
    private lateinit var component : SuccessComponent
    private val owner = mock<LifecycleOwner> {
        on { lifecycle } doReturn mock()
    }

    @Before
    fun setUp() {
        component = SuccessComponentMock(mock(), EventBusFactory.get(owner))
    }

    @Test
    fun testLoading() {
        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.Loading)
        Mockito.verify(component.uiView, Mockito.times(1)).hide()
        Mockito.verify(component.uiView, Mockito.times(0)).show()
    }

    @Test
    fun testLoaded() {
        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.Loaded)
        Mockito.verify(component.uiView, Mockito.times(0)).hide()
        Mockito.verify(component.uiView, Mockito.times(1)).show()
    }

    @Test
    fun testError() {
        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.Error)
        Mockito.verify(component.uiView, Mockito.times(1)).hide()
        Mockito.verify(component.uiView, Mockito.times(0)).show()
    }
}

class SuccessComponentMock(container: ViewGroup, bus: EventBusFactory) : SuccessComponent(container, bus) {
    override fun initView(container: ViewGroup, bus: EventBusFactory): SuccessView {
        return mock()
    }
}