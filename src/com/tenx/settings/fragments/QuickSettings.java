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
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.provider.Settings;
import androidx.preference.*;

import com.android.internal.logging.nano.MetricsProto;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

import lineageos.preference.LineageSecureSettingListPreference;
import lineageos.preference.LineageSecureSettingSwitchPreference;
import com.tenx.support.preferences.SecureSettingSwitchPreference;
import com.tenx.support.preferences.SystemSettingListPreference;
import com.tenx.support.preferences.SystemSettingSeekBarPreference;
import com.tenx.support.preferences.SystemSettingSwitchPreference;

public class QuickSettings extends SettingsPreferenceFragment
        implements Preference.OnPreferenceChangeListener {

    private static final String KEY_BATTERY_STYLE = "qs_battery_style";
    private static final String KEY_BATTERY_PERCENT = "qs_show_battery_percent";
    private static final String KEY_QS_LOCKSCREEN_DISABLED = "secure_lockscreen_qs_disabled";
    private static final String KEY_NOTIF_DISMISS = "notification_material_dismiss";
    private static final String KEY_NOTIF_DISMISS_BACKGROUND = "notification_material_dismiss_background_style";
    private static final String KEY_NOTIF_DISMISS_ICON = "notification_material_dismiss_icon_style";
    private static final String KEY_SHOW_BRIGHTNESS_SLIDER = "qs_show_brightness_slider";
    private static final String KEY_BRIGHTNESS_SLIDER_POSITION = "qs_brightness_slider_position";
    private static final String KEY_SHOW_AUTO_BRIGHTNESS = "qs_show_auto_brightness";
    private static final String KEY_QS_REQUIRES_UNLOCKING = "qstile_requires_unlocking";
    private static final String KEY_SHOW_DATA_USAGE = "qs_show_data_usage";
    private static final String KEY_SHOW_DATA_USAGE_ICON = "qs_show_data_usage_icon";
    private static final String KEY_SHOW_DATA_USAGE_PANEL = "qs_data_usage_panel";
    private static final String KEY_SHOW_SYSTEM_INFO = "qs_system_info";
    private static final String KEY_SHOW_QUICK_QS_SETTINGS = "quick_qs_show_settings";
    private static final String KEY_SHOW_QUICK_QS_TENX_TEXT = "quick_qs_show_tenx_text";
    private static final String KEY_SHOW_QUICK_QS_VIEW = "quick_qs_show_view";
    private static final String KEY_CORNER_RADIUS = "default_qs_tile_corner_radius";
    private static final String KEY_TILE_STYLE = "default_qs_tile_styles";
    private static final String KEY_TILE_ICON_COLOR = "qs_tile_icon_color";
    private static final String KEY_PRIMARY_LABEL_COLOR = "qs_tile_primary_label_color";
    private static final String KEY_SECONDARY_LABEL_COLOR = "qs_tile_secondary_label_color";
    private static final String KEY_HIDE_LABEL = "qs_tile_label_hide";
    private static final String KEY_HIDE_SECONDARY_LABEL = "qs_tile_secondary_label_hide";
    private static final String KEY_LABEL_SIZE = "qs_tile_label_size";
    private static final String KEY_VERTICAL_LAYOUT = "qs_tile_vertical_layout";
    private static final String KEY_COLUMNS_PORTRAIT = "qs_layout_columns";
    private static final String KEY_COLUMNS_LANDSCAPE = "qs_layout_columns_landscape";
    private static final String KEY_QUICK_QS_ROWS_PORTRAIT = "qqs_layout_rows";
    private static final String KEY_QUICK_QS_ROWS_LANDSCAPE = "qqs_layout_rows_landscape";
    private static final String KEY_QS_TRANSPARENCY = "qs_transparency";
    private static final String KEY_QS_DUAL_TONE = "qs_dual_tone";
    private static final String KEY_HEADER_IMAGE = "category_custom_header";
    private static final String KEY_QS_BT_AUTO_ON = "qs_bt_auto_on";

    private static final int BATTERY_STYLE_PORTRAIT = 0;
    private static final int BATTERY_STYLE_TEXT = 4;
    private static final int BATTERY_STYLE_HIDDEN = 5;

    private SystemSettingListPreference mBatteryStyle;
    private SystemSettingListPreference mBatteryPercent;
    private SystemSettingSwitchPreference mDisableQsInLockscreen;
    private SystemSettingSwitchPreference mNotifDismiss;
    private SystemSettingListPreference mNotifDismissBackground;
    private SystemSettingListPreference mNotifDismissIcon;
    private LineageSecureSettingListPreference mShowBrightnessSlider;
    private LineageSecureSettingListPreference mBrightnessSliderPosition;
    private LineageSecureSettingSwitchPreference mShowAutoBrightness;
    private SecureSettingSwitchPreference mRequiresUnlocking;
    private SystemSettingSwitchPreference mDataUsage;
    private SystemSettingSwitchPreference mDataUsageIcon;
    private SystemSettingSwitchPreference mDataUsagePanel;
    private SystemSettingSwitchPreference mSystemInfo;
    private SystemSettingSwitchPreference mShowQuickQsSettings;
    private SystemSettingSwitchPreference mShowQuickQsTenXText;
    private SystemSettingSwitchPreference mShowQuickQsView;
    private SystemSettingSeekBarPreference mCornerRadius;
    private SystemSettingListPreference mTileStyles;
    private SystemSettingListPreference mIconColor;
    private SystemSettingListPreference mPrimaryLabelColor;
    private SystemSettingListPreference mSecondaryLabelColor;
    private SystemSettingSwitchPreference mHideLabel;
    private SystemSettingSwitchPreference mSecondaryLabelHide;
    private SystemSettingSeekBarPreference mLabelSize;
    private SystemSettingSwitchPreference mVerticalLayout;
    private SystemSettingSeekBarPreference mColumnsPortrait;
    private SystemSettingSeekBarPreference mColumnsLandscape;
    private SystemSettingSeekBarPreference mQuickQsRowsPortrait;
    private SystemSettingSeekBarPreference mQuickQsRowsLandscape;
    private SystemSettingSeekBarPreference mQsTransparency;
    private SystemSettingSwitchPreference mQsDualTone;
    private Preference mHeaderImage;
    private SystemSettingSwitchPreference mQsBtAutoOn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.tenx_settings_quicksettings);

        final Context context = getContext();
        final ContentResolver resolver = context.getContentResolver();

        mBatteryStyle = (SystemSettingListPreference) findPreference(KEY_BATTERY_STYLE);
        mBatteryPercent = (SystemSettingListPreference) findPreference(KEY_BATTERY_PERCENT);
        mDisableQsInLockscreen = (SystemSettingSwitchPreference) findPreference(KEY_QS_LOCKSCREEN_DISABLED);
        mNotifDismiss = (SystemSettingSwitchPreference) findPreference(KEY_NOTIF_DISMISS);
        mNotifDismissBackground = (SystemSettingListPreference) findPreference(KEY_NOTIF_DISMISS_BACKGROUND);
        mNotifDismissIcon = (SystemSettingListPreference) findPreference(KEY_NOTIF_DISMISS_ICON);
        mShowBrightnessSlider = (LineageSecureSettingListPreference) findPreference(KEY_SHOW_BRIGHTNESS_SLIDER);
        mBrightnessSliderPosition = (LineageSecureSettingListPreference) findPreference(KEY_BRIGHTNESS_SLIDER_POSITION);
        mShowAutoBrightness = (LineageSecureSettingSwitchPreference) findPreference(KEY_SHOW_AUTO_BRIGHTNESS);
        mRequiresUnlocking = (SecureSettingSwitchPreference) findPreference(KEY_QS_REQUIRES_UNLOCKING);
        mDataUsage = (SystemSettingSwitchPreference) findPreference(KEY_SHOW_DATA_USAGE);
        mDataUsageIcon = (SystemSettingSwitchPreference) findPreference(KEY_SHOW_DATA_USAGE_ICON);
        mDataUsagePanel = (SystemSettingSwitchPreference) findPreference(KEY_SHOW_DATA_USAGE_PANEL);
        mSystemInfo = (SystemSettingSwitchPreference) findPreference(KEY_SHOW_SYSTEM_INFO);
        mShowQuickQsSettings = (SystemSettingSwitchPreference) findPreference(KEY_SHOW_QUICK_QS_SETTINGS);
        mShowQuickQsTenXText = (SystemSettingSwitchPreference) findPreference(KEY_SHOW_QUICK_QS_TENX_TEXT);
        mShowQuickQsView = (SystemSettingSwitchPreference) findPreference(KEY_SHOW_QUICK_QS_VIEW);
        mCornerRadius = (SystemSettingSeekBarPreference) findPreference(KEY_CORNER_RADIUS);
        mTileStyles = (SystemSettingListPreference) findPreference(KEY_TILE_STYLE);
        mIconColor = (SystemSettingListPreference) findPreference(KEY_TILE_ICON_COLOR);
        mPrimaryLabelColor = (SystemSettingListPreference) findPreference(KEY_PRIMARY_LABEL_COLOR);
        mSecondaryLabelColor = (SystemSettingListPreference) findPreference(KEY_SECONDARY_LABEL_COLOR);
        mHideLabel = (SystemSettingSwitchPreference) findPreference(KEY_HIDE_LABEL);
        mSecondaryLabelHide = (SystemSettingSwitchPreference) findPreference(KEY_HIDE_SECONDARY_LABEL);
        mLabelSize = (SystemSettingSeekBarPreference) findPreference(KEY_LABEL_SIZE);
        mVerticalLayout = (SystemSettingSwitchPreference) findPreference(KEY_VERTICAL_LAYOUT);
        mColumnsPortrait = (SystemSettingSeekBarPreference) findPreference(KEY_COLUMNS_PORTRAIT);
        mColumnsLandscape = (SystemSettingSeekBarPreference) findPreference(KEY_COLUMNS_LANDSCAPE);
        mQuickQsRowsPortrait = (SystemSettingSeekBarPreference) findPreference(KEY_QUICK_QS_ROWS_PORTRAIT);
        mQuickQsRowsLandscape = (SystemSettingSeekBarPreference) findPreference(KEY_QUICK_QS_ROWS_LANDSCAPE);
        mQsTransparency = (SystemSettingSeekBarPreference) findPreference(KEY_QS_TRANSPARENCY);
        mQsDualTone = (SystemSettingSwitchPreference) findPreference(KEY_QS_DUAL_TONE);
        mHeaderImage = (Preference) findPreference(KEY_HEADER_IMAGE);
        mQsBtAutoOn = (SystemSettingSwitchPreference) findPreference(KEY_QS_BT_AUTO_ON);

        int batterystyle = Settings.System.getIntForUser(resolver,
                Settings.System.QS_BATTERY_STYLE, BATTERY_STYLE_PORTRAIT, UserHandle.USER_CURRENT);

        mBatteryStyle.setOnPreferenceChangeListener(this);

        mBatteryPercent.setEnabled(
                batterystyle != BATTERY_STYLE_TEXT && batterystyle != BATTERY_STYLE_HIDDEN);

        setLayoutToPreference();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mBatteryStyle) {
            int value = Integer.parseInt((String) newValue);
            mBatteryPercent.setEnabled(
                    value != BATTERY_STYLE_TEXT && value != BATTERY_STYLE_HIDDEN);
            return true;
        }
        return false;
    }

    private void setLayoutToPreference() {
        mBatteryStyle.setLayoutResource(R.layout.tenx_preference_top);
        mBatteryPercent.setLayoutResource(R.layout.tenx_preference_bottom);
        mDisableQsInLockscreen.setLayoutResource(R.layout.tenx_preference);
        mQsTransparency.setLayoutResource(R.layout.tenx_preference_seekbar_top);
        mQsDualTone.setLayoutResource(R.layout.tenx_preference_middle);
        mNotifDismiss.setLayoutResource(R.layout.tenx_preference_middle);
        mNotifDismissBackground.setLayoutResource(R.layout.tenx_preference_middle);
        mNotifDismissIcon.setLayoutResource(R.layout.tenx_preference_middle);
        mShowBrightnessSlider.setLayoutResource(R.layout.tenx_preference_middle);
        mBrightnessSliderPosition.setLayoutResource(R.layout.tenx_preference_middle);
        mShowAutoBrightness.setLayoutResource(R.layout.tenx_preference_middle);
        mRequiresUnlocking.setLayoutResource(R.layout.tenx_preference_middle);
        mDataUsage.setLayoutResource(R.layout.tenx_preference_middle);
        mDataUsageIcon.setLayoutResource(R.layout.tenx_preference_middle);
        mDataUsagePanel.setLayoutResource(R.layout.tenx_preference_middle);
        mSystemInfo.setLayoutResource(R.layout.tenx_preference_middle);
        mShowQuickQsSettings.setLayoutResource(R.layout.tenx_preference_middle);
        mShowQuickQsTenXText.setLayoutResource(R.layout.tenx_preference_middle);
        mShowQuickQsView.setLayoutResource(R.layout.tenx_preference_bottom);
        mCornerRadius.setLayoutResource(R.layout.tenx_preference_seekbar_top);
        mTileStyles.setLayoutResource(R.layout.tenx_preference_middle);
        mIconColor.setLayoutResource(R.layout.tenx_preference_middle);
        mPrimaryLabelColor.setLayoutResource(R.layout.tenx_preference_middle);
        mSecondaryLabelColor.setLayoutResource(R.layout.tenx_preference_middle);
        mHideLabel.setLayoutResource(R.layout.tenx_preference_middle);
        mSecondaryLabelHide.setLayoutResource(R.layout.tenx_preference_middle);
        mLabelSize.setLayoutResource(R.layout.tenx_preference_seekbar_middle);
        mVerticalLayout.setLayoutResource(R.layout.tenx_preference_middle);
        mColumnsPortrait.setLayoutResource(R.layout.tenx_preference_seekbar_middle);
        mColumnsLandscape.setLayoutResource(R.layout.tenx_preference_seekbar_middle);
        mQuickQsRowsPortrait.setLayoutResource(R.layout.tenx_preference_seekbar_middle);
        mQuickQsRowsLandscape.setLayoutResource(R.layout.tenx_preference_seekbar_bottom);
        mHeaderImage.setLayoutResource(R.layout.tenx_preference);
        mQsBtAutoOn.setLayoutResource(R.layout.tenx_preference_middle);
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.TENX;
    }
}
