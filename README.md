 Crasher is a library built to allow users to easily send stack traces and useful device info to developers without the use of third party services. For demonstration purposes, sample APKs can be downloaded [here](https://github.com/TheAndroidMaster/Crasher/releases).
 
## Screenshots

|Main Screen|Somewhat Unnecessary Proof of StackOverflow|
|-----|-----|
|![img](https://raw.githubusercontent.com/TheAndroidMaster/TheAndroidMaster.github.io/master/images/screenshots/Crasher-Main.png)|![img](https://raw.githubusercontent.com/TheAndroidMaster/TheAndroidMaster.github.io/master/images/screenshots/Crasher-StackOverflow.png)|

## Usage

### Setup

The Gradle dependency is available through jCenter, which is used by default in Android Studio. To add the dependency to your project, copy this line into the dependencies section of your build.gradle file.

```gradle
compile 'me.jfenn:crasher:0.0.2'
```

### Setting the Exception Handler

Creating a `new Crasher(context)` will automatically set Crasher as the default exception handler in the current thread. This will need to be done in every thread that your application uses, or every thread that you want Crasher to intercept exceptions from.

**Note: doing this will prevent the Play Store's crash reporting from working, as Crasher will intercept all exceptions and the "this application has stopped working!" dialog will not be shown as a result.**
You can use the method `CrashUtils.isInstalledFromPlayStore(context)` (or simply `BuildConfig.DEBUG` to check if it's a debug build) to prevent this. 

It is recommended to store one instance of `Crasher` in your app's [`Application`](https://developer.android.com/reference/android/app/Application.html) class, and simply pass that to `Thread.setDefaultUncaughtExceptionHandler(crasher)` in each new thread that you create. If you do not engage in the fine art of multithreading, then you do not need to worry about this, and only need to create one `Crasher`, either in the `onCreate` method of your `Application` class, or the `onCreate` of the first `Activity` that is opened by your app.

### Options

#### App Name
Crasher uses `R.string.app_name` as the name of the application to display in the toolbar. The default value of this is "Crasher", but can easily be changed by setting the `app_name` string resource in your strings.xml file.

#### Email
To specify an email for the user to optionally send crash reports to, simply call `crasher.setEmail(email)` and pass the desired email as a string, or pass `null` to remove the button (this will be obsolete in most cases, since it is not displayed by default unless an email is passed).

#### Primary Color
By default, Crasher will use `#F5F5F5` as its primary color, which can be overriden by either creating the `colorPrimary` resource in your application or calling `crasher.setColor(color)` on the `Crasher` object.

#### Stack Overflow
Crasher can also optionally open the search results of the exception message on stackoverflow when the app crashes. This may be somewhat annoying during "regular use", as it would take much more time to solve common errors that most developers know on sight (missing a declaration in the manifest, for example), and is really just here for show. You can enable it for debug builds of the application only by calling `crasher.setStackoverflowEnabled(true)`, or for all builds by calling `crasher.setForceStackOverflow(true)` (I'm not sure what I was thinking when I decided on the capitalization for those two methods, but I can't be bothered to fix it right now so they'll probably stay for a while).

#### Enabling/Disabling Crasher After Setting The Exception Handler This Is A Really Long Title Help
Call `crasher.setCrashActivityEnabled(isCrashThingyEnabled)`. Pretty self-explanatory.

#### Other Stuff
You can override any string resource the app uses by specifying it in your own strings.xml file (if you want to make your own translations, for example). The strings used by Crasher are as follows:

|Resource Name|Value|Used For|
|-----|-----|-----|
|app_name|Crasher|The app's name in the action bar title/other random strings|
|title_crasher_crashed|%1$s Crashed|The action bar title, with String.format|
|title_crasher_exception|%1$s in %2$s|Used for the email subject, like "NullPointerException in My App"|
|msg_crasher_crashed|Unfortunately, %1$s has stopped working. You can report this as a bug using the buttons below, or you can try to fix it yourself. When reporting the bug, please include as much detail as possible to help fix the issue as quickly as possible. Please note that your device info, such as the manufacturer and model number, may be included in the bug report.|The message displayed to the user when a crash occurs|
|title_crasher_copy|Copy|Text of the button to copy the stack trace to the clipboard|
|title_crasher_share|Share|Text of the button to bring up the share menu with the stack trace|
|title_crasher_email|Email|Text of the button to share the stack trace and device info through an email|
|title_crasher_send_email|Send Email|The title of the share menu that will appear if the user has more than one email app to choose from|
|title_crasher_stack_trace|Stack Trace|The title of the button to show/hide the stack trace|
