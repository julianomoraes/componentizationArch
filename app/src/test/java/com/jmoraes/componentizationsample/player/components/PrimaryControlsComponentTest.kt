package com.jmoraes.componentizationsample.player.components

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.netflix.componentizationV1.EventBusFactory
import com.netflix.elfo.components.PrimaryControlsUIView
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class PrimaryControlsComponentTest {
    private lateinit var component : TestPrimaryControlsComponent
    private val owner = mock<LifecycleOwner> {
        on { lifecycle } doReturn mock()
    }

    @Before
    fun setUp() {
        component = TestPrimaryControlsComponent(mock(), EventBusFactory.get(owner))
    }

    @Test
    fun testBuffering() {
        EventBusFactory.get(owner).emit(PlayerEvents::class.java, PlayerEvents.Buffering)
        Mockito.verify(component.uiView, Mockito.times(1)).hide()
        Mockito.verify(component.uiView, Mockito.times(0)).show()
    }

    @Test
    fun testPlayStarted() {
        EventBusFactory.get(owner).emit(PlayerEvents::class.java, PlayerEvents.PlayStarted)
        Mockito.verify(component.uiView, Mockito.times(0)).hide()
        Mockito.verify(component.uiView, Mockito.times(1)).show()
        Mockito.verify(component.uiView, Mockito.times(1)).setPlayPauseImageResource(true)
    }

    @Test
    fun testPaused() {
        EventBusFactory.get(owner).emit(PlayerEvents::class.java, PlayerEvents.Paused)
        Mockito.verify(component.uiView, Mockito.times(0)).hide()
        Mockito.verify(component.uiView, Mockito.times(1)).show()
        Mockito.verify(component.uiView, Mockito.times(1)).setPlayPauseImageResource(false)
    }
}

class TestPrimaryControlsComponent(container: ViewGroup, busFactory: EventBusFactory) : PrimaryControlsComponent(container, busFactory) {
    override fun initView(container: ViewGroup, bus: EventBusFactory): PrimaryControlsUIView {
        return mock()
    }
}