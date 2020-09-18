package com.tencent.qqmusictvsdkdemo.ui.main

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.tencent.qqmusicsdk.protocol.PlayStateHelper
import com.tencent.qqmusictvsdk.QQMusicSDK
import com.tencent.qqmusictvsdk.lyric.LyricManager
import com.tencent.qqmusictvsdk.player.*
import com.tencent.qqmusictvsdk.player.ErrorCode.ERROR_OK
import com.tencent.qqmusictvsdk.player.Event.API_EVENT_MV_PLAY_ERROR
import com.tencent.qqmusictvsdk.player.Event.API_EVENT_PLAY_STATE_CHANGED
import com.tencent.qqmusictvsdk.player.Event.API_EVENT_SONG_PLAY_ERROR
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
    lateinit var songinfo: TextView
    lateinit var currTime: TextView
    lateinit var totleTime: TextView
    lateinit var progressBar: SeekBar
    lateinit var mSongInfo: SongInfo
    lateinit var mMVInfo: MVInfo
    lateinit var songQuality: Spinner
    lateinit var mvResolution: Spinner
    lateinit var soundEffect: Spinner

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
                API_EVENT_SONG_PLAY_ERROR -> {
                    var code = arg.getInt(Key.API_RETURN_KEY_CODE)
                    if (code == ERROR_OK) {
                        uiThread {
                            mSongInfo = playerManager.getCurrentSongInfo()
                            songinfo.text = "${mSongInfo.song_name}---${mSongInfo.singer_name}"
                            totleTime.text = playerManager.getDuration()?.let { getTime(it) }
                            timeHandler.sendEmptyMessageDelayed(0, 1000)
                        }
                    }
                }
                API_EVENT_MV_PLAY_ERROR -> {
                    var code = arg.getInt(Key.API_RETURN_KEY_CODE)
                    if (code == ERROR_OK) {
                        uiThread {
                            mMVInfo = playerManager.getCurrentMVInfo()!!
                            songinfo.text = "${mMVInfo.mv_title}---${mMVInfo.singers[0].name}"
                            totleTime.text = playerManager.getDuration()?.let { getTime(it) }
                            timeHandler.sendEmptyMessageDelayed(0, 1000)
                        }
                    }
                }
            }
        }
    }

    private val timeHandler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            uiThread {
                var currTimeInt = playerManager.getCurrentPlayTime()
                if (currTimeInt != null) {
                    currTime.text = getTime(currTimeInt)
                    var duration = playerManager.getDuration()
                    if (duration != 0L) {
                        progressBar.progress = (currTimeInt * 100 / duration!!).toInt()
                    }
                }
                sendEmptyMessageDelayed(0, 1000)
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
        songinfo = view.findViewById(R.id.songinfo)
        currTime = view.findViewById(R.id.currTime)
        totleTime = view.findViewById(R.id.totleTime)
        progressBar = view.findViewById(R.id.progress_bar_h)
        progressBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, progress: Int, isFromUser: Boolean) {
                if (isFromUser) {
                    playerManager.seek((progress * playerManager.getDuration()!! / 100 ).toInt())
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })

        songQuality = view.findViewById(R.id.songQuality)
        songQuality.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, id: Long) {
                when(pos) {
                    0 -> playerManager.setSongQuality(PlayerEnums.Quality.SQ)
                    1 -> playerManager.setSongQuality(PlayerEnums.Quality.HQ)
                    2 -> playerManager.setSongQuality(PlayerEnums.Quality.BZ)
                }
            }
        }
        mvResolution = view.findViewById(R.id.mvResolution)
        mvResolution.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, id: Long) {
                when(pos) {
                    0 -> playerManager.setMVResolution(PlayerEnums.Resolution.LAN_GUANG)
                    1 -> playerManager.setMVResolution(PlayerEnums.Resolution.CHAO_QING)
                    2 -> playerManager.setMVResolution(PlayerEnums.Resolution.GAO_QING)
                    3 -> playerManager.setMVResolution(PlayerEnums.Resolution.BIAO_QING)
                }
            }
        }
        soundEffect = view.findViewById(R.id.soundEffect)
        soundEffect.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, id: Long) {
                when(pos) {
                    0 -> playerManager.setSongEffectType(PlayerEnums.EffectType.NONE_TYPE)
                    1 -> playerManager.setSongEffectType(PlayerEnums.EffectType.SURROUND_TYPE)
                    2 -> playerManager.setSongEffectType(PlayerEnums.EffectType.BASS_TYPE)
                    3 -> playerManager.setSongEffectType(PlayerEnums.EffectType.VOCAL_TYPE)
                    4 -> playerManager.setSongEffectType(PlayerEnums.EffectType.STUDIO_PRESET)
                    5 -> playerManager.setSongEffectType(PlayerEnums.EffectType.WARM_PRESET)
                    6 -> playerManager.setSongEffectType(PlayerEnums.EffectType.RETRO_PRESET)
                    7 -> playerManager.setSongEffectType(PlayerEnums.EffectType.WIDE_PRESET)
                }
            }
        }
        return view
    }

    fun uiThread(block: () -> Unit) {
        if (Thread.currentThread() !== Looper.getMainLooper().thread) {
            //不在主线程
            Handler(Looper.getMainLooper()).post(block)
        } else {
            block()
        }
    }

    private fun getTime(time: Long): String? {
        val m = (time / (60 * 1000)).toInt()
        val s = (time % (60 * 1000) / 1000).toInt()
        val mi = m.toString()
        var se = s.toString()
        if (s < 10) {
            se = "0$s"
        }
        return "$mi:$se"
    }
}