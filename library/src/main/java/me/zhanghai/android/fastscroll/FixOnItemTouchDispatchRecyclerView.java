/*
 * Copyright 2020 Google LLC
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
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class FixOnItemTouchDispatchRecyclerView extends RecyclerView implements ViewHelperProvider {

    @NonNull
    private final ViewHelper mViewHelper = new ViewHelper(this);

    @Nullable
    private OnItemTouchListener mPhantomOnItemTouchListener = null;
    private OnItemTouchListener mInterceptingOnItemTouchListener = null;

    public FixOnItemTouchDispatchRecyclerView(@NonNull Context context) {
        super(context);
    }

    public FixOnItemTouchDispatchRecyclerView(@NonNull Context context,
                                              @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FixOnItemTouchDispatchRecyclerView(@NonNull Context context,
                                              @Nullable AttributeSet attrs,
                                              @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @NonNull
    @Override
    public FastScroller.ViewHelper getViewHelper() {
        return mViewHelper;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        mInterceptingOnItemTouchListener = null;
        if (findInterceptingOnItemTouchListener(e)) {
            cancelScroll();
            return true;
        }
        return super.onInterceptTouchEvent(e);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (dispatchOnItemTouchListeners(e)) {
            cancelScroll();
            return true;
        }
        return super.onTouchEvent(e);
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        if (mPhantomOnItemTouchListener != null) {
            mPhantomOnItemTouchListener.onRequestDisallowInterceptTouchEvent(disallowIntercept);
        }
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
    }

    private void cancelScroll() {
        MotionEvent syntheticCancel = MotionEvent.obtain(
                0, 0, MotionEvent.ACTION_CANCEL, 0f, 0f, 0);
        super.onInterceptTouchEvent(syntheticCancel);
        syntheticCancel.recycle();
    }

    private boolean dispatchOnItemTouchListeners(@NonNull MotionEvent e) {
        if (mInterceptingOnItemTouchListener == null) {
            if (e.getAction() == MotionEvent.ACTION_DOWN) {
                return false;
            }
            return findInterceptingOnItemTouchListener(e);
        } else {
            mInterceptingOnItemTouchListener.onTouchEvent(this, e);
            final int action = e.getAction();
            if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
                mInterceptingOnItemTouchListener = null;
            }
            return true;
        }
    }

    private boolean findInterceptingOnItemTouchListener(@NonNull MotionEvent e) {
        int action = e.getAction();
        if (mPhantomOnItemTouchListener != null
                && mPhantomOnItemTouchListener.onInterceptTouchEvent(this, e)
                && action != MotionEvent.ACTION_CANCEL) {
            mInterceptingOnItemTouchListener = mPhantomOnItemTouchListener;
            return true;
        }
        return false;
    }

    private class ViewHelper extends RecyclerViewHelper {

        ViewHelper(@NonNull RecyclerView view) {
            super(view);
        }

        @Override
        public void addOnTouchEventListener(@NonNull Predicate<MotionEvent> onTouchEvent) {
            mPhantomOnItemTouchListener = new RecyclerView.SimpleOnItemTouchListener() {
                @Override
                public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView,
                                                     @NonNull MotionEvent event) {
                    return onTouchEvent.test(event);
                }

                @Override
                public void onTouchEvent(@NonNull RecyclerView recyclerView,
                                         @NonNull MotionEvent event) {
                    onTouchEvent.test(event);
                }
            };
        }
    }
}
