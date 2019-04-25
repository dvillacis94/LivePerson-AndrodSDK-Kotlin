package com.devsupport

import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.devsupport.messaging.LivePersonSDK
import com.liveperson.infra.LPAuthenticationParams
import com.liveperson.infra.messaging_ui.fragment.ConversationFragment

class MessagingFragment : AppCompatActivity() {

  private var mConversationFragment : ConversationFragment? = null
  private var TAG = ConversationFragment::class.java.simpleName

  /**
   * LifeCycle - Fragment Created
   * @param savedInstanceState
   */
  override fun onCreate(savedInstanceState: Bundle?) {
    // Super Init
    super.onCreate(savedInstanceState)
    // Bind Layout
    setContentView(R.layout.fragment_conversation)
    // Handle Push Registration
    LivePersonSDK.registerForPushNotifications(applicationContext)
    // Init Fragment
    initConversationFragment()
  }

  /**
   * App LifeCycle - Fragment Resume
   */
  override fun onResume() {
    // Super Init
    super.onResume()
    // Log
    Log.i(TAG, "onResume")
    // Check Fragment is not null
    if (mConversationFragment != null) {
      // Attach Fragment
      attachFragment()
    }
  }

  /**
   * App LifeCycle - OnPause
   */
  override fun onPause() {
    // Super Init
    super.onPause()
    // Log
    Log.i(TAG, "onPause")
    // Check Fragment is not null
    if (mConversationFragment != null) {
      // De-Attach Fragment
      deattachFragment()
    }
  }

  /**
   * App LifeCycle - Back Button was Pressed
   */
  override fun onBackPressed() {
    // Unwrap Fragment
    mConversationFragment?.let {
      // Check if Back was pressed
      if (!it.onBackPressed()){
        // Super Init
        super.onBackPressed()
      }
    }
  }

  /**
   * Initialize Conversation Fragment
   */
  private fun initConversationFragment(){
    // Log
    Log.i(TAG, "Initializing Fragment")
    // Try to Fetch Conversation Fragment
    mConversationFragment = supportFragmentManager.findFragmentByTag(LivePersonSDK.FRAGMENT_ID) as? ConversationFragment
    // Check if Fragment has been initialize
    if(mConversationFragment == null){
      val authenticationParams : LPAuthenticationParams = LPAuthenticationParams(LPAuthenticationParams.LPAuthenticationType.AUTH).setHostAppJWT("eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImFsZ29yaXRobSI6IlJTMjU2In0.eyJzdWIiOiJtYmFsYWJhc2NhcmluMkBrZHMuY29tIiwiZXhwIjoxNTg1MTgzMDc4LCJpYXQiOjE1NTM2MjYxMjYsImlzcyI6Imh0dHBzOi8vd3d3LmxpdmVwZXJzb24uY29tIiwicHJlZmVycmVkX3VzZXJuYW1lIjoiTXV0aHVrdW1hcmFzYW15IiwibHBfc2RlcyI6W3sidHlwZSI6ImN0bXJpbmZvIiwiaW5mbyI6eyJjc3RhdHVzIjoiQ29ubmVjdCBQb3J0YWwiLCJjdXN0b21lcklkIjoibWJhbGFiYXNjYXJpbjJAa2RzLmNvbSIsImN0eXBlIjoiQUFQQzAxIiwiYWNjb3VudE5hbWUiOiJBYWIgQ29tcGFueSIsImNvbXBhbnlCcmFuY2giOiJmaXJzdF9nbG9iYWwiLCJzb2NpYWxJZCI6IlRpbWJ1a3R1IC0gQUJDMDAxIC0gQUFQQzAyLE5vd2hlcmUgLSBBQkMwOTkgLSBBQVBDMDEifX0seyJ0eXBlIjoicGVyc29uYWwiLCJwZXJzb25hbCI6eyJmaXJzdG5hbWUiOiJGaXJzdE5hbWUiLCJsYW5ndWFnZSI6ImVuLVVTIiwiY29udGFjdHMiOlt7InBob25lIjoiKzEgNDA4IDU1NSAxMjM0IiwiZW1haWwiOiJmaXJzdG5hbWUubGFzdG5hbWVAZXhhbXBsZS5jb20ifV0sImNvbXBhbnkiOiJBYWIgQ29tcGFueSIsImdlbmRlciI6IiIsImxhc3RuYW1lIjoiTGFzdE5hbWUifX1dfQ.h6Fzz56S0yJecLjPAbfaJOBYa3ffroTYppkqIbSzXsR2rdNkRdHzPkmrcv1LsnBDKlj7joLWHLYq8OvYEVVgvjpZpP11QWwfDvZuL567xIYbd3VFj4r6FZWVvPsvo1swU-111JYkKaaAvboYZvOcT_zxmneiQyJL9QzWZ4jLvbg")
      // Get Conversation Fragment
      mConversationFragment = LivePersonSDK.getConversationFragment(authenticationParams)
      // Check State
      if(this.isStateValid()){
        // Initialize Notifications
        LivePersonSDK.initFragmentNotifications(applicationContext, MessagingFragment::class.java)
        // Get Transaction Manager
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        // Add Fragment to Manager and Commit
        fragmentTransaction.add(R.id.custom_fragment_container, mConversationFragment, LivePersonSDK.FRAGMENT_ID).commitAllowingStateLoss()
      }
    } else {
      // Attach Existing Fragment
      attachFragment()
    }
  }

  /**
   * Will Attach Conversation Fragment to FragmentActivity
   */
  private fun attachFragment(){
    // Unwrap Fragment
    mConversationFragment?.let {
      // Check Fragment State
      if (it.isDetached){
        // Log State
        Log.i(TAG, "Attaching Conversation Fragment")
        // Check State
        if (isStateValid()){
          // Get Transaction Manager
          val fragmentTransaction = supportFragmentManager.beginTransaction()
          // Add Fragment to Manager and Commit
          fragmentTransaction.attach(it).commitAllowingStateLoss()
        }
      }
    }
  }

  /**
   * Will De-Attach Conversation Fragment from FragmentActivity
   */
  private fun deattachFragment(){
    // Unwrap Fragment
    mConversationFragment?.let {
      // Check Fragment State
      if (it.isAdded){
        // Log State
        Log.i(TAG, "De-Attaching Conversation Fragment")
        // Get Transaction Manager
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        // Remove Fragment
        fragmentTransaction.detach(it).commitAllowingStateLoss()
      }
    }
  }

  /**
   * Will Show Toast Notification
   * @param message - String(Toast Message)
   */
  private fun showToast(message : String) {
    // Create Toast
    Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
  }

  /**
   * Will check is Fragment State isValid
   */
  private fun isStateValid() : Boolean{
    // Check SDK Version
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
      (!fragmentManager.isDestroyed && !isFinishing)
    } else{
      (!isFinishing)
    }
  }
}