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
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.fragment.DynamicFragment;

import java.util.ArrayList;

import static com.pranavpandey.android.dynamic.support.permission.DynamicPermissions.ADS_INTENT_EXTRA_ACTION;
import static com.pranavpandey.android.dynamic.support.permission.DynamicPermissions.ADS_INTENT_EXTRA_INTENT;
import static com.pranavpandey.android.dynamic.support.permission.DynamicPermissions.ADS_INTENT_EXTRA_PERMISSIONS;
import static com.pranavpandey.android.dynamic.support.permission.DynamicPermissions.ADS_INTENT_PERMISSIONS;

/**
 * Base fragment class to show a list of required permissions in a
 * recycler view. It will be used internally by the {@link DynamicPermissionsActivity}
 * to request or manage permissions.
 */
public class DynamicPermissionsFragment extends DynamicFragment {

    /**
     * Constant for permission request code to open settings screen.
     */
    public static final int ADS_PERMISSIONS_REQUEST_CODE = 1;

    /**
     * Constant for permission max count to request all the permissions
     * at once.
     */
    public static final int ADS_PERMISSIONS_DANGEROUS_MAX_COUNT = 1;

    /**
     * Constant for permission request delay to update the status
     * accordingly.
     */
    public static final long ADS_PERMISSION_REQUEST_DELAY = 300;

    /**
     * Dynamic permissions view used by this fragment.
     */
    private DynamicPermissionsView mDynamicPermissionsView;

    /**
     * Request counter for the dangerous permissions.
     */
    private int mDangerousPermissionsRequest;

    /**
     * Request counter for the special permissions.
     */
    private int mSpecialPermissionsRequest;

    /**
     * No. of special permissions to be requested.
     */
    private int mSpecialPermissionsSize = 0;

    /**
     * {@code true} to request all the permissions at once.
     */
    private boolean mRequestAll;

    /**
     * {@code true} to request the dangerous permissions.
     */
    private boolean mRequestDangerousPermissions = true;

    /**
     * Initialize the new instance of this fragment.
     *
     * @param permissionsIntent The intent with all the requested permissions
     *                          and action.
     *
     * @return A instance of {@link DynamicPermissionsFragment}.
     */
    public static Fragment newInstance(@Nullable Intent permissionsIntent) {
        DynamicPermissionsFragment fragment = new DynamicPermissionsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ADS_INTENT_PERMISSIONS, permissionsIntent);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ads_fragment_permissions, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDynamicPermissionsView = view.findViewById(R.id.ads_permissions_view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getDynamicActivity().setFAB(R.drawable.ads_ic_done_all,
                View.VISIBLE, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetRequestCounter(true);
                requestAllPermissions();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@Nullable Menu menu, @Nullable MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.ads_menu_permissions, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@Nullable MenuItem item) {
        if (item != null) {
            int id = item.getItemId();
            if (id == R.id.ads_menu_app_info) {
                DynamicPermissionUtils.launchAppInfo(getContext());
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();

        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                initPermissions();
                if (mRequestDangerousPermissions) {
                    requestDangerousPermissions(mDynamicPermissionsView.getDangerousPermissions());
                    mRequestDangerousPermissions = false;
                }

                requestAllPermissions();
            }
        }, ADS_PERMISSION_REQUEST_DELAY);
    }

    /**
     * Reset the request counter.
     *
     * @param request {@code true} to request all the permission again.
     */
    private void resetRequestCounter(boolean request) {
        mDangerousPermissionsRequest = 0;
        mSpecialPermissionsRequest = 0;
        mRequestAll = request;
    }

    /**
     * Request all the permissions at once including dangerous and
     * special permissions.
     */
    private void requestAllPermissions() {
        if (mDangerousPermissionsRequest <= ADS_PERMISSIONS_DANGEROUS_MAX_COUNT
                && mSpecialPermissionsRequest <= mSpecialPermissionsSize) {
            if (mRequestAll && !mDynamicPermissionsView.isAllPermissionsGranted()) {
                if (mDynamicPermissionsView.isDangerousPermissionsLeft()) {
                    mDangerousPermissionsRequest++;
                    if (mDangerousPermissionsRequest <= ADS_PERMISSIONS_DANGEROUS_MAX_COUNT) {
                        requestDangerousPermissions(mDynamicPermissionsView.getDangerousPermissionsLeft());
                    } else {
                        requestAllPermissions();
                    }
                } else if (mDynamicPermissionsView.isSpecialPermissionsLeft()) {
                    mSpecialPermissionsRequest++;
                    if (mSpecialPermissionsRequest <= mSpecialPermissionsSize) {
                        requestPermission(mDynamicPermissionsView.getSpecialPermissionsLeft().get(0));
                    } else {
                        requestAllPermissions();
                    }
                } else {
                    resetRequestCounter(false);
                }
            }
        } else {
            resetRequestCounter(false);
            showHint();
        }
    }

