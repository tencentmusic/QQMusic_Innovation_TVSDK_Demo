package com.tencent.qqmusictvsdkdemo.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.tencent.qqmusicsdk.protocol.PlayStateHelper
import com.tencent.qqmusictvsdk.QQMusicSDK
import com.tencent.qqmusictvsdk.lyric.LyricManager
import com.tencent.qqmusictvsdk.player.Event.API_EVENT_PLAY_STATE_CHANGED
import com.tencent.qqmusictvsdk.player.IMediaEventListener
import com.tencent.qqmusictvsdk.player.IPlayerManager
import com.tencent.qqmusictvsdk.player.MVInfo
import com.tencent.qqmusictvsdk.player.SongInfo
import com.tencent.qqmusictvsdkdemo.R

/*
 * Copyright (C) 2020 Tencent Music Entertainment Group. All Rights Reserved. 
 *  
 *
 * @Author chaoccwang
 * @Date 09/08/2020
 */
class PlayerFragment : Fragment() {
    companion object {
        const val TAG = "PlayerFragment"
    }
    private fun getMV(): ArrayList<MVInfo> {
        return ArrayList(listOf("g00349jxqpw", "r0034oiuica", "i0034xbq2g9").map {
            MVInfo().also { mv ->
                mv.mv_vid = it
            }
        })
    }

    private fun getSongs(): ArrayList<SongInfo> {

        return ArrayList(listOf("002w57E00BGzXn", "002VIFU90S4ICL", "002GwAma2DGN2x").map {
            SongInfo().also { song ->
                song.song_mid = it
            }
        })
    }

    lateinit var playerManager: IPlayerManager
    lateinit var playMV: Button
    lateinit var playSongs: Button
    lateinit var play: Button
    lateinit var next: Button
    lateinit var prev: Button
    lateinit var startPlayMV: FrameLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playerManager = QQMusicSDK.getPlayerManager()
        playerManager.registerEventListener(mIMediaEventListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        playerManager.unregisterEventListener(mIMediaEventListener)
    }

    private val mIMediaEventListener = object: IMediaEventListener {
        override fun onEvent(event: String, arg: Bundle) {
            Log.d(TAG, "onEvent event = $event, arg = $arg")
            when(event) {
                API_EVENT_PLAY_STATE_CHANGED -> {
                    if (PlayStateHelper.isPlayingForUI()) {
                        play.text = "Pause"
                    } else if (PlayStateHelper.isPausedForUI()) {
                        play.text = "Play"
                    }
                }
            }
        }
    }

    private val clickListener = View.OnClickListener {
        when(it?.id) {
            R.id.playMV -> {
                var mvView = playerManager.playMV(getMV(), 0) as View
                mvView.layoutParams = FrameLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.MATCH_PARENT,
                    ConstraintLayout.LayoutParams.MATCH_PARENT
                )
                mvView.visibility = View.VISIBLE
                val vg = mvView.parent
                if (vg != null) {
                    (vg as ViewGroup).removeViewInLayout(mvView)
                }
                startPlayMV.addView(mvView)
            }
            R.id.playSongs -> {
                playerManager.playSongs(getSongs(), 0)
                LyricManager.instance.startLoadLyric(0)
            }
            R.id.next -> {
                playerManager.next()
            }
            R.id.prev -> {
                playerManager.prev()
            }
            R.id.play -> {
                if (PlayStateHelper.isPlayingForUI()) {
                    playerManager.pause()
                } else if (PlayStateHelper.isPausedForUI()) {
                    playerManager.play()
                }
            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_player, container, false)
        play = view.findViewById(R.id.play)
        play.setOnClickListener(clickListener)

        playMV = view.findViewById(R.id.playMV)
        playMV.setOnClickListener(clickListener)

        playSongs = view.findViewById(R.id.playSongs)
        playSongs.setOnClickListener(clickListener)

        next = view.findViewById(R.id.next)
        next.setOnClickListener(clickListener)

        prev = view.findViewById(R.id.prev)
        prev.setOnClickListener(clickListener)
        startPlayMV = view.findViewById(R.id.mv_play)
        return view
    }
}