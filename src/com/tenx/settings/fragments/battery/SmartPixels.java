/*
 * Copyright (C) 2019-2024 TenX-OS
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

package com.tenx.settings.fragments.battery;

import android.content.Context;
import android.content.ContentResolver;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.UserHandle;
import android.provider.Settings;

import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.preference.Preference.OnPreferenceChangeListener;
import androidx.preference.SwitchPreference;

import com.android.internal.logging.nano.MetricsProto;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

import com.tenx.support.preferences.SystemSettingSwitchPreference;
import com.tenx.support.preferences.SystemSettingListPreference;

public class SmartPixels extends SettingsPreferenceFragment {

    private static final String TAG = "SmartPixels";

    private static final String KEY_ENABLE = "smart_pixels_enable";
    private static final String KEY_PATTERN = "smart_pixels_pattern";
    private static final String KEY_POWER_SAVE = "smart_pixels_on_power_save";
    private static final String KEY_TIMEOUT = "smart_pixels_shift_timeout";
    private static final String SMART_PIXELS_FOOTER = "smart_pixels_footer";

    private SystemSettingSwitchPreference mEnable;
    private SystemSettingListPreference mPattern;
    private SystemSettingSwitchPreference mSave;
    private SystemSettingListPreference mTimeout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.smart_pixels);

        findPreference(SMART_PIXELS_FOOTER).setTitle(R.string.smart_pixels_warning_text);

        mEnable = (SystemSettingSwitchPreference) findPreference(KEY_ENABLE);
        mPattern = (SystemSettingListPreference) findPreference(KEY_PATTERN);
        mSave = (SystemSettingSwitchPreference) findPreference(KEY_POWER_SAVE);
        mTimeout = (SystemSettingListPreference) findPreference(KEY_TIMEOUT);

        setLayoutToPreference();
    }

    private void setLayoutToPreference() {
        mEnable.setLayoutResource(R.layout.tenx_preference_top);
        mPattern.setLayoutResource(R.layout.tenx_preference_middle);
        mSave.setLayoutResource(R.layout.tenx_preference_middle);
        mTimeout.setLayoutResource(R.layout.tenx_preference_bottom);
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.TENX;
    }
}
