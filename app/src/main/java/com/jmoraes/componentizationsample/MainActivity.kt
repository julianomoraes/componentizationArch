package com.jmoraes.componentizationsample

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.jmoraes.componentizationsample.basic.eventTypes.ScreenStateEvent
import com.jmoraes.componentizationsample.basic.eventTypes.UserInteractionEvent
import com.jmoraes.componentizationsample.basic.components.ErrorComponent
import com.jmoraes.componentizationsample.basic.components.LoadingComponent
import com.jmoraes.componentizationsample.basic.components.SuccessComponent
import com.netflix.componentizationV1.EventBusFactory
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), LifecycleOwner {
    private lateinit var lifecycleRegistry: LifecycleRegistry

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleRegistry = LifecycleRegistry(this)
        lifecycleRegistry.markState(Lifecycle.State.CREATED)

        setContentView(R.layout.activity_main)

        initComponents(findViewById(R.id.root))

        startSimulation()
    }

    /**
     * Initialize all UI Components
     */
    @SuppressLint("CheckResult")
    private fun initComponents(rootViewContainer: ViewGroup) {
        LoadingComponent(rootViewContainer, EventBusFactory.get(this))
        // If the UI Component emits Interaction Events it can be observed like this
        ErrorComponent(rootViewContainer, EventBusFactory.get(this))
            .getUserInteractionEvents()
            .subscribe {
                when (it) {
                    UserInteractionEvent.IntentTapRetry -> {
                        startSimulation()
                    }
                    // ... all other events if any should be here
                }
            }
        SuccessComponent(rootViewContainer, EventBusFactory.get(this))
    }

    /**
     * Start a simulation emitting events every 2 seconds
     */
    private fun startSimulation() {
        Observable.just(Any())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                EventBusFactory.get(this)
                    .emit(ScreenStateEvent::class.java, ScreenStateEvent.Loading)
            }
            .delay(2, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                EventBusFactory.get(this)
                    .emit(ScreenStateEvent::class.java, ScreenStateEvent.Loaded)
            }
            .delay(2, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                EventBusFactory.get(this).emit(ScreenStateEvent::class.java, ScreenStateEvent.Error)
            }
            .subscribe()
    }

    override fun getLifecycle(): Lifecycle {
        return lifecycleRegistry
    }
}
