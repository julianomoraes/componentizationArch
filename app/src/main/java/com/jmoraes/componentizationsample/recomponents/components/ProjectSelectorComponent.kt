package com.jmoraes.componentizationsample.recomponents.components

import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.annotation.VisibleForTesting
import com.jmoraes.componentizationsample.R
import com.jmoraes.componentizationsample.recomponents.models.Project
import com.jmoraes.componentizationsample.recomponents.redux.actions.FetchData
import com.netflix.recomponents.UIComponent

data class ProjectSelectorComponentState(
    val projects: List<Project>,
    val selectedProject: Project?
)

class ProjectSelectorComponent(
    container: ViewGroup
) : UIComponent<ProjectSelectorComponentState>() {

    companion object {
        private const val TAG = "ProjectSelector"
    }

    private val rootView = getRootView(container, R.layout.project_selector)

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    var list = mutableListOf<String>()

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val spinnerAdapter by lazy { ArrayAdapter(rootView.context, R.layout.simple_spinner_item, list) }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val selectorView: Spinner by lazy {
        rootView.findViewById<Spinner>(R.id.projectSelector)
            .apply {
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                adapter = spinnerAdapter

                onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }

                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        dispatchFunction(FetchData(position + 1))
                    }

                }
            }
    }

    override fun render(state: ProjectSelectorComponentState) {
        if (state.projects.isNotEmpty()) {
                var selectedProjectIndex = 0

                list.clear().apply {
                    state.projects.map {
                        list.add(it.title)
                        if (it.id == state.selectedProject?.id) {
                            selectedProjectIndex = list.size - 1
                        }
                    }
                }
                spinnerAdapter.notifyDataSetChanged()
                selectorView.tag = selectedProjectIndex
                selectorView.setSelection(selectedProjectIndex)
                selectorView.visibility = View.VISIBLE
            }
    }
}

