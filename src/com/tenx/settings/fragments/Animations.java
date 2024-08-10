/*
 * Copyright (C) 2020 TenX-OS|elegan
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
import android.content.res.Resources;
import android.database.ContentObserver;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.UserHandle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;
import androidx.preference.*;

import com.android.internal.logging.nano.MetricsProto;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import com.tenx.support.preferences.SystemSettingListPreference;
import com.tenx.support.preferences.SystemSettingSeekBarPreference;

public class Animations extends SettingsPreferenceFragment
        implements Preference.OnPreferenceChangeListener {

    private static final String KEY_TILE_ANIM_STYLE = "qs_tile_animation_style";
    private static final String KEY_TILE_ANIM_DURATION = "qs_tile_animation_duration";
    private static final String KEY_TILE_ANIM_INTERPOLATOR = "qs_tile_animation_interpolator";

    private SystemSettingListPreference mTileAnimationStyle;
    private SystemSettingSeekBarPreference mTileAnimationDuration;
    private SystemSettingListPreference mTileAnimationInterpolator;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        addPreferencesFromResource(R.xml.tenx_settings_animations);
        final PreferenceScreen prefScreen = getPreferenceScreen();

        mTileAnimationStyle = (SystemSettingListPreference) findPreference(KEY_TILE_ANIM_STYLE);
        mTileAnimationDuration = (SystemSettingSeekBarPreference) findPreference(KEY_TILE_ANIM_DURATION);
        mTileAnimationInterpolator = (SystemSettingListPreference) findPreference(KEY_TILE_ANIM_INTERPOLATOR);

        mTileAnimationStyle.setOnPreferenceChangeListener(this);

        int tileAnimationStyle = Settings.System.getIntForUser(getContentResolver(),
                Settings.System.QS_TILE_ANIMATION_STYLE, 0, UserHandle.USER_CURRENT);
        updateTileAnimStyle(tileAnimationStyle);

        setLayoutToPreference();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mTileAnimationStyle) {
            int value = Integer.parseInt((String) newValue);
            updateTileAnimStyle(value);
            return true;
        }
        return false;
    }

    private void updateTileAnimStyle(int tileAnimationStyle) {
        mTileAnimationDuration.setEnabled(tileAnimationStyle != 0);
        mTileAnimationInterpolator.setEnabled(tileAnimationStyle != 0);
    }

    private void setLayoutToPreference() {
        mTileAnimationStyle.setLayoutResource(R.layout.tenx_preference_top);
        mTileAnimationDuration.setLayoutResource(R.layout.tenx_preference_seekbar_middle);
        mTileAnimationInterpolator.setLayoutResource(R.layout.tenx_preference_bottom);
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.TENX;
    }
}
