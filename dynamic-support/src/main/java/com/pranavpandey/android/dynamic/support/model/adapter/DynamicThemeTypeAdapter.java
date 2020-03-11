/*
 * Copyright 2018-2020 Pranav Pandey
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

package com.pranavpandey.android.dynamic.support.model.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.pranavpandey.android.dynamic.support.model.DynamicWidgetTheme;
import com.pranavpandey.android.dynamic.theme.AppTheme;
import com.pranavpandey.android.dynamic.theme.AppWidgetTheme;
import com.pranavpandey.android.dynamic.theme.Theme;
import com.pranavpandey.android.dynamic.theme.utils.DynamicThemeUtils;

import java.io.IOException;

/**
 * Gson type adapter to export or import the dynamic theme.
 *
 * @see AppTheme
 * @see AppWidgetTheme
 */
public class DynamicThemeTypeAdapter<T extends AppTheme> extends TypeAdapter<T> {

    @Override
    public void write(JsonWriter writer, T value) throws IOException {
        try {
            writer.beginObject();

            writer.name(Theme.Key.BACKGROUND);
            writer.value(DynamicThemeUtils.getValueFromColor(
                    value.getBackgroundColor(false)));
            writer.name(Theme.Key.TINT_BACKGROUND);
            writer.value(DynamicThemeUtils.getValueFromColor(
                    value.getTintBackgroundColor(false)));
            writer.name(Theme.Key.SURFACE);
            writer.value(DynamicThemeUtils.getValueFromColor(
                    value.getSurfaceColor(false)));
            writer.name(Theme.Key.TINT_SURFACE);
            writer.value(DynamicThemeUtils.getValueFromColor(
                    value.getTintSurfaceColor(false)));
            writer.name(Theme.Key.PRIMARY);
            writer.value(DynamicThemeUtils.getValueFromColor(
                    value.getPrimaryColor(false)));
            writer.name(Theme.Key.TINT_PRIMARY);
            writer.value(DynamicThemeUtils.getValueFromColor(
                    value.getTintPrimaryColor(false)));
            writer.name(Theme.Key.PRIMARY_DARK);
            writer.value(DynamicThemeUtils.getValueFromColor(
                    value.getPrimaryColorDark(false)));
            writer.name(Theme.Key.TINT_PRIMARY_DARK);
            writer.value(DynamicThemeUtils.getValueFromColor(
                    value.getTintPrimaryColorDark(false)));
            writer.name(Theme.Key.ACCENT);
            writer.value(DynamicThemeUtils.getValueFromColor(
                    value.getAccentColor(false)));
            writer.name(Theme.Key.TINT_ACCENT);
            writer.value(DynamicThemeUtils.getValueFromColor(
                    value.getTintAccentColor(false)));
            writer.name(Theme.Key.ACCENT_DARK);
            writer.value(DynamicThemeUtils.getValueFromColor(
                    value.getAccentColorDark(false)));
            writer.name(Theme.Key.TINT_ACCENT_DARK);
            writer.value(DynamicThemeUtils.getValueFromColor(
                    value.getTintAccentColorDark(false)));
            writer.name(Theme.Key.TEXT_PRIMARY);
            writer.value(DynamicThemeUtils.getValueFromColor(
                    value.getTextPrimaryColor(false)));
            writer.name(Theme.Key.TEXT_PRIMARY_INVERSE);
            writer.value(DynamicThemeUtils.getValueFromColor(
                    value.getTextPrimaryColorInverse(false)));
            writer.name(Theme.Key.TEXT_SECONDARY);
            writer.value(DynamicThemeUtils.getValueFromColor(
                    value.getTextSecondaryColor(false)));
            writer.name(Theme.Key.TEXT_SECONDARY_INVERSE);
            writer.value(DynamicThemeUtils.getValueFromColor(
                    value.getTextSecondaryColorInverse(false)));
            writer.name(Theme.Key.FONT_SCALE);
            writer.value(DynamicThemeUtils.getValueFromFontScale(
                    value.getFontScale(false)));
            writer.name(Theme.Key.CORNER_RADIUS);
            writer.value(DynamicThemeUtils.getValueFromCornerRadius(
                    value.getCornerRadius(false)));
            writer.name(Theme.Key.BACKGROUND_AWARE);
            writer.value(DynamicThemeUtils.getValueFromBackgroundAware(
                    value.getBackgroundAware(false)));

            if (value instanceof AppWidgetTheme) {
                writer.name(Theme.Key.HEADER);
                writer.value(DynamicThemeUtils.getValueFromVisibility(
                        ((AppWidgetTheme) value).getHeader()));
                writer.name(Theme.Key.OPACITY);
                writer.value(((AppWidgetTheme) value).getOpacity());
            }

            writer.endObject();
        } catch (Exception ignored) {
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public T read(JsonReader reader) throws IOException {
        T theme = (T) new DynamicWidgetTheme();

        try {
            JsonToken token = reader.peek();
            if (token.equals(JsonToken.BEGIN_OBJECT)) {
                reader.beginObject();

                while (!reader.peek().equals(JsonToken.END_OBJECT)) {
                    if (reader.peek().equals(JsonToken.NAME)) {
                        switch (reader.nextName()) {
                            default:
                                reader.skipValue();
                                break;
                            case Theme.Key.BACKGROUND:
                            case Theme.Key.Short.BACKGROUND:
                                theme.setBackgroundColor(DynamicThemeUtils
                                        .getValueFromColor(reader.nextString()), false);
                                break;
                            case Theme.Key.TINT_BACKGROUND:
                            case Theme.Key.Short.TINT_BACKGROUND:
                                theme.setTintBackgroundColor(DynamicThemeUtils
                                        .getValueFromColor(reader.nextString()));
                                break;
                            case Theme.Key.SURFACE:
                            case Theme.Key.Short.SURFACE:
                                theme.setSurfaceColor(DynamicThemeUtils
                                        .getValueFromColor(reader.nextString()), false);
                                break;
                            case Theme.Key.TINT_SURFACE:
                            case Theme.Key.Short.TINT_SURFACE:
                                theme.setTintSurfaceColor(DynamicThemeUtils
                                        .getValueFromColor(reader.nextString()));
                                break;
                            case Theme.Key.PRIMARY:
                            case Theme.Key.Short.PRIMARY:
                                theme.setPrimaryColor(DynamicThemeUtils.getValueFromColor(
                                        reader.nextString()), false);
                                break;
                            case Theme.Key.TINT_PRIMARY:
                            case Theme.Key.Short.TINT_PRIMARY:
                                theme.setTintPrimaryColor(DynamicThemeUtils
                                        .getValueFromColor(reader.nextString()));
                                break;
                            case Theme.Key.PRIMARY_DARK:
                            case Theme.Key.Short.PRIMARY_DARK:
                                theme.setPrimaryColorDark(DynamicThemeUtils
                                        .getValueFromColor(reader.nextString()), false);
                                break;
                            case Theme.Key.TINT_PRIMARY_DARK:
                            case Theme.Key.Short.TINT_PRIMARY_DARK:
                                theme.setTintPrimaryColorDark(DynamicThemeUtils
                                        .getValueFromColor(reader.nextString()));
                                break;
                            case Theme.Key.ACCENT:
                            case Theme.Key.Short.ACCENT:
                                theme.setAccentColor(DynamicThemeUtils
                                        .getValueFromColor(reader.nextString()), false);
                                break;
                            case Theme.Key.TINT_ACCENT:
                            case Theme.Key.Short.TINT_ACCENT:
                                theme.setTintAccentColor(DynamicThemeUtils
                                        .getValueFromColor(reader.nextString()));
                                break;
                            case Theme.Key.ACCENT_DARK:
                            case Theme.Key.Short.ACCENT_DARK:
                                theme.setAccentColorDark(DynamicThemeUtils
                                        .getValueFromColor(reader.nextString()), false);
                                break;
                            case Theme.Key.TINT_ACCENT_DARK:
                            case Theme.Key.Short.TINT_ACCENT_DARK:
                                theme.setTintAccentColorDark(DynamicThemeUtils
                                        .getValueFromColor(reader.nextString()));
                                break;
                            case Theme.Key.TEXT_PRIMARY:
                            case Theme.Key.Short.TEXT_PRIMARY:
                                theme.setTextPrimaryColor(DynamicThemeUtils
                                        .getValueFromColor(reader.nextString()), false);
                                break;
                            case Theme.Key.TEXT_PRIMARY_INVERSE:
                            case Theme.Key.Short.TEXT_PRIMARY_INVERSE:
                                theme.setTextPrimaryColorInverse(DynamicThemeUtils
                                        .getValueFromColor(reader.nextString()));
                                break;
                            case Theme.Key.TEXT_SECONDARY:
                            case Theme.Key.Short.TEXT_SECONDARY:
                                theme.setTextSecondaryColor(DynamicThemeUtils
                                        .getValueFromColor(reader.nextString()), false);
                                break;
                            case Theme.Key.TEXT_SECONDARY_INVERSE:
                            case Theme.Key.Short.TEXT_SECONDARY_INVERSE:
                                theme.setTextSecondaryColorInverse(DynamicThemeUtils
                                        .getValueFromColor(reader.nextString()));
                                break;
                            case Theme.Key.FONT_SCALE:
                            case Theme.Key.Short.FONT_SCALE:
                                theme.setFontScale(DynamicThemeUtils
                                        .getValueFromFontScale(reader.nextString()));
                                break;
                            case Theme.Key.CORNER_RADIUS:
                            case Theme.Key.Short.CORNER_RADIUS:
                                theme.setCornerRadiusDp(DynamicThemeUtils
                                        .getValueFromCornerRadius(reader.nextString()));
                                break;
                            case Theme.Key.BACKGROUND_AWARE:
                            case Theme.Key.Short.BACKGROUND_AWARE:
                                theme.setBackgroundAware(DynamicThemeUtils
                                        .getValueFromBackgroundAware(reader.nextString()));
                                break;
                            case Theme.Key.HEADER:
                            case Theme.Key.Short.HEADER:
                                ((AppWidgetTheme) theme).setHeader(DynamicThemeUtils
                                        .getValueFromVisibility(reader.nextString()));
                                break;
                            case Theme.Key.OPACITY:
                            case Theme.Key.Short.OPACITY:
                                ((AppWidgetTheme) theme).setOpacity(reader.nextInt());
                                break;
                        }
                    } else {
                        reader.skipValue();
                    }
                }

                reader.endObject();
            }
        } catch (Exception ignored) {
            theme = null;
        }

        return theme;
    }
}
