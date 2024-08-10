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

import android.content.Context;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.UserHandle;
import android.provider.Settings;
import android.view.View;

import androidx.preference.*;

import com.android.internal.logging.nano.MetricsProto;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

import lineageos.preference.LineageSystemSettingListPreference;
import com.tenx.support.preferences.SecureSettingSwitchPreference;
import com.tenx.support.preferences.SystemSettingListPreference;
import com.tenx.support.preferences.SystemSettingSwitchPreference;
import com.tenx.support.preferences.SystemSettingSeekBarPreference;

public class StatusBar extends SettingsPreferenceFragment implements
    Preference.OnPreferenceChangeListener {

    private static final String KEY_STATUS_BAR_ICONS = "status_bar_icons";
    private static final String KEY_NETWORK_TRAFFIC = "network_traffic_settings";
    private static final String KEY_BATTERY_BAR = "batterybar";
    private static final String KEY_DATA_DISABLED_ICON = "data_disabled_icon";
    private static final String KEY_FOUR_G_ICON = "show_fourg_icon";
    private static final String KEY_WIFI_STANDARD_ICON = "wifi_standard_icon";
    private static final String KEY_BATTERY_STYLE = "status_bar_battery_style";
    private static final String KEY_BATTERY_PERCENT = "status_bar_show_battery_percent";
    private static final String KEY_BATTERY_TEXT_CHARGING = "status_bar_battery_text_charging";
    private static final String KEY_CLOCK = "clock";
    private static final String KEY_LOGO = "status_bar_logo";
    private static final String KEY_LOGO_POSITION = "status_bar_logo_position";
    private static final String KEY_LOGO_STYLE = "status_bar_logo_style";
    private static final String KEY_CAMERA_PRIVACY = "enable_camera_privacy_indicator";
    private static final String KEY_LOCATION_PRIVACY = "enable_location_privacy_indicator";
    private static final String KEY_PROJECTION_PRIVACY = "enable_projection_privacy_indicator";
    private static final String KEY_COLORED_ICONS = "statusbar_colored_icons";
    private static final String KEY_NOTIF_COUNT = "statusbar_notif_count";
    private static final String KEY_BT_BATTERY = "bluetooth_show_battery";
    private static final String KEY_BRIGHTNESS_CONTROL = "status_bar_brightness_control";
    private static final String KEY_QUICK_PULLDOWN = "qs_quick_pulldown";
    private static final String KEY_DUAL_STATUS_BAR = "use_dual_statusbar_mod";
    private static final String KEY_USE_START_PADDING = "use_custom_statusbar_padding_start";
    private static final String KEY_START_PADDING = "custom_statusbar_padding_start";
    private static final String KEY_USE_END_PADDING = "use_custom_statusbar_padding_end";
    private static final String KEY_END_PADDING = "custom_statusbar_padding_end";
    private static final String KEY_USE_HEIGHT = "use_custom_statusbar_height";
    private static final String KEY_HEIGHT = "custom_statusbar_height";

    private static final int BATTERY_STYLE_PORTRAIT = 0;
    private static final int BATTERY_STYLE_TEXT = 4;
    private static final int BATTERY_STYLE_HIDDEN = 5;
    private static final int PULLDOWN_DIR_NONE = 0;
    private static final int PULLDOWN_DIR_RIGHT = 1;
    private static final int PULLDOWN_DIR_LEFT = 2;
    private static final int PULLDOWN_DIR_BOTH = 3;

    private Preference mStatusbarIcons;
    private Preference mNetworkTraffic;
    private Preference mBatteryBar;
    private SystemSettingSwitchPreference mDataDisabledIcon;
    private SystemSettingSwitchPreference mFourGIcon;
    private SystemSettingSwitchPreference mWiFiStandardIcon;
    private SystemSettingListPreference mBatteryPercent;
    private SystemSettingListPreference mBatteryStyle;
    private SystemSettingSwitchPreference mBatteryTextCharging;
    private Preference mClock;
    private SystemSettingSwitchPreference mLogo;
    private SystemSettingListPreference mLogoPosition;
    private SystemSettingListPreference mLogoStyle;
    private SecureSettingSwitchPreference mCameraPrivacy;
    private SecureSettingSwitchPreference mLocationPrivacy;
    private SecureSettingSwitchPreference mProjectionPrivacy;
    private SystemSettingSwitchPreference mColoredIcons;
    private SystemSettingSwitchPreference mNotifCount;
    private SystemSettingSwitchPreference mBtBattery;
    private SystemSettingSwitchPreference mBrightnessControl;
    private LineageSystemSettingListPreference mQuickPulldown;

    private SystemSettingSwitchPreference mDualStatusBar;
    private SystemSettingSwitchPreference mUseStartPadding;
    private SystemSettingSeekBarPreference mStartPadding;
    private SystemSettingSwitchPreference mUseEndPadding;
    private SystemSettingSeekBarPreference mEndPadding;
    private SystemSettingSwitchPreference mUseHeight;
    private SystemSettingSeekBarPreference mHeight;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        addPreferencesFromResource(R.xml.tenx_settings_statusbar);
        PreferenceScreen prefSet = getPreferenceScreen();

        final Context context = getContext();
        final ContentResolver resolver = context.getContentResolver();

        mStatusbarIcons = (Preference) findPreference(KEY_STATUS_BAR_ICONS);
        mNetworkTraffic = (Preference) findPreference(KEY_NETWORK_TRAFFIC);
        mBatteryBar = (Preference) findPreference(KEY_BATTERY_BAR);
        mDataDisabledIcon = (SystemSettingSwitchPreference) findPreference(KEY_DATA_DISABLED_ICON);
        mFourGIcon = (SystemSettingSwitchPreference) findPreference(KEY_FOUR_G_ICON);
        mWiFiStandardIcon = (SystemSettingSwitchPreference) findPreference(KEY_WIFI_STANDARD_ICON);
        mBatteryStyle = (SystemSettingListPreference) findPreference(KEY_BATTERY_STYLE);
        mBatteryPercent = (SystemSettingListPreference) findPreference(KEY_BATTERY_PERCENT);
        mBatteryTextCharging = (SystemSettingSwitchPreference) findPreference(KEY_BATTERY_TEXT_CHARGING);
        mClock = (Preference) findPreference(KEY_CLOCK);
        mLogo = (SystemSettingSwitchPreference) findPreference(KEY_LOGO);
        mLogoPosition = (SystemSettingListPreference) findPreference(KEY_LOGO_POSITION);
        mLogoStyle = (SystemSettingListPreference) findPreference(KEY_LOGO_STYLE);
        mCameraPrivacy = (SecureSettingSwitchPreference) findPreference(KEY_CAMERA_PRIVACY);
        mLocationPrivacy = (SecureSettingSwitchPreference) findPreference(KEY_LOCATION_PRIVACY);
        mProjectionPrivacy = (SecureSettingSwitchPreference) findPreference(KEY_PROJECTION_PRIVACY);
        mColoredIcons = (SystemSettingSwitchPreference) findPreference(KEY_COLORED_ICONS);
        mNotifCount = (SystemSettingSwitchPreference) findPreference(KEY_NOTIF_COUNT);
        mBtBattery = (SystemSettingSwitchPreference) findPreference(KEY_BT_BATTERY);
        mBrightnessControl = (SystemSettingSwitchPreference) findPreference(KEY_BRIGHTNESS_CONTROL);
        mDualStatusBar = (SystemSettingSwitchPreference) findPreference(KEY_DUAL_STATUS_BAR);
        mUseStartPadding = (SystemSettingSwitchPreference) findPreference(KEY_USE_START_PADDING);
        mStartPadding = (SystemSettingSeekBarPreference) findPreference(KEY_START_PADDING);
        mUseEndPadding = (SystemSettingSwitchPreference) findPreference(KEY_USE_END_PADDING);
        mEndPadding = (SystemSettingSeekBarPreference) findPreference(KEY_END_PADDING);
        mUseHeight = (SystemSettingSwitchPreference) findPreference(KEY_USE_HEIGHT);
        mHeight = (SystemSettingSeekBarPreference) findPreference(KEY_HEIGHT);

        int batterystyle = Settings.System.getIntForUser(resolver,
                Settings.System.STATUS_BAR_BATTERY_STYLE, BATTERY_STYLE_PORTRAIT, UserHandle.USER_CURRENT);
        int batterypercent = Settings.System.getIntForUser(resolver,
                Settings.System.STATUS_BAR_SHOW_BATTERY_PERCENT, 0, UserHandle.USER_CURRENT);

        mBatteryStyle.setOnPreferenceChangeListener(this);

        mBatteryPercent.setEnabled(
                batterystyle != BATTERY_STYLE_TEXT && batterystyle != BATTERY_STYLE_HIDDEN);
        mBatteryPercent.setOnPreferenceChangeListener(this);

        mBatteryTextCharging.setEnabled(batterystyle == BATTERY_STYLE_HIDDEN ||
                (batterystyle != BATTERY_STYLE_TEXT && batterypercent != 2));

        mQuickPulldown =
                (LineageSystemSettingListPreference) findPreference(KEY_QUICK_PULLDOWN);
        mQuickPulldown.setOnPreferenceChangeListener(this);
        updateQuickPulldownSummary(mQuickPulldown.getIntValue(0));

        if (getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
            mQuickPulldown.setEntries(R.array.status_bar_quick_pull_down_entries_rtl);
            mQuickPulldown.setEntryValues(R.array.status_bar_quick_pull_down_values_rtl);
        }

        setLayoutToPreference();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        final Context context = getContext();
        final ContentResolver resolver = context.getContentResolver();

        if (preference == mBatteryStyle) {
            int value = Integer.parseInt((String) newValue);
            int batterypercent = Settings.System.getIntForUser(resolver,
                    Settings.System.STATUS_BAR_SHOW_BATTERY_PERCENT, 0, UserHandle.USER_CURRENT);
            mBatteryPercent.setEnabled(
                    value != BATTERY_STYLE_TEXT && value != BATTERY_STYLE_HIDDEN);
            mBatteryTextCharging.setEnabled(value == BATTERY_STYLE_HIDDEN ||
                    (value != BATTERY_STYLE_TEXT && batterypercent != 2));
            return true;
        } else if (preference == mBatteryPercent) {
            int value = Integer.parseInt((String) newValue);
            int batterystyle = Settings.System.getIntForUser(resolver,
                    Settings.System.STATUS_BAR_BATTERY_STYLE, BATTERY_STYLE_PORTRAIT, UserHandle.USER_CURRENT);
            mBatteryTextCharging.setEnabled(batterystyle == BATTERY_STYLE_HIDDEN ||
                    (batterystyle != BATTERY_STYLE_TEXT && value != 2));
            return true;
        } else if (preference == mQuickPulldown) {
            int value = Integer.parseInt((String) newValue);
            updateQuickPulldownSummary(value);
            return true;
        }
        return false;
    }

    private void updateQuickPulldownSummary(int value) {
        String summary = "";
        switch (value) {
            case PULLDOWN_DIR_NONE:
                summary = getResources().getString(
                    R.string.status_bar_quick_pull_down_off);
                break;
            case PULLDOWN_DIR_RIGHT:
            case PULLDOWN_DIR_LEFT:
            case PULLDOWN_DIR_BOTH:
                summary = getResources().getString(
                    R.string.status_bar_quick_pull_down_summary,
                    getResources().getString(
                        value == PULLDOWN_DIR_RIGHT
                            ? R.string.status_bar_quick_pull_down_right
                            : value == PULLDOWN_DIR_LEFT
                                ? R.string.status_bar_quick_pull_down_left
                                : R.string.status_bar_quick_pull_down_both
                    )
                );
                break;
        }
        mQuickPulldown.setSummary(summary);
    }

    private void setLayoutToPreference() {
        mStatusbarIcons.setLayoutResource(R.layout.tenx_preference_top);
        mNetworkTraffic.setLayoutResource(R.layout.tenx_preference_middle);
        mBatteryBar.setLayoutResource(R.layout.tenx_preference_middle);
        mDataDisabledIcon.setLayoutResource(R.layout.tenx_preference_middle);
        mFourGIcon.setLayoutResource(R.layout.tenx_preference_middle);
        mWiFiStandardIcon.setLayoutResource(R.layout.tenx_preference_middle);
        mBatteryStyle.setLayoutResource(R.layout.tenx_preference_middle);
        mBatteryPercent.setLayoutResource(R.layout.tenx_preference_middle);
        mBatteryTextCharging.setLayoutResource(R.layout.tenx_preference_middle);
        mClock.setLayoutResource(R.layout.tenx_preference_middle);
        mLogo.setLayoutResource(R.layout.tenx_preference_middle);
        mLogoPosition.setLayoutResource(R.layout.tenx_preference_middle);
        mLogoStyle.setLayoutResource(R.layout.tenx_preference_middle);
        mCameraPrivacy.setLayoutResource(R.layout.tenx_preference_middle);
        mLocationPrivacy.setLayoutResource(R.layout.tenx_preference_middle);
        mProjectionPrivacy.setLayoutResource(R.layout.tenx_preference_middle);
        mColoredIcons.setLayoutResource(R.layout.tenx_preference_middle);
        mNotifCount.setLayoutResource(R.layout.tenx_preference_middle);
        mNotifCount.setLayoutResource(R.layout.tenx_preference_middle);
        mBtBattery.setLayoutResource(R.layout.tenx_preference_bottom);
        mBrightnessControl.setLayoutResource(R.layout.tenx_preference_top);
        mQuickPulldown.setLayoutResource(R.layout.tenx_preference_bottom);
        mDualStatusBar.setLayoutResource(R.layout.tenx_preference_top);
        mUseStartPadding.setLayoutResource(R.layout.tenx_preference_middle);
        mStartPadding.setLayoutResource(R.layout.tenx_preference_seekbar_middle);
        mUseEndPadding.setLayoutResource(R.layout.tenx_preference_middle);
        mEndPadding.setLayoutResource(R.layout.tenx_preference_seekbar_middle);
        mUseHeight.setLayoutResource(R.layout.tenx_preference_middle);
        mHeight.setLayoutResource(R.layout.tenx_preference_seekbar_bottom);
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.TENX;
    }
}
