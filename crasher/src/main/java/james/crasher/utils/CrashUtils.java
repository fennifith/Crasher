package james.crasher.utils;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;

public class CrashUtils {

    public static boolean isInstalledFromPlayStore(Context context) {
        return new ArrayList<>(Arrays.asList("com.android.vending", "com.google.android.feedback"))
                .contains(context.getPackageManager().getInstallerPackageName(context.getPackageName()));
    }

    public static String getCause(Context context, String stackTrace) {
        int index = stackTrace.indexOf("at " + context.getPackageName());
        if (index >= 0) {
            stackTrace = stackTrace.substring(index);
            index = stackTrace.indexOf("(");

            if (index > 0)
                return stackTrace.substring(index + 1, index + stackTrace.substring(index).indexOf(")"));
        }

        return null;
    }

}
