/*
 * Copyright 2018-2024 Pranav Pandey
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

package com.pranavpandey.android.dynamic.support.theme.fragment.base;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.pranavpandey.android.dynamic.preferences.DynamicPreferences;
import com.pranavpandey.android.dynamic.support.Dynamic;
import com.pranavpandey.android.dynamic.support.R;
import com.pranavpandey.android.dynamic.support.intent.DynamicIntent;
import com.pranavpandey.android.dynamic.support.listener.DynamicColorResolver;
import com.pranavpandey.android.dynamic.support.model.DynamicAppTheme;
import com.pranavpandey.android.dynamic.support.motion.DynamicMotion;
import com.pranavpandey.android.dynamic.support.permission.DynamicPermissions;
import com.pranavpandey.android.dynamic.support.setting.base.DynamicColorPreference;
import com.pranavpandey.android.dynamic.support.setting.base.DynamicSliderPreference;
import com.pranavpandey.android.dynamic.support.setting.base.DynamicSpinnerPreference;
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme;
import com.pranavpandey.android.dynamic.support.theme.view.DynamicPresetsView;
import com.pranavpandey.android.dynamic.support.theme.view.base.ThemePreview;
import com.pranavpandey.android.dynamic.theme.Theme;
import com.pranavpandey.android.dynamic.util.DynamicPackageUtils;
import com.pranavpandey.android.dynamic.util.DynamicSdkUtils;

/**
 * A {@link ThemeFragment} to provide {@link DynamicAppTheme} editing functionality.
 */
public class DynamicAppThemeFragment extends ThemeFragment<DynamicAppTheme> {

    /**
     * View to show the theme presets.
     */
    protected DynamicPresetsView<DynamicAppTheme> mPresetsView;

    /**
     * Dynamic color preference to control the background colors.
     */
    protected DynamicColorPreference mColorBackgroundPreference;

    /**
     * Dynamic color preference to control the surface color.
     */
    protected DynamicColorPreference mColorSurfacePreference;

    /**
     * Dynamic color preference to control the primary colors.
     */
    protected DynamicColorPreference mColorPrimaryPreference;

    /**
     * Dynamic color preference to control the accent colors.
     */
    protected DynamicColorPreference mColorAccentPreference;

    /**
     * Dynamic color preference to control the dark primary colors.
     */
    protected DynamicColorPreference mColorPrimaryDarkPreference;

    /**
     * Dynamic color preference to control the dark accent colors.
     */
    protected DynamicColorPreference mColorAccentDarkPreference;

    /**
     * Dynamic color preference to control the error colors.
     */
    protected DynamicColorPreference mColorErrorPreference;

    /**
     * Dynamic color preference to control the primary text colors.
     */
    protected DynamicColorPreference mTextPrimaryPreference;

    /**
     * Dynamic color preference to control the secondary text colors.
     */
    protected DynamicColorPreference mTextSecondaryPreference;

    /**
     * Dynamic slider preference to control the font scale.
     */
    protected DynamicSliderPreference mFontScalePreference;

    /**
     * Dynamic slider preference to control the corner radius.
     */
    protected DynamicSliderPreference mCornerSizePreference;

    /**
     * Dynamic spinner preference to control the background aware functionality.
     */
    protected DynamicSpinnerPreference mBackgroundAwarePreference;

    /**
     * Dynamic slider preference to control the contrast.
     */
    protected DynamicSliderPreference mContrastPreference;

    /**
     * Dynamic slider preference to control the opacity.
     */
    protected DynamicSliderPreference mOpacityPreference;

    /**
     * Dynamic spinner preference to control the elevation functionality.
     */
    protected DynamicSpinnerPreference mElevationPreference;

    /**
     * Dynamic spinner preference to control the style functionality.
     */
    protected DynamicSpinnerPreference mStylePreference;

    /**
     * Initialize the new instance of this fragment.
     *
     * @param dynamicAppTheme The dynamic app theme.
     * @param dynamicAppThemeDefault The default dynamic app theme.
     *
     * @return An instance of {@link DynamicAppThemeFragment}.
     *
     * @see #newInstance(String, String, boolean)
     */
    public static @NonNull DynamicAppThemeFragment newInstance(@Nullable String dynamicAppTheme,
            @Nullable String dynamicAppThemeDefault) {
        return newInstance(dynamicAppTheme, dynamicAppThemeDefault,
                DynamicIntent.EXTRA_THEME_SHOW_PRESETS_DEFAULT);
    }

