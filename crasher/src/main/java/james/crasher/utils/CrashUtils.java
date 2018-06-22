package james.crasher.utils;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;

public class CrashUtils {

    public static boolean isInstalledFromPlayStore(Context context) {
        return new ArrayList<>(Arrays.asList("com.android.vending", "com.google.android.feedback"))
                .contains(context.getPackageManager().getInstallerPackageName(context.getPackageName()));
    }

}
