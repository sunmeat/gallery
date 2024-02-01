package com.sunmeat.facebook

import android.os.Bundle
import android.util.Log
import com.facebook.FacebookBroadcastReceiver

// пример показывает, как можно поставить обработчик длительных операций вроде загрузки фото
class HelloFacebookBroadcastReceiver: FacebookBroadcastReceiver() {
    override fun onSuccessfulAppCall(appCallId: String, action: String, extras: Bundle) {
        Log.d("HelloFacebook", "Фото по запросу # $appCallId загружено.")
    }

    override fun onFailedAppCall(appCallId: String, action: String, extras: Bundle) {
        Log.d("HelloFacebook", "Фото по запросу # $appCallId НЕ загружено.")
    }
}