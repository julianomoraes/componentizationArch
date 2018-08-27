package com.jmoraes.componentizationsample.presenters

import com.jmoraes.componentizationsample.arch.Presenter
import com.jmoraes.componentizationsample.arch.UIView
import com.jmoraes.componentizationsample.eventTypes.ScreenStateEvent
import com.jmoraes.componentizationsample.eventTypes.UserInteractionEvent
import io.reactivex.Observable
import io.reactivex.rxkotlin.subscribeBy

class LoadingPresenter<SSE : ScreenStateEvent, UIE : UserInteractionEvent>(
        uiView: UIView<UIE>,
        screenStateEvent: Observable<SSE>,
        destroyObservable: Observable<Unit>
) : Presenter<SSE, UIE>(uiView, screenStateEvent, destroyObservable) {

    init {
        screenStateEvent
            .takeUntil(destroyObservable)
            .subscribeBy(
                onNext = {
                    when (it) {
                        ScreenStateEvent.Loading -> {
                            uiView.show()
                        }
                        ScreenStateEvent.Loaded -> {
                            uiView.hide()
                        }
                        ScreenStateEvent.Error -> {
                            uiView.hide()
                        }
                    }
                }
            )
    }
}
