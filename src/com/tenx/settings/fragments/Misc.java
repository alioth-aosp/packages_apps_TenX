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
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.SystemProperties;
import android.provider.Settings;
import androidx.preference.*;

import com.android.internal.logging.nano.MetricsProto;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.tenx.support.preferences.SystemSettingSwitchPreference;
import com.android.internal.util.tenx.SystemRestartUtils;

public class Misc extends SettingsPreferenceFragment
        implements Preference.OnPreferenceChangeListener {

    public static final String TAG = "Misc";

    private static final String KEY_POCKET_JUDGE = "pocket_judge";
    private static final String KEY_DEV_SETTINGS = "hide_developer_status_settings";
    private static final String KEY_PIF_JSON_FILE_PREFERENCE = "pif_json_file_preference";
    private static final String SYS_GAMEPROP_ENABLED = "persist.sys.gameprops.enabled";
    private static final String KEY_GAME_PROPS_JSON_FILE_PREFERENCE = "game_props_json_file_preference";

    private SystemSettingSwitchPreference mPocketJudge;
    private Preference mDeveloperSettings;
    private Preference mPifJsonFilePreference;
    private Preference mGamePropsJsonFilePreference;
    private Preference mGamePropsSpoof;

    private Handler mHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new Handler();
        addPreferencesFromResource(R.xml.tenx_settings_misc);
        mGamePropsSpoof = findPreference(SYS_GAMEPROP_ENABLED);
        final PreferenceScreen prefScreen = getPreferenceScreen();
        final Resources res = getResources();

        mPocketJudge = (SystemSettingSwitchPreference) findPreference(KEY_POCKET_JUDGE);
        mDeveloperSettings = (Preference) findPreference(KEY_DEV_SETTINGS);
        mPifJsonFilePreference = findPreference(KEY_PIF_JSON_FILE_PREFERENCE);

        mGamePropsJsonFilePreference = findPreference(KEY_GAME_PROPS_JSON_FILE_PREFERENCE);    
        mGamePropsSpoof.setOnPreferenceChangeListener(this);

        boolean mPocketJudgeSupported = res.getBoolean(
                com.android.internal.R.bool.config_pocketModeSupported);
        if (!mPocketJudgeSupported)
            prefScreen.removePreference(mPocketJudge);

        mDeveloperSettings.setLayoutResource(R.layout.tenx_preference_middle);
    }


    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        if (preference == mPifJsonFilePreference) {
            mPifJsonFilePreference.setOnPreferenceClickListener(pref -> {
            openFileSelector(10001);
            return true;
        });
        return true;
    } else if (preference == mGamePropsJsonFilePreference) {
        mGamePropsJsonFilePreference.setOnPreferenceClickListener(pref -> {
            openFileSelector(10002);
            return true;
        });
        return true;
    }
        return super.onPreferenceTreeClick(preference); // Default handling
    }

    private void openFileSelector(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/json");
        startActivityForResult(intent, requestCode);
   }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            Uri uri = data.getData();

            
            if (uri != null) {
                if (requestCode == 10001) {
                    loadPifJson(uri);
                } else if (requestCode == 10002) {
                    loadGameSpoofingJson(uri);
                }
            }
        }
    }

    private void loadPifJson(Uri uri) {
        Log.d(TAG, "Loading PIF JSON from URI: " + uri.toString());
        try (InputStream inputStream = getActivity().getContentResolver().openInputStream(uri)) {
            if (inputStream != null) {
                String json = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                Log.d(TAG, "PIF JSON data: " + json);
                JSONObject jsonObject = new JSONObject(json);
                for (Iterator<String> it = jsonObject.keys(); it.hasNext(); ) {
                    String key = it.next();
                    String value = jsonObject.getString(key);
                    Log.d(TAG, "Setting PIF property: persist.sys.pihooks_" + key + " = " + value);
                    SystemProperties.set("persist.sys.pihooks_" + key, value);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error reading PIF JSON or setting properties", e);
        }
        mHandler.postDelayed(() -> {
            SystemRestartUtils.showSystemRestartDialog(getContext());
        }, 1250);
    }

    private void loadGameSpoofingJson(Uri uri) {
        Log.d(TAG, "Loading Game Props JSON from URI: " + uri.toString());
        try (InputStream inputStream = getActivity().getContentResolver().openInputStream(uri)) {
            if (inputStream != null) {
                String json = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                Log.d(TAG, "Game Props JSON data: " + json);
                JSONObject jsonObject = new JSONObject(json);
                for (Iterator<String> it = jsonObject.keys(); it.hasNext(); ) {
                    String key = it.next();
                    if (key.startsWith("PACKAGES_") && !key.endsWith("_DEVICE")) {
                        String deviceKey = key + "_DEVICE";
                        if (jsonObject.has(deviceKey)) {
                            JSONObject deviceProps = jsonObject.getJSONObject(deviceKey);
                            JSONArray packages = jsonObject.getJSONArray(key);
                            for (int i = 0; i < packages.length(); i++) {
                                String packageName = packages.getString(i);
                                Log.d(TAG, "Spoofing package: " + packageName);
                                setGameProps(packageName, deviceProps);
                            }
                        }            
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error reading JSON or setting properties", e);
        }
        mHandler.postDelayed(() -> {
            SystemRestartUtils.showSystemRestartDialog(getContext());
        }, 1250);
    }

    private void setGameProps(String packageName, JSONObject deviceProps) {
        try {
            for (Iterator<String> it = deviceProps.keys(); it.hasNext(); ) {
                String key = it.next();
                String value = deviceProps.getString(key);
                String systemPropertyKey = "persist.sys.gameprops." + packageName + "." + key;
                SystemProperties.set(systemPropertyKey, value);
                Log.d(TAG, "Set system property: " + systemPropertyKey + " = " + value);
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing device properties", e);
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mGamePropsSpoof) {
            SystemRestartUtils.showSystemRestartDialog(getContext());
        return true;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.TENX;
    }
}
