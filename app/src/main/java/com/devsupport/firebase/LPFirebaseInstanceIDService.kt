package com.devsupport.firebase

import android.content.Intent
import android.util.Log
import com.google.firebase.iid.FirebaseInstanceIdService

class LPFirebaseInstanceIDService : FirebaseInstanceIdService(){

  private val TAG: String? = LPFirebaseInstanceIDService::class.simpleName

  /**
   * Called if InstanceID token is updated. This may occur if the security of
   * the previous token had been compromised. Note that this is called when the InstanceID token
   * is initially generated so this is where you would retrieve the token.
   */
  override fun onTokenRefresh() {
    // Log
    Log.d(TAG, "Refreshing Token")
    // Update Instance ID Token
    val intent = Intent(this, FirebaseRegistrationIntentService::class.java)
    // Start Service
    startService(intent)
  }
}