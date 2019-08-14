# Componentization Architecture v2 - [ReComponents] 

Reactive, lifecycle aware, testable and reusable UI components for Android using Redux.

[Blog Post](https://medium.com/@NetflixTechBlog/making-our-android-studio-apps-reactive-with-ui-components-redux-5e37aac3b244)

## Installation
~~~gradle
dependencies {
    // Netflix's ReComponents 0.0.1 Beta
    api(name:'recomponents-0.0.1', ext:'aar')
}
~~~

## Fragment / Activity
~~~kotlin
private val store = StoreImpl(
    reducer = ::appReducer,
    initialState = AppState(),
    middleware = listOf(::appMiddleware)
)

class YourActivity : AppCompatActivity() {
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
            subscribe(YourComponent(container)) {
                YourComponentState(
                    ...
                )
            }
            
            ...
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        uiComponentManager.store.unsubscribeAll()
    }
~~~

## Component
~~~kotlin
data class YourComponentState(
    ...
)

class YourComponent(
    container: ViewGroup
) : UIComponent<YourComponentState>() {
    override fun render(state: MyComponentState) {}
}

~~~

## Complete Code Samples
[Activity](https://github.com/julianomoraes/componentizationArch/blob/master/app/src/main/java/com/jmoraes/componentizationsample/ReComponentsSampleActivity.kt)

[Redux + Components](https://github.com/julianomoraes/componentizationArch/tree/master/app/src/main/java/com/jmoraes/componentizationsample/recomponents)

[Components & RecyclerViews](https://github.com/julianomoraes/componentizationArch/blob/master/app/src/main/java/com/jmoraes/componentizationsample/recomponents/components/WildCardListComponent.kt)

## References
[ReKotlin](https://github.com/ReKotlin/ReKotlin)


#### .
#### .
#### .
#### .
#### .

# [DEPRECATED] Netflix’s Android Componentization Architecture V1

Lifecycle aware, testable and reusable UI components for Android.

[Droidcon NYC 2018 Talk](https://youtu.be/dS9gho9Rxn4)

[Droidcon SF 2018 Talk](https://youtu.be/1cWwfh_5ZQs)

## Sample code
### Fragment / Activity

~~~kotlin
...
LoadingComponent(container, EventBusFactory.get(this))
...
~~~

### Component (Presenter/Controller/ViewModel)

~~~kotlin
...
bus.getSafeManagedObservable(ScreenStateEvent::class.java)
            .subscribe {
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
...   
~~~

### UIView

~~~kotlin
class LoadingView(container: ViewGroup) : UIView<UserInteractionEvent>(container) {
    private val view: View =
        LayoutInflater.from(container.context).inflate(R.layout.loading, container, true)
            .findViewById(R.id.loadingSpinner)
...            
~~~

## Complete Samples
[Basic Loading/Error/Success Screen](https://github.com/julianomoraes/componentizationArch/tree/master/app/src/main/java/com/jmoraes/componentizationsample/basic/components)

[Basic Player UI Component](https://github.com/julianomoraes/componentizationArch/tree/master/app/src/main/java/com/jmoraes/componentizationsample/player/components)

## Unit Tests
[Unit tests for Components](https://github.com/julianomoraes/componentizationArch/tree/master/app/src/test/java/com/jmoraes/componentizationsample)

