package com.pranavpandey.android.dynamic.support.adapter.recycler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Pranav Pandey
 */
public class SectionBinderHolder<S extends Section, V extends ViewType, VH> {

    private final List<List<Binder<V, VH>>> mSectionItemsList = new ArrayList<>();

    public int getSectionIndex(S section) {
        initSections(section);
        final int sectionPosition = section.position();
        if (sectionPosition == 0) {
            return 0;
        }
        int index = 0;
        for (final List<Binder<V, VH>> items : mSectionItemsList
                .subList(0, sectionPosition)) {
            index += items.size();
        }
        return index;
    }

    public int getSectionSize(final S section) {
        initSections(section);
        return getSectionSize(section.position());
    }

    public <B extends Binder<V, VH>> void add(final S section, final B item) {
        initSections(section);
        mSectionItemsList.get(section.position()).add(item);
    }

    public <B extends Binder<V, VH>> void insert(final S section, final B item,
            final int index) {
        initSections(section);
        mSectionItemsList.get(section.position()).add(index, item);
    }

    public <B extends Binder<V, VH>> void remove(final S section, final B item) {
        initSections(section);
        mSectionItemsList.get(section.position()).remove(item);
    }

    public void clear(final S section) {
        initSections(section);
        mSectionItemsList.get(section.position()).clear();
    }

    public List<Binder<V, VH>> getAllItem(final S section) {
        initSections(section);
        return mSectionItemsList.get(section.position());
    }

    public Binder<V, VH> getItem(final S section, final int index) {
        initSections(section);
        final List<Binder<V, VH>> list = mSectionItemsList
                .get(section.position());
        if (list.size() <= index) {
            return null;
        }
        return list.get(index);
    }

    public boolean isEmpty(final S section) {
        initSections(section);
        final List<Binder<V, VH>> list = mSectionItemsList
                .get(section.position());
        return list.isEmpty();
    }

    public void clear() {
        for (final List<Binder<V, VH>> items : mSectionItemsList) {
            items.clear();
        }
        mSectionItemsList.clear();
    }

    private void initSections(final S section) {
        final int sectionPosition = section.position();
        if (sectionPosition < mSectionItemsList.size()){
            return;
        }
        for (int i = 0; i <= sectionPosition; i++) {
            mSectionItemsList.add(new ArrayList<Binder<V, VH>>());
        }
    }

    private int getSectionSize(final int sectionPosition) {
        return mSectionItemsList.get(sectionPosition).size();
    }
}