    /**
     * Initialize the new instance of this fragment.
     *
     * @param dynamicAppTheme The dynamic app theme.
     * @param dynamicAppThemeDefault The default dynamic app theme.
     * @param showPresets {@code true} to show the presets.
     *
     * @return An instance of {@link DynamicAppThemeFragment}.
     */
    public static @NonNull DynamicAppThemeFragment newInstance(@Nullable String dynamicAppTheme,
            @Nullable String dynamicAppThemeDefault, boolean showPresets) {
        DynamicAppThemeFragment fragment = new DynamicAppThemeFragment();
        Bundle args = new Bundle();
        args.putString(DynamicIntent.EXTRA_THEME, dynamicAppTheme);
        args.putString(DynamicIntent.EXTRA_THEME_DEFAULT, dynamicAppThemeDefault);
        args.putBoolean(DynamicIntent.EXTRA_THEME_SHOW_PRESETS, showPresets);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setResult(Activity.RESULT_CANCELED, null, false);

        if (getSavedInstanceState() == null) {
            mSettingsChanged = false;
        }

        mDynamicTheme = getThemeFromArguments(DynamicIntent.EXTRA_THEME);
        mDynamicThemeDefault = getThemeFromArguments(DynamicIntent.EXTRA_THEME_DEFAULT);

        if (mDynamicTheme == null) {
            mDynamicTheme = DynamicTheme.getInstance().generateDefaultTheme();
        }

        if (mDynamicThemeDefault == null) {
            mDynamicThemeDefault = new DynamicAppTheme(mDynamicTheme);
        }

        mDynamicTheme.setType(mDynamicThemeDefault.getType());
    }

