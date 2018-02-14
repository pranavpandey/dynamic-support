package com.pranavpandey.android.dynamic.support.recyclerview.adapter;

import android.support.annotation.NonNull;

import com.pranavpandey.android.dynamic.support.recyclerview.binder.DynamicRecyclerViewBinder;

import java.util.HashMap;
import java.util.Map;

/**
 * A recycler view adapter to display different type of
 * {@link DynamicRecyclerViewBinder} inside a recycler view.
 */
public abstract class DynamicTypeBinderAdapter<E extends Enum<E>>
        extends DynamicBinderAdapter {

    /**
     * Default item type {@code enums} for this adapter.
     */
    protected enum ItemViewType {
        /**
         * Enum for the type empty.
         */
        EMPTY,

        /**
         * Enum for the type section header.
         */
        HEADER,

        /**
         * Enum for the type item.
         */
        ITEM,

        /**
         * Enum for the type divider.
         */
        DIVIDER
    }

    /**
     * Map to hold the data binders.
     */
    private Map<E, DynamicRecyclerViewBinder> mDataBinderMap = new HashMap<>();

    @Override
    public int getItemCount() {
        int itemCount = 0;
        for (DynamicRecyclerViewBinder binder : mDataBinderMap.values()) {
            itemCount += binder.getItemCount();
        }

        return itemCount;
    }

    @Override
    public int getItemViewType(int position) {
        return getEnumFromPosition(position).ordinal();
    }

    @Override
    public <T extends DynamicRecyclerViewBinder> T getDataBinder(int viewType) {
        return getDataBinderBinder(getEnumFromOrdinal(viewType));
    }

    @Override
    public int getPosition(@NonNull DynamicRecyclerViewBinder binder, int binderPosition) {
        E targetViewType = getEnumFromBinder(binder);
        for (int i = 0, count = getItemCount(); i < count; i++) {
            if (targetViewType == getEnumFromPosition(i)) {
                binderPosition--;
                if (binderPosition < 0) {
                    return i;
                }
            }
        }

        return getItemCount();
    }

    @Override
    public int getBinderPosition(int position) {
        E targetViewType = getEnumFromPosition(position);
        int binderPosition = -1;
        for (int i = 0; i <= position; i++) {
            if (targetViewType == getEnumFromPosition(i)) {
                binderPosition++;
            }
        }

        if (binderPosition == -1) {
            throw new IllegalArgumentException("Binder does not exists in the adapter.");
        }
        return binderPosition;
    }

    @Override
    public void notifyBinderItemRangeChanged(@NonNull DynamicRecyclerViewBinder binder,
                                             int positionStart, int itemCount) {
        for (int i = positionStart; i <= itemCount; i++) {
            notifyItemChanged(getPosition(binder, i));
        }
    }

    @Override
    public void notifyBinderItemRangeInserted(@NonNull DynamicRecyclerViewBinder binder,
                                              int positionStart, int itemCount) {
        for (int i = positionStart; i <= itemCount; i++) {
            notifyItemInserted(getPosition(binder, i));
        }
    }

    @Override
    public void notifyBinderItemRangeRemoved(@NonNull DynamicRecyclerViewBinder binder,
                                             int positionStart, int itemCount) {
        for (int i = positionStart; i <= itemCount; i++) {
            notifyItemRemoved(getPosition(binder, i));
        }
    }

    /**
     * Get the item type enum associated with position the position.
     *
     * @param position Position to get the corresponding {@code enum}.
     *
     * @return {@code enum} corresponding to the given position.
     */
    public abstract E getEnumFromPosition(int position);

    /**
     * Get the item type enum according to the ordinal.
     *
     * @param ordinal Ordinal to get the corresponding {@code enum}.
     *
     * @return {@code enum} corresponding to the given ordinal.
     */
    public abstract E getEnumFromOrdinal(int ordinal);

    public E getEnumFromBinder(DynamicRecyclerViewBinder binder) {
        for (Map.Entry<E, DynamicRecyclerViewBinder> entry : mDataBinderMap.entrySet()) {
            if (entry.getValue().equals(binder)) {
                return entry.getKey();
            }
        }

        throw new IllegalArgumentException("Invalid data binder.");
    }

    /**
     * Get dynamic data binder according to the supplied {@code enum}.
     */
    public <T extends DynamicRecyclerViewBinder> T getDataBinderBinder(E e) {
        return (T) mDataBinderMap.get(e);
    }

    /**
     * Getter for {@link #mDataBinderMap}
     */
    public Map<E, DynamicRecyclerViewBinder> getDataBinderMap() {
        return mDataBinderMap;
    }

    /**
     * Add data binders to display in this adapter.
     *
     * @param binder DynamicTheme data binder to be added in this adapter.
     *
     * @see #mDataBinderMap
     */
    public void putDataBinder(E e, DynamicRecyclerViewBinder binder) {
        mDataBinderMap.put(e, binder);
    }
}
