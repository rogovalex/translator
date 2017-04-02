package ru.rogovalex.translator;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.rogovalex.translator.domain.translate.TranslateResult;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 01.04.2017
 * Time: 23:30
 */
public class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<TranslateResult> mItems = new ArrayList<>();
    private OnFavoriteChangedListener mListener;

    public void setItems(List<TranslateResult> items) {
        mItems.clear();
        mItems.addAll(items);
        notifyDataSetChanged();
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
        ((ItemViewHolder) holder).bind(mItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView favIcon;
        TextView text;
        TextView translation;
        TextView lang;

        TranslateResult mItem;

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
        }

        public void bind(TranslateResult item) {
            mItem = item;
            updateFavIcon();
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
    }

    public interface OnFavoriteChangedListener {
        void onFavoriteChanged(TranslateResult item);
    }
}
