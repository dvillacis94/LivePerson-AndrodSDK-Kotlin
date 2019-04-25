package com.devsupport

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.Toast
import com.devsupport.messaging.LivePersonSDK
import com.devsupport.push.NotificationUI
import com.liveperson.infra.LPAuthenticationParams
import com.liveperson.infra.callbacks.LogoutLivePersonCallBack
import com.liveperson.messaging.sdk.api.callbacks.LogoutLivePersonCallback
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

class MainActivity : AppCompatActivity(), OnClickListener {

  /**
   * App LifeCycle - OnCreate
   * @param savedInstanceState : Bundle
   */
  override fun onCreate(savedInstanceState: Bundle?) {
    // Super Init
    super.onCreate(savedInstanceState)
    // Bind Layout
    setContentView(R.layout.activity_main)
    // Init UI
    this.initUI()
    // Handle if App is Launch from Push
    handlePush(intent)
  }

  /**
   * Will bind UI and set required Listeners
   */
  private fun initUI() {
    // Bind Messaging Button
    val messagingButton = findViewById<Button>(R.id.messagingButton)
    val fragmentButton = findViewById<Button>(R.id.messagingFragmentButton)
    val logoutButton = findViewById<Button>(R.id.logoutButton)
    // Set OnClick Listener
    messagingButton.setOnClickListener(this)
    fragmentButton.setOnClickListener(this)
    logoutButton.setOnClickListener(this)
  }

  /**
   * OnClick Listener
   */
  override fun onClick(view: View?) {
    // Unwrap View
    view?.let {
      // Check with Button was click
      when (it.id) {
        messagingButton.id -> this.openMessagingScreen()
        messagingFragmentButton.id -> this.openFragmentScreen()
        logoutButton.id -> this.logout()
      }
    }
  }

  /**
   * Show Messaging Screen
   */
  private fun openMessagingScreen(){
    // Set User Profile
    LivePersonSDK.setUserProfile("David", "Villacis", "(123) 456 6787")
    // Create Auth Params
    val authenticationParams = LPAuthenticationParams(LPAuthenticationParams.LPAuthenticationType.AUTH)
    // Set Auth Key
    authenticationParams.authKey = "dedf7bbbdb521295e1ce4b7878aa4e8b022693f7f5a274ccba19ba2e7109872ec9ac2ea1cc5ec56c8236989ce7aed96922e6357e66a5d9a7"
    // Show Conversation Screen
    LivePersonSDK.showConversation(this, authenticationParams)
  }

  /**
   *  Show Messaging Fragment
   */
  private fun openFragmentScreen(){
    // Create Intent to Show Messaging Fragment
    val intent = Intent(this, MessagingFragment::class.java)
    // Display Activity
    startActivity(intent)
  }

  /**
   *  Perform Logout from LPMessagingSDK
   */
  private fun logout(){
    //
    LivePersonSDK.logout(applicationContext, object: LogoutLivePersonCallback, LogoutLivePersonCallBack {
      override fun onLogoutFailed(p0: Exception?) {
        Toast.makeText(applicationContext, "Logout Failed", Toast.LENGTH_SHORT).show()
      }

      override fun onLogoutFailed() {
        Toast.makeText(applicationContext, "Logout Failed", Toast.LENGTH_SHORT).show()
      }

      override fun onLogoutSucceed() {
        Toast.makeText(applicationContext, "Logout Success", Toast.LENGTH_SHORT).show()
      }
    })
  }

  /**
   * Check if App was launch for Push
   * @param intent - App Intent
   */
  private fun handlePush(intent : Intent){
    // Get Extra From Intent - This contains if App is being launch from Push Notification
    val isFromPush = intent.getBooleanExtra(NotificationUI.NOTIFICATION_EXTRA, false)
    // Check if App was launched from Push
    if (isFromPush){
      // Show Conversation Screen
      openMessagingScreen()
    }
  }
}
