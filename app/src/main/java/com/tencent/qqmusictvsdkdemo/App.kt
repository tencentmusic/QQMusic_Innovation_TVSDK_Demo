package com.tencent.qqmusictvsdkdemo

import android.text.TextUtils
import android.util.Log
import androidx.multidex.MultiDexApplication
import com.tencent.qqmusictvsdk.ITokenProvider
import com.tencent.qqmusictvsdk.QQMusicSDK
import com.tencent.qqmusictvsdk.Token
import java.nio.charset.Charset
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import kotlin.experimental.and


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
            val sign = md5(str)
            Log.i(TAG, sign)
            return Token(sign, timestamp)
        }
        fun md5(data: String): String {
            MessageDigest.getInstance("MD5").apply {
                update(data.toByteArray(Charset.forName("UTF-8")))
                return encode(digest())
            }
        }
        private val HEX = "0123456789abcdef"
        fun encode(data: ByteArray): String {
            val sb = StringBuffer()
            data.forEach {
                val d = it.toInt()
                val h = d.shr(4).and(0xf)
                val l = d.and(0xf)
                sb.append(HEX[h])
                sb.append(HEX[l])
            }
            return sb.toString()
        }
    }
    override fun onCreate() {
        super.onCreate()
        QQMusicSDK.init(applicationContext, OPI_APP_ID, OPI_APP_KEY, localTokenProvider, true)
    }
    companion object {
        const val TAG = "App"

        /**
         * 以下是测试账号，正式发布请用QQ音乐申请的账号信息。
         */
        /**
         * 仅仅为了演示， 安全考虑，private key应该保存在服务端。
         */
        private const val APP_SECRET = "SIOzhRyofYAPeluV_qRdvrcxQJYjcXwnK"
        private const val Prefix = "OpitrtqeGzopIlwxs"
        const val OPI_APP_ID = "2000000224"
        const val OPI_APP_KEY = "SIOzhRyofYAPeluV"

    }
}