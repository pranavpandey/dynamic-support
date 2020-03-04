package com.pranavpandey.android.dynamic.support.recyclerview.adapter;

import androidx.annotation.NonNull;

import com.pranavpandey.android.dynamic.support.recyclerview.binder.DynamicRecyclerViewBinder;

import java.util.HashMap;
import java.util.Map;

/**
 * A recycler view adapter to display different type of {@link VB}
 * inside a recycler view.
 */
public abstract class DynamicTypeBinderAdapter<E extends Enum<E>, 
        VB extends DynamicRecyclerViewBinder> extends DynamicBinderAdapter<VB> {

    /**
     * Default item type {@code enums} for this adapter.
     */
    public enum ItemViewType {

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
    private Map<E, VB> mDataBinderMap = new HashMap<>();

    @Override
    public int getItemCount() {
        int itemCount = 0;
        for (VB binder : mDataBinderMap.values()) {
            itemCount += binder.getItemCount();
        }

        return itemCount;
    }

    @Override
    public int getItemViewType(int position) {
        return getEnumFromPosition(position).ordinal();
    }

    @Override
    public VB getDataBinder(int viewType) {
        return getDataBinderBinder(getEnumFromOrdinal(viewType));
    }

    @Override
    public int getPosition(@NonNull VB binder, int binderPosition) {
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
    public void notifyBinderItemRangeChanged(@NonNull VB binder,
            int positionStart, int itemCount) {
        for (int i = positionStart; i <= itemCount; i++) {
            notifyItemChanged(getPosition(binder, i));
        }
    }

    @Override
    public void notifyBinderItemRangeInserted(@NonNull VB binder,
            int positionStart, int itemCount) {
        for (int i = positionStart; i <= itemCount; i++) {
            notifyItemInserted(getPosition(binder, i));
        }
    }

    @Override
    public void notifyBinderItemRangeRemoved(@NonNull VB binder,
            int positionStart, int itemCount) {
        for (int i = positionStart; i <= itemCount; i++) {
            notifyItemRemoved(getPosition(binder, i));
        }
    }

    /**
     * Get the item type enum associated with position the position.
     *
     * @param position The position to get the corresponding {@code enum}.
     *
     * @return The {@code enum} corresponding to the given position.
     */
    public abstract @NonNull E getEnumFromPosition(int position);

    /**
     * Get the item type enum according to the ordinal.
     *
     * @param ordinal The ordinal to get the corresponding {@code enum}.
     *
     * @return The {@code enum} corresponding to the given ordinal.
     */
    public abstract E getEnumFromOrdinal(int ordinal);

    public E getEnumFromBinder(VB binder) {
        for (Map.Entry<E, VB> entry : mDataBinderMap.entrySet()) {
            if (entry.getValue().equals(binder)) {
                return entry.getKey();
            }
        }

        throw new IllegalArgumentException("Invalid data binder.");
    }

    /**
     * Returns the dynamic data binder according to the supplied {@code enum}.
     *
     * @param e The data binder enum.
     *
     * @return The dynamic data binder according to the supplied {@code enum}.
     */
    public VB getDataBinderBinder(E e) {
        return mDataBinderMap.get(e);
    }

    /**
     * Get the map to hold the data binders.
     *
     * @return The map to hold the data binders.
     */
    public @NonNull Map<E, VB> getDataBinderMap() {
        return mDataBinderMap;
    }

    /**
     * Add data binders to display in this adapter.
     *
     * @param e The data binder enum.
     * @param binder The data binder to be added in this adapter.
     */
    public void putDataBinder(E e, VB binder) {
        mDataBinderMap.put(e, binder);
    }
}
