/*
 * Copyright 2019 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.zhanghai.android.fastscroll.sample;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @NonNull
    private MainFragment mMainFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Calls ensureSubDecor().
        findViewById(android.R.id.content);

        if (savedInstanceState == null) {
            mMainFragment = MainFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .add(android.R.id.content, mMainFragment)
                    .commit();
        } else {
            mMainFragment = (MainFragment) getSupportFragmentManager().findFragmentById(
                    android.R.id.content);
        }
    }

    @Override
    public void onBackPressed() {
        if (mMainFragment.onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void applyOverrideConfiguration(@NonNull Configuration overrideConfiguration) {
        // Workaround b/141132133.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        super.applyOverrideConfiguration(overrideConfiguration);
    }
}
