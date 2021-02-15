/*
 * Copyright 2018-2021 Pranav Pandey
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

package com.pranavpandey.android.dynamic.support.theme;

import android.content.res.Configuration;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pranavpandey.android.dynamic.support.listener.DynamicResolver;
import com.pranavpandey.android.dynamic.support.model.DynamicRemoteTheme;
import com.pranavpandey.android.dynamic.theme.Theme;
import com.pranavpandey.android.dynamic.utils.DynamicSdkUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * A {@link DynamicResolver} to resolve the theme.
 */
public class DynamicThemeResolver implements DynamicResolver {

    /**
     * Dynamic theme instance used by this resolver.
     */
    private final DynamicTheme mDynamicTheme;

    /**
     * Constructor to initialize an object of this class.
     *
     * @param dynamicTheme The dynamic theme instance for this resolver.
     *                     <p>If {@code null}, then it will try to use the the default instance.
     */
    public DynamicThemeResolver(@Nullable DynamicTheme dynamicTheme) {
        this.mDynamicTheme = dynamicTheme;
    }

    /**
     * Get the dynamic theme instance used by this resolver.
     * 
     * @return The dynamic theme instance used by this resolver.
     */
    public @NonNull DynamicTheme getDynamicTheme() {
        return mDynamicTheme != null ? mDynamicTheme : DynamicTheme.getInstance();
    }

    @Override
    public boolean isHideDividers() {
        return getDynamicTheme().get().getPrimaryColor()
                == getDynamicTheme().get().getAccentColorDark();
    }

    @Override
    public boolean isSystemNightMode() {
        return (getDynamicTheme().getContext().getResources().getConfiguration().uiMode
                & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
    }

    @Override
    public int resolveSystemColor(boolean isNight) {
        if (DynamicSdkUtils.is28()) {
            return isNight ? DynamicRemoteTheme.SYSTEM_COLOR_NIGHT
                    : DynamicRemoteTheme.SYSTEM_COLOR;
        } else if (DynamicSdkUtils.is21()) {
            return DynamicRemoteTheme.SYSTEM_COLOR;
        } else {
            return DynamicRemoteTheme.SYSTEM_COLOR_NIGHT;
        }
    }

    @Override
    public boolean isNight() {
        Date date = new Date();
        return date.getTime() >= getNightTimeStart().getTime()
                || date.getTime() < getNightTimeEnd().getTime();
    }

    @Override
    public boolean isNight(@Theme int theme) {
        return theme == Theme.NIGHT || (theme == Theme.AUTO && isNight());
    }

    @Override
    public boolean isNight(@Theme.ToString String theme) {
        return isNight(Integer.parseInt(theme));
    }

    @Override
    public @NonNull Date getNightTimeStart() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 19);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        return calendar.getTime();
    }

    @Override
    public @NonNull Date getNightTimeEnd() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 6);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        return calendar.getTime();
    }

    @Override
    public boolean resolveNightTheme(@Theme int appTheme, @Theme.Night int implementation) {
        if (appTheme == Theme.AUTO) {
            switch (implementation) {
                case Theme.Night.CUSTOM:
                    return false;
                case Theme.Night.AUTO:
                    return isNight(appTheme);
                case Theme.Night.BATTERY:
                    return getDynamicTheme().isPowerSaveMode();
                case Theme.Night.SYSTEM:
                default:
                    return isSystemNightMode();
            }
        }

        return appTheme == Theme.NIGHT;
    }

    @Override
    public boolean resolveNightTheme(@Theme.ToString String appTheme,
            @Theme.Night.ToString String implementation) {
        return resolveNightTheme(Integer.parseInt(appTheme), Integer.parseInt(implementation));
    }
}
