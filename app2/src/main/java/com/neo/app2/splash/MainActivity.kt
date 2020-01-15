package com.neo.app2.splash

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.neo.app2.R
import com.neo.app2.splash.components.ErrorComponent
import com.neo.app2.splash.components.ProgressComponent
import com.netflix.componentizationV1.EventBusFactory
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), LifecycleOwner {

    private lateinit var lifecycleRegistry: LifecycleRegistry

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //1
        lifecycleRegistry = LifecycleRegistry(this)
        lifecycleRegistry.markState(Lifecycle.State.CREATED)
        setContentView(R.layout.activity_main)

        //2
        initComponents(findViewById(R.id.root_layout))

        //3
//        startTest()
        EventBusFactory.get(this)
                .emit(ScreenStateEvent::class.java, ScreenStateEvent.Error)
    }

    private fun startTest() {
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

    @SuppressLint("CheckResult")
    private fun initComponents(rootViewContainer: ViewGroup) {
        ProgressComponent(rootViewContainer, EventBusFactory.get(this))
        // If the UI Component emits Interaction Events it can be observed like this
        ErrorComponent(rootViewContainer, EventBusFactory.get(this))
                .getUserInteractionEvents()
                .subscribe {
                    when (it) {
                        UserActionEvent.Retry -> {
                            //startSimulation()
                        }
                        // ... all other events if any should be here
                    }
                }

        //SuccessComponent(rootViewContainer, EventBusFactory.get(this))
    }
}
