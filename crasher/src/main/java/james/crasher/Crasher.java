package james.crasher;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import james.crasher.activities.CrashActivity;

public class Crasher implements Thread.UncaughtExceptionHandler {

    private Context context;
    private List<OnCrashListener> listeners;

    private boolean isStackOverflow;
    private boolean isForceStackOverflow;
    private boolean isCrashActivity = true;
    private boolean isBackground;

    private String email;
    private Integer color;

    public Crasher(Context context) {
        this.context = context.getApplicationContext();
        listeners = new ArrayList<>();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    public Crasher addListener(OnCrashListener listener) {
        listeners.add(listener);
        return this;
    }

    public Crasher removeListener(OnCrashListener listener) {
        listeners.remove(listener);
        return this;
    }

    public Crasher setBackgroundCrash(boolean isBackground) {
        this.isBackground = isBackground;
        return this;
    }

    public boolean isBackgroundCrash() {
        return isBackground;
    }

    public Crasher setStackoverflowEnabled(boolean isEnabled) {
        isStackOverflow = isEnabled;
        return this;
    }

    public boolean isStackoverflowEnabled() {
        return BuildConfig.DEBUG && isStackOverflow;
    }

    public Crasher setForceStackOverflow(boolean isForced) {
        isForceStackOverflow = isForced;
        return this;
    }

    public boolean isForceStackOverflow() {
        return isForceStackOverflow;
    }

    public Crasher setCrashActivityEnabled(boolean isEnabled) {
        isCrashActivity = isEnabled;
        return this;
    }

    public boolean isCrashActivityEnabled() {
        return isCrashActivity;
    }

    public Crasher setEmail(String email) {
        this.email = email;
        return this;
    }

    @Nullable
    public String getEmail() {
        return email;
    }

    public Crasher setColor(@ColorInt int color) {
        this.color = color;
        return this;
    }

    @ColorInt
    @Nullable
    public Integer getColor() {
        return color;
    }

    @Override
    public void uncaughtException(Thread t, final Throwable e) {
        Intent intent = null;
        if ((BuildConfig.DEBUG && isStackOverflow) || isForceStackOverflow) {
            e.printStackTrace();
            Log.d("Crasher", "Exception thrown: " + e.getClass().getName() + ". Opening StackOverflow query for \"" + e.getMessage() + "\".");

            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://stackoverflow.com/search?q=[java][android]" + e.getMessage()));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } else if (isCrashActivity) {
            StringWriter writer = new StringWriter();
            e.printStackTrace(new PrintWriter(writer));

            intent = new Intent(context, CrashActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(CrashActivity.EXTRA_NAME, e.getClass().getName());
            intent.putExtra(CrashActivity.EXTRA_MESSAGE, e.getLocalizedMessage());
            intent.putExtra(CrashActivity.EXTRA_STACK_TRACE, writer.toString());

            if (email != null)
                intent.putExtra(CrashActivity.EXTRA_EMAIL, email);

            if (color != null)
                intent.putExtra(CrashActivity.EXTRA_COLOR, color);
        } else e.printStackTrace();

        if (intent != null) {
            if (isBackground) {
                NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                if (manager != null) {
                    NotificationCompat.Builder builder;

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        manager.createNotificationChannel(new NotificationChannel("crashNotifications", context.getString(R.string.title_crash_notifications), NotificationManager.IMPORTANCE_DEFAULT));
                        builder = new NotificationCompat.Builder(context, "crashNotifications");
                    } else builder = new NotificationCompat.Builder(context);

                    manager.notify(0, builder.setContentTitle(String.format(context.getString(R.string.title_crash_notifications), context.getString(R.string.app_name)))
                            .setContentText(String.format(context.getString(R.string.title_email), e.getClass().getName(), context.getString(R.string.app_name)))
                            .build());
                }
            } else context.startActivity(intent);
        }

        for (OnCrashListener listener : listeners) {
            listener.onCrash(t, e);
        }

        System.exit(1);
    }

    public interface OnCrashListener {
        void onCrash(Thread thread, Throwable throwable);
    }
}
