package com.devsupport

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import android.widget.Toast
import com.devsupport.messaging.LivePersonSDK
import com.liveperson.api.LivePersonIntents
import com.liveperson.api.sdk.LPConversationData
import com.liveperson.api.sdk.PermissionType
import com.liveperson.infra.callbacks.InitLivePersonCallBack
import com.liveperson.messaging.TaskType
import com.liveperson.messaging.model.AgentData
import java.lang.Exception

class MainApplication : Application() {

  private val TAG : String? = MainApplication::class.simpleName
  private var mLivePersonReceiver : BroadcastReceiver? = null

  /**
   * App LifeCycle
   */
  override fun onCreate() {
    // Super Init
    super.onCreate()
    // Init Event Receiver
    this.registerToLivePersonEvents()
    // Init Liveperson SDK
    LivePersonSDK.initSDK(this, object : InitLivePersonCallBack {
      override fun onInitFailed(exception: Exception?) {
        // Log Error
        Log.e("LP_INIT ::", exception.toString())
      }
      override fun onInitSucceed() {
        // Log Success
        Log.i("LP_INIT ::", ": Initialization : Success")
        //
        LivePersonSDK.registerForPushNotifications(applicationContext)
      }
    })
  }

  /**
   * Will register to LivePerson Events
   */
  private fun registerToLivePersonEvents(){
    // Create Receivers for Events
    this.createLivePersonReceiver()
    // Register Broadcast to All LivePerson Events
    LocalBroadcastManager.getInstance(applicationContext).registerReceiver(mLivePersonReceiver, LivePersonIntents.getIntentFilterForAllEvents())
  }

  /**
   * Will create LivePerson Receiver
   */
  private fun createLivePersonReceiver(){
    // Check if receiver exists
    if (this.mLivePersonReceiver != null){
      // Don't Create Receiver as it already exists
      return
    }
    // Init Receiver
    this.mLivePersonReceiver = object : BroadcastReceiver() {

      /**
       * Receiver
       */
      override fun onReceive(context: Context?, intent: Intent?) {
        // Unwrap Intent
        intent?.let {
          // Log Intent
          Log.d(TAG, "Get LPIntent with Action :: ${it.action}")
          // Filter Intents
          when(it.action){
            LivePersonIntents.ILivePersonIntentAction.LP_ON_AGENT_AVATAR_TAPPED_INTENT_ACTION -> onAgentAvatarTapped(LivePersonIntents.getAgentData(it))
            LivePersonIntents.ILivePersonIntentAction.LP_ON_AGENT_DETAILS_CHANGED_INTENT_ACTION -> onAgentDetailsChanged(LivePersonIntents.getAgentData(it))
            LivePersonIntents.ILivePersonIntentAction.LP_ON_AGENT_TYPING_INTENT_ACTION -> onAgentIsTyping(LivePersonIntents.getAgentTypingValue(it))
            LivePersonIntents.ILivePersonIntentAction.LP_ON_CONNECTION_CHANGED_INTENT_ACTION -> onConnectionChanged(LivePersonIntents.getConnectedValue(it))
            LivePersonIntents.ILivePersonIntentAction.LP_ON_CONVERSATION_MARKED_AS_NORMAL_INTENT_ACTION -> onConversationMarkedAsNormal()
            LivePersonIntents.ILivePersonIntentAction.LP_ON_CONVERSATION_MARKED_AS_URGENT_INTENT_ACTION -> onConversationMarkedAsUrgent()
            LivePersonIntents.ILivePersonIntentAction.LP_ON_CONVERSATION_RESOLVED_INTENT_ACTION -> onConversationResolve(LivePersonIntents.getLPConversationData(it))
            LivePersonIntents.ILivePersonIntentAction.LP_ON_CONVERSATION_STARTED_INTENT_ACTION -> onConversationStarted(LivePersonIntents.getLPConversationData(it))
            LivePersonIntents.ILivePersonIntentAction.LP_ON_TOKEN_EXPIRED_INTENT_ACTION -> onTokenExpired()
            LivePersonIntents.ILivePersonIntentAction.LP_ON_ERROR_INTENT_ACTION -> onError(it)
            LivePersonIntents.ILivePersonIntentAction.LP_ON_CSAT_LAUNCHED_INTENT_ACTION -> onCSATLaunched()
            LivePersonIntents.ILivePersonIntentAction.LP_ON_CSAT_DISMISSED_INTENT_ACTION -> onCSATDismissed()
            LivePersonIntents.ILivePersonIntentAction.LP_ON_CSAT_SKIPPED_INTENT_ACTION -> onCSATSkipped()
            LivePersonIntents.ILivePersonIntentAction.LP_ON_CSAT_SUBMITTED_INTENT_ACTION -> onCSATSubmitted()
            LivePersonIntents.ILivePersonIntentAction.LP_ON_OFFLINE_HOURS_CHANGES_INTENT_ACTION -> onOfflineHoursAreTurnedOn(LivePersonIntents.getOfflineHoursOn(it))
            LivePersonIntents.ILivePersonIntentAction.LP_ON_USER_DENIED_PERMISSION -> onUserDeniedPermission(it)
            LivePersonIntents.ILivePersonIntentAction.LP_ON_USER_ACTION_ON_PREVENTED_PERMISSION -> onUserPreventPermission(it)
            LivePersonIntents.ILivePersonIntentAction.LP_ON_STRUCTURED_CONTENT_LINK_CLICKED -> onStructureContentClicked(LivePersonIntents.getLinkUri(it))
            LivePersonIntents.ILivePersonIntentAction.LP_ON_UNAUTHENTICATED_USER_EXPIRED_INTENT_ACTION -> onUnauthenticatedUserExpired()

          }
        }
      }
    }
  }

