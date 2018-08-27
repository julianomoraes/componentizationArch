package com.jmoraes.componentizationsample.arch

import android.view.View
import android.view.ViewGroup
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * View helper to make sure every View follows the expected constructor, provide a userInteraction observable
 */
abstract class UIView<UIE>(container: ViewGroup) {
    // The view to be inflated from an XML layout, or programmatically created
    abstract val view: View
    protected val userInteractionEvents: PublishSubject<UIE> = PublishSubject.create()

    fun getUserInteractionEvents() : Observable<UIE> {
        return userInteractionEvents
    }

    fun show() {
        view.visibility = View.VISIBLE
    }

    fun hide() {
        view.visibility = View.GONE
    }
}