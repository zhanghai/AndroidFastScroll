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

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import me.zhanghai.android.fastscroll.FastScrollerBuilder;
import me.zhanghai.android.fastscroll.sample.databinding.WebViewFragmentBinding;

public class WebViewFragment extends Fragment {

    private WebViewFragmentBinding mBinding;

    @NonNull
    public static WebViewFragment newInstance() {
        return new WebViewFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = WebViewFragmentBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    @SuppressLint("SetJavaScriptEnabled")
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mBinding.scrollingView.getSettings().setJavaScriptEnabled(true);
        mBinding.scrollingView.setOnApplyWindowInsetsListener(
                new ScrollingViewOnApplyWindowInsetsListener() {
                    @NonNull
                    @Override
                    public WindowInsets onApplyWindowInsets(@NonNull View view,
                                                            @NonNull WindowInsets insets) {
                        insets = super.onApplyWindowInsets(view, insets);
                        updateWebViewPadding();
                        return insets;
                    }
                });
        mBinding.scrollingView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(@NonNull WebView view, @NonNull String url) {
                updateWebViewPadding();
            }
        });
        String html = getString(R.string.html_format, License.get(
                mBinding.scrollingView.getContext()));
        String data = Base64.encodeToString(html.getBytes(), Base64.NO_PADDING);
        mBinding.scrollingView.loadData(data, null, "base64");
        new FastScrollerBuilder(mBinding.scrollingView).useMd2Style().build();
    }

    private void updateWebViewPadding() {
        mBinding.scrollingView.loadUrl("javascript:(function () { document.body.style.margin = '"
                + pxToDp(mBinding.scrollingView.getPaddingTop()) + "px "
                + pxToDp(mBinding.scrollingView.getPaddingRight()) + "px "
                + pxToDp(mBinding.scrollingView.getPaddingBottom()) + "px "
                + pxToDp(mBinding.scrollingView.getPaddingLeft()) + "px'; })();");
    }

    private float pxToDp(float px) {
        return px / getResources().getDisplayMetrics().density;
    }
}
