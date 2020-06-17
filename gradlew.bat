/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package androidx.appcompat.view.menu;

import static androidx.annotation.RestrictTo.Scope.LIBRARY_GROUP_PREFIX;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.PopupWindow.OnDismissListener;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.annotation.StyleRes;
import androidx.appcompat.R;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;

/**
 * Presents a menu as a small, simple popup anchored to another view.
 *
 * @hide
 */
@RestrictTo(LIBRARY_GROUP_PREFIX)
public class MenuPopupHelper implements MenuHelper {
    private static final int TOUCH_EPICENTER_SIZE_DP = 48;

    private final Context mContext;

    // Immutable cached popup menu properties.
    private final MenuBuilder mMenu;
    private final boolean mOverflowOnly;
    private final int mPopupStyleAttr;
    private final int mPopupStyleRes;

    // Mutable cached popup menu properties.
    private View mAnchorView;
    private int mDropDownGravity = Gravity.START;
    private boolean mForceShowIcon;
    private MenuPresenter.Callback mPresenterCallback;

    private MenuPopup mPopup;
    private OnDismissListener mOnDismissListener;

    public MenuPopupHelper(@NonNull Context context, @NonNull MenuBuilder menu) {
        this(context, menu, null, false, R.attr.popu