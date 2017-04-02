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

    public void setItems(List<TranslateResult> items) {
        mItems.clear();
        mItems.addAll(items);
        notifyDataSetChanged();
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

        public ItemViewHolder(View itemView) {
            super(itemView);

            favIcon = (ImageView) itemView.findViewById(R.id.fav_icon);
            text = (TextView) itemView.findViewById(R.id.text);
            translation = (TextView) itemView.findViewById(R.id.translation);
            lang = (TextView) itemView.findViewById(R.id.lang);
        }

        public void bind(TranslateResult item) {
            favIcon.setImageResource(item.isFavorite()
                    ? R.drawable.ic_favorite_accent_24dp
                    : R.drawable.ic_favorite_border_gray_24dp);
            text.setText(item.getText());
            translation.setText(item.getTranslation());
            lang.setText(itemView.getResources().getString(
                    R.string.translation_direction, item.getTextLang(),
                    item.getTranslationLang()));
        }
    }
}
