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
      val authenticationParams : LPAuthenticationParams = LPAuthenticationParams(LPAuthenticationParams.LPAuthenticationType.AUTH).setHostAppJWT("eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImFsZ29yaXRobSI6IlJTMjU2In0.eyJzdWIiOiJtYmFsYWJhc2NhcmluMkBrZHMuY29tIiwiZXhwIjoxNTg1MTgzMDc4LCJpYXQiOjE1NTM2MjYxMjYsImlzcyI6Imh0dHBzOi8vd3d3LmxpdmVwZXJzb24uY29tIiwicHJlZmVycmVkX3VzZXJuYW1lIjoiTXV0aHVrdW1hcmFzYW15IiwibHBfc2RlcyI6W3sidHlwZSI6ImN0bXJpbmZvIiwiaW5mbyI6eyJjc3RhdHVzIjoiQ29ubmVjdCBQb3J0YWwiLCJjdXN0b21lcklkIjoibWJhbGFiYXNjYXJpbjJAa2RzLmNvbSIsImN0eXBlIjoiQUFQQzAxIiwiYWNjb3VudE5hbWUiOiJBYWIgQ29tcGFueSIsImNvbXBhbnlCcmFuY2giOiJmaXJzdF9nbG9iYWwiLCJzb2NpYWxJZCI6IlRpbWJ1a3R1IC0gQUJDMDAxIC0gQUFQQzAyLE5vd2hlcmUgLSBBQkMwOTkgLSBBQVBDMDEifX0seyJ0eXBlIjoicGVyc29uYWwiLCJwZXJzb25hbCI6eyJmaXJzdG5hbWUiOiJGaXJzdE5hbWUiLCJsYW5ndWFnZSI6ImVuLVVTIiwiY29udGFjdHMiOlt7InBob25lIjoiKzEgNDA4IDU1NSAxMjM0IiwiZW1haWwiOiJmaXJzdG5hbWUubGFzdG5hbWVAZXhhbXBsZS5jb20ifV0sImNvbXBhbnkiOiJBYWIgQ29tcGFueSIsImdlbmRlciI6IiIsImxhc3RuYW1lIjoiTGFzdE5hbWUifX1dfQ.h6Fzz56S0yJecLjPAbfaJOBYa3ffroTYppkqIbSzXsR2rdNkRdHzPkmrcv1LsnBDKlj7joLWHLYq8OvYEVVgvjpZpP11QWwfDvZuL567xIYbd3VFj4r6FZWVvPsvo1swU-111JYkKaaAvboYZvOcT_zxmneiQyJL9QzWZ4jLvbg")
      // Register for Push
      LivePersonSDK.registerToLivepersonPusher(LivePersonSDK.Account, LivePersonSDK.AppId, token, authenticationParams)
    }
  }
}