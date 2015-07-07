/*
 * Copyright 2015 Thomas Hoffmann
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.j4velin.headsetturnon;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

public class AdminReceiver extends DeviceAdminReceiver {


    @Override
    public CharSequence onDisableRequested(final Context context, final Intent intent) {
        return context.getString(R.string.device_warning);
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
