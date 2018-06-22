package james.crasher;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
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

    private String email;
    private String debugMessage;
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

    public Crasher setDebugMessage(String debugMessage) {
        this.debugMessage = debugMessage;
        return this;
    }

    @Nullable
    public String getDebugMessage() {
        return debugMessage;
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
        if ((BuildConfig.DEBUG && isStackOverflow) || isForceStackOverflow) {
            e.printStackTrace();
            Log.d("Crasher", "Exception thrown: " + e.getClass().getName() + ". Opening StackOverflow query for \"" + e.getMessage() + "\".");

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://stackoverflow.com/search?q=[java][android]" + e.getMessage()));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else if (isCrashActivity) {
            StringWriter writer = new StringWriter();
            e.printStackTrace(new PrintWriter(writer));

            Intent intent = new Intent(context, CrashActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(CrashActivity.EXTRA_NAME, e.getClass().getName());
            intent.putExtra(CrashActivity.EXTRA_MESSAGE, e.getLocalizedMessage());
            intent.putExtra(CrashActivity.EXTRA_STACK_TRACE, writer.toString());

            if (email != null)
                intent.putExtra(CrashActivity.EXTRA_EMAIL, email);

            if (debugMessage != null)
                intent.putExtra(CrashActivity.EXTRA_DEBUG_MESSAGE, debugMessage);

            if (color != null)
                intent.putExtra(CrashActivity.EXTRA_COLOR, color);

            context.startActivity(intent);
        } else e.printStackTrace();

        for (OnCrashListener listener : listeners) {
            listener.onCrash(t, e);
        }

        System.exit(1);
    }

    public interface OnCrashListener {
        void onCrash(Thread thread, Throwable throwable);
    }
}
