package edu.missouri.mca.android.practice2.databinding;

import android.databinding.BindingAdapter;
import android.databinding.ObservableList;
import android.widget.ImageView;
import android.widget.ListView;

import edu.missouri.mca.android.practice2.Application;

/**
 * Static methods for use by generated code in the Android data binding library.
 */

@SuppressWarnings({"unused"})
public final class BindingAdapters {
    private BindingAdapters() {
        // Prevent instantiation.
    }

    @BindingAdapter({"items", "layout"})
    public static <E> void setItems(final ListView view,
                                    final ObservableList<E> oldList, final int oldLayoutId,
                                    final ObservableList<E> newList, final int newLayoutId) {
        if (oldList == newList && oldLayoutId == newLayoutId)
            return;
        // The ListAdapter interface is not generic, so this cannot be checked.
        @SuppressWarnings("unchecked") ObservableListAdapter<E> adapter =
                (ObservableListAdapter<E>) view.getAdapter();
        // If the layout changes, any existing adapter must be replaced.
        if (adapter != null && oldList != null && oldLayoutId != newLayoutId) {
            adapter.setList(null);
            adapter = null;
        }
        // Avoid setting an adapter when there is no new list or layout.
        if (newList == null || newLayoutId == 0)
            return;
        if (adapter == null) {
            adapter = new ObservableListAdapter<>(view.getContext(), newLayoutId, newList);
            view.setAdapter(adapter);
        }
        // Either the list changed, or this is an entirely new listener because the layout changed.
        adapter.setList(newList);
    }

    @BindingAdapter({"android:src"})
    public static void setSrc(final ImageView view, final String uri) {
        Application.getComponent().getPicasso().load(uri).into(view);
    }
}
