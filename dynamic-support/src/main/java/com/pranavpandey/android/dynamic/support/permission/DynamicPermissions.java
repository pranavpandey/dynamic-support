/*
 * Copyright 2019 Pranav Pandey
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pranavpandey.android.dynamic.support.permission;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionGroupInfo;
import android.content.pm.PermissionInfo;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.pranavpandey.android.dynamic.support.intent.DynamicIntent;
import com.pranavpandey.android.dynamic.support.model.DynamicAction;
import com.pranavpandey.android.dynamic.support.model.DynamicPermission;
import com.pranavpandey.android.dynamic.support.permission.activity.DynamicPermissionsActivity;
import com.pranavpandey.android.dynamic.support.utils.DynamicPermissionUtils;
import com.pranavpandey.android.dynamic.support.utils.DynamicResourceUtils;
import com.pranavpandey.android.dynamic.utils.DynamicSdkUtils;

import java.util.ArrayList;

/**
 * Help class to request and manage runtime permissions introduced in API 23.
 * <p>It must be initialized before using any of its functions or requesting any permissions.
 *
 * <p><p>Register the {@link DynamicPermissionsActivity} via {@link #setPermissionActivity(Class)}
 * to request the permissions via this manager.
 *
 * @see <a href="https://developer.android.com/training/permissions/requesting.html">
 *      Requesting Permissions at Run Time</a>
 */
@TargetApi(Build.VERSION_CODES.M)
public class DynamicPermissions {

    /**
     * Singleton instance of {@link DynamicPermissions}.
     */
    private static DynamicPermissions sInstance;

    /**
     * Context used by this instance.
     */
    private Context mContext;

    /**
     * Permissions activity used by this manager.
     */
    private Class<?> mPermissionActivity;

    /**
     * Making the default constructor private so that it cannot be initialized without a context.
     * <p>Use {@link #initializeInstance(Context)} instead.
     */
    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    protected DynamicPermissions() { }

    private DynamicPermissions(@NonNull Context context) {
        this.mContext = context;
    }

    /**
     * Initialize permissions when application starts.
     * <p>Must be initialized once.
     *
     * @param context The context to request and manage permissions.
     */
    public static synchronized void initializeInstance(@Nullable Context context) {
        if (context == null) {
            throw new NullPointerException("Context should not be null");
        }

        if (sInstance == null) {
            sInstance = new DynamicPermissions(context);
        }
    }

    /**
     * Returns the context used by this instance.
     *
     * @return The context used by this instance.
     */
    public @NonNull Context getContext() {
        return mContext;
    }

    /**
     * Sets the context used by this instance.
     *
     * @param context The context to be set.
     */
    public void setContext(@NonNull Context context) {
        this.mContext = context;
    }

    /**
     * Get instance to access public methods. Must be called before accessing the methods.
     *
     * @return The singleton instance of this class.
     */
    public static synchronized DynamicPermissions getInstance() {
        if (sInstance == null) {
            throw new IllegalStateException(DynamicPermissions.class.getSimpleName() +
                    " is not initialized, call initializeInstance(..) method first.");
        }

        return sInstance;
    }

    /**
     * Get the permission activity used by this manager.
     *
     * @return The permission activity used by this manager.
     */
    public Class<?> getPermissionActivity() {
        return mPermissionActivity;
    }

    /**
     * Sets the permission activity for this instance.
     *
     * @param permissionActivity The permission activity class to be set.
     */
    public void setPermissionActivity(Class<?> permissionActivity) {
        this.mPermissionActivity = permissionActivity;
    }