    /**
     * Initialize the permissions view according to the requested
     * permissions status.
     */
    public void initPermissions() {
        String[] permissionsArray = getPermissions();
        ArrayList<DynamicPermission> permissions;
        ((DynamicPermissionsActivity) getActivity()).updateSubtitle(permissionsArray.length);

        permissions = DynamicPermissions.getInstance()
                .getPermissionItemArrayList(permissionsArray);

        mDynamicPermissionsView.setPermissions(permissions, new DynamicPermissionsView.PermissionListener() {
            @Override
            public void onPermissionSelected(@NonNull View view, int position,
                                             @NonNull DynamicPermission dynamicPermission) {
                requestPermission(dynamicPermission);
            }
        });

        if (mSpecialPermissionsSize == 0) {
            mSpecialPermissionsSize = mDynamicPermissionsView.getSpecialPermissionsLeft().size();
        }

        if (mDynamicPermissionsView.isAllPermissionsGranted()) {
            getDynamicActivity().hideFAB();

            if (mRequestAll) {
                mRequestAll = false;

                if (mDynamicPermissionsView.isPermissionsLeft()) {
                    showHint();
                }
            }

            if (!mDynamicPermissionsView.isPermissionsLeft()) {
                checkForAction();
                finishActivity();
            }
        } else {
            getDynamicActivity().showFAB();
        }
    }

    /**
     * Show hint if permission request is cancelled by the user.
     */
    @SuppressLint("Range")
    private void showHint() {
        getDynamicActivity().getSnackBar(
                R.string.ads_perm_info_grant_all, Snackbar.LENGTH_LONG).show();
    }

    /**
     * @return The permissions to be requested from the supplied
     *         arguments.
     */
    public String[] getPermissions() {
        return getIntent().getStringArrayExtra(ADS_INTENT_EXTRA_PERMISSIONS);
    }

    /**
     * @return The permissions extra from the fragment arguments.
     *
     * @see Fragment#getArguments()
     */
    public Intent getIntent() {
        return getArguments().getParcelable(ADS_INTENT_PERMISSIONS);
    }

    /**
     * Request a dynamic permission according to its type.
     * Either dangerous or special.
     */
    private void requestPermission(@NonNull DynamicPermission dynamicPermission) {
        String permission = dynamicPermission.getPermission();

        if (dynamicPermission.isDangerous()) {
            if (!dynamicPermission.isAskAgain()) {
                DynamicPermissionUtils.launchAppInfo(getContext());
            } else {
                requestDangerousPermissions(dynamicPermission.getPermission());
            }
        } else {
            if (permission.equals(Manifest.permission.WRITE_SETTINGS)
                    || permission.equals(Manifest.permission.PACKAGE_USAGE_STATS)
                    || permission.equals(Manifest.permission.SYSTEM_ALERT_WINDOW)) {
                DynamicPermissionUtils.openPermissionSettings(getContext(), permission);
            }
        }
    }

    /**
     * Request the supplied dangerous permissions.
     *
     * @param permissions The dangerous permissions to be requested.
     *
     * @see ActivityCompat#requestPermissions(Activity, String[], int)
     */
    private void requestDangerousPermissions(@NonNull String... permissions) {
        if (permissions.length != 0) {
            ActivityCompat.requestPermissions(
                    getActivity(), permissions, ADS_PERMISSIONS_REQUEST_CODE);
        }
    }

    /**
     * Check if there is any action to be performed after allowing
     * all the permissions.
     */
    private void checkForAction() {
        Intent actionIntent = getIntent().getParcelableExtra(ADS_INTENT_EXTRA_INTENT);

        if (actionIntent != null) {
            switch (getIntent().getIntExtra(
                    ADS_INTENT_EXTRA_ACTION, DynamicPermissionsAction.NONE)) {
                case DynamicPermissionsAction.START_SERVICE:
                    getContext().startService(actionIntent);
                    break;
                case DynamicPermissionsAction.START_FOREGROUND_SERVICE:
                    ContextCompat.startForegroundService(getContext(), actionIntent);
                    break;
                case DynamicPermissionsAction.START_ACTIVITY:
                    getContext().startActivity(actionIntent);
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case ADS_PERMISSIONS_REQUEST_CODE:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mDynamicPermissionsView.isPermissionsLeft()) {
            setResult(Activity.RESULT_CANCELED);
        } else {
            setResult(Activity.RESULT_OK);
        }
    }
}
