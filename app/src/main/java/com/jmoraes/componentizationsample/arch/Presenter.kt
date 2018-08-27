package com.jmoraes.componentizationsample.arch

import io.reactivex.Observable

/**
 * Presenter helper to make sure every Presenter follows the expected constructor
 */
abstract class Presenter<SSE, UIE>(
        uiView: UIView<UIE>,
        screenStateEvent: Observable<SSE>,
        destroyObservable: Observable<Unit>
)