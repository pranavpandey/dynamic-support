/*
 * Copyright 2018 Pranav Pandey
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
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionGroupInfo;
import android.content.pm.PermissionInfo;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.pranavpandey.android.dynamic.support.utils.DynamicResourceUtils;
import com.pranavpandey.android.dynamic.utils.DynamicVersionUtils;

import java.util.ArrayList;

/**
 * Help class to request and manage runtime permissions introduced in Android M.
 * It must be initialized before using any of its functions or requesting any
 * permissions.
 *
 * <p>Register the {@link DynamicPermissionsActivity} via
 * {@link #setPermissionActivity(Class)} to request the permissions via this
 * manager.</p>
 *
 * @see <a href="https://developer.android.com/training/permissions/requesting.html">
 *      Requesting Permissions at Run Time</a>
 */
@TargetApi(Build.VERSION_CODES.M)
public class DynamicPermissions {

    /**
     * Constant for permissions intent.
     */
    public static final String ADS_INTENT_PERMISSIONS = "permissions_intent";

    /**
     * Constant for permissions intent extra.
     */
    public static final String ADS_INTENT_EXTRA_PERMISSIONS = "permissions";

    /**
     * Constant for permissions intent extra intent to perform it when
     * all the permissions are granted.
     */
    public static final String ADS_INTENT_EXTRA_INTENT = "permissions_extra_intent";

    /**
     * Constant for permissions intent extra action to perform it when
     * all the permissions are granted.
     */
    public static final String ADS_INTENT_EXTRA_ACTION = "permissions_extra_action";

    /**
     * Settings intent action constant for write system settings.
     *
     * @see Settings#ACTION_MANAGE_WRITE_SETTINGS
     */
    public static final String ADS_ACTION_WRITE_SYSTEM_SETTINGS
            = Settings.ACTION_MANAGE_WRITE_SETTINGS;

    /**
     * Settings intent action constant for overlay settings.
     *
     * @see Settings#ACTION_MANAGE_OVERLAY_PERMISSION
     */
    public static final String ADS_ACTION_OVERLAY_SETTINGS
            = Settings.ACTION_MANAGE_OVERLAY_PERMISSION;

    /**
     * Settings intent action constant for usage access settings.
     *
     * @see Settings#ACTION_USAGE_ACCESS_SETTINGS
     */
    public static final String ADS_ACTION_USAGE_ACCESS_SETTINGS
            = Settings.ACTION_USAGE_ACCESS_SETTINGS;

    /**
     * Singleton instance of {@link DynamicPermissions}.
     */
    private static DynamicPermissions sInstance;

    /**
     * Context used by this instance.
     */
    protected Context mContext;

    /**
     * Permissions activity used by this manager.
     */
    private Class<?> mPermissionActivity;

    /**
     * Making default constructor private so that it cannot be initialized
     * without a context. Use {@link #initializeInstance(Context)} instead.
     */
    private DynamicPermissions() { }

    private DynamicPermissions(@NonNull Context context) {
        this.mContext = context;
    }

    /**
     * Initialize permissions when application starts. 
     * Must be initialized once.
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
     * Get instance to access public methods. Must be called before
     * accessing the methods.
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
     * @return The permission activity used by this manager.
     */
    public Class<?> getPermissionActivity() {
        return mPermissionActivity;
    }

    /**
     * Sets the permission activity for this instance.
     *
     * @param permissionActivity The permission activity class
     *                           to be set.
     */
    public void setPermissionActivity(Class<?> permissionActivity) {
        this.mPermissionActivity = permissionActivity;
    }

    /**
     * @return {@code true} if all the supplied permissions has been granted.
     * It can also be used to automatically request the permissions those are
     * denied or not requested yet by using the {@link #mPermissionActivity}.
     *
     * @param permissions The array of permissions to be requested.
     * @param request {@code true} to automatically request the permissions
     *                  if not granted.
     * @param actionIntent The intent which should be called after all the
     *                     permissions has been granted.
     * @param action The intent action, either start an activity or a service.
     */
    public boolean isGranted(@NonNull String[] permissions, boolean request,
                             @Nullable Intent actionIntent, @DynamicPermissionsAction int action) {
        String[] permissionsNotGranted = isGranted(permissions);

        if (request && permissionsNotGranted.length != 0) {
            requestPermissions(permissions, true, actionIntent, action);
        }

        return permissionsNotGranted.length == 0;
    }