  private fun onAgentAvatarTapped(agentData: AgentData) {
    // Show Toast
    showToast("AgentAvatar Tapped : ${agentData.mNickName}")
  }

  private fun onAgentDetailsChanged(agentData: AgentData) {
    // Show Toast
    showToast("AgentDetails Changed : ${agentData.mNickName}")
  }

  private fun onAgentIsTyping(isTyping : Boolean) {
    // Show Toast
    showToast("Agent isTyping : $isTyping")
  }

  private fun onConnectionChanged(isConnected : Boolean) {
    // Show Toast
    showToast("isConnected : $isConnected")
  }

  private fun onConversationMarkedAsNormal() {
    // Show Toast
    showToast("Conversation Marked as Normal")
  }

  private fun onConversationMarkedAsUrgent() {
    // Show Toast
    showToast("Conversation Marked as Urgent")
  }

  private fun onConversationResolve(conversationData : LPConversationData){
    // Show Toast
    showToast("Conversation Resolved : ${conversationData.id} - Reason : ${conversationData.closeReason}")
  }

  private fun onConversationStarted(conversationData : LPConversationData){
    // Show Toast
    showToast("Conversation Started : ${conversationData.id} - Reason : ${conversationData.closeReason}")
  }

  private fun onTokenExpired(){
    // Show Toast
    showToast("Token Expired")
  }

  private fun onError(intent: Intent){
    // Get Error and Message from Intent
    val type : TaskType = LivePersonIntents.getOnErrorTaskType(intent)
    val message: String = LivePersonIntents.getOnErrorMessage(intent)
    // Show Toast
    showToast("Error: ${type.name} + $message")
  }

  private fun onCSATLaunched(){
    // Show Toast
    showToast("CSAT Launched")
  }

  private fun onCSATDismissed(){
    // Show Toast
    showToast("CSAT Dismissed")
  }

  private fun onCSATSkipped(){
    // Show Toast
    showToast("CSAT Skipped")
  }

  private fun onCSATSubmitted(){
    // Show Toast
    showToast("CSAT Submitted")
  }

  private fun onOfflineHoursAreTurnedOn(offlineHoursOn : Boolean){
    // Show Toast
    showToast("Offline Hours On : $offlineHoursOn")
  }

  private fun onUserDeniedPermission(intent: Intent){
    // Unwrap Values
    val type : PermissionType = LivePersonIntents.getPermissionType(intent)
    val doNotShowAgainMarked : Boolean = LivePersonIntents.getPermissionDoNotShowAgainMarked(intent)
    // Show Toast
    showToast("User Denied Permission: ${type.name} + $doNotShowAgainMarked")
  }

  private fun onUserPreventPermission(intent: Intent){
    // Unwrap Value
    val type : PermissionType = LivePersonIntents.getPermissionType(intent)
    // Show Toast
    showToast("User Prevent Permissions : ${type.name}")
  }

  private fun onStructureContentClicked(url : String){
    // Show Toast
    showToast("Structure Content Link : $url")
  }

  private fun onUnauthenticatedUserExpired(){
    // Show Toast
    showToast("Unauthenticated User Expired")
  }

  /**
   * Will Show Toast Notification
   * @param message - String(Toast Message)
   */
  private fun showToast(message : String){
    // Create Toast
    Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
  }

}