package com.jmoraes.componentizationsample

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import com.jmoraes.componentizationsample.eventTypes.ScreenStateEvent
import com.jmoraes.componentizationsample.eventTypes.UserInteractionEvent
import com.jmoraes.componentizationsample.components.ErrorComponent
import com.jmoraes.componentizationsample.components.LoadingComponent
import com.jmoraes.componentizationsample.components.SuccessComponent
import com.netflix.arch.EventBusFactory
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initComponents(findViewById(R.id.root))
        initUserInteractionEventsObservable()

        startSimulation()
    }

    /**
     * Initialize all UI Components
     */
    private fun initComponents(rootViewContainer: ViewGroup) {
        LoadingComponent(rootViewContainer, EventBusFactory.get(this))
        ErrorComponent(rootViewContainer, EventBusFactory.get(this))
        SuccessComponent(rootViewContainer, EventBusFactory.get(this))
    }

    /**
     * Observes on UserInteractionEvents reacting when required
     */
    @SuppressLint("CheckResult")
    private fun initUserInteractionEventsObservable() {
        EventBusFactory.get(this).getSafeManagedObservable(UserInteractionEvent::class.java)
            .subscribe {
                when (it) {
                    UserInteractionEvent.IntentTapRetry -> {
                        startSimulation()
                    }
                    // ... all other events if any should be here
                }
            }
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
