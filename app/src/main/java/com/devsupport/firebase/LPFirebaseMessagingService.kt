package com.devsupport.firebase

import android.util.Log
import com.devsupport.messaging.LivePersonSDK
import com.devsupport.push.NotificationUI
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class LPFirebaseMessagingService : FirebaseMessagingService(){

  private val TAG: String? = LPFirebaseMessagingService::class.simpleName

  /**
   *
   * @param remoteMessage - Push Message
   */
  override fun onMessageReceived(remoteMessage: RemoteMessage?) {
    // [START_EXCLUDE]
    // There are two types of messages data messages and notification messages. Data messages are handled
    // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
    // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
    // is in the foreground. When the app is in the background an automatically generated notification is displayed.
    // When the user taps on the notification they are returned to the app. Messages containing both notification
    // and data payloads are treated as notification messages. The Firebase console always sends notification
    // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
    // [END_EXCLUDE]
    Log.i(TAG, "Push Received")
    // Unwrap Remote Message
    remoteMessage?.let {
      // Check if message contains any data
      if (it.data.isNotEmpty()){
        // Log Data
        Log.d(TAG, "Message Data: ${it.data}")
        // Ingest Push Data
        val message = LivePersonSDK.handlePush(this, remoteMessage.data, false)
        // Check if message is not null
        if (message != null){
          // Log Data
          Log.d(TAG, "Rendering Push Notification")
          // Render Push
          NotificationUI.showPushNotification(this, message)
        }
      }
    }
  }
}