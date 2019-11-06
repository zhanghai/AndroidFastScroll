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

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainFragment extends Fragment {

    private static final Uri GITHUB_URI = Uri.parse(
            "https://github.com/zhanghai/AndroidFastScroll");

    @BindView(R.id.drawer)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.navigation)
    NavigationView mNavigationView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.app_bar)
    AppBarLayout mAppBarLayout;

    @NonNull
    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        AppCompatActivity activity = (AppCompatActivity) requireActivity();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decorView = activity.getWindow().getDecorView();
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility()
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }

        activity.setSupportActionBar(mToolbar);

        if (savedInstanceState == null) {
            setNavigationCheckedItem(R.id.recycler_view_list);
        }
        mNavigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);

        AppBarLayoutLiftOnScrollHack.hack(mAppBarLayout, R.id.scrolling_view);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_view_on_github:
                viewOnGitHub();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }
        return false;
    }

    private void viewOnGitHub() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, GITHUB_URI));
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        switch (item.getItemId()) {
            case R.id.recycler_view_list:
            case R.id.recycler_view_list_classic:
            case R.id.recycler_view_list_stateful:
            case R.id.recycler_view_grid:
            case R.id.scroll_view:
            case R.id.nested_scroll_view:
            case R.id.web_view:
                setNavigationCheckedItem(itemId);
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return true;
            default:
                return false;
        }
    }

    private void setNavigationCheckedItem(@IdRes int itemId) {
        MenuItem item = mNavigationView.getCheckedItem();
        if (item != null && item.getItemId() == itemId) {
            return;
        }
        Fragment fragment;
        switch (itemId) {
            case R.id.recycler_view_list:
                fragment = RecyclerViewListFragment.newInstance();
                break;
            case R.id.recycler_view_list_classic:
                fragment = RecyclerViewListClassicFragment.newInstance();
                break;
            case R.id.recycler_view_list_stateful:
                fragment = RecyclerViewListStatefulFragment.newInstance();
                break;
            case R.id.recycler_view_grid:
                fragment = RecyclerViewGridFragment.newInstance();
                break;
            case R.id.scroll_view:
                fragment = ScrollViewFragment.newInstance();
                break;
            case R.id.nested_scroll_view:
                fragment = NestedScrollViewFragment.newInstance();
                break;
            case R.id.web_view:
                fragment = WebViewFragment.newInstance();
                break;
            default:
                throw new AssertionError(itemId);
        }
        getChildFragmentManager().beginTransaction()
                .replace(R.id.content, fragment)
                .commit();
        mNavigationView.setCheckedItem(itemId);
    }
}
