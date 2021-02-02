package com.pranavpandey.android.dynamic.support.recyclerview.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pranavpandey.android.dynamic.support.recyclerview.binder.DynamicRecyclerViewBinder;

import java.util.HashMap;
import java.util.Map;

/**
 * A {@link DynamicBinderAdapter} to display different type of {@link VB} inside a recycler view.
 */
public abstract class DynamicTypeBinderAdapter<E extends Enum<E>,
        VB extends DynamicRecyclerViewBinder<?>> extends DynamicBinderAdapter<VB> {

    /**
     * Default item type {@code enums} for this adapter.
     */
    public enum ItemViewType {

        /**
         * Enum for the type unknown.
         */
        UNKNOWN,

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
         * Enum for the setting item.
         */
        SETTING,

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
        for (VB binder : getDataBinderMap().values()) {
            itemCount += binder.getItemCount();
        }

        return itemCount;
    }

    @Override
    public int getItemViewType(int position) {
        return getEnumFromPosition(position).ordinal();
    }

    @Override
    public @NonNull VB getDataBinder(int viewType) {
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
    public @NonNull E getEnumFromPosition(int position) {
        return getEnumFromOrdinal(getItemViewType(position));
    }

    /**
     * Get the item type enum according to the ordinal.
     *
     * @param viewType The ordinal to get the corresponding {@code enum}.
     *
     * @return The {@code enum} corresponding to the given ordinal.
     */
    public abstract @NonNull E getEnumFromOrdinal(int viewType);

    public @NonNull E getEnumFromBinder(@NonNull VB binder) {
        for (Map.Entry<E, VB> entry : getDataBinderMap().entrySet()) {
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
    public VB getDataBinderBinder(@NonNull E e) {
        return getDataBinderMap().get(e);
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
    public void putDataBinder(@Nullable E e, @NonNull VB binder) {
        getDataBinderMap().put(e, binder);
    }
}