    @Override
    public @LayoutRes int getLayoutRes() {
        return R.layout.ads_fragment_theme;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPresetsView = view.findViewById(R.id.ads_theme_presets_view);
        mColorBackgroundPreference = view.findViewById(R.id.ads_pref_theme_color_background);
        mColorSurfacePreference = view.findViewById(R.id.ads_pref_theme_color_surface);
        mColorPrimaryPreference = view.findViewById(R.id.ads_pref_theme_color_primary);
        mColorAccentPreference = view.findViewById(R.id.ads_pref_theme_color_accent);
        mColorPrimaryDarkPreference = view.findViewById(R.id.ads_pref_theme_color_primary_dark);
        mColorAccentDarkPreference = view.findViewById(R.id.ads_pref_theme_color_accent_dark);
        mColorErrorPreference = view.findViewById(R.id.ads_pref_theme_color_error);
        mTextPrimaryPreference = view.findViewById(R.id.ads_pref_theme_text_primary);
        mTextSecondaryPreference = view.findViewById(R.id.ads_pref_theme_text_secondary);
        mFontScalePreference = view.findViewById(R.id.ads_pref_theme_font_scale);
        mCornerSizePreference = view.findViewById(R.id.ads_pref_theme_corner_size);
        mBackgroundAwarePreference = view.findViewById(R.id.ads_pref_theme_background_aware);
        mContrastPreference = view.findViewById(R.id.ads_pref_theme_contrast);
        mOpacityPreference = view.findViewById(R.id.ads_pref_theme_opacity);
        mElevationPreference = view.findViewById(R.id.ads_pref_theme_elevation);
        mStylePreference = view.findViewById(R.id.ads_pref_theme_style);

        if (getBooleanFromArguments(DynamicIntent.EXTRA_THEME_SHOW_PRESETS,
                DynamicIntent.EXTRA_THEME_SHOW_PRESETS_DEFAULT)) {
            Dynamic.setVisibility(mPresetsView, View.VISIBLE);

            mPresetsView.setPresetsAdapter(this, getPresetLayoutRes(),
                    new DynamicPresetsView.DynamicPresetsListener<DynamicAppTheme>() {
                @Override
                public void onRequestPermissions(@NonNull String[] permissions) {
                    DynamicPermissions.getInstance().isGranted(permissions, true);
                }

                @Override
                public @Nullable DynamicAppTheme getDynamicTheme(@NonNull String theme) {
                    try {
                        return new DynamicAppTheme(theme).setStyle(mDynamicTheme.getStyle())
                                .setType(mDynamicTheme.getType(false));
                    } catch (Exception ignored) {
                        return null;
                    }
                }

                @Override
                public void onPresetClick(@NonNull View anchor, @NonNull String theme,
                        @NonNull ThemePreview<DynamicAppTheme> themePreview) {
                    importTheme(themePreview.getDynamicTheme().toDynamicString(),
                            Theme.Action.IMPORT);
                }
            });
        } else {
            Dynamic.setVisibility(mPresetsView, View.GONE);
        }

        mColorBackgroundPreference.setDynamicColorResolver(new DynamicColorResolver() {
            @Override
            public int getDefaultColor(@Nullable String tag) {
                return mDynamicThemeDefault.getBackgroundColor(false, false);
            }

            @Override
            public @ColorInt int getAutoColor(@Nullable String tag) {
                return mThemePreview.getDynamicTheme().getBackgroundColor();
            }
        });
        mColorBackgroundPreference.setAltDynamicColorResolver(new DynamicColorResolver() {
            @Override
            public int getDefaultColor(@Nullable String tag) {
                return mDynamicThemeDefault.getTintBackgroundColor(false, false);
            }

            @Override
            public @ColorInt int getAutoColor(@Nullable String tag) {
                return mThemePreview.getDynamicTheme().getTintBackgroundColor();
            }
        });

        mColorSurfacePreference.setDynamicColorResolver(new DynamicColorResolver() {
            @Override
            public int getDefaultColor(@Nullable String tag) {
                return mDynamicThemeDefault.getSurfaceColor(false, false);
            }

            @Override
            public @ColorInt int getAutoColor(@Nullable String tag) {
                return mThemePreview.getDynamicTheme().getSurfaceColor();
            }
        });
        mColorSurfacePreference.setAltDynamicColorResolver(new DynamicColorResolver() {
            @Override
            public int getDefaultColor(@Nullable String tag) {
                return mDynamicThemeDefault.getTintSurfaceColor(false, false);
            }

            @Override
            public @ColorInt int getAutoColor(@Nullable String tag) {
                return mThemePreview.getDynamicTheme().getTintSurfaceColor();
            }
        });

        mColorPrimaryPreference.setDynamicColorResolver(new DynamicColorResolver() {
            @Override
            public int getDefaultColor(@Nullable String tag) {
                return mDynamicThemeDefault.getPrimaryColor(false, false);
            }

            @Override
            public @ColorInt int getAutoColor(@Nullable String tag) {
                return mThemePreview.getDynamicTheme().getPrimaryColor();
            }
        });
        mColorPrimaryPreference.setAltDynamicColorResolver(new DynamicColorResolver() {
            @Override
            public int getDefaultColor(@Nullable String tag) {
                return mDynamicThemeDefault.getTintPrimaryColor(false, false);
            }

            @Override
            public @ColorInt int getAutoColor(@Nullable String tag) {
                return mThemePreview.getDynamicTheme().getTintPrimaryColor();
            }
        });

        mColorAccentPreference.setDynamicColorResolver(new DynamicColorResolver() {
            @Override
            public int getDefaultColor(@Nullable String tag) {
                return mDynamicThemeDefault.getAccentColor(false, false);
            }

            @Override
            public @ColorInt int getAutoColor(@Nullable String tag) {
                return mThemePreview.getDynamicTheme().getAccentColor();
            }
        });
        mColorAccentPreference.setAltDynamicColorResolver(new DynamicColorResolver() {
            @Override
            public int getDefaultColor(@Nullable String tag) {
                return mDynamicThemeDefault.getTintPrimaryColor(false, false);
            }

            @Override
            public @ColorInt int getAutoColor(@Nullable String tag) {
                return mThemePreview.getDynamicTheme().getTintAccentColor();
            }
        });

        mColorPrimaryDarkPreference.setDynamicColorResolver(new DynamicColorResolver() {
            @Override
            public int getDefaultColor(@Nullable String tag) {
                return mDynamicThemeDefault.getPrimaryColorDark(false, false);
            }

            @Override
            public @ColorInt int getAutoColor(@Nullable String tag) {
                return mThemePreview.getDynamicTheme().getPrimaryColorDark();
            }
        });
        mColorPrimaryDarkPreference.setAltDynamicColorResolver(new DynamicColorResolver() {
            @Override
            public int getDefaultColor(@Nullable String tag) {
                return mDynamicThemeDefault.getTintPrimaryColorDark(false, false);
            }

            @Override
            public @ColorInt int getAutoColor(@Nullable String tag) {
                return mThemePreview.getDynamicTheme().getTintPrimaryColorDark();
            }
        });

        mColorAccentDarkPreference.setDynamicColorResolver(new DynamicColorResolver() {
            @Override
            public int getDefaultColor(@Nullable String tag) {
                return mDynamicThemeDefault.getAccentColorDark(false, false);
            }

            @Override
            public @ColorInt int getAutoColor(@Nullable String tag) {
                return mThemePreview.getDynamicTheme().getAccentColorDark();
            }
        });
        mColorAccentDarkPreference.setAltDynamicColorResolver(new DynamicColorResolver() {
            @Override
            public int getDefaultColor(@Nullable String tag) {
                return mDynamicThemeDefault.getTintAccentColorDark(false, false);
            }

            @Override
            public @ColorInt int getAutoColor(@Nullable String tag) {
                return mThemePreview.getDynamicTheme().getTintAccentColorDark();
            }
        });

        mColorErrorPreference.setDynamicColorResolver(new DynamicColorResolver() {
            @Override
            public int getDefaultColor(@Nullable String tag) {
                return mDynamicThemeDefault.getErrorColor(false, false);
            }

            @Override
            public @ColorInt int getAutoColor(@Nullable String tag) {
                return mThemePreview.getDynamicTheme().getErrorColor();
            }
        });
        mColorErrorPreference.setAltDynamicColorResolver(new DynamicColorResolver() {
            @Override
            public int getDefaultColor(@Nullable String tag) {
                return mDynamicThemeDefault.getTintErrorColor(false, false);
            }

            @Override
            public @ColorInt int getAutoColor(@Nullable String tag) {
                return mThemePreview.getDynamicTheme().getTintErrorColor();
            }
        });

        mTextPrimaryPreference.setDynamicColorResolver(new DynamicColorResolver() {
            @Override
            public int getDefaultColor(@Nullable String tag) {
                return mDynamicThemeDefault.getTextPrimaryColor(false, false);
            }

            @Override
            public @ColorInt int getAutoColor(@Nullable String tag) {
                return mThemePreview.getDynamicTheme()
                        .getTextPrimaryColor(true, false);
            }
        });
        mTextPrimaryPreference.setAltDynamicColorResolver(new DynamicColorResolver() {
            @Override
            public int getDefaultColor(@Nullable String tag) {
                return Dynamic.getTintColor(mThemePreview.getDynamicTheme()
                        .getTextPrimaryColor(), mThemePreview.getDynamicTheme());
            }

            @Override
            public @ColorInt int getAutoColor(@Nullable String tag) {
                return mThemePreview.getDynamicTheme()
                        .getTextPrimaryColorInverse(true, false);
            }
        });

        mTextSecondaryPreference.setDynamicColorResolver(new DynamicColorResolver() {
            @Override
            public int getDefaultColor(@Nullable String tag) {
                return mDynamicThemeDefault.getTextSecondaryColor(false, false);
            }

            @Override
            public @ColorInt int getAutoColor(@Nullable String tag) {
                return mThemePreview.getDynamicTheme()
                        .getTextSecondaryColor(true, false);
            }
        });
        mTextSecondaryPreference.setAltDynamicColorResolver(new DynamicColorResolver() {
            @Override
            public int getDefaultColor(@Nullable String tag) {
                return Dynamic.getTintColor(mThemePreview.getDynamicTheme()
                        .getTextSecondaryColor(), mThemePreview.getDynamicTheme());
            }

            @Override
            public @ColorInt int getAutoColor(@Nullable String tag) {
                return mThemePreview.getDynamicTheme()
                        .getTextSecondaryColorInverse(true, false);
            }
        });

        onLoadTheme(mDynamicTheme);
        onSetAction(Theme.Action.APPLY, mThemePreview, true);

        if (getSavedInstanceState() == null) {
            Dynamic.setBottomSheetState(getActivity(), BottomSheetBehavior.STATE_EXPANDED);
        }

        Dynamic.addHeader(getActivity(), R.layout.ads_header_appbar,
                true, getSavedInstanceState() == null);
    }

