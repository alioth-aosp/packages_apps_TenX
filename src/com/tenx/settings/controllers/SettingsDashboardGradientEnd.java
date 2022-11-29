/*
 * Copyright (C) 2020-2023 TenX-OS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tenx.settings.controllers;

import android.content.Context;
import android.content.ContentResolver;
import android.os.UserHandle;
import android.provider.Settings;
import android.provider.SearchIndexableResource;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;

import com.android.settings.R;
import com.android.settingslib.core.AbstractPreferenceController;

import com.tenx.support.preferences.SystemSettingListPreference;
import com.tenx.support.colorpicker.ColorPickerPreference;

public class SettingsDashboardGradientEnd extends AbstractPreferenceController implements
        Preference.OnPreferenceChangeListener {

    private static final String GRADIENT_END_COLOR = "settings_dashboard_background_gradient_end_color";
    private static final String SETTINGS_DASHBOARD_STYLES = "settings_dashboard_background_style";
    static final int DEFAULT_COLOR = 0xff1a73e8;

    private ColorPickerPreference mGradientEndColor;
    private SystemSettingListPreference mSettingsDashboardStyles;

    public SettingsDashboardGradientEnd(Context context) {
        super(context);
    }

    @Override
    public String getPreferenceKey() {
        return GRADIENT_END_COLOR;
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public void displayPreference(PreferenceScreen screen) {
        super.displayPreference(screen);
        mGradientEndColor = (ColorPickerPreference) screen.findPreference(GRADIENT_END_COLOR);
        int gradientEndColor = Settings.System.getIntForUser(mContext.getContentResolver(),
                Settings.System.SETTINGS_DASHBOARD_BACKGROUND_GRADIENT_END_COLOR, DEFAULT_COLOR, UserHandle.USER_CURRENT);
        String gradientEndHex = String.format("#%08x", (0xff1a73e8 & gradientEndColor));
        if (gradientEndHex.equals("#ff1a73e8")) {
            mGradientEndColor.setSummary(R.string.default_string);
        } else {
            mGradientEndColor.setSummary(gradientEndHex);
        }
        mGradientEndColor.setNewPreviewColor(gradientEndColor);
        mGradientEndColor.setOnPreferenceChangeListener(this);

        mSettingsDashboardStyles = (SystemSettingListPreference) screen.findPreference(SETTINGS_DASHBOARD_STYLES);
        int settingsDashboardStyles = Settings.System.getIntForUser(mContext.getContentResolver(),
                Settings.System.SETTINGS_DASHBOARD_BACKGROUND_STYLE, 0, UserHandle.USER_CURRENT);
        mSettingsDashboardStyles.setValue(String.valueOf(settingsDashboardStyles));
        mSettingsDashboardStyles.setSummary(mSettingsDashboardStyles.getEntry());
        mSettingsDashboardStyles.setOnPreferenceChangeListener(this);
        updatePreference(settingsDashboardStyles);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mGradientEndColor) {
            String hex = ColorPickerPreference.convertToARGB(
                    Integer.valueOf(String.valueOf(newValue)));
            if (hex.equals("#ff1a73e8")) {
                mGradientEndColor.setSummary(R.string.default_string);
            } else {
                mGradientEndColor.setSummary(hex);
            }
            int intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putIntForUser(mContext.getContentResolver(),
                    Settings.System.SETTINGS_DASHBOARD_BACKGROUND_GRADIENT_END_COLOR, intHex, UserHandle.USER_CURRENT);
            return true;
        } else if (preference == mSettingsDashboardStyles) {
            int settingsDashboardStyles = Integer.valueOf((String) newValue);
            int index = mSettingsDashboardStyles.findIndexOfValue((String) newValue);
            Settings.System.putIntForUser(mContext.getContentResolver(),
                    Settings.System.SETTINGS_DASHBOARD_BACKGROUND_STYLE, settingsDashboardStyles, UserHandle.USER_CURRENT);
            mSettingsDashboardStyles.setSummary(mSettingsDashboardStyles.getEntries()[index]);
            updatePreference(settingsDashboardStyles);
            return true;
        }
        return false;
    }

    private void updatePreference(int settingsDashboardStyles) {
        if (settingsDashboardStyles >= 0 && settingsDashboardStyles <= 5) {
            mGradientEndColor.setEnabled(false);
        } else {
            mGradientEndColor.setEnabled(true);
        }
    }
}
