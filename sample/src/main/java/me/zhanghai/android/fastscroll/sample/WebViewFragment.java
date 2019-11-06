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
import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhanghai.android.fastscroll.FastScrollWebView;
import me.zhanghai.android.fastscroll.FastScrollerBuilder;

public class WebViewFragment extends Fragment {

    @BindView(R.id.scrolling_view)
    FastScrollWebView mWebView;

    @NonNull
    public static WebViewFragment newInstance() {
        return new WebViewFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.web_view_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);
    }

    @Override
    @SuppressLint("SetJavaScriptEnabled")
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setOnApplyWindowInsetsListener(new ScrollingViewOnApplyWindowInsetsListener() {
            @NonNull
            @Override
            public WindowInsets onApplyWindowInsets(@NonNull View view,
                                                    @NonNull WindowInsets insets) {
                insets = super.onApplyWindowInsets(view, insets);
                updateWebViewPadding();
                return insets;
            }
        });
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(@NonNull WebView view, @NonNull String url) {
                updateWebViewPadding();
            }
        });
        String html = getString(R.string.html_format, License.get(mWebView.getContext()));
        String data = Base64.encodeToString(html.getBytes(), Base64.NO_PADDING);
        mWebView.loadData(data, null, "base64");
        new FastScrollerBuilder(mWebView).useMd2Style().build();
    }

    private void updateWebViewPadding() {
        mWebView.loadUrl("javascript:(function () { document.body.style.margin = '"
                + pxToDp(mWebView.getPaddingTop()) + "px "
                + pxToDp(mWebView.getPaddingRight()) + "px "
                + pxToDp(mWebView.getPaddingBottom()) + "px "
                + pxToDp(mWebView.getPaddingLeft()) + "px'; })();");
    }

    private float pxToDp(float px) {
        return px / getResources().getDisplayMetrics().density;
    }
}
