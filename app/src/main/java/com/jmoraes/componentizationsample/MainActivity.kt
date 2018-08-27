package com.jmoraes.componentizationsample

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
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private var destroyObservable: PublishSubject<Unit> = PublishSubject.create()
    private var screenStateEvent: PublishSubject<ScreenStateEvent> = PublishSubject.create()

    private lateinit var loadingView: LoadingView
    private lateinit var successView: SuccessView
    private lateinit var errorView: ErrorView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // recreates the observables
        destroyObservable = PublishSubject.create()
        screenStateEvent = PublishSubject.create()

        setContentView(R.layout.activity_main)

        val rootViewContainer = findViewById<ViewGroup>(android.R.id.content)

        initComponents(rootViewContainer)
        initUserInteractionEventsObservable()

        startSimulation()
    }

    /**
     * Observes on UserInteractionEvents reacting when required
     */
    private fun initUserInteractionEventsObservable() {
        Observable
            .merge<UserInteractionEvent>(
                loadingView.getUserInteractionEvents(),
                errorView.getUserInteractionEvents(),
                successView.getUserInteractionEvents()
            )
            .takeUntil(destroyObservable)
            .subscribeBy {
                when (it) {
                    UserInteractionEvent.IntentTapRetry -> {
                        startSimulation()
                    }
                    // ... all other events if any should be here
                }
            }
    }

    /**
     * Initialize all UI Components
     */
    private fun initComponents(rootViewContainer: ViewGroup) {
        loadingView = LoadingView(rootViewContainer)
        LoadingPresenter(loadingView, screenStateEvent, destroyObservable)

        successView = SuccessView(rootViewContainer)
        SuccessPresenter(successView, screenStateEvent, destroyObservable)

        errorView = ErrorView(rootViewContainer)
        ErrorPresenter(errorView, screenStateEvent, destroyObservable)
    }

    /**
     * Start a simulation emitting events every 2 seconds
     */
    private fun startSimulation() {
        Observable.just(Any())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                screenStateEvent.onNext(ScreenStateEvent.Loading)
            }
            .delay(2, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                screenStateEvent.onNext(ScreenStateEvent.Loaded)
            }
            .delay(2, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                screenStateEvent.onNext(ScreenStateEvent.Error)
            }
            .subscribe()
    }

    /**
     * Stop all subscriptions
     */
    override fun onDestroy() {
        super.onDestroy()
        destroyObservable.onNext(Unit)
        destroyObservable.onComplete()
    }
}