    @Override
    public void onAddActivityHeader(@Nullable View view) {
        super.onAddActivityHeader(view);

        if (getContext() == null || view == null) {
            return;
        }

        Dynamic.set(view.findViewById(R.id.ads_header_appbar_icon),
                DynamicPackageUtils.getAppIcon(getContext()));
        Dynamic.set(view.findViewById(R.id.ads_header_appbar_title), getSubtitle());
        Dynamic.set(view.findViewById(R.id.ads_header_appbar_subtitle),
                getString(R.string.ads_theme_customise_desc));

        setSubtitle(DynamicPackageUtils.getAppLabel(getContext()));
    }

    @Override
    public boolean setHasOptionsMenu() {
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();

        updatePreferences();
    }

    /**
     * Retrieves the theme from the supplies arguments
     *
     * @param key The key to be retrieved.
     *
     * @return The dynamic theme from the supplied arguments.
     */
    public @Nullable DynamicAppTheme getThemeFromArguments(@NonNull String key) {
        return DynamicTheme.getInstance().getTheme(getStringFromArguments(key));
    }

    @Override
    public @NonNull DynamicAppTheme onImportTheme(@NonNull String theme) {
        try {
            return new DynamicAppTheme(theme);
        } catch (Exception ignored) {
        }

        return mThemePreview.getDynamicTheme();
    }

