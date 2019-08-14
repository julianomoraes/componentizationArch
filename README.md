# Componentization Architecture v2 - [ReComponents] 

A reactive, declarative, lifecycle aware, testable and reusable way of using UI components on Android using Redux.

[Blog Post](https://medium.com/@NetflixTechBlog/making-our-android-studio-apps-reactive-with-ui-components-redux-5e37aac3b244)

> Fragment is not your view

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

[Netflix’s Android Componentization Architecture V1 - Droidcon NYC 2018 Talk](https://youtu.be/dS9gho9Rxn4)

[Netflix’s Android Componentization Architecture V1 - Droidcon SF 2018 Talk](https://youtu.be/1cWwfh_5ZQs)
