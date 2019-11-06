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
import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhanghai.android.fastscroll.FastScroller;
import me.zhanghai.android.fastscroll.FastScrollerBuilder;

public abstract class RecyclerViewFragment extends Fragment {

    @BindView(R.id.scrolling_view)
    RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recycler_view_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRecyclerView.setLayoutManager(createLayoutManager(mRecyclerView));
        mRecyclerView.setAdapter(new LocaleListAdapter());
        FastScroller fastScroller = createFastScroller(mRecyclerView);
        mRecyclerView.setOnApplyWindowInsetsListener(
                new ScrollingViewOnApplyWindowInsetsListener(mRecyclerView, fastScroller));
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
