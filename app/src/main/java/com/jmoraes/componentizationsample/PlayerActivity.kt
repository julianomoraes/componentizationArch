package com.jmoraes.componentizationsample

import android.annotation.SuppressLint
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.view.ViewGroup
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.TransferListener
import com.google.android.exoplayer2.util.Util
import com.jmoraes.componentizationsample.basic.components.LoadingComponent
import com.jmoraes.componentizationsample.basic.eventTypes.ScreenStateEvent
import com.netflix.arch.EventBusFactory
import com.jmoraes.componentizationsample.player.components.PlayerEvents
import com.netflix.elfo.components.PlayerUserInteractionEvents
import com.jmoraes.componentizationsample.player.components.PrimaryControlsComponent
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

class PlayerActivity : AppCompatActivity() {
    private var player: SimpleExoPlayer? = null
    private val playerView: PlayerView by lazy { findViewById<PlayerView>(R.id.player_view) }
    private val eventBusFactory = EventBusFactory.get(this)
    private lateinit var primaryControlsComponent: PrimaryControlsComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        val container = findViewById<ConstraintLayout>(R.id.player_root)

        initializeUIComponents(container)
        layoutUIComponents(container)
    }

    @SuppressLint("CheckResult")
    private fun initializeUIComponents(container: ViewGroup) {
        // Reusing this component from the Basic Sample package
        LoadingComponent(container, eventBusFactory)

        primaryControlsComponent = PrimaryControlsComponent(container, eventBusFactory)
        primaryControlsComponent.getUserInteractionEvents()
            .subscribe {
                when (it) {
                    PlayerUserInteractionEvents.IntentPlayPauseClicked -> {
                        player?.playWhenReady = !player?.playWhenReady!!
                    }
                    PlayerUserInteractionEvents.IntentRwClicked -> {
                        player?.seekTo(player?.currentPosition!! / 2)
                    }
                    PlayerUserInteractionEvents.IntentFwClicked -> {
                        player?.seekTo(player?.currentPosition!! * 2)
                    }
                }
            }
    }

    private fun layoutUIComponents(rootViewContainer: ConstraintLayout) {
        // Create a constraintSet and clone it from the main container
        val mainContainerConstraintSet = ConstraintSet()
        mainContainerConstraintSet.clone(rootViewContainer)

        mainContainerConstraintSet.connect(
            primaryControlsComponent.getContainerId(),
            ConstraintSet.TOP,
            ConstraintSet.PARENT_ID,
            ConstraintSet.TOP
        )
        mainContainerConstraintSet.connect(
            primaryControlsComponent.getContainerId(),
            ConstraintSet.BOTTOM,
            ConstraintSet.PARENT_ID,
            ConstraintSet.BOTTOM
        )

        // Apply the constraints from all UI components to the parent
        mainContainerConstraintSet.applyTo(rootViewContainer)
    }

    override fun onStart() {
        super.onStart()
        initializePlayer()
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    private fun initializePlayer() {
        playerView.requestFocus()
        player = ExoPlayerFactory.newSimpleInstance(
            this,
            DefaultTrackSelector(AdaptiveTrackSelection.Factory(DefaultBandwidthMeter()))
        )

        player?.let {
            playerView.player = it
            it.addListener(PlayerEventListener())
        }

        // artificial delay to simulate a network request
        Observable.just(Any())
            .delay(5, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                val mediaSource = ExtractorMediaSource.Factory(
                    DefaultDataSourceFactory(
                        this, Util.getUserAgent(this, "mediaPlayerSample"),
                        DefaultBandwidthMeter() as TransferListener<in DataSource>
                    )
                ).createMediaSource(Uri.parse("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"))

                player?.prepare(mediaSource, false, false)
            }
            .subscribe()

        player?.playWhenReady = true
    }

    private fun releasePlayer() {
        player?.let {
            it.release()
            player = null
        }
    }

    private inner class PlayerEventListener : Player.DefaultEventListener() {
        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            when (playbackState) {
                // The player does not have any media to play yet.
                Player.STATE_IDLE -> {
                    eventBusFactory.emit(ScreenStateEvent::class.java, ScreenStateEvent.Loading)
                }
                // The player is buffering (loading the content)
                Player.STATE_BUFFERING -> {
                    eventBusFactory.emit(ScreenStateEvent::class.java, ScreenStateEvent.Loading)
                    eventBusFactory.emit(PlayerEvents::class.java, PlayerEvents.Buffering)
                }
                // The player is able to immediately play
                Player.STATE_READY -> {
                    eventBusFactory.emit(ScreenStateEvent::class.java, ScreenStateEvent.Loaded)
                    if (playWhenReady) {
                        eventBusFactory.emit(PlayerEvents::class.java, PlayerEvents.PlayStarted)
                    } else {
                        eventBusFactory.emit(PlayerEvents::class.java, PlayerEvents.Paused)
                    }
                }
                // The player has finished playing the media
                Player.STATE_ENDED -> {
                    // TODO
                }
            }
        }
    }
}
