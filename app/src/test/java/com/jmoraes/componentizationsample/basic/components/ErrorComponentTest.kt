package com.jmoraes.componentizationsample.basic.components

import android.arch.lifecycle.LifecycleOwner
import android.view.ViewGroup
import com.jmoraes.componentizationsample.basic.eventTypes.ScreenStateEvent
import com.jmoraes.componentizationsample.basic.components.uiViews.ErrorView
import com.netflix.arch.EventBusFactory
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class ErrorComponentTest {
    private lateinit var component : ErrorComponent
    private val owner = mock<LifecycleOwner> {
        on { lifecycle } doReturn mock()
    }

    @Before
    fun setUp() {
        component = ErrorComponentMock(mock(), EventBusFactory.get(owner))
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
        Mockito.verify(component.uiView, Mockito.times(1)).hide()
        Mockito.verify(component.uiView, Mockito.times(0)).show()
    }

    @Test
    fun testError() {
        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.Error)
        Mockito.verify(component.uiView, Mockito.times(0)).hide()
        Mockito.verify(component.uiView, Mockito.times(1)).show()
    }
}

class ErrorComponentMock(container: ViewGroup, busFactory: EventBusFactory) : ErrorComponent(container, busFactory) {
    override fun initView(container: ViewGroup, bus: EventBusFactory): ErrorView {
        return mock()
    }
}