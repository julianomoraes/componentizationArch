# Netflix’s Android Componentization Architecture

## Sample code for Droidcon NYC 2018
[Droidcon NYC Talk](https://youtu.be/dS9gho9Rxn4)

### Fragment / Activity

~~~kotlin
...
LoadingPresenter(
    LoadingView(rootViewContainer),
    screenStateEvent,
    destroyObservable
)
...
~~~

### Presenter

~~~kotlin
...
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
...   
~~~

### View

~~~kotlin
class LoadingView(container: ViewGroup) : UIView<UserInteractionEvent>(container) {
    override val view: View = LayoutInflater.from(container.context).inflate(R.layout.loading, container, false)
    init {
        container.addView(view)
    }
}
~~~

