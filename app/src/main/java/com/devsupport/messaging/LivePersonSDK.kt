package com.devsupport.messaging

import android.app.Activity
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.devsupport.BuildConfig
import com.devsupport.MessagingFragment
import com.devsupport.firebase.FirebaseRegistrationIntentService
import com.devsupport.push.NotificationUI
import com.liveperson.infra.*
import com.liveperson.infra.callbacks.InitLivePersonCallBack
import com.liveperson.infra.callbacks.LogoutLivePersonCallBack
import com.liveperson.infra.messaging_ui.fragment.ConversationFragment
import com.liveperson.infra.model.PushMessage
import com.liveperson.messaging.sdk.api.LivePerson
import com.liveperson.messaging.sdk.api.callbacks.LogoutLivePersonCallback
import com.liveperson.messaging.sdk.api.model.ConsumerProfile
import java.lang.Exception

class LivePersonSDK {

  companion object Singleton {

    private val TAG : String? = LivePersonSDK::class.simpleName
    const val Account : String = BuildConfig.LP_ACCOUNT
    const val AppId : String = BuildConfig.APPLICATION_ID
    const val AppInstallationId : String = BuildConfig.LP_AP_INSTALLATION_ID
    private var isInitialize : Boolean = false
    const val FRAGMENT_ID = "liveperson_fragment"

    /**
     * Initializer
     */
    init {
      print(TAG + "Class invoke")
    }

    /**
     * Will Initialize Liveperson SDK
     * @param application
     */
    fun initSDK(application: Application, callBack: InitLivePersonCallBack? = null){
      // Create Monitoring Initialization Params - Needed for Unauthenticated Users
      val monitoringInitParams = MonitoringInitParams(AppInstallationId)
      // Initialize LP SDK
      LivePerson.initialize(application.applicationContext, InitLivePersonProperties(Account, AppId, monitoringInitParams, object : InitLivePersonCallBack{
        override fun onInitFailed(exception: Exception?) {
          // Update Initialization Flag
          isInitialize = false
          // Log Error
          Log.e(TAG, exception.toString())
          // Check if Callback is available
          callBack?.let {
            it.onInitFailed(exception)
          }
        }
        override fun onInitSucceed() {
          // Update Initialization Flag
          isInitialize = true
          // Log Success
          Log.i(TAG, ": Initialization : Success")
          //
          LivePerson.setIsDebuggable(BuildConfig.LP_DEBUGGING)
          // Check if Callback is available
          callBack?.let {
            it.onInitSucceed()
          }
        }
      }))
    }

    /**
     * Perform Logout from LivePersonSDK
     */
     fun logout(context: Context, callback: LogoutLivePersonCallback?){
       // Perform Logout
       LivePerson.logOut(context, this.Account, BuildConfig.APPLICATION_ID, object : LogoutLivePersonCallback {
         override fun onLogoutFailed() {
           Log.e(TAG, "Logout Error")
           // Trigger Callback
           callback?.onLogoutFailed()
         }

         override fun onLogoutSucceed() {
           Log.d(TAG, "Logout Succeed ")
           // Trigger Callback
           callback?.onLogoutSucceed()
         }
       })
     }

    /**
     * Will register for Push Notifications
     */
    fun registerForPushNotifications(context: Context){
      val intent = Intent(context, FirebaseRegistrationIntentService::class.java)
      // Queue Service
      context.startService(intent)
    }

    /**
     * Will set Consumer Profile on LivePerson SDK
     * @param firstName - Consumer First Name
     * @param lastName - Consumer Last Name
     * @param phoneNumber - Consumer Phone Number
     */
    fun setUserProfile(firstName: String, lastName : String, phoneNumber: String){
      // Create LP User
      val consumerProfile : ConsumerProfile.Builder = ConsumerProfile.Builder()
      // Set User Properties
      consumerProfile.setFirstName(firstName)
      consumerProfile.setLastName(lastName)
      consumerProfile.setPhoneNumber(phoneNumber)
      // Set Profile
      LivePerson.setUserProfile(consumerProfile.build())
    }

    /**
     * Will show Liveperson Messaging Screen
     * @param activity - Activity Hosting the SDK
     * @param authenticationParams - Authentication Data
     * @param conversationParams - Conversation View Options
     */
    fun showConversation(activity: Activity, authenticationParams : LPAuthenticationParams = LPAuthenticationParams(), conversationParams : ConversationViewParams =  ConversationViewParams(false)) {
      // Show LivePerson Conversation Screen
      LivePerson.showConversation(activity, authenticationParams, conversationParams)
    }

    /**
     * Will create Liveperson ConversationFragment
     * @param authenticationParams - Authentication Data
     * @param conversationParams - Conversation View
     */
    fun getConversationFragment(authenticationParams : LPAuthenticationParams = LPAuthenticationParams(), conversationParams : ConversationViewParams =  ConversationViewParams(false)) : ConversationFragment {
      // Build ConversationFragment
      return LivePerson.getConversationFragment(authenticationParams, conversationParams) as ConversationFragment
    }

    /**
     * Will perform Reconnect when the Authenticated Token Expires
     */
    fun reconnect(authenticationParams : LPAuthenticationParams = LPAuthenticationParams(LPAuthenticationParams.LPAuthenticationType.AUTH)){
      // Perform Reconnect for Token Expired
      LivePerson.reconnect(authenticationParams)
    }

    /**
     * Will Initialize ConversationFragment PhotoSharing Notifications
     * @param context - Application/Activity Context
     * @param activity - Class
     */
    fun initFragmentNotifications(context: Context, activity: Class<MessagingFragment>){
      // Create Pending Intent for Notification Service
      val notificationIntent = Intent(context, activity)
      // Set Intent Flag
      notificationIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
      // Create Pending Intent
      val pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0)
      // Attach Intent
      LivePerson.setImageServicePendingIntent(pendingIntent)
      // Set Service - Upload
      val upload = NotificationUI.createUploadNotificationBuilder(context)
      // Set Service - Download
      val download = NotificationUI.createaDownloadNotificationBuilder(context)
      // Attach Notifications to Liveperson SDK
      LivePerson.setImageServiceUploadNotificationBuilder(upload)
      LivePerson.setImageServiceDownloadNotificationBuilder(download)
    }

    /**
     * Will return parsed Notification if it belongs to LP
     * @param context - Application/Activity
     * @param
     * @param
     * @param
     */
    fun handlePush(context: Context, data : Map<String, String>, showNotification : Boolean = false) : PushMessage? {
      // Return Push Message
      return LivePerson.handlePushMessage(context, data, this.Account, showNotification)
    }

    /**
     * Will register for Liveperson Push Notifications
     * @param account - Liveperson Account Id
     * @param applicationId - PackageName/BundleId
     * @param token - Firebase Push Token
     * @param authenticationParams - Liveperson Authentication Data
     */
    fun registerToLivepersonPusher(account : String, applicationId: String, token : String, authenticationParams : LPAuthenticationParams = LPAuthenticationParams()){
      // Register to Pusher
      LivePerson.registerLPPusher(account, applicationId, token, authenticationParams, object : ICallback<Void, Exception> {
        override fun onSuccess(p0: Void?) {
          // Log Success Block
          Log.i(TAG, ": Register for Push : Success")
        }
        override fun onError(p0: Exception?) {
          // Log Error Block
          Log.e(TAG, ": Register for Push : Error")
        }
      })
    }
  }

}