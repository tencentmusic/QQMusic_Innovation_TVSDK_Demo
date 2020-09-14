package com.tencent.qqmusictvsdkdemo

import android.util.Log
import androidx.multidex.MultiDexApplication
import com.tencent.qqmusictvsdk.ITokenProvider
import com.tencent.qqmusictvsdk.QQMusicSDK
import com.tencent.qqmusictvsdk.Token
import com.tencent.qqmusictvsdk.internal.util.MD5Utils

/*
 * Copyright (C) 2020 Tencent Music Entertainment Group. All Rights Reserved. 
 *  
 *
 * @Author chaoccwang
 * @Date 09/07/2020
 */
class App : MultiDexApplication() {
    private val localTokenProvider = object: ITokenProvider {
        override fun getToken(): Token {
            val timestamp = (System.currentTimeMillis()/1000).toString()
            val str = "${Prefix}_${OPI_APP_ID}_${APP_SECRET}_${timestamp}"
            Log.i(TAG, str)
            val sign = MD5Utils.md5(str)
            Log.i(TAG, sign)
            return Token(sign, timestamp)
        }
    }
    override fun onCreate() {
        super.onCreate()
        QQMusicSDK.init(applicationContext, BuildConfig.DEBUG, OPI_APP_ID, OPI_APP_KEY, localTokenProvider)
    }
    companion object {
        const val TAG = "App"

        /**
         * 以下是测试账号，正式发布请用QQ音乐申请的账号信息。
         */
        /**
         * 仅仅为了演示， 安全考虑，private key应该保存在服务端。
         */
        private const val APP_SECRET = "PNOhlByHHKiIGOSl_chQqMUObILrFGcvu"
        private const val Prefix = "OpitrtqeGzopIlwxs"
        const val OPI_APP_ID = "12345888"
        const val OPI_APP_KEY = "PNOhlByHHKiIGOSl"

    }
}