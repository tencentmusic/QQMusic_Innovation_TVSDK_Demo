package com.tencent.qqmusictvsdkdemo.ui.main

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.tencent.qqmusictvsdk.QQMusicSDK
import com.tencent.qqmusictvsdk.SDKException
import com.tencent.qqmusictvsdk.auth.AuthType
import com.tencent.qqmusictvsdk.auth.IAuthListener
import com.tencent.qqmusictvsdkdemo.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/*
 * Copyright (C) 2020 Tencent Music Entertainment Group. All Rights Reserved. 
 *  
 *
 * @Author chaoccwang
 * @Date 09/07/2020
 */
class LoginFragment : Fragment(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + Dispatchers.Main

    lateinit var qrCodeImageView: ImageView
    lateinit var avatarImageView: ImageView
    lateinit var nickname: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_login, container, false)
        root.apply {
            qrCodeImageView = findViewById(R.id.qrcode)
            avatarImageView = findViewById(R.id.avatar)
            nickname = findViewById(R.id.nickname)
        }
        return root
    }

    lateinit var type: AuthType
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val arg = arguments?.getString(ARG_AUTH_TYPE) ?: AuthType.WX.value
        type = AuthType.valueOf(arg)
    }
    private val listener = object : IAuthListener {
        override fun onError(error: SDKException) {
            Log.e(TAG, "", error)
        }

        override fun onGetQrCode(bitmap: ByteArray) {
            val image = BitmapFactory.decodeByteArray(bitmap, 0, bitmap.size)
            qrCodeImageView.setImageBitmap(image)
        }

        override fun onScanned() {
            TODO("not implemented")
        }

        override fun onSuccess(openID: String, accessToken: String, expireTime: Long) {
            val userInfo = QQMusicSDK.getAccountManager().getAccount()
            launch {
                Glide.with(activity!!).load(userInfo.avatarUrl).into(avatarImageView)
                nickname.text = userInfo.nickname
            }
        }
    }

    override fun onStart() {
        super.onStart()
        QQMusicSDK.getDiffDevAuth(type).also {
            it.start(listener)
        }
    }

    override fun onStop() {
        super.onStop()
        QQMusicSDK.getDiffDevAuth(type).also {
            it.stop()
        }
    }
    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_AUTH_TYPE = "auth_type"
        const val TAG = "Login"
        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(type: AuthType): LoginFragment {
            return LoginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_AUTH_TYPE, type.name)
                }
            }
        }
    }
}