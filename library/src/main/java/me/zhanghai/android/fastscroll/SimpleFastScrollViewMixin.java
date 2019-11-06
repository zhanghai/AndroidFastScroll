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

import android.graphics.Canvas;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SimpleFastScrollViewMixin implements SimpleFastScrollView {

    @NonNull
    private final ViewAccessor mView;

    @Nullable
    private Runnable mOnPreDrawListener;

    @Nullable
    private Runnable mOnScrollChangedListener;

    @Nullable
    private Predicate<MotionEvent> mOnTouchEventListener;
    private boolean mListenerInterceptingTouchEvent;

    public SimpleFastScrollViewMixin(@NonNull ViewAccessor view) {
        mView = view;
    }

    @Override
    public void setOnPreDrawListener(@Nullable Runnable listener) {
        mOnPreDrawListener = listener;
    }

    public void draw(@NonNull Canvas canvas) {

        if (mOnPreDrawListener != null) {
            mOnPreDrawListener.run();
        }

        mView.superDraw(canvas);
    }

    @Override
    public void setOnScrollChangedListener(@Nullable Runnable listener) {
        mOnScrollChangedListener = listener;
    }

    public void onScrollChanged(int left, int top, int oldLeft, int oldTop) {
        mView.superOnScrollChanged(left, top, oldLeft, oldTop);

        if (mOnScrollChangedListener != null) {
            mOnScrollChangedListener.run();
        }
    }

    @Override
    public void setOnTouchEventListener(@Nullable Predicate<MotionEvent> listener) {
        mOnTouchEventListener = listener;
    }

    public boolean onInterceptTouchEvent(@NonNull MotionEvent event) {

        if (mOnTouchEventListener != null && mOnTouchEventListener.test(event)) {

            int actionMasked = event.getActionMasked();
            if (actionMasked != MotionEvent.ACTION_UP
                    && actionMasked != MotionEvent.ACTION_CANCEL) {
                mListenerInterceptingTouchEvent = true;
            }

            if (actionMasked != MotionEvent.ACTION_CANCEL) {
                MotionEvent cancelEvent = MotionEvent.obtain(event);
                cancelEvent.setAction(MotionEvent.ACTION_CANCEL);
                mView.superOnInterceptTouchEvent(cancelEvent);
                cancelEvent.recycle();
            } else {
                mView.superOnInterceptTouchEvent(event);
            }

            return true;
        }

        return mView.superOnInterceptTouchEvent(event);
    }

    public boolean onTouchEvent(@NonNull MotionEvent event) {

        if (mOnTouchEventListener != null) {
            if (mListenerInterceptingTouchEvent) {

                mOnTouchEventListener.test(event);

                int actionMasked = event.getActionMasked();
                if (actionMasked == MotionEvent.ACTION_UP
                        || actionMasked == MotionEvent.ACTION_CANCEL) {
                    mListenerInterceptingTouchEvent = false;
                }

                return true;
            } else {
                int actionMasked = event.getActionMasked();
                if (actionMasked != MotionEvent.ACTION_DOWN && mOnTouchEventListener.test(event)) {

                    if (actionMasked != MotionEvent.ACTION_UP
                            && actionMasked != MotionEvent.ACTION_CANCEL) {
                        mListenerInterceptingTouchEvent = true;
                    }

                    if (actionMasked != MotionEvent.ACTION_CANCEL) {
                        MotionEvent cancelEvent = MotionEvent.obtain(event);
                        cancelEvent.setAction(MotionEvent.ACTION_CANCEL);
                        mView.superOnTouchEvent(cancelEvent);
                        cancelEvent.recycle();
                    } else {
                        mView.superOnTouchEvent(event);
                    }

                    return true;
                }
            }
        }

        return mView.superOnTouchEvent(event);
    }

    @Override
    public int getScrollRange() {
        return mView.computeVerticalScrollRange();
    }

    @Override
    public int getScrollOffset() {
        return mView.computeVerticalScrollOffset();
    }

    public interface ViewAccessor {

        void superDraw(@NonNull Canvas canvas);

        void superOnScrollChanged(int left, int top, int oldLeft, int oldTop);

        boolean superOnInterceptTouchEvent(@NonNull MotionEvent event);

        boolean superOnTouchEvent(@NonNull MotionEvent event);

        int computeVerticalScrollRange();

        int computeVerticalScrollOffset();
    }
}
