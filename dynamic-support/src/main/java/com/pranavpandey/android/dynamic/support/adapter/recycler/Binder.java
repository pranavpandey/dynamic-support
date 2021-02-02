package com.pranavpandey.android.dynamic.support.adapter.recycler;

import android.view.ViewGroup;

/**
 * @author Pranav Pandey
 */
public interface Binder<V extends ViewType, VH> {

    VH onCreateViewHolder(ViewGroup parent);

    void onBindViewHolder(VH viewHolder, int position);

    void onViewRecycled(VH holder);

    void onRemoved();

    V getViewType();
}
