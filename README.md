# LivePerson SDK - Kotlin
Implementation of the LivePerson Android SDK using Kotlin

> Note: for more information about the LivePerson Android SDK, click [here](https://github.com/LP-Messaging/Android-Messaging-SDK), and for the latest release notes click [here](https://developers.liveperson.com/mobile-app-messaging-sdk-for-android-latest-release-notes.html)

### Step 1: 

Clone the repository:

```sh
git clone https://github.com/dvillacis94/LivePerson-AndrodSDK-Kotlin.git
```

After the cloning is done, open project with Android Studio.

## Step 2:

Once the Project finish syncing, navigate to build.gradle(Module: App) and set the following Configurations:

```ruby
debug {
  minifyEnabled false
  proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
  buildConfigField("boolean", "LP_DEBUGGING", "true")
  buildConfigField("String", "LP_ACCOUNT", "\"ACCOUNT_ID\"")
  buildConfigField("String", "LP_AP_INSTALLATION_ID", "\"APP_INSTALLATION_ID\"")
}
```

Property   | Required | Type | Description
------------- |  -------------  | ------------- |  ------------- 
LP_ACCOUNT | YES | String | LivePerson's Account Id
LP_AP_INSTALLATION_ID | YES | String | LivePerson's AppInstallationId
LP_DEBUGGING | YES | boolean | Controls SDK logging capabilities

> Note: Debugging capabilities on the SDK are control using the following method, this is just an example on how it can be done:

```kotlin
// File: LivePersonSDK
LivePerson.setIsDebuggable(BuildConfig.LP_DEBUGGING)
```

## Step 3:

Now that the BrandId and the AccountId are set you'll need to **Build & Run** to launch the Simulator.