    /**
     * @return {@code true} if all the supplied permissions has been granted.
     * It can also be used to automatically request the permissions those are
     * denied or not requested yet by using the {@link #mPermissionActivity}.
     *
     * @param permissions The array of permissions to be requested.
     * @param request {@code true} to automatically request the permissions
     *                  if not granted.
     */
    public boolean isGranted(@NonNull String[] permissions, boolean request) {
        return isGranted(permissions, request,
                null, DynamicPermissionsAction.NONE);
    }

    /**
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

        return permissionsNotGranted.toArray(new String[permissionsNotGranted.size()]);
    }

    /**
     * Request the supplied permissions if not granted.
     *
     * @param permissions The array of permissions to be requested.
     * @param history {@code false} to exclude the system settings activity
     *                from the recents.
     * @param actionIntent The intent which should be called after all the
     *                     permissions has been granted.
     * @param action The intent action, either start an activity or a service.
     */
    public void requestPermissions(@NonNull String[] permissions, boolean history,
                                   @Nullable Intent actionIntent,
                                   @DynamicPermissionsAction int action) {
        Intent intent = new Intent(mContext, mPermissionActivity);
        intent.putExtra(ADS_INTENT_EXTRA_PERMISSIONS, permissions);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (!history) {
            intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        }

        if (actionIntent != null) {
            intent.putExtra(ADS_INTENT_EXTRA_INTENT, actionIntent);
            intent.putExtra(ADS_INTENT_EXTRA_ACTION, action);
        }

        mContext.startActivity(intent);
    }

    /**
     * @return {@code true} if can write system settings.
     *
     * @see Manifest.permission#WRITE_SETTINGS
     */
    public boolean canWriteSystemSettings() {
        return !DynamicVersionUtils.isMarshmallow() || Settings.System.canWrite(mContext);

    }

    /**
     * @return {@code true} if can draw overlays.
     *
     * @see Manifest.permission#SYSTEM_ALERT_WINDOW
     */
    public boolean canDrawOverlays() {
        return !DynamicVersionUtils.isMarshmallow() || Settings.canDrawOverlays(mContext);
    }

    /**
     * @return {@code true} if has usage access.
     *
     * @see Manifest.permission#PACKAGE_USAGE_STATS
     */
    public boolean hasUsageAccess() {
        if (DynamicVersionUtils.isLollipop()) {
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

    /**
     * Convert The array of permissions to the array list of
     * {@link DynamicPermissions}.
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

                DynamicPermission dynamicPermission = new DynamicPermission(permission,
                        DynamicResourceUtils.getDrawable(mContext,
                                DynamicPermissionUtils.getPermissionIcon(permission)),
                        mContext.getString(DynamicPermissionUtils.getPermissionTitle(permission)),
                        mContext.getString(DynamicPermissionUtils.getPermissionSubtitle(permission)));

                if (permission.equals(Manifest.permission.WRITE_SETTINGS)) {
                    dynamicPermission.setGranted(canWriteSystemSettings());
                }

                if (permission.equals(Manifest.permission.PACKAGE_USAGE_STATS)) {
                    dynamicPermission.setGranted(hasUsageAccess());
                }

                if (permission.equals(Manifest.permission.SYSTEM_ALERT_WINDOW)) {
                    dynamicPermission.setGranted(canDrawOverlays());
                }

                permissionsList.add(dynamicPermission);
            } else {
                try {
                    PermissionInfo permInfo = packageManager
                            .getPermissionInfo(permission, PackageManager.GET_META_DATA);

                    PermissionGroupInfo permGroupInfo = packageManager
                            .getPermissionGroupInfo(permInfo.group, PackageManager.GET_META_DATA);

                    DynamicPermission dynamicPermission = new DynamicPermission(permission,
                            permGroupInfo.loadIcon(packageManager),
                            permGroupInfo.loadLabel(packageManager).toString(),
                            permGroupInfo.loadDescription(packageManager).toString());

                    dynamicPermission.setDangerous(true);
                    dynamicPermission.setGranted(ContextCompat.checkSelfPermission(mContext,
                            permission) == PackageManager.PERMISSION_GRANTED);



                    if (!permissionsList.contains(dynamicPermission)) {
                        permissionsList.add(dynamicPermission);
                    }
                } catch (PackageManager.NameNotFoundException ignored) {
                }
            }
        }

        return permissionsList;
    }
}
