/*
 * Copyright (C) 2019-2024 TenX-OS
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
package com.tenx.settings.fragments.statusbar;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.UserHandle;
import android.provider.Settings;

import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.preference.Preference.OnPreferenceChangeListener;
import androidx.preference.SwitchPreferenceCompat;

import com.android.internal.logging.nano.MetricsProto;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

import com.tenx.support.colorpicker.ColorPickerPreference;
import com.tenx.support.preferences.CustomSeekBarPreference;
import com.tenx.support.preferences.SystemSettingSeekBarPreference;
import com.tenx.support.preferences.SystemSettingListPreference;
import com.tenx.support.preferences.SystemSettingSwitchPreference;
import com.tenx.support.colorpicker.ColorPickerSystemPreference;

public class BatteryBar extends SettingsPreferenceFragment
            implements Preference.OnPreferenceChangeListener  {

    private static final String PREF_BATT_BAR = "statusbar_battery_bar";
    private static final String KEY_THICKNESS = "statusbar_battery_bar_thickness";
    private static final String KEY_STYLE = "statusbar_battery_bar_style";
    private static final String KEY_BLEND = "statusbar_battery_bar_blend_color";
    private static final String KEY_BLEND_REVERSE = "statusbar_battery_bar_blend_color_reverse";
    private static final String KEY_COLOR = "statusbar_battery_bar_color";
    private static final String KEY_LOW_COLOR_WARNING = "statusbar_battery_bar_battery_low_color_warning";
    private static final String KEY_ANIMATE = "statusbar_battery_bar_animate";
    private static final String KEY_ANIMATE_COLOR = "statusbar_battery_bar_animate_color";
    private static final String KEY_CHARGING_COLOR_ENABLE = "statusbar_battery_bar_enable_charging_color";
    private static final String KEY_CHARGING_COLOR = "statusbar_battery_bar_charging_color";
    private static final String KEY_GRADIENT_COLOR = "statusbar_battery_bar_use_gradient_color";
    private static final String KEY_LOW_COLOR = "statusbar_battery_bar_low_color";
    private static final String KEY_HIGH_COLOR = "statusbar_battery_bar_high_color";

    private SwitchPreferenceCompat mBatteryBar;
    private SystemSettingSeekBarPreference mThickness;
    private SystemSettingListPreference mStyle;
    private SystemSettingSwitchPreference mBlendColor;
    private SystemSettingSwitchPreference mBlendColorReverse;
    private ColorPickerSystemPreference mColor;
    private ColorPickerSystemPreference mLowWarningColor;
    private SystemSettingSwitchPreference mAnimate;
    private ColorPickerSystemPreference mAnimateColor;
    private SystemSettingSwitchPreference mChargingColorEnable;
    private ColorPickerSystemPreference mChargingColor;
    private SystemSettingSwitchPreference mGradientColor;
    private ColorPickerSystemPreference mLowColor;
    private ColorPickerSystemPreference mHighColor;

    private boolean mIsBarSwitchingMode = false;
    private Handler mHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.battery_bar);

        PreferenceScreen prefSet = getPreferenceScreen();
        ContentResolver resolver = getActivity().getContentResolver();

        int intColor;
        String hexColor;

        mBatteryBar = (SwitchPreferenceCompat) findPreference(PREF_BATT_BAR);
        mThickness = (SystemSettingSeekBarPreference) findPreference(KEY_THICKNESS);
        mStyle = (SystemSettingListPreference) findPreference(KEY_STYLE);
        mBlendColor = (SystemSettingSwitchPreference) findPreference(KEY_BLEND);
        mBlendColorReverse = (SystemSettingSwitchPreference) findPreference(KEY_BLEND_REVERSE);
        mColor = (ColorPickerSystemPreference) findPreference(KEY_COLOR);
        mLowWarningColor = (ColorPickerSystemPreference) findPreference(KEY_LOW_COLOR_WARNING);
        mAnimate = (SystemSettingSwitchPreference) findPreference(KEY_ANIMATE);
        mAnimateColor = (ColorPickerSystemPreference) findPreference(KEY_ANIMATE_COLOR);
        mChargingColorEnable = (SystemSettingSwitchPreference) findPreference(KEY_CHARGING_COLOR_ENABLE);
        mChargingColor = (ColorPickerSystemPreference) findPreference(KEY_CHARGING_COLOR);
        mGradientColor = (SystemSettingSwitchPreference) findPreference(KEY_GRADIENT_COLOR);
        mLowColor = (ColorPickerSystemPreference) findPreference(KEY_LOW_COLOR);
        mHighColor = (ColorPickerSystemPreference) findPreference(KEY_HIGH_COLOR);

        mHandler = new Handler();

        boolean showing = Settings.System.getIntForUser(resolver,
                Settings.System.STATUSBAR_BATTERY_BAR, 0, UserHandle.USER_CURRENT) != 0;
        mBatteryBar.setChecked(showing);
        mBatteryBar.setOnPreferenceChangeListener(this);

        setLayoutToPreference();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        ContentResolver resolver = getActivity().getContentResolver();
        if (preference == mBatteryBar) {
            if (mIsBarSwitchingMode) {
                return false;
            }
            mIsBarSwitchingMode = true;
            boolean value = ((Boolean)newValue);
            Settings.System.putIntForUser(resolver, Settings.System.STATUSBAR_BATTERY_BAR,
                    value ? 1 : 0, UserHandle.USER_CURRENT);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mIsBarSwitchingMode = false;
                    boolean showing = Settings.System.getIntForUser(resolver,
                            Settings.System.STATUSBAR_BATTERY_BAR, 0, UserHandle.USER_CURRENT) != 0;
                    mBatteryBar.setChecked(showing);
                }
            }, 1500);
            return true;
        }
        return false;
    }

    private void setLayoutToPreference() {
        mBatteryBar.setLayoutResource(R.layout.tenx_preference_top);
        mThickness.setLayoutResource(R.layout.tenx_preference_seekbar_middle);
        mStyle.setLayoutResource(R.layout.tenx_preference_middle);
        mBlendColor.setLayoutResource(R.layout.tenx_preference_middle);
        mBlendColorReverse.setLayoutResource(R.layout.tenx_preference_middle);
        mColor.setLayoutResource(R.layout.tenx_preference_colorpicker_middle);
        mLowWarningColor.setLayoutResource(R.layout.tenx_preference_colorpicker_middle);
        mAnimate.setLayoutResource(R.layout.tenx_preference_middle);
        mAnimateColor.setLayoutResource(R.layout.tenx_preference_colorpicker_middle);
        mChargingColorEnable.setLayoutResource(R.layout.tenx_preference_middle);
        mChargingColor.setLayoutResource(R.layout.tenx_preference_colorpicker_middle);
        mGradientColor.setLayoutResource(R.layout.tenx_preference_middle);
        mLowColor.setLayoutResource(R.layout.tenx_preference_colorpicker_middle);
        mHighColor.setLayoutResource(R.layout.tenx_preference_colorpicker_bottom);
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.TENX;
    }
}
