package com.jmoraes.componentizationsample

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import com.jmoraes.componentizationsample.basic.eventTypes.ScreenStateEvent
import com.jmoraes.componentizationsample.basic.eventTypes.UserInteractionEvent
import com.jmoraes.componentizationsample.basic.components.ErrorComponent
import com.jmoraes.componentizationsample.basic.components.LoadingComponent
import com.jmoraes.componentizationsample.basic.components.SuccessComponent
import com.netflix.arch.EventBusFactory
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
}
