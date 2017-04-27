package james.crasher;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;

import james.crasher.activities.CrashActivity;

public class Crasher implements Thread.UncaughtExceptionHandler {

    private Context context;

    private boolean isStackOverflow;
    private boolean isForceStackOverflow;
    private boolean isCrashActivity = true;

    private String email;
    private Integer color;

    public Crasher(Context context) {
        this.context = context.getApplicationContext();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    public void setStackoverflowEnabled(boolean isEnabled) {
        isStackOverflow = isEnabled;
    }

    public boolean isStackoverflowEnabled() {
        return BuildConfig.DEBUG && isStackOverflow;
    }

    public void setForceStackOverflow(boolean isForced) {
        isForceStackOverflow = isForced;
    }

    public boolean isForceStackOverflow() {
        return isForceStackOverflow;
    }

    public void setCrashActivityEnabled(boolean isEnabled) {
        isCrashActivity = isEnabled;
    }

    public boolean isCrashActivityEnabled() {
        return isCrashActivity;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Nullable
    public String getEmail() {
        return email;
    }

    public void setColor(@ColorInt int color) {
        this.color = color;
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

            if (color != null)
                intent.putExtra(CrashActivity.EXTRA_COLOR, color);

            context.startActivity(intent);
        } else e.printStackTrace();

        System.exit(1);
    }
}
