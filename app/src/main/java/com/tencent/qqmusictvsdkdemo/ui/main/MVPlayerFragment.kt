package com.tencent.qqmusictvsdkdemo.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.tencent.qqmusictvsdk.QQMusicSDK
import com.tencent.qqmusictvsdk.player.IPlayerManager
import com.tencent.qqmusictvsdk.player.MVInfo
import com.tencent.qqmusictvsdkdemo.R

/*
 * Copyright (C) 2020 Tencent Music Entertainment Group. All Rights Reserved. 
 *  
 *
 * @Author chaoccwang
 * @Date 09/08/2020
 */
class MVPlayerFragment : Fragment() {

    private fun getMV(): ArrayList<MVInfo> {
        return ArrayList(listOf("g00349jxqpw", "r0034oiuica", "i0034xbq2g9").map {
            MVInfo().also { mv ->
                mv.mv_vid = it
            }
        })
    }

    lateinit var playerManager: IPlayerManager
    lateinit var play: Button
    lateinit var next: Button
    lateinit var prev: Button
    lateinit var startPlayMV: FrameLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playerManager = QQMusicSDK.getPlayerManager()
    }

    private val clickListener = View.OnClickListener {
        when(it?.id) {
            R.id.play -> {
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
        val view = inflater.inflate(R.layout.fragment_mv_player, container, false)
        play = view.findViewById(R.id.play)
        play.setOnClickListener(clickListener)

        next = view.findViewById(R.id.next)
        next.setOnClickListener(clickListener)

        prev = view.findViewById(R.id.prev)
        prev.setOnClickListener(clickListener)
        startPlayMV = view.findViewById(R.id.mv_play)
        return view
    }
}