    @Override
    public void onLoadTheme(@NonNull DynamicAppTheme theme) {
        if (mSettingsChanged) {
            return;
        }

        mColorBackgroundPreference.setColor(theme.getBackgroundColor(false, false));
        mColorBackgroundPreference.setAltColor(theme.getTintBackgroundColor(false, false));
        mColorSurfacePreference.setColor(theme.getSurfaceColor(false, false));
        mColorSurfacePreference.setAltColor(theme.getTintSurfaceColor(false, false));
        mColorPrimaryPreference.setColor(theme.getPrimaryColor(false, false));
        mColorPrimaryPreference.setAltColor(theme.getTintPrimaryColor(false, false));
        mColorAccentPreference.setColor(theme.getAccentColor(false, false));
        mColorAccentPreference.setAltColor(theme.getTintAccentColor(false, false));
        mColorPrimaryDarkPreference.setColor(theme.getPrimaryColorDark(false, false));
        mColorPrimaryDarkPreference.setAltColor(theme.getTintPrimaryColorDark(false, false));
        mColorAccentDarkPreference.setColor(theme.getAccentColorDark(false, false));
        mColorAccentDarkPreference.setAltColor(theme.getTintAccentColorDark(false, false));
        mColorErrorPreference.setColor(theme.getErrorColor(false, false));
        mColorErrorPreference.setAltColor(theme.getTintErrorColor(false, false));
        mTextPrimaryPreference.setColor(theme.getTextPrimaryColor(false, false));
        mTextPrimaryPreference.setAltColor(theme.getTextPrimaryColorInverse(false, false));
        mTextSecondaryPreference.setColor(theme.getTextSecondaryColor(false, false));
        mTextSecondaryPreference.setAltColor(theme.getTextSecondaryColorInverse(false, false));

        if (theme.getFontScale(false) != Theme.Font.AUTO) {
            mFontScalePreference.setPreferenceValue(Theme.Font.ToString.CUSTOM);
            mFontScalePreference.setValue(theme.getFontScale());
        } else {
            mFontScalePreference.setPreferenceValue(Theme.Font.ToString.AUTO);
            mFontScalePreference.setValue(mDynamicThemeDefault.getFontScale());
        }

        if (theme.getCornerRadius(false) != Theme.Corner.AUTO
                && theme.getCornerRadius(false) != Theme.Corner.SYSTEM) {
            mCornerSizePreference.setPreferenceValue(Theme.Corner.ToString.CUSTOM);
            mCornerSizePreference.setValue(theme.getCornerSize());
        } else {
            if (DynamicSdkUtils.is31()
                    && theme.getCornerRadius(false) == Theme.Corner.SYSTEM) {
                mCornerSizePreference.setPreferenceValue(Theme.Corner.ToString.SYSTEM);
            } else {
                mCornerSizePreference.setPreferenceValue(Theme.Corner.ToString.AUTO);
            }
            mCornerSizePreference.setValue(mDynamicThemeDefault.getCornerSize());
        }

        mBackgroundAwarePreference.setPreferenceValue(
                Integer.toString(theme.getBackgroundAware(false)));
        mElevationPreference.setPreferenceValue(String.valueOf(theme.getElevation(false)));
        mStylePreference.setPreferenceValue(String.valueOf(theme.getStyle()));

        if (theme.getContrast(false) != Theme.Contrast.AUTO
                && theme.getContrast(false) != Theme.Contrast.SYSTEM) {
            mContrastPreference.setPreferenceValue(Theme.Contrast.ToString.CUSTOM);
            mContrastPreference.setValue(theme.getContrast());
        } else {
            if (DynamicSdkUtils.is34()
                    && theme.getContrast(false) == Theme.Contrast.SYSTEM) {
                mContrastPreference.setPreferenceValue(Theme.Contrast.ToString.SYSTEM);
            } else {
                mContrastPreference.setPreferenceValue(Theme.Contrast.ToString.AUTO);
            }
            mContrastPreference.setValue(mDynamicThemeDefault.getContrast());
        }

        if (theme.getOpacity(false) != Theme.Opacity.AUTO) {
            mOpacityPreference.setPreferenceValue(Theme.Opacity.ToString.CUSTOM);
            mOpacityPreference.setValue(theme.getOpacity());
        } else {
            mOpacityPreference.setPreferenceValue(Theme.Opacity.ToString.AUTO);
            mOpacityPreference.setValue(mDynamicThemeDefault.getOpacity());
        }

        updatePreferences();
    }

