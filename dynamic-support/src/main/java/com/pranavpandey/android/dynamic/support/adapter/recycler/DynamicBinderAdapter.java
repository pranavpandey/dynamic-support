package com.pranavpandey.android.dynamic.support.adapter.recycler;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * @author Pranav Pandey
 */
public class DynamicBinderAdapter<S extends Section, V extends ViewType>
        extends DynamicRecyclerAdapter<V, RecyclerView.ViewHolder> {

    private final SectionBinderHolder<S, V, RecyclerView.ViewHolder> mSectionBinderHolder
            = new SectionBinderHolder<>();
    private final Object mLock = new Object();
    private boolean notifyOnChange = true;

    public int getSectionPosition(final S section) {
        return mSectionBinderHolder.getSectionIndex(section);
    }

    public int getSectionSize(final S section) {
        return mSectionBinderHolder.getSectionSize(section);
    }

    public <B extends Binder<V, RecyclerView.ViewHolder>> void add(final S section, final B item) {
        insert(section, item, mSectionBinderHolder.getSectionSize(section));
    }

    public <B extends Binder<V, RecyclerView.ViewHolder>> void addAll(final S section,
            final List<B> items) {
        insertAll(section, items, mSectionBinderHolder.getSectionSize(section));
    }

    public <B extends Binder<V, RecyclerView.ViewHolder>> void addIfEmpty(final S section,
            final B item) {
        if (mSectionBinderHolder.isEmpty(section)) {
            insert(section, item, mSectionBinderHolder.getSectionSize(section));
        }
    }

    public <B extends Binder<V, RecyclerView.ViewHolder>> void insert(final S section, final B item,
            final int index) {
        synchronized (mLock) {
            mSectionBinderHolder.insert(section, item, index);
            insert(item, mSectionBinderHolder.getSectionIndex(section) + index);
            if (notifyOnChange) {
                notifyDataSetChanged();
            }
        }
    }

    public <B extends Binder<V, RecyclerView.ViewHolder>> void insertAll(final S section,
            final List<B> items, final int index) {
        synchronized (mLock) {
            final int sectionIndex = mSectionBinderHolder.getSectionIndex(section);
            int insertPosition = index;
            for (final Binder<V, RecyclerView.ViewHolder> item : items) {
                mSectionBinderHolder.insert(section, item, index);
                insert(item, sectionIndex + insertPosition);
                insertPosition++;
            }
            if (notifyOnChange) {
                notifyDataSetChanged();
            }
        }
    }

    public <B extends Binder<V, RecyclerView.ViewHolder>> void remove(final S section,
            final B item) {
        synchronized (mLock) {
            mSectionBinderHolder.remove(section, item);
            remove(item);
            if (notifyOnChange) {
                notifyDataSetChanged();
            }
        }
    }

    public <B extends Binder<V, RecyclerView.ViewHolder>> void removeAll(final S section,
            final List<B> items) {
        synchronized (mLock) {
            for (final Binder<V, RecyclerView.ViewHolder> item : items) {
                mSectionBinderHolder.remove(section, item);
                remove(item);
            }
            if (notifyOnChange) {
                notifyDataSetChanged();
            }
        }
    }

    public void removeAll(final S section) {
        synchronized (mLock) {
            final List<Binder<V, RecyclerView.ViewHolder>> removeItems = mSectionBinderHolder
                    .getAllItem(section);
            for (final Binder<V, RecyclerView.ViewHolder> removeItem : removeItems) {
                remove(removeItem);
            }
            mSectionBinderHolder.clear(section);
            if (notifyOnChange) {
                notifyDataSetChanged();
            }
        }
    }

    public <B extends Binder<V, RecyclerView.ViewHolder>> void replaceAll(final S section,
            final B item) {
        synchronized (mLock) {
            final List<Binder<V, RecyclerView.ViewHolder>> removeItems = mSectionBinderHolder
                    .getAllItem(section);
            for (final Binder<V, RecyclerView.ViewHolder> removeItem : removeItems) {
                remove(removeItem);
            }
            removeItems.clear();
            final int moduleIndex = mSectionBinderHolder.getSectionIndex(section);
            mSectionBinderHolder.add(section, item);
            insert(item, moduleIndex);
            if (notifyOnChange) {
                notifyDataSetChanged();
            }
        }
    }

    public <B extends Binder<V, RecyclerView.ViewHolder>> void replaceAll(final S section,
            final List<B> items) {
        synchronized (mLock) {
            final List<Binder<V, RecyclerView.ViewHolder>> removeItems = mSectionBinderHolder
                    .getAllItem(section);
            for (final Binder<V, RecyclerView.ViewHolder> removeItem : removeItems) {
                remove(removeItem);
            }
            removeItems.clear();
            final int sectionIndex = mSectionBinderHolder.getSectionIndex(section);
            int insertPosition = 0;
            for (final Binder<V, RecyclerView.ViewHolder> item : items) {
                mSectionBinderHolder.insert(section, item, insertPosition);
                insert(item, sectionIndex + insertPosition);
                insertPosition++;
            }
            if (notifyOnChange) {
                notifyDataSetChanged();
            }
        }
    }

    public List<? extends Binder<V, RecyclerView.ViewHolder>> getAllItem(final S section) {
        return mSectionBinderHolder.getAllItem(section);
    }

    public Binder<V, RecyclerView.ViewHolder> getItem(final S section, final int index) {
        return mSectionBinderHolder.getItem(section, index);
    }

    public boolean isEmpty(final S section) {
        return mSectionBinderHolder.isEmpty(section);
    }

    @Override
    public void clear() {
        super.clear();
        mSectionBinderHolder.clear();
    }

    public void setNotifyOnChange(boolean notifyOnChange) {
        this.notifyOnChange = notifyOnChange;
    }
}
