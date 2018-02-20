package edu.missouri.mca.android.practice2.databinding;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableList;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.lang.ref.WeakReference;

import edu.missouri.mca.android.practice2.BR;

/**
 * A generic {@code ListAdapter} backed by a {@code ObservableKeyedList}.
 */

class ObservableListAdapter<E> extends BaseAdapter {
    private final OnListChangedCallback<E> callback = new OnListChangedCallback<>(this);
    private final int layoutId;
    private final LayoutInflater layoutInflater;
    private ObservableList<E> list;

    ObservableListAdapter(final Context context, final int layoutId,
                          final ObservableList<E> list) {
        this.layoutId = layoutId;
        layoutInflater = LayoutInflater.from(context);
        setList(list);
    }

    @Override
    public int getCount() {
        return list != null ? list.size() : 0;
    }

    @Override
    public E getItem(final int position) {
        if (list == null || position < 0 || position >= list.size())
            return null;
        return list.get(position);
    }

    @Override
    public long getItemId(final int position) {
        return position;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        ViewDataBinding binding = DataBindingUtil.getBinding(convertView);
        if (binding == null)
            binding = DataBindingUtil.inflate(layoutInflater, layoutId, parent, false);
        binding.setVariable(BR.item, getItem(position));
        binding.executePendingBindings();
        return binding.getRoot();
    }

    void setList(final ObservableList<E> newList) {
        if (list != null)
            list.removeOnListChangedCallback(callback);
        list = newList;
        if (list != null) {
            list.addOnListChangedCallback(callback);
        }
        notifyDataSetChanged();
    }

    private static final class OnListChangedCallback<E>
            extends ObservableList.OnListChangedCallback<ObservableList<E>> {

        private final WeakReference<ObservableListAdapter<E>> weakAdapter;

        private OnListChangedCallback(final ObservableListAdapter<E> adapter) {
            weakAdapter = new WeakReference<>(adapter);
        }

        @Override
        public void onChanged(final ObservableList<E> sender) {
            final ObservableListAdapter<E> adapter = weakAdapter.get();
            if (adapter != null)
                adapter.notifyDataSetChanged();
            else
                sender.removeOnListChangedCallback(this);
        }

        @Override
        public void onItemRangeChanged(final ObservableList<E> sender, final int positionStart,
                                       final int itemCount) {
            onChanged(sender);
        }

        @Override
        public void onItemRangeInserted(final ObservableList<E> sender, final int positionStart,
                                        final int itemCount) {
            onChanged(sender);
        }

        @Override
        public void onItemRangeMoved(final ObservableList<E> sender, final int fromPosition,
                                     final int toPosition, final int itemCount) {
            onChanged(sender);
        }

        @Override
        public void onItemRangeRemoved(final ObservableList<E> sender, final int positionStart,
                                       final int itemCount) {
            onChanged(sender);
        }
    }
}
