/*
 * Copyright (C) 2020 TenX-OS
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
package com.tenx.settings.fragments;

import android.content.ContentResolver;
import android.content.res.Resources;
import android.content.Context;
import android.os.Bundle;
import android.os.UserHandle;
import android.os.Vibrator;
import android.provider.Settings;
import androidx.preference.*;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

import com.android.internal.logging.nano.MetricsProto;

import lineageos.preference.LineagePartsPreference;
import com.tenx.support.preferences.SystemSettingListPreference;
import com.tenx.support.preferences.SystemSettingSwitchPreference;

public class Buttons extends SettingsPreferenceFragment implements
    Preference.OnPreferenceChangeListener {

    private static final String KEY_LINEAGE_BUTTON_SETTINGS = "button_settings";
    private static final String KEY_LINEAGE_POWER_MENU = "power_menu";
    private static final String KEY_VOLUME_TEXT = "VOLUME_TEXTVIEW";
    private static final String KEY_VOLUME_TEXT_TYPE = "VOLUME_TEXTVIEW_STYLE";

    private LineagePartsPreference mLineageButtonSettings;
    private LineagePartsPreference mLineagePowerMenu;
    private SystemSettingSwitchPreference mVolumeText;
    private SystemSettingListPreference mVolumeTextType;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        addPreferencesFromResource(R.xml.tenx_settings_buttons);
        PreferenceScreen prefSet = getPreferenceScreen();
        final Resources res = getResources();
        final PreferenceScreen prefScreen = getPreferenceScreen();

        mLineageButtonSettings = (LineagePartsPreference) findPreference(KEY_LINEAGE_BUTTON_SETTINGS);
        mLineagePowerMenu = (LineagePartsPreference) findPreference(KEY_LINEAGE_POWER_MENU);
        mVolumeText = (SystemSettingSwitchPreference) findPreference(KEY_VOLUME_TEXT);
        mVolumeTextType = (SystemSettingListPreference) findPreference(KEY_VOLUME_TEXT_TYPE);

        setLayoutToPreference();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object objValue) {
        return false;
    }

    private void setLayoutToPreference() {
        mLineageButtonSettings.setLayoutResource(R.layout.tenx_preference_top);
        mLineagePowerMenu.setLayoutResource(R.layout.tenx_preference_bottom);
        mVolumeText.setLayoutResource(R.layout.tenx_preference_top);
        mVolumeTextType.setLayoutResource(R.layout.tenx_preference_bottom);
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.TENX;
    }
}
