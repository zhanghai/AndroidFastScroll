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

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import me.zhanghai.android.fastscroll.FastScroller;
import me.zhanghai.android.fastscroll.FastScrollerBuilder;
import me.zhanghai.android.fastscroll.sample.databinding.RecyclerViewFragmentBinding;

public abstract class RecyclerViewFragment extends Fragment {

    private RecyclerViewFragmentBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = RecyclerViewFragmentBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mBinding.scrollingView.setLayoutManager(createLayoutManager(mBinding.scrollingView));
        mBinding.scrollingView.setAdapter(new LocaleListAdapter());
        FastScroller fastScroller = createFastScroller(mBinding.scrollingView);
        mBinding.scrollingView.setOnApplyWindowInsetsListener(
                new ScrollingViewOnApplyWindowInsetsListener(mBinding.scrollingView, fastScroller));
    }

    @NonNull
    protected LinearLayoutManager createLayoutManager(@NonNull RecyclerView recyclerView) {
        return new LinearLayoutManager(recyclerView.getContext(), RecyclerView.VERTICAL, false);
    }

    @NonNull
    protected FastScroller createFastScroller(@NonNull RecyclerView recyclerView) {
        return new FastScrollerBuilder(recyclerView).useMd2Style().build();
    }
}
