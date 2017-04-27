package james.crasher;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.ColorInt;

import java.io.PrintWriter;
import java.io.StringWriter;

import james.crasher.activities.CrashActivity;

public class Crasher implements Thread.UncaughtExceptionHandler {

    private Context context;

    private String email;
    private Integer color;

    public Crasher(Context context) {
        this.context = context.getApplicationContext();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setColor(@ColorInt int color) {
        this.color = color;
    }

    @Override
    public void uncaughtException(Thread t, final Throwable e) {
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

        System.exit(1);
    }
}
