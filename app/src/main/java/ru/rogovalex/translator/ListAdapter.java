package ru.rogovalex.translator;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.rogovalex.translator.domain.model.Translation;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 01.04.2017
 * Time: 23:30
 */
public class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements Filterable {

    private List<Translation> mItems = new ArrayList<>();
    private List<Translation> mVisibleItems = new ArrayList<>();
    private OnFavoriteChangedListener mListener;

    public void setItems(List<Translation> items, String constraint) {
        mItems = items;
        mVisibleItems.clear();
        mVisibleItems.addAll(items);
        getFilter().filter(constraint);
    }

    public void setFavoriteChangedListener(OnFavoriteChangedListener listener) {
        mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_item_list, parent, false);
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
                List<Translation> filtered = new ArrayList<>();
                List<Translation> secondary = new ArrayList<>();

                String prefix = constraint.toString().toLowerCase();
                if (prefix.isEmpty()) {
                    filtered = mItems;
                } else {
                    for (Translation item : mItems) {
                        String text = item.getText().toLowerCase();
                        String translation = item.getTranslation().toLowerCase();
                        if (text.startsWith(prefix) || translation.startsWith(prefix)) {
                            filtered.add(item);
                        } else if (text.contains(prefix) || translation.contains(prefix)) {
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
                mVisibleItems.addAll((List<Translation>) results.values);
                notifyDataSetChanged();
            }
        };
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView favIcon;
        TextView text;
        TextView translation;
        TextView lang;

        Translation mItem;

        public ItemViewHolder(View itemView) {
            super(itemView);

            favIcon = (ImageView) itemView.findViewById(R.id.fav_icon);
            text = (TextView) itemView.findViewById(R.id.text);
            translation = (TextView) itemView.findViewById(R.id.translation);
            lang = (TextView) itemView.findViewById(R.id.lang);

            favIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItem.setFavorite(!mItem.isFavorite());
                    updateFavIcon();
                    if (mListener != null) {
                        mListener.onFavoriteChanged(mItem);
                    }
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int maxLines = text.getMaxLines() == 1 ? Integer.MAX_VALUE : 1;
                    updateTextMaxLines(maxLines);
                }
            });
        }

        public void bind(Translation item) {
            mItem = item;
            updateFavIcon();
            updateTextMaxLines(1);
            text.setText(item.getText());
            translation.setText(item.getTranslation());
            lang.setText(itemView.getResources().getString(
                    R.string.translation_direction, item.getTextLang(),
                    item.getTranslationLang()));
        }

        private void updateFavIcon() {
            favIcon.setImageResource(mItem.isFavorite()
                    ? R.drawable.ic_favorite_accent_24dp
                    : R.drawable.ic_favorite_border_gray_24dp);
        }

        private void updateTextMaxLines(int maxLines) {
            text.setMaxLines(maxLines);
            translation.setMaxLines(maxLines);
        }
    }

    public interface OnFavoriteChangedListener {
        void onFavoriteChanged(Translation item);
    }
}
