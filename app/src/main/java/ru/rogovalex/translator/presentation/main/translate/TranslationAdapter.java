package ru.rogovalex.translator.presentation.main.translate;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.rogovalex.translator.R;
import ru.rogovalex.translator.domain.model.Definition;
import ru.rogovalex.translator.domain.model.DefinitionOption;
import ru.rogovalex.translator.domain.model.Translation;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 01.04.2017
 * Time: 23:30
 */
public class TranslationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<AdapterItem> mItems = new ArrayList<>();
    private OnFavoriteChangedListener mListener;

    public void setTranslation(Translation translation) {
        mItems.clear();

        mItems.add(new MainItem(translation));
        for (Definition def : translation.getDefinitions()) {
            mItems.add(new DefinitionItem(def));
            int index = 0;
            for (DefinitionOption o : def.getDefinitionOptions()) {
                index++;
                mItems.add(new DefinitionOptionItem(index, o));
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
            case DefinitionOptionItem.TYPE:
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
                .inflate(R.layout.view_item_definition_option, parent, false);
        return new DefinitionOptionItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        mItems.get(position).bind(holder);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class MainItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text)
        TextView text;
        @BindView(R.id.fav_icon)
        ImageView favIcon;

        Translation mItem;

        public MainItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(MainItem item) {
            mItem = item.translation;
            updateFavIcon();
            text.setText(mItem.getTranslation());
        }

        @OnClick(R.id.fav_icon)
        void favoriteIconClick() {
            mItem.setFavorite(!mItem.isFavorite());
            updateFavIcon();
            if (mListener != null) {
                mListener.onFavoriteChanged(mItem);
            }
        }

        private void updateFavIcon() {
            favIcon.setImageResource(mItem.isFavorite()
                    ? R.drawable.ic_favorite_accent_24dp
                    : R.drawable.ic_favorite_border_gray_24dp);
        }
    }

    class DefinitionItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text)
        TextView text;
        @BindView(R.id.transcription)
        TextView transcription;
        @BindView(R.id.pos)
        TextView pos;

        public DefinitionItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
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

    class DefinitionOptionItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.index)
        TextView index;
        @BindView(R.id.synonyms)
        TextView synonyms;
        @BindView(R.id.meanings)
        TextView meanings;
        @BindView(R.id.examples)
        TextView examples;

        public DefinitionOptionItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(DefinitionOptionItem item) {
            index.setText(String.valueOf(item.index));
            synonyms.setText(item.option.getSynonyms());

            if (TextUtils.isEmpty(item.option.getMeanings())) {
                meanings.setVisibility(View.GONE);
            } else {
                meanings.setText(item.option.getMeanings());
                meanings.setVisibility(View.VISIBLE);
            }

            if (TextUtils.isEmpty(item.option.getExamples())) {
                examples.setVisibility(View.GONE);
            } else {
                examples.setText(item.option.getExamples());
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

        Translation translation;

        public MainItem(Translation translation) {
            this.translation = translation;
        }

        @Override
        public int getType() {
            return TYPE;
        }

        @Override
        public void bind(RecyclerView.ViewHolder viewHolder) {
            ((MainItemViewHolder) viewHolder).bind(this);
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
            ((DefinitionItemViewHolder) viewHolder).bind(this);
        }
    }

    private static class DefinitionOptionItem implements AdapterItem {
        static final int TYPE = 2;

        int index;
        DefinitionOption option;

        public DefinitionOptionItem(int index, DefinitionOption option) {
            this.index = index;
            this.option = option;
        }

        @Override
        public int getType() {
            return TYPE;
        }

        @Override
        public void bind(RecyclerView.ViewHolder viewHolder) {
            ((DefinitionOptionItemViewHolder) viewHolder).bind(this);
        }
    }

    public interface OnFavoriteChangedListener {
        void onFavoriteChanged(Translation item);
    }
}
