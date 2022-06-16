/*
 * Copyright 2018-2022 Pranav Pandey
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
import com.pranavpandey.android.dynamic.util.DynamicColorUtils;
import com.pranavpandey.android.dynamic.util.DynamicSdkUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * A {@link DynamicResolver} to resolve the theme.
 */
public class DynamicThemeResolver implements DynamicResolver {

    /**
     * Dynamic theme instance used by this resolver.
     */
    private final DynamicTheme mTheme;

    /**
     * Dynamic colors used by this resolver.
     */
    private final DynamicColors mColors;

    /**
     * Local dynamic colors used by this resolver.
     */
    private final DynamicColors mColorsLocal;

    /**
     * Constructor to initialize an object of this class.
     *
     * @param theme The dynamic theme instance for this resolver.
     *              <p>If {@code null}, then it will try to use the the default instance.
     */
    public DynamicThemeResolver(@Nullable DynamicTheme theme) {
        this(theme, null, null);
    }

    /**
     * Constructor to initialize an object of this class.
     *
     * @param theme The dynamic theme instance for this resolver.
     *              <p>If {@code null}, then it will try to use the the default instance.
     * @param colors The dynamic colors for this resolver.
     *               <p>Pass {@code null}, to use the default implementation.
     * @param colorsLocal The local dynamic colors for this resolver.
     *                    <p>Pass {@code null}, to use the default implementation.
     */
    public DynamicThemeResolver(@Nullable DynamicTheme theme,
            @Nullable DynamicColors colors, @Nullable DynamicColors colorsLocal) {
        this.mTheme = theme;
        this.mColors = colors != null ? colors : new DynamicColors();
        this.mColorsLocal = colorsLocal != null ? colorsLocal : new DynamicColors();
    }

    @Override
    public @NonNull DynamicTheme getTheme() {
        return mTheme != null ? mTheme : DynamicTheme.getInstance();
    }

    @Override
    public @NonNull DynamicColors getColors(boolean resolve) {
        if (resolve) {
            return mColorsLocal.getMutated().isEmpty() ? mColors : mColorsLocal;
        }

        return mColors;
    }

    @Override
    public @NonNull DynamicColors getColors() {
        return getColors(true);
    }

    @Override
    public boolean isSystemNightMode() {
        return (getTheme().getContext().getResources().getConfiguration().uiMode
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
    public boolean resolveNightTheme(@Theme int theme,
            @Theme.Night int implementation, boolean data) {
        if (theme == Theme.AUTO) {
            switch (implementation) {
                case Theme.Night.AUTO:
                    return isNight(implementation);
                case Theme.Night.BATTERY:
                    return getTheme().isPowerSaveMode();
                case Theme.Night.SYSTEM:
                    return isSystemNightMode();
                case Theme.Night.CUSTOM:
                default:
                    return false;
            }
        } else if (theme == Theme.CUSTOM) {
            if (data) {
                if (getTheme().isDynamicColor() && getTheme().get()
                        .getBackgroundColor(false) == Theme.AUTO) {
                    return DynamicColorUtils.isColorDark(getColors().getOriginal(
                            Theme.ColorType.BACKGROUND, getTheme().get().getBackgroundColor()));
                } else {
                    return DynamicColorUtils.isColorDark(getTheme().get().getBackgroundColor());
                }
            } else {
                return false;
            }
        }

        return theme == Theme.NIGHT;
    }

    @Override
    public boolean resolveNightTheme(@Theme.ToString String theme,
            @Theme.Night.ToString String implementation, boolean data) {
        return resolveNightTheme(Integer.parseInt(theme), Integer.parseInt(implementation), data);
    }

    public @Theme int resolveAppTheme(@Theme int theme, @Theme.Night int night, boolean data) {
        switch (theme) {
            case Theme.AUTO:
                if (resolveNightTheme(theme, night, data)) {
                    return Theme.NIGHT;
                } else {
                    return Theme.DAY;
                }
            case Theme.DAY:
                return Theme.DAY;
            case Theme.NIGHT:
                return Theme.NIGHT;
            case Theme.CUSTOM:
                return Theme.CUSTOM;
            case Theme.APP:
            case Theme.DISABLE:
            case Theme.REMOTE:
            case Theme.SYSTEM:
            default:
                return theme;
        }
    }

    @Override
    public @Theme int resolveAppTheme(@Theme.ToString String theme,
            @Theme.Night.ToString String night, boolean data) {
        return resolveAppTheme(Integer.parseInt(theme), Integer.parseInt(night), data);
    }
}