    @Override
    public void onSetAction(@Theme.Action int themeAction,
            @Nullable ThemePreview<DynamicAppTheme> themePreview, boolean enable) {
        if (themePreview == null) {
            return;
        }

        Dynamic.setResource(themePreview.getActionView(), enable
                ? R.drawable.ads_ic_save : themePreview.getDynamicTheme().isDynamicColor()
                ? R.drawable.adt_ic_app : R.drawable.ads_ic_style);
    }

    @Override
    public int getFontScale() {
        return getPreferenceValue(mFontScalePreference, mDynamicTheme.getFontScale());
    }

    @Override
    public int getCornerSize() {
        return getPreferenceValue(mCornerSizePreference, mDynamicTheme.getCornerSize());
    }

    @Override
    public @Theme.BackgroundAware int getBackgroundAware() {
        return getPreferenceValue(mBackgroundAwarePreference,
                mDynamicTheme.getBackgroundAware(false));
    }

    @Override
    public int getContrast() {
        return getPreferenceValue(mContrastPreference, mDynamicTheme.getContrast());
    }

    @Override
    public int getOpacity() {
        return getPreferenceValue(mOpacityPreference, mDynamicTheme.getOpacity());
    }

    @Override
    public @Theme.Elevation int getElevation() {
        return getPreferenceValue(mElevationPreference, mDynamicTheme.getElevation(false));
    }

    @Override
    public @Theme.Style int getStyle() {
        return getPreferenceValue(mStylePreference, mDynamicTheme.getStyle());
    }

    /**
     * Update the theme preview.
     */
    private void updateThemePreview() {
        mThemePreview.setDynamicTheme(new DynamicAppTheme(mDynamicTheme)
                .setBackgroundColor(mColorBackgroundPreference.getColor(false), false)
                .setTintBackgroundColor(mColorBackgroundPreference.getAltColor(false))
                .setSurfaceColor(mColorSurfacePreference.getColor(false), false)
                .setTintSurfaceColor(mColorSurfacePreference.getAltColor(false))
                .setPrimaryColor(mColorPrimaryPreference.getColor(false), false)
                .setTintPrimaryColor(mColorPrimaryPreference.getAltColor(false))
                .setPrimaryColorDark(mColorPrimaryDarkPreference.getColor(false), false)
                .setTintPrimaryColorDark(mColorPrimaryDarkPreference.getAltColor(false))
                .setAccentColor(mColorAccentPreference.getColor(false), false)
                .setTintAccentColor(mColorAccentPreference.getAltColor(false))
                .setAccentColorDark(mColorAccentDarkPreference.getColor(false), false)
                .setTintAccentColorDark(mColorAccentDarkPreference.getAltColor(false))
                .setErrorColor(mColorErrorPreference.getColor(false), false)
                .setTintErrorColor(mColorErrorPreference.getAltColor(false))
                .setTextPrimaryColor(mTextPrimaryPreference.getColor(false), false)
                .setTextPrimaryColorInverse(mTextPrimaryPreference.getAltColor(false))
                .setTextSecondaryColor(mTextSecondaryPreference.getColor(false), false)
                .setTextSecondaryColorInverse(mTextSecondaryPreference.getAltColor(false))
                .setFontScale(getFontScale())
                .setCornerSize(getCornerSize())
                .setBackgroundAware(getBackgroundAware())
                .setContrast(getContrast())
                .setOpacity(getOpacity())
                .setElevation(getElevation())
                .setStyle(getStyle()));

        mSettingsChanged = true;
    }

