package ru.rogovalex.translator;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.rogovalex.translator.domain.translate.Definition;
import ru.rogovalex.translator.domain.translate.TranslateResult;
import ru.rogovalex.translator.domain.translate.Translation;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 01.04.2017
 * Time: 23:30
 */
public class TranslationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<AdapterItem> mItems = new ArrayList<>();
    private OnFavoriteChangedListener mListener;

    public void setTranslation(TranslateResult translation) {
        mItems.clear();

        mItems.add(new MainItem(translation));
        for (Definition def : translation.getDefinitions()) {
            mItems.add(new DefinitionItem(def));
            int index = 0;
            for (Translation t : def.getTranslations()) {
                index++;
                mItems.add(new TranslationItem(index, t));
            }
        }

        notifyDataSetChanged();
    }

    public void clear() {
        mItems.clear();
        notifyDataSetChanged();
    }

    public void setFavoriteChangedListener(OnFavoriteChangedListener listener) {
        mListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        return mItems.get(position).getType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case MainItem.TYPE:
                return createMainItemViewHolder(parent);
            case DefinitionItem.TYPE:
                return createDefinitionItemViewHolder(parent);
            case TranslationItem.TYPE:
                return createTranslationItemViewHolder(parent);
            default:
                throw new IllegalArgumentException("Unknown view type " + viewType);
        }
    }

    private RecyclerView.ViewHolder createMainItemViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_item_main, parent, false);
        return new MainItemViewHolder(view);
    }

    private RecyclerView.ViewHolder createDefinitionItemViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_item_definition, parent, false);
        return new DefinitionItemViewHolder(view);
    }

    private RecyclerView.ViewHolder createTranslationItemViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_item_translation, parent, false);
        return new TranslationItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        mItems.get(position).bind(holder);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    private class MainItemViewHolder extends RecyclerView.ViewHolder {

        TextView text;
        ImageView favIcon;

        TranslateResult mItem;

        public MainItemViewHolder(View itemView) {
            super(itemView);

            text = (TextView) itemView.findViewById(R.id.text);
            favIcon = (ImageView) itemView.findViewById(R.id.fav_icon);

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

        public void bind(MainItem item) {
            mItem = item.translation;
            updateFavIcon();
            text.setText(mItem.getTranslation());
        }

        private void updateFavIcon() {
            favIcon.setImageResource(mItem.isFavorite()
                    ? R.drawable.ic_favorite_accent_24dp
                    : R.drawable.ic_favorite_border_gray_24dp);
        }
    }

    private class DefinitionItemViewHolder extends RecyclerView.ViewHolder {

        TextView text;
        TextView transcription;
        TextView pos;

        public DefinitionItemViewHolder(View itemView) {
            super(itemView);

            text = (TextView) itemView.findViewById(R.id.text);
            transcription = (TextView) itemView.findViewById(R.id.transcription);
            pos = (TextView) itemView.findViewById(R.id.pos);
        }

        public void bind(DefinitionItem item) {
            text.setText(item.definition.getText());

            if (TextUtils.isEmpty(item.definition.getPos())) {
                pos.setVisibility(View.GONE);
            } else {
                pos.setText(item.definition.getPos());
                pos.setVisibility(View.VISIBLE);
            }

            if (TextUtils.isEmpty(item.definition.getTranscription())) {
                transcription.setVisibility(View.GONE);
            } else {
                transcription.setText(itemView.getResources().getString(
                        R.string.transcription, item.definition.getTranscription()));
                transcription.setVisibility(View.VISIBLE);
            }
        }
    }

    private class TranslationItemViewHolder extends RecyclerView.ViewHolder {

        TextView index;
        TextView synonyms;
        TextView meanings;
        TextView examples;

        public TranslationItemViewHolder(View itemView) {
            super(itemView);

            index = (TextView) itemView.findViewById(R.id.index);
            synonyms = (TextView) itemView.findViewById(R.id.synonyms);
            meanings = (TextView) itemView.findViewById(R.id.meanings);
            examples = (TextView) itemView.findViewById(R.id.examples);
        }

        public void bind(TranslationItem item) {
            index.setText(String.valueOf(item.index));
            synonyms.setText(item.translation.getSynonyms());

            if (TextUtils.isEmpty(item.translation.getMeanings())) {
                meanings.setVisibility(View.GONE);
            } else {
                meanings.setText(item.translation.getMeanings());
                meanings.setVisibility(View.VISIBLE);
            }

            if (TextUtils.isEmpty(item.translation.getExamples())) {
                examples.setVisibility(View.GONE);
            } else {
                examples.setText(item.translation.getExamples());
                examples.setVisibility(View.VISIBLE);
            }
        }
    }

    private interface AdapterItem {
        int getType();

        void bind(RecyclerView.ViewHolder viewHolder);
    }

    private static class MainItem implements AdapterItem {
        static final int TYPE = 0;

        TranslateResult translation;

        public MainItem(TranslateResult translation) {
            this.translation = translation;
        }

        @Override
        public int getType() {
            return TYPE;
        }

        @Override
        public void bind(RecyclerView.ViewHolder viewHolder) {
            if (viewHolder instanceof MainItemViewHolder) {
                ((MainItemViewHolder) viewHolder).bind(this);
            }
        }
    }

    private static class DefinitionItem implements AdapterItem {
        static final int TYPE = 1;

        Definition definition;

        public DefinitionItem(Definition definition) {
            this.definition = definition;
        }

        @Override
        public int getType() {
            return TYPE;
        }

        @Override
        public void bind(RecyclerView.ViewHolder viewHolder) {
            if (viewHolder instanceof DefinitionItemViewHolder) {
                ((DefinitionItemViewHolder) viewHolder).bind(this);
            }
        }
    }

    private static class TranslationItem implements AdapterItem {
        static final int TYPE = 2;

        int index;
        Translation translation;

        public TranslationItem(int index, Translation translation) {
            this.index = index;
            this.translation = translation;
        }

        @Override
        public int getType() {
            return TYPE;
        }

        @Override
        public void bind(RecyclerView.ViewHolder viewHolder) {
            if (viewHolder instanceof TranslationItemViewHolder) {
                ((TranslationItemViewHolder) viewHolder).bind(this);
            }
        }
    }

    public interface OnFavoriteChangedListener {
        void onFavoriteChanged(TranslateResult item);
    }
}
