package de.j4velin.headsetturnon;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

public class AdminReceiver extends DeviceAdminReceiver {


    @Override
    public CharSequence onDisableRequested(final Context context, final Intent intent) {
        return "Without this permission, HeadsetTurnOn will not be able to turn off the display!";
    }

    public static void lock(final Context context) {
        final DevicePolicyManager mDPM =
                (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        try {
            mDPM.lockNow();
        } catch (Exception se) {
            se.printStackTrace();
        }
    }

    public static boolean isActive(final Context context) {
        return ((DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE))
                .isAdminActive(new ComponentName(context, AdminReceiver.class));
    }
}
