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

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

@SuppressLint("MissingSuperCall")
public class FastScrollNestedScrollView extends NestedScrollView implements SimpleFastScrollView {

    @NonNull
    private SimpleFastScrollViewMixin mMixin;

    public FastScrollNestedScrollView(@NonNull Context context) {
        super(context);

        init();
    }

    public FastScrollNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public FastScrollNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs,
                                      @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        mMixin = new SimpleFastScrollViewMixin(new SimpleFastScrollViewMixin.ViewAccessor() {
            @Override
            public void superDraw(@NonNull Canvas canvas) {
                FastScrollNestedScrollView.super.draw(canvas);
            }
            @Override
            public void superOnScrollChanged(int left, int top, int oldLeft, int oldTop) {
                FastScrollNestedScrollView.super.onScrollChanged(left, top, oldLeft, oldTop);
            }
            @Override
            public boolean superOnInterceptTouchEvent(@NonNull MotionEvent event) {
                return FastScrollNestedScrollView.super.onInterceptTouchEvent(event);
            }
            @Override
            public boolean superOnTouchEvent(@NonNull MotionEvent event) {
                return FastScrollNestedScrollView.super.onTouchEvent(event);
            }
            @Override
            public int computeVerticalScrollRange() {
                return FastScrollNestedScrollView.this.computeVerticalScrollRange();
            }
            @Override
            public int computeVerticalScrollOffset() {
                return FastScrollNestedScrollView.this.computeVerticalScrollOffset();
            }
        });
    }

    @Override
    public void setOnPreDrawListener(@Nullable Runnable listener) {
        mMixin.setOnPreDrawListener(listener);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        mMixin.draw(canvas);
    }

    @Override
    public void setOnScrollChangedListener(@Nullable Runnable listener) {
        mMixin.setOnScrollChangedListener(listener);
    }

    @Override
    protected void onScrollChanged(int left, int top, int oldLeft, int oldTop) {
        mMixin.onScrollChanged(left, top, oldLeft, oldTop);
    }

    @Override
    public void setOnTouchEventListener(@Nullable Predicate<MotionEvent> listener) {
        mMixin.setOnTouchEventListener(listener);
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull MotionEvent event) {
        return mMixin.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        return mMixin.onTouchEvent(event);
    }

    @Override
    public int getScrollRange() {
        return mMixin.getScrollRange() + getPaddingTop() + getPaddingBottom();
    }

    @Override
    public int getScrollOffset() {
        return mMixin.getScrollOffset();
    }
}
