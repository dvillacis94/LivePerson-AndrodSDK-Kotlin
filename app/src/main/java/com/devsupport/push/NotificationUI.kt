package com.devsupport.push

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.support.annotation.RequiresApi
import com.devsupport.MainActivity
import com.devsupport.R
import com.liveperson.infra.model.PushMessage
import com.liveperson.messaging.sdk.api.LivePerson

class NotificationUI {

  companion object {
    // Notification ID
    private const val PUSH_NOTIFICATION_ID = 1234567
    // Notification Extra
    const val NOTIFICATION_EXTRA = "notification_extra"
    // Notification Channel Id
    private const val CHANNEL_SERVICE_NOTIFICATION_ID = "channel_service_notification"
    // Notification Push Id
    private const val CHANNEL_PUSH_NOTIFICATION_ID = "channel_push_notification"

    /**
     * Will display Push Notification
     * @param context - Application/Activity Context
     * @param pushMessage
     */
    fun showPushNotification(context: Context, pushMessage: PushMessage){
      // Create Notification Builder
      val builder = createNotificationBuilder(context, CHANNEL_PUSH_NOTIFICATION_ID, "Push Notification", true)
      // Set Builder Properties
      builder
        .setContentIntent(getPendingIntent(context))
        .setContentTitle(pushMessage.from)
        .setContentText(pushMessage.message)
        .setAutoCancel(true)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setNumber(pushMessage.currentUnreadMessgesCounter)
      // Set Sound & Lights if API < Oreo(26), if 26+ this are added to the NotificationChannel
      if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
        // Set Defaults
        builder.setDefaults(Notification.DEFAULT_SOUND)
        builder.setLights(Notification.DEFAULT_LIGHTS,1000, 1000)
      }
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
        // Add Category
        builder.setCategory(Notification.CATEGORY_MESSAGE)
        builder.setPriority(Notification.PRIORITY_HIGH)
      }
      // Display Notification
      getNotificationManager(context).notify(PUSH_NOTIFICATION_ID, builder.build())
    }

    /**
     * Will create Notification Builder for Uploading Image
     * @param context - ApplicationContext/ActivityContext
     */
    fun createUploadNotificationBuilder(context: Context) : Notification.Builder {
      // Create Service
      return createServiceNotificationBuilder(context, "Uploading image...", android.R.drawable.arrow_up_float)
    }

    /**
     * Will create Notification Builder for Downloading Image
     * @param context - ApplicationContext/ActivityContext
     */
    fun createaDownloadNotificationBuilder(context: Context) : Notification.Builder {
      // Create Service
      return createServiceNotificationBuilder(context, "Downloading image...", android.R.drawable.arrow_down_float)
    }

    /**
     * Will hide Push Notification
     * @param context - ApplicationContext
     */
    fun hideNotification(context: Context){
      // Dismiss Push Notification
      getNotificationManager(context).cancel(PUSH_NOTIFICATION_ID)
    }

    /**
     * Will create Notification Builder Service
     * @param context - ApplicationContext
     * @param contentTitle - Notification Context Title
     * @param smallIcon - Icon Type
     * @return Notification.Builder
     */
    private fun createServiceNotificationBuilder (context: Context, contentTitle : String,  smallIcon : Int) : Notification.Builder{
      // Create Notification Builder
      val notificationBuilder = this.createNotificationBuilder(context, CHANNEL_SERVICE_NOTIFICATION_ID, "ForegroundService", false)
      // Set Builder Properties
      notificationBuilder
        .setContentIntent(getPendingIntent(context))
        .setContentText(contentTitle)
        .setSmallIcon(smallIcon)
        .setProgress(0,0,true)
      // Return NotificationBuilder
      return notificationBuilder
    }

    /**
     * Will return Notification Builder Object
     * @param context - ApplicationContext
     * @param channelId - Notification Channel Id
     * @param channelName - Notification Channel Name
     * @param isHighImportance - Notification Importance
     * @return Notification.Builder
     */
    private fun createNotificationBuilder(context: Context, channelId: String, channelName: String, isHighImportance: Boolean) : Notification.Builder {
      // Create Notification Builder
      // Return Notification Builder
      return if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
        // Create Notification Builder for Non-Oreo Devices
        Notification.Builder(context)
      } else {
        // Create Notification Channel - Oreo or Higher Devices
        createNotificationChannel(context, channelId, channelName, isHighImportance)
        // Create Notification Builder - Oreo or Higher Devices
        Notification.Builder(context, channelId)
      }
    }

    /**
     * Will create Notification Channel for API O or Higher
     * @param context - ApplicationContext
     * @param channelId - Notification Channel Id
     * @param channelName - Notification Channel Name
     * @param isHighImportance - Notification Importance
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun createNotificationChannel(context: Context, channelId : String, channelName : String, isHighImportance : Boolean = false){
      // Check Importance
      val notificationChannel = when (isHighImportance) {
        true -> NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
        false -> NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
      }
      // Add Sound and Lights to Notification Channel
      notificationChannel.enableLights(true)
      notificationChannel.lightColor = Color.CYAN
      notificationChannel.enableVibration(true)
      getNotificationManager(context).createNotificationChannel(notificationChannel)
    }

    /**
     * Will get System Notification Manager
     * @param context - ApplicationContext
     * @return NotificationManager
     */
    private fun getNotificationManager(context: Context) : NotificationManager {
      // Return System Notification Manager
      return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    /**
     * Will get Pending Intent for Push Notification
     * @param context - ApplicationContext
     * @return Intent
     */
    private fun getPendingIntent(context: Context) : PendingIntent {
      // Create Intent
      val intent = Intent(context, MainActivity::class.java)
      // Attach Extra - Intent Sent From Push
      intent.putExtra(NOTIFICATION_EXTRA, true)
      // Return Intent
      return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    /**
     * Will set Unread Count on Application Icon
     * @param context - Application/Activity Context
     * @param count - Unread Messages Count
     */
    fun setBadge(context: Context, count : Int){
      // Get Launcher Name
      val launcherName = getLauncherClassName(context)
      // Unwrap Launcher Name
      launcherName?.let {
        // Create Intent to Update Count
        val intent = Intent("android.intent.action.BADGE_COUNT_UPDATE")
        // Add Intent Properties
        intent.putExtra("badge_count", count)
        intent.putExtra("badge_count_package_name", context.packageName)
        intent.putExtra("badge_count_class_name", it)
        // Broadcast Intent
        context.sendBroadcast(intent)
      }
    }

    /**
     * Will get Launcher Class Name
     * @param context - Application/Activity Context
     */
    private fun getLauncherClassName(context: Context) : String? {
      // Get Package Manager
      val packageManager = context.packageManager
      // Get Intent
      val intent = Intent(Intent.ACTION_MAIN)
      // Add Category - Looking for Launcher
      intent.addCategory(Intent.CATEGORY_LAUNCHER)
      // Query Activities from PackageManager
      val resolveInfos = packageManager.queryIntentActivities(intent, 0)
      // Iterate Query
      for (info in resolveInfos){
        // Check Context PackageName matches Activity Info Package Name
        if (info.activityInfo.applicationInfo.packageName.equals(context.packageName, true)){
          // Return Activity Name
          return  info.activityInfo.name
        }
      }
      // Not Found
      return null
    }

    /**
     * Listen to changes in unread messages counter and updating app icon badge
     */
    class BadgeBroadcastReceiver : BroadcastReceiver() {

      override fun onReceive(context: Context?, intent: Intent?) {
        // Unwrap Intent
        intent?.let { it ->
          // Get Unread Messages Counter from Intent
          val counter = it.getIntExtra(LivePerson.ACTION_LP_UPDATE_NUM_UNREAD_MESSAGES_EXTRA, 0)
          // Unwrap Context
          context?.let { ctx ->
            // Update App Icon Unread Badge
            NotificationUI.setBadge(ctx, counter)
          }
        }
      }
    }
  }
}