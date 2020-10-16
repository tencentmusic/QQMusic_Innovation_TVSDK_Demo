package com.tencent.qqmusictvsdkdemo.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.tencent.qqmusictvsdk.QQMusicSDK
import com.tencent.qqmusictvsdk.player.IPlayerManager
import com.tencent.qqmusictvsdk.player.SongInfo
import com.tencent.qqmusictvsdkdemo.R

/*
 * Copyright (C) 2020 Tencent Music Entertainment Group. All Rights Reserved. 
 *  
 *
 * @Author chaoccwang
 * @Date 09/08/2020
 */
class MusicPlayerFragment : Fragment() {

    private fun getSongs(): ArrayList<SongInfo> {

        return ArrayList(listOf("002w57E00BGzXn", "002VIFU90S4ICL", "0039MnYb0qxYhV").map {
            SongInfo().also { song ->
                song.song_mid = it
            }
        })
    }

    lateinit var playerManager: IPlayerManager
    lateinit var play: Button
    lateinit var next: Button
    lateinit var prev: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playerManager = QQMusicSDK.getPlayerManager()
    }

    private val clickListener = View.OnClickListener {
        when(it?.id) {
            R.id.playSongs -> {
                playerManager.playSongs(getSongs(), 0)
            }
            R.id.next -> {
                playerManager.next()
            }
            R.id.prev -> {
                playerManager.prev()
            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_music_player, container, false)
        play = view.findViewById(R.id.playSongs)
        play.setOnClickListener(clickListener)

        next = view.findViewById(R.id.next)
        next.setOnClickListener(clickListener)

        prev = view.findViewById(R.id.prev)
        prev.setOnClickListener(clickListener)
        return view
    }
}