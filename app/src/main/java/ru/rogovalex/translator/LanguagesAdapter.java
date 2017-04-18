package ru.rogovalex.translator;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.rogovalex.translator.domain.translate.Language;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 18.04.2017
 * Time: 11:07
 */
public class LanguagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements Filterable {

    private List<Language> mItems = new ArrayList<>();
    private List<Language> mVisibleItems = new ArrayList<>();
    private OnItemClickListener mListener;

    public void setItems(List<Language> items, String constraint) {
        mItems = items;
        mVisibleItems.clear();
        mVisibleItems.addAll(mItems);
        getFilter().filter(constraint);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_item_lang, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ItemViewHolder) holder).bind(mVisibleItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mVisibleItems.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                List<Language> filtered = new ArrayList<>();
                List<Language> secondary = new ArrayList<>();

                String constraintStr = constraint.toString().toLowerCase();
                if (constraintStr.isEmpty()) {
                    filtered = mItems;
                } else {
                    for (Language item : mItems) {
                        String name = item.getName().toLowerCase();
                        if (name.startsWith(constraintStr)) {
                            filtered.add(item);
                        } else if (name.contains(constraintStr)) {
                            secondary.add(item);
                        }
                    }
                }

                filtered.addAll(secondary);
                results.count = filtered.size();
                results.values = filtered;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mVisibleItems.clear();
                //noinspection unchecked
                mVisibleItems.addAll((List<Language>) results.values);
                notifyDataSetChanged();
            }
        };
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView title;

        Language mItem;

        public ItemViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onItemClick(mItem);
                    }
                }
            });
        }

        public void bind(Language item) {
            mItem = item;
            title.setText(item.getName());
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Language item);
    }
}
