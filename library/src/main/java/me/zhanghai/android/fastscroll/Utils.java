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

package me.zhanghai.android.fastscroll;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.DisplayMetrics;

import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;

class Utils {

    @ColorInt
    public static int getColorFromAttrRes(@AttrRes int attrRes, @NonNull Context context) {
        ColorStateList colorStateList = getColorStateListFromAttrRes(attrRes, context);
        return colorStateList != null ? colorStateList.getDefaultColor() : 0;
    }

    @Nullable
    public static ColorStateList getColorStateListFromAttrRes(@AttrRes int attrRes,
                                                              @NonNull Context context) {
        TypedArray a = context.obtainStyledAttributes(new int[]{attrRes});
        int resId;
        try {
            resId = a.getResourceId(0, 0);
            if (resId != 0) {
                return AppCompatResources.getColorStateList(context, resId);
            }
            return a.getColorStateList(0);
        } finally {
            a.recycle();
        }
    }

    public static int dpToPixel(@NonNull final Context context, int dp) {
        float scaleFactor = (1.0f / DisplayMetrics.DENSITY_DEFAULT) * context.getResources().getDisplayMetrics().densityDpi;
        return (int) (dp * scaleFactor);
    }
}
