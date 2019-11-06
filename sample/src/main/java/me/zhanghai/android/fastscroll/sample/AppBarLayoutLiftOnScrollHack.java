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

import com.google.android.material.appbar.AppBarLayout;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

public class AppBarLayoutLiftOnScrollHack {

    private AppBarLayoutLiftOnScrollHack() {}

    public static void hack(@NonNull AppBarLayout appBarLayout, int liftOnScrollTargetViewId) {
        appBarLayout.getViewTreeObserver().addOnPreDrawListener(() -> {
            // Invalidate the cached view reference so that this works after replacing fragment.
            appBarLayout.setLiftOnScrollTargetViewId(liftOnScrollTargetViewId);
            // Call AppBarLayout.Behavior.onNestedPreScroll() with dy == 0 to update lifted state.
            CoordinatorLayout.Behavior behavior =
                    ((CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams()).getBehavior();
            CoordinatorLayout coordinatorLayout = (CoordinatorLayout) appBarLayout.getParent();
            behavior.onNestedPreScroll(coordinatorLayout, appBarLayout, coordinatorLayout, 0, 0,
                    null, 0);
            return true;
        });
    }
}
