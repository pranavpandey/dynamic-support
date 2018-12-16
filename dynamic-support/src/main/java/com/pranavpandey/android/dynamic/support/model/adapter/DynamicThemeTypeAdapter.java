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

package com.pranavpandey.android.dynamic.support.model.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.pranavpandey.android.dynamic.support.model.DynamicAppTheme;
import com.pranavpandey.android.dynamic.support.model.DynamicWidgetTheme;
import com.pranavpandey.android.dynamic.support.utils.DynamicThemeUtils;

import java.io.IOException;

/**
 * Gson type adapter to export or import the dynamic theme.
 *
 * @see DynamicAppTheme
 * @see DynamicWidgetTheme
 */
public class DynamicThemeTypeAdapter<T extends DynamicAppTheme> extends TypeAdapter<T> {

    @Override
    public void write(JsonWriter writer, T value) throws IOException {
        try {
            writer.beginObject();

            writer.name(DynamicThemeUtils.ADS_NAME_BACKGROUND_COLOR);
            writer.value(DynamicThemeUtils.getValueFromColor(
                    value.getBackgroundColor(false)));
            writer.name(DynamicThemeUtils.ADS_NAME_TINT_BACKGROUND_COLOR);
            writer.value(DynamicThemeUtils.getValueFromColor(
                    value.getTintBackgroundColor(false)));
            writer.name(DynamicThemeUtils.ADS_NAME_PRIMARY_COLOR);
            writer.value(DynamicThemeUtils.getValueFromColor(
                    value.getPrimaryColor(false)));
            writer.name(DynamicThemeUtils.ADS_NAME_TINT_PRIMARY_COLOR);
            writer.value(DynamicThemeUtils.getValueFromColor(
                    value.getTintPrimaryColor(false)));
            writer.name(DynamicThemeUtils.ADS_NAME_PRIMARY_COLOR_DARK);
            writer.value(DynamicThemeUtils.getValueFromColor(
                    value.getPrimaryColorDark(false)));
            writer.name(DynamicThemeUtils.ADS_NAME_TINT_PRIMARY_COLOR_DARK);
            writer.value(DynamicThemeUtils.getValueFromColor(
                    value.getTintPrimaryColorDark(false)));
            writer.name(DynamicThemeUtils.ADS_NAME_ACCENT_COLOR);
            writer.value(DynamicThemeUtils.getValueFromColor(
                    value.getAccentColor(false)));
            writer.name(DynamicThemeUtils.ADS_NAME_TINT_ACCENT_COLOR);
            writer.value(DynamicThemeUtils.getValueFromColor(
                    value.getTintAccentColor(false)));
            writer.name(DynamicThemeUtils.ADS_NAME_ACCENT_COLOR_DARK);
            writer.value(DynamicThemeUtils.getValueFromColor(
                    value.getAccentColorDark(false)));
            writer.name(DynamicThemeUtils.ADS_NAME_TINT_ACCENT_COLOR_DARK);
            writer.value(DynamicThemeUtils.getValueFromColor(
                    value.getTintAccentColorDark(false)));
            writer.name(DynamicThemeUtils.ADS_NAME_TEXT_PRIMARY_COLOR);
            writer.value(DynamicThemeUtils.getValueFromColor(
                    value.getTextPrimaryColor(false)));
            writer.name(DynamicThemeUtils.ADS_NAME_TEXT_PRIMARY_COLOR_INVERSE);
            writer.value(DynamicThemeUtils.getValueFromColor(
                    value.getTextPrimaryColorInverse(false)));
            writer.name(DynamicThemeUtils.ADS_NAME_TEXT_SECONDARY_COLOR);
            writer.value(DynamicThemeUtils.getValueFromColor(
                    value.getTextSecondaryColor(false)));
            writer.name(DynamicThemeUtils.ADS_NAME_TEXT_SECONDARY_COLOR_INVERSE);
            writer.value(DynamicThemeUtils.getValueFromColor(
                    value.getTextSecondaryColorInverse(false)));
            writer.name(DynamicThemeUtils.ADS_NAME_CORNER_RADIUS);
            writer.value(DynamicThemeUtils.getValueFromCornerRadius(
                    value.getCornerRadius(false)));
            writer.name(DynamicThemeUtils.ADS_NAME_BACKGROUND_AWARE);
            writer.value(DynamicThemeUtils.getValueFromBackgroundAware(
                    value.getBackgroundAware(false)));

            if (value instanceof DynamicWidgetTheme) {
                writer.name(DynamicThemeUtils.ADS_NAME_HEADER);
                writer.value(DynamicThemeUtils.getValueFromVisibility(
                        ((DynamicWidgetTheme) value).getHeader()));
                writer.name(DynamicThemeUtils.ADS_NAME_OPACITY);
                writer.value(((DynamicWidgetTheme) value).getOpacity());
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
                            case DynamicThemeUtils.ADS_NAME_BACKGROUND_COLOR:
                                theme.setBackgroundColor(DynamicThemeUtils
                                        .getValueFromColor(reader.nextString()), false);
                                break;
                            case DynamicThemeUtils.ADS_NAME_TINT_BACKGROUND_COLOR:
                                theme.setTintBackgroundColor(DynamicThemeUtils
                                        .getValueFromColor(reader.nextString()));
                                break;
                            case DynamicThemeUtils.ADS_NAME_PRIMARY_COLOR:
                                theme.setPrimaryColor(DynamicThemeUtils.getValueFromColor(
                                        reader.nextString()), false);
                                break;
                            case DynamicThemeUtils.ADS_NAME_TINT_PRIMARY_COLOR:
                                theme.setTintPrimaryColor(DynamicThemeUtils
                                        .getValueFromColor(reader.nextString()));
                                break;
                            case DynamicThemeUtils.ADS_NAME_PRIMARY_COLOR_DARK:
                                theme.setPrimaryColorDark(DynamicThemeUtils
                                        .getValueFromColor(reader.nextString()), false);
                                break;
                            case DynamicThemeUtils.ADS_NAME_TINT_PRIMARY_COLOR_DARK:
                                theme.setTintPrimaryColorDark(DynamicThemeUtils
                                        .getValueFromColor(reader.nextString()));
                                break;
                            case DynamicThemeUtils.ADS_NAME_ACCENT_COLOR:
                                theme.setAccentColor(DynamicThemeUtils
                                        .getValueFromColor(reader.nextString()), false);
                                break;
                            case DynamicThemeUtils.ADS_NAME_TINT_ACCENT_COLOR:
                                theme.setTintAccentColor(DynamicThemeUtils
                                        .getValueFromColor(reader.nextString()));
                                break;
                            case DynamicThemeUtils.ADS_NAME_ACCENT_COLOR_DARK:
                                theme.setAccentColorDark(DynamicThemeUtils
                                        .getValueFromColor(reader.nextString()), false);
                                break;
                            case DynamicThemeUtils.ADS_NAME_TINT_ACCENT_COLOR_DARK:
                                theme.setTintAccentColorDark(DynamicThemeUtils
                                        .getValueFromColor(reader.nextString()));
                                break;
                            case DynamicThemeUtils.ADS_NAME_TEXT_PRIMARY_COLOR:
                                theme.setTextPrimaryColor(DynamicThemeUtils
                                        .getValueFromColor(reader.nextString()), false);
                                break;
                            case DynamicThemeUtils.ADS_NAME_TEXT_PRIMARY_COLOR_INVERSE:
                                theme.setTextPrimaryColorInverse(DynamicThemeUtils
                                        .getValueFromColor(reader.nextString()));
                                break;
                            case DynamicThemeUtils.ADS_NAME_TEXT_SECONDARY_COLOR:
                                theme.setTextSecondaryColor(DynamicThemeUtils
                                        .getValueFromColor(reader.nextString()), false);
                                break;
                            case DynamicThemeUtils.ADS_NAME_TEXT_SECONDARY_COLOR_INVERSE:
                                theme.setTextSecondaryColorInverse(DynamicThemeUtils
                                        .getValueFromColor(reader.nextString()));
                                break;
                            case DynamicThemeUtils.ADS_NAME_CORNER_RADIUS:
                                theme.setCornerRadiusDp(DynamicThemeUtils
                                        .getValueFromCornerRadius(reader.nextString()));
                                break;
                            case DynamicThemeUtils.ADS_NAME_BACKGROUND_AWARE:
                                theme.setBackgroundAware(DynamicThemeUtils
                                        .getValueFromBackgroundAware(reader.nextString()));
                                break;
                            case DynamicThemeUtils.ADS_NAME_HEADER:
                                ((DynamicWidgetTheme) theme).setHeader(DynamicThemeUtils
                                        .getValueFromVisibility(reader.nextString()));
                                break;
                            case DynamicThemeUtils.ADS_NAME_OPACITY:
                                ((DynamicWidgetTheme) theme).setOpacity(reader.nextInt());
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
