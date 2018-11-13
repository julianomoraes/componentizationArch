package com.jmoraes.componentizationsample

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import com.jmoraes.componentizationsample.eventTypes.ScreenStateEvent
import com.jmoraes.componentizationsample.eventTypes.UserInteractionEvent
import com.jmoraes.componentizationsample.presenters.ErrorPresenter
import com.jmoraes.componentizationsample.presenters.LoadingPresenter
import com.jmoraes.componentizationsample.presenters.SuccessPresenter
import com.jmoraes.componentizationsample.views.ErrorView
import com.jmoraes.componentizationsample.views.LoadingView
import com.jmoraes.componentizationsample.views.SuccessView
import com.netflix.arch.EventBusFactory
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.PublishSubject
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
        LoadingPresenter(rootViewContainer, EventBusFactory.get(this))
        ErrorPresenter(rootViewContainer, EventBusFactory.get(this))
        SuccessPresenter(rootViewContainer, EventBusFactory.get(this))
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