    /**
     * Request the supplied permissions if not granted.
     *
     * @param context The context to start the activity.
     * @param permissions The array of permissions to be requested.
     * @param history {@code false} to exclude the system settings activity from the recents.
     * @param actionIntent The intent which should be called after all the permissions has been
     *                     granted.
     * @param action The intent action, either start an activity or a service.
     * @param requestCode The request code for the result.
     */
    public void requestPermissions(@NonNull Context context,
            @NonNull String[] permissions, boolean history, @Nullable Intent actionIntent,
            @DynamicAction int action, int requestCode) {
        Intent intent = requestPermissionsIntent(
                context, permissions, history, actionIntent, action);

        if (context instanceof Activity) {
            ((Activity) context).startActivityForResult(intent, requestCode);
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    /**
     * Request the permissions activity intent for the supplied permissions.
     *
     * @param context The context to build the intent.
     * @param permissions The array of permissions to be requested.
     * @param history {@code false} to exclude the system settings activity from the recents.
     * @param actionIntent The intent which should be called after all the permissions has been
     *                     granted.
     * @param action The intent action, either start an activity or a service.
     */
    public Intent requestPermissionsIntent(@NonNull Context context,
            @NonNull String[] permissions, boolean history,
            @Nullable Intent actionIntent, @DynamicAction int action) {
        Intent intent = new Intent(context, mPermissionActivity);
        intent.setAction(DynamicIntent.ACTION_PERMISSIONS);
        intent.putExtra(DynamicIntent.EXTRA_PERMISSIONS, permissions);
        if (!history) {
            intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        }

        if (actionIntent != null) {
            intent.putExtra(DynamicIntent.EXTRA_PERMISSIONS_INTENT, actionIntent);
            intent.putExtra(DynamicIntent.EXTRA_PERMISSIONS_ACTION, action);
        }

        return intent;
    }

    /**
     * Request the supplied permissions if not granted.
     *
     * @param fragment The fragment to start the activity.
     * @param permissions The array of permissions to be requested.
     * @param history {@code false} to exclude the system settings activity from the recents.
     * @param actionIntent The intent which should be called after all the permissions has been
     *                     granted.
     * @param action The intent action, either start an activity or a service.
     * @param requestCode The request code for the result.
     */
    public void requestPermissions(@NonNull Fragment fragment,
            @NonNull String[] permissions, boolean history, @Nullable Intent actionIntent,
            @DynamicAction int action, int requestCode) {
        Intent intent = new Intent(fragment.getContext(), mPermissionActivity);
        intent.setAction(DynamicIntent.ACTION_PERMISSIONS);
        intent.putExtra(DynamicIntent.EXTRA_PERMISSIONS, permissions);
        if (!history) {
            intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        }

        if (actionIntent != null) {
            intent.putExtra(DynamicIntent.EXTRA_PERMISSIONS_INTENT, actionIntent);
            intent.putExtra(DynamicIntent.EXTRA_PERMISSIONS_ACTION, action);
        }

        fragment.startActivityForResult(intent, requestCode);
    }

    /**
     * Request the supplied permissions if not granted.
     *
     * @param context The context to start the activity.
     * @param permissions The array of permissions to be requested.
     * @param history {@code false} to exclude the system settings activity from the recents.
     * @param actionIntent The intent which should be called after all the permissions has been
     *                     granted.
     * @param action The intent action, either start an activity or a service.
     */
    public void requestPermissions(@NonNull Context context, @NonNull String[] permissions,
            boolean history, @Nullable Intent actionIntent, @DynamicAction int action) {
        requestPermissions(context, permissions, history, actionIntent,
                action, DynamicIntent.REQUEST_PERMISSIONS);
    }

    /**
     * Request the supplied permissions if not granted.
     *
     * @param fragment The fragment to start the activity.
     * @param permissions The array of permissions to be requested.
     * @param history {@code false} to exclude the system settings activity from the recents.
     * @param actionIntent The intent which should be called after all the permissions has been
     *                     granted.
     * @param action The intent action, either start an activity or a service.
     */
    public void requestPermissions(@NonNull Fragment fragment, @NonNull String[] permissions,
            boolean history, @Nullable Intent actionIntent, @DynamicAction int action) {
        requestPermissions(fragment, permissions, history, actionIntent,
                action, DynamicIntent.REQUEST_PERMISSIONS);
    }

    /**
     * Request the supplied permissions if not granted.
     *
     * @param permissions The array of permissions to be requested.
     * @param history {@code false} to exclude the system settings activity from the recents.
     * @param actionIntent The intent which should be called after all the permissions has been
     *                     granted.
     * @param action The intent action, either start an activity or a service.
     */
    public void requestPermissions(@NonNull String[] permissions, boolean history,
            @Nullable Intent actionIntent, @DynamicAction int action) {
        requestPermissions(mContext, permissions, history, actionIntent, action);
    }

    /**
     * Checks whether the supplied permissions have been granted. It can also be used to
     * automatically request the permissions those are denied or not requested yet by using the
     * permission activity.
     *
     * @param context The context to start the activity.
     * @param permissions The array of permissions to be requested.
     * @param request {@code true} to automatically request the permissions if not granted.
     * @param actionIntent The intent which should be called after all the permissions has been
     *                     granted.
     * @param action The intent action, either start an activity or a service.
     * @param requestCode The request code for the result.
     *
     * @return {@code true} if all the supplied permissions has been granted.
     */
    public boolean isGranted(@NonNull Context context,
            @NonNull String[] permissions, boolean request, @Nullable Intent actionIntent,
            @DynamicAction int action, int requestCode) {
        String[] permissionsNotGranted = isGranted(permissions);
        if (request && permissionsNotGranted.length != 0) {
            requestPermissions(context, permissions, true, actionIntent, action, requestCode);
        }

        return permissionsNotGranted.length == 0;
    }

    /**
     * Checks whether the supplied permissions have been granted. It can also be used to
     * automatically request the permissions those are denied or not requested yet by using the
     * permission activity.
     *
     * @param fragment The fragment to start the activity.
     * @param permissions The array of permissions to be requested.
     * @param request {@code true} to automatically request the permissions if not granted.
     * @param actionIntent The intent which should be called after all the permissions has been
     *                     granted.
     * @param action The intent action, either start an activity or a service.
     * @param requestCode The request code for the result.
     *
     * @return {@code true} if all the supplied permissions has been granted.
     */
    public boolean isGranted(@NonNull Fragment fragment,
            @NonNull String[] permissions, boolean request, @Nullable Intent actionIntent,
            @DynamicAction int action, int requestCode) {
        String[] permissionsNotGranted = isGranted(permissions);
        if (request && permissionsNotGranted.length != 0) {
            requestPermissions(fragment, permissions, true, actionIntent, action, requestCode);
        }

        return permissionsNotGranted.length == 0;
    }

    /**
     * Checks whether the supplied permissions have been granted. It can also be used to
     * automatically request the permissions those are denied or not requested yet by using the
     * permission activity.
     *
     * @param permissions The array of permissions to be requested.
     * @param request {@code true} to automatically request the permissions if not granted.
     * @param actionIntent The intent which should be called after all the permissions has been
     *                     granted.
     * @param action The intent action, either start an activity or a service.
     *
     * @return {@code true} if all the supplied permissions has been granted.
     */
    public boolean isGranted(@NonNull String[] permissions, boolean request,
            @Nullable Intent actionIntent, @DynamicAction int action) {
        return isGranted(mContext, permissions, request, actionIntent,
                action, DynamicIntent.REQUEST_PERMISSIONS);
    }

    /**
     * Checks whether the supplied permissions have been granted. It can also be used to
     * automatically request the permissions those are denied or not requested yet by using the
     * permission activity.
     *
     * @param context The context to start the activity.
     * @param permissions The array of permissions to be requested.
     * @param request {@code true} to automatically request the permissions if not granted.
     * @param requestCode The request code for the result.
     *
     * @return {@code true} if all the supplied permissions has been granted.
     */
    public boolean isGranted(@NonNull Context context,
            @NonNull String[] permissions, boolean request, int requestCode) {
        return isGranted(context, permissions, request,
                null, DynamicAction.NONE, requestCode);
    }

    /**
     * Checks whether the supplied permissions have been granted. It can also be used to
     * automatically request the permissions those are denied or not requested yet by using the
     * permission activity.
     *
     * @param fragment The fragment to start the activity.
     * @param permissions The array of permissions to be requested.
     * @param request {@code true} to automatically request the permissions if not granted.
     * @param requestCode The request code for the result.
     *
     * @return {@code true} if all the supplied permissions has been granted.
     */
    public boolean isGranted(@NonNull Fragment fragment,
            @NonNull String[] permissions, boolean request, int requestCode) {
        return isGranted(fragment, permissions, request,
                null, DynamicAction.NONE, requestCode);
    }

    /**
     * Checks whether the supplied permissions have been granted. It can also be used to
     * automatically request the permissions those are denied or not requested yet by using the
     * permission activity.
     *
     * @param permissions The array of permissions to be requested.
     * @param request {@code true} to automatically request the permissions if not granted.
     *
     * @return {@code true} if all the supplied permissions has been granted.
     */
    public boolean isGranted(@NonNull String[] permissions, boolean request) {
        return isGranted(permissions, request, null, DynamicAction.NONE);
    }

    /**
     * Checks whether the supplied permissions have been granted.
     *
     * @return {@code true} if all the supplied permissions has been granted.
     *
     * @param permissions The array of permissions to be requested.
     */
    private String[] isGranted(@NonNull String[] permissions) {
        final ArrayList<String> permissionsNotGranted = new ArrayList<String>();

        for (String permission: permissions) {
            switch (permission) {
                case Manifest.permission.WRITE_SETTINGS:
                    if (!canWriteSystemSettings()) {
                        permissionsNotGranted.add(permission);
                    }
                    break;
                case Manifest.permission.SYSTEM_ALERT_WINDOW:
                    if (!canDrawOverlays()) {
                        permissionsNotGranted.add(permission);
                    }
                    break;
                case Manifest.permission.PACKAGE_USAGE_STATS:
                    if (!hasUsageAccess()) {
                        permissionsNotGranted.add(permission);
                    }
                    break;
                default:
                    if (ContextCompat.checkSelfPermission(mContext,
                            permission) != PackageManager.PERMISSION_GRANTED) {
                        permissionsNotGranted.add(permission);
                    }
                    break;
            }
        }

        return permissionsNotGranted.toArray(new String[0]);
    }

    /**
     * Checks whether the package can write system settings.
     *
     * @return {@code true} if can write system settings.
     *
     * @see Manifest.permission#WRITE_SETTINGS
     */
    public boolean canWriteSystemSettings() {
        return !DynamicSdkUtils.is23() || Settings.System.canWrite(mContext);

    }

    /**
     * Checks whether the package has overlay permission.
     *
     * @return {@code true} if can draw overlays.
     *
     * @see Manifest.permission#SYSTEM_ALERT_WINDOW
     */
    public boolean canDrawOverlays() {
        return !DynamicSdkUtils.is23() || Settings.canDrawOverlays(mContext);
    }

    /**
     * Checks whether the package has usage access permission.
     *
     * @return {@code true} if has usage access.
     *
     * @see Manifest.permission#PACKAGE_USAGE_STATS
     */
    public boolean hasUsageAccess() {
        if (DynamicSdkUtils.is21()) {
            try {
                PackageManager packageManager = mContext.getPackageManager();
                ApplicationInfo applicationInfo = packageManager
                        .getApplicationInfo(mContext.getPackageName(), 0);
                AppOpsManager appOpsManager = (AppOpsManager) mContext
                        .getSystemService(Context.APP_OPS_SERVICE);

                int mode = AppOpsManager.MODE_ERRORED;
                if (appOpsManager != null) {
                    mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                            applicationInfo.uid, applicationInfo.packageName);
                }

                return (mode == AppOpsManager.MODE_ALLOWED);
            } catch (Exception e) {
                return false;
            }
        }

        return true;
    }

    public boolean isIgnoringBatteryOptimizations(boolean settings) {
        if (DynamicSdkUtils.is23()) {
            boolean ignoring = false;

            try {
                PowerManager powerManager = ((PowerManager)
                        mContext.getSystemService(Context.POWER_SERVICE));

                if (powerManager != null) {
                    ignoring = powerManager.isIgnoringBatteryOptimizations(
                            mContext.getPackageName());
                }

                if (!ignoring && settings) {
                    DynamicPermissionUtils.openPermissionSettings(mContext,
                            Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                }
            } catch (Exception ignored) {
                return false;
            }

            return ignoring;
        }

        return true;
    }

    /**
     * Checks whether the activity can be launched from the background.
     *
     * @return {@code true} if activity can be launched from the background.
     */
    public boolean canLaunchFromBackground() {
        return !DynamicSdkUtils.is29() || canDrawOverlays();
    }

    /**
     * Converts the array of permissions to the array list of {@link DynamicPermissions}.
     *
     * @param permissions The permissions array to be converted.
     * 
     * @return The array list containing {@link DynamicPermissions}.
     */
    public ArrayList<DynamicPermission> getPermissionItemArrayList(
            @NonNull String[] permissions) {
        ArrayList<DynamicPermission> permissionsList = new ArrayList<>();
        PackageManager packageManager = mContext.getPackageManager();

        for (String permission: permissions) {
            if (permission.equals(Manifest.permission.WRITE_SETTINGS)
                    || permission.equals(Manifest.permission.PACKAGE_USAGE_STATS)
                    || permission.equals(Manifest.permission.SYSTEM_ALERT_WINDOW)) {

                DynamicPermission dynamicPermission = new DynamicPermission(
                        permission, DynamicResourceUtils.getDrawable(mContext,
                        DynamicPermissionUtils.getPermissionIcon(permission)),
                        mContext.getString(DynamicPermissionUtils.getPermissionTitle(permission)),
                        mContext.getString(DynamicPermissionUtils.getPermissionSubtitle(permission)));

                if (permission.equals(Manifest.permission.WRITE_SETTINGS)) {
                    dynamicPermission.setAllowed(canWriteSystemSettings());
                }

                if (permission.equals(Manifest.permission.PACKAGE_USAGE_STATS)) {
                    dynamicPermission.setAllowed(hasUsageAccess());
                }

                if (permission.equals(Manifest.permission.SYSTEM_ALERT_WINDOW)) {
                    dynamicPermission.setAllowed(canDrawOverlays());
                }

                permissionsList.add(dynamicPermission);
            } else {
                try {
                    PermissionInfo permInfo = packageManager
                            .getPermissionInfo(permission, PackageManager.GET_META_DATA);

                    PermissionGroupInfo permGroupInfo = packageManager
                            .getPermissionGroupInfo(permInfo.group, PackageManager.GET_META_DATA);

                    DynamicPermission dynamicPermission = new DynamicPermission(
                            permission, permGroupInfo.loadIcon(packageManager),
                            permGroupInfo.loadLabel(packageManager).toString());

                    if (permGroupInfo.loadDescription(packageManager) != null) {
                        dynamicPermission.setSubtitle(
                                permGroupInfo.loadDescription(packageManager).toString());
                    }

                    dynamicPermission.setDangerous(true);
                    dynamicPermission.setAllowed(ContextCompat.checkSelfPermission(mContext,
                            permission) == PackageManager.PERMISSION_GRANTED);

                    if (!permissionsList.contains(dynamicPermission)) {
                        permissionsList.add(dynamicPermission);
                    }
                } catch (Exception ignored) {
                }
            }
        }

        return permissionsList;
    }
}
