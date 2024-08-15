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
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.provider.Settings;
import androidx.preference.*;

import com.android.internal.logging.nano.MetricsProto;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

import lineageos.preference.LineagePartsPreference;

public class Notifications extends SettingsPreferenceFragment
        implements Preference.OnPreferenceChangeListener {

    public static final String TAG = "Notifications";

    private static final String KEY_NOTIF_LIGHTS_PREFERENCE_CATEGORY = "notification_light";
    private static final String KEY_NOTIF_LIGHTS = "notification_lights";

    private LineagePartsPreference mNotifLights;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.tenx_settings_notifications);
        PreferenceScreen prefScreen = getPreferenceScreen();
        final Resources res = getResources();

        mNotifLights = (LineagePartsPreference) findPreference(KEY_NOTIF_LIGHTS);
        boolean mNotLightsSupported = res.getBoolean(
                com.android.internal.R.bool.config_intrusiveNotificationLed);

        if (!mNotLightsSupported) {
            final PreferenceCategory lightsCategory =
                    (PreferenceCategory) prefScreen.findPreference(KEY_NOTIF_LIGHTS_PREFERENCE_CATEGORY);
            prefScreen.removePreference(lightsCategory);
        }

        setLayoutToPreference();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object objValue) {
        return false;
    }

    private void setLayoutToPreference() {
        boolean mNotLightsSupported = getActivity().getApplicationContext().getResources().getBoolean(
                com.android.internal.R.bool.config_intrusiveNotificationLed);

        if (mNotLightsSupported) {
            mNotifLights.setLayoutResource(R.layout.tenx_preference);
        }
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.TENX;
    }
}
