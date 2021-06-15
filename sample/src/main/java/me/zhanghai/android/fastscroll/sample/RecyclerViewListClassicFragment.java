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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import me.zhanghai.android.fastscroll.FastScroller;
import me.zhanghai.android.fastscroll.FastScrollerBuilder;

public class RecyclerViewListClassicFragment extends RecyclerViewFragment {

    @NonNull
    public static RecyclerViewListClassicFragment newInstance() {
        return new RecyclerViewListClassicFragment();
    }

    @NonNull
    protected FastScroller createFastScroller(@NonNull RecyclerView recyclerView) {
        return new FastScrollerBuilder(recyclerView).build();
    }
}