    /**
     * Update all the preferences.
     */
    public void updatePreferences() {
        updateThemePreview();

        mColorBackgroundPreference.update();
        mColorSurfacePreference.update();
        mColorPrimaryPreference.update();
        mColorAccentPreference.update();
        mColorPrimaryDarkPreference.update();
        mColorAccentDarkPreference.update();
        mColorErrorPreference.update();
        mTextPrimaryPreference.update();
        mTextSecondaryPreference.update();
        mFontScalePreference.update();
        mCornerSizePreference.update();
        mBackgroundAwarePreference.update();
        mContrastPreference.update();
        mOpacityPreference.update();
        mElevationPreference.update();
        mStylePreference.update();

        mContrastPreference.setEnabled(mThemePreview.getDynamicTheme().isBackgroundAware());
        mFontScalePreference.setSeekEnabled(mThemePreview.getDynamicTheme()
                .getFontScale(false) != Theme.Font.AUTO);
        mCornerSizePreference.setSeekEnabled(mThemePreview.getDynamicTheme()
                .getCornerRadius(false) != Theme.Corner.AUTO
                && (mThemePreview.getDynamicTheme()
                .getCornerRadius(false) != Theme.Corner.SYSTEM));
        mContrastPreference.setSeekEnabled(mThemePreview.getDynamicTheme()
                .getContrast(false) != Theme.Contrast.AUTO
                && (mThemePreview.getDynamicTheme()
                .getContrast(false) != Theme.Contrast.SYSTEM));
        mOpacityPreference.setSeekEnabled(mThemePreview.getDynamicTheme()
                .getOpacity(false) != Theme.Opacity.AUTO);
    }

    @Override
    public boolean isOnSharedPreferenceChangeListener() {
        return true;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
            @Nullable String key) {
        super.onSharedPreferenceChanged(sharedPreferences, key);

        if (DynamicPreferences.isNullKey(key)) {
            return;
        }

        switch (key) {
            case ADS_PREF_THEME_FONT_SCALE:
            case ADS_PREF_THEME_FONT_SCALE_ALT:
            case ADS_PREF_THEME_BACKGROUND_AWARE:
            case ADS_PREF_THEME_STYLE:
                DynamicMotion.getInstance().beginDelayedTransition(mThemePreview);
            case ADS_PREF_THEME_COLOR_BACKGROUND:
            case ADS_PREF_THEME_COLOR_TINT_BACKGROUND:
            case ADS_PREF_THEME_COLOR_SURFACE:
            case ADS_PREF_THEME_COLOR_TINT_SURFACE:
            case ADS_PREF_THEME_COLOR_PRIMARY:
            case ADS_PREF_THEME_COLOR_TINT_PRIMARY:
            case ADS_PREF_THEME_COLOR_PRIMARY_DARK:
            case ADS_PREF_THEME_COLOR_TINT_PRIMARY_DARK:
            case ADS_PREF_THEME_COLOR_ACCENT:
            case ADS_PREF_THEME_COLOR_TINT_ACCENT:
            case ADS_PREF_THEME_COLOR_ACCENT_DARK:
            case ADS_PREF_THEME_COLOR_TINT_ACCENT_DARK:
            case ADS_PREF_THEME_COLOR_ERROR:
            case ADS_PREF_THEME_COLOR_TINT_ERROR:
            case ADS_PREF_THEME_TEXT_PRIMARY:
            case ADS_PREF_THEME_TEXT_INVERSE_PRIMARY:
            case ADS_PREF_THEME_TEXT_SECONDARY:
            case ADS_PREF_THEME_TEXT_INVERSE_SECONDARY:
            case ADS_PREF_THEME_CORNER_SIZE:
            case ADS_PREF_THEME_CORNER_SIZE_ALT:
            case ADS_PREF_THEME_CONTRAST:
            case ADS_PREF_THEME_CONTRAST_ALT:
            case ADS_PREF_THEME_OPACITY:
            case ADS_PREF_THEME_OPACITY_ALT:
            case ADS_PREF_THEME_ELEVATION:
                updatePreferences();
                break;
        }
    }
}
