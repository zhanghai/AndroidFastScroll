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

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Locale;

import me.zhanghai.android.fastscroll.PopupTextProvider;
import me.zhanghai.android.fastscroll.sample.databinding.ListItemBinding;

public class LocaleListAdapter extends RecyclerView.Adapter<LocaleListAdapter.ViewHolder>
        implements PopupTextProvider {

    @NonNull
    private final Locale[] mLocales;

    public LocaleListAdapter() {
        setHasStableIds(true);

        mLocales = Locale.getAvailableLocales();
    }

    @Override
    public int getItemCount() {
        return mLocales.length;
    }

    @NonNull
    private Locale getItem(int position) {
        return mLocales[position];
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).hashCode();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(ListItemBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false));
        ListItemBinding binding = holder.binding;
        binding.getRoot().setClickable(true);
        binding.getRoot().setFocusable(true);
        binding.titleText.setEllipsize(TextUtils.TruncateAt.END);
        binding.titleText.setSingleLine();
        binding.subtitleText.setEllipsize(TextUtils.TruncateAt.END);
        binding.subtitleText.setSingleLine();
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Locale locale = getItem(position);
        ListItemBinding binding = holder.binding;
        binding.titleText.setText(locale.toString());
        binding.subtitleText.setText(locale.getDisplayName());
    }

    @NonNull
    @Override
    public CharSequence getPopupText(@NonNull View view, int position) {
        Locale locale = getItem(position);
        return locale.toString().substring(0, 1).toUpperCase(Locale.getDefault());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        public final ListItemBinding binding;

        public ViewHolder(@NonNull ListItemBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }
    }
}
