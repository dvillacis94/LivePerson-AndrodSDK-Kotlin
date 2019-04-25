package com.devsupport.firebase

import android.app.IntentService
import android.content.Intent
import android.util.Log
import com.devsupport.messaging.LivePersonSDK
import com.google.firebase.iid.FirebaseInstanceId
import com.liveperson.infra.ICallback
import com.liveperson.infra.LPAuthenticationParams
import com.liveperson.infra.callbacks.InitLivePersonCallBack
import java.lang.Exception

private val TAG: String? = FirebaseRegistrationIntentService::class.simpleName

class FirebaseRegistrationIntentService : IntentService(TAG) {

  override fun onHandleIntent(intent: Intent) {
    // Log
    Log.i(TAG, ": Registering token")
    FirebaseInstanceId.getInstance().token?.let { token ->
      // Log Token
      Log.d(TAG, ": Token : $token")
      val authenticationParams : LPAuthenticationParams = LPAuthenticationParams(LPAuthenticationParams.LPAuthenticationType.AUTH).setHostAppJWT("JWT")
      // Register for Push
      LivePersonSDK.registerToLivepersonPusher(LivePersonSDK.Account, LivePersonSDK.AppId, token, authenticationParams)
    }
  }
}