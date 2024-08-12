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

import android.app.Activity;
import android.content.Context;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.hardware.fingerprint.FingerprintManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import androidx.preference.*;

import com.android.internal.logging.nano.MetricsProto;
import com.android.internal.util.tenx.OmniJawsClient;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

import com.tenx.support.preferences.SystemSettingSwitchPreference;

public class LockScreen extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    private static final String KEY_DOUBLE_TAP_TO_SLEEP = "double_tap_sleep_lockscreen";
    private static final String KEY_WEATHER_PREFERENCE = "weather_preference";
    private static final String KEY_WEATHER_ENABLED = "lockscreen_weather_enabled";
    private static final String KEY_WEATHER_LOCATION = "lockscreen_weather_location";
    private static final String KEY_WEATHER_TEXT = "lockscreen_weather_text";

    private SystemSettingSwitchPreference mDoubleTapToSleep;
    private Preference mWeatherPreference;
    private SystemSettingSwitchPreference mWeatherEnabled;
    private SystemSettingSwitchPreference mWeatherLocation;
    private SystemSettingSwitchPreference mWeatherText;

    private OmniJawsClient mWeatherClient;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        addPreferencesFromResource(R.xml.tenx_settings_lockscreen);
        final PreferenceScreen prefScreen = getPreferenceScreen();
        Resources resources = getResources();

        mDoubleTapToSleep = (SystemSettingSwitchPreference) findPreference(KEY_DOUBLE_TAP_TO_SLEEP);
        mWeatherPreference = (Preference) findPreference(KEY_WEATHER_PREFERENCE);
        mWeatherEnabled = (SystemSettingSwitchPreference) findPreference(KEY_WEATHER_ENABLED);
        mWeatherLocation = (SystemSettingSwitchPreference) findPreference(KEY_WEATHER_LOCATION);
        mWeatherText = (SystemSettingSwitchPreference) findPreference(KEY_WEATHER_TEXT);

        mWeatherClient = new OmniJawsClient(getContext());
        updateWeatherSettings();

        setLayoutToPreference();
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        return false;
    }

    private void setLayoutToPreference() {
        mDoubleTapToSleep.setLayoutResource(R.layout.tenx_preference);
        mWeatherPreference.setLayoutResource(R.layout.tenx_preference_top);
        mWeatherEnabled.setLayoutResource(R.layout.tenx_preference_middle);
        mWeatherLocation.setLayoutResource(R.layout.tenx_preference_middle);
        mWeatherText.setLayoutResource(R.layout.tenx_preference_bottom);
    }

    private void updateWeatherSettings() {
        if (mWeatherClient == null || mWeatherEnabled == null) return;

        boolean weatherEnabled = mWeatherClient.isOmniJawsEnabled();
        mWeatherEnabled.setEnabled(weatherEnabled);
        mWeatherEnabled.setSummary(weatherEnabled ? R.string.lockscreen_weather_summary :
            R.string.lockscreen_weather_enabled_info);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateWeatherSettings();
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.TENX;
    }
}
