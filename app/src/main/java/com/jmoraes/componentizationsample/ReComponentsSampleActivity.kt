package com.jmoraes.componentizationsample

import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.jmoraes.componentizationsample.recomponents.components.ProjectSelectorComponent
import com.jmoraes.componentizationsample.recomponents.components.ProjectSelectorComponentState
import com.jmoraes.componentizationsample.recomponents.components.WildcardListComponent
import com.jmoraes.componentizationsample.recomponents.components.WildcardListComponentState
import com.jmoraes.componentizationsample.recomponents.redux.middleware.appMiddleware
import com.jmoraes.componentizationsample.recomponents.redux.reducers.appReducer
import com.jmoraes.componentizationsample.recomponents.redux.state.AppState
import com.netflix.recomponents.StoreImpl
import com.netflix.recomponents.UIComponentManager

private val store = StoreImpl(
    reducer = ::appReducer,
    initialState = AppState(),
    middleware = listOf(::appMiddleware)
)

class ReComponentsSampleActivity : AppCompatActivity() {
    private val uiComponentManager by lazy {
        ViewModelProviders.of(this,
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                    return UIComponentManager(store) as T
                }
            }
        )[UIComponentManager::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.linearlayout_main)

        initComponents(findViewById(R.id.root))
    }

    private fun initComponents(container: ViewGroup) {
        with(uiComponentManager) {

            // Selector Component
            subscribe(ProjectSelectorComponent(container)) {
                ProjectSelectorComponentState(
                    selectedProject = (it as AppState).selectedProject,
                    projects = it.projects
                )
            }

            // List Component
            subscribe(WildcardListComponent(container)) {
                WildcardListComponentState(
                    isLoading = (it as AppState).isFetching,
                    wildcardList = it.wildcards
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        uiComponentManager.store.unsubscribeAll()
    }
}
