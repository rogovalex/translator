package ru.rogovalex.translator;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.rogovalex.translator.api.Entry;
import ru.rogovalex.translator.api.Translation;
import ru.rogovalex.translator.domain.translate.TranslateResult;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 01.04.2017
 * Time: 23:30
 */
public class TranslationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<AdapterItem> mItems = new ArrayList<>();


    public void setTranslation(TranslateResult translation) {
        mItems.clear();

        mItems.add(new MainItem(translation.getTranslation()));
        for (Entry e : translation.getEntries()) {
            mItems.add(new DefinitionItem(e));
            int index = 0;
            for (Translation t : e.getTranslations()) {
                index++;
                mItems.add(new TranslationItem(index, t));
            }
        }

        notifyDataSetChanged();
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

        public MainItemViewHolder(View itemView) {
            super(itemView);

            text = (TextView) itemView.findViewById(R.id.text);
        }

        public void bind(MainItem item) {
            text.setText(item.text);
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
            text.setText(item.entry.getText());

            if (TextUtils.isEmpty(item.entry.getPos())) {
                pos.setVisibility(View.GONE);
            } else {
                pos.setText(item.entry.getPos());
                pos.setVisibility(View.VISIBLE);
            }

            if (TextUtils.isEmpty(item.entry.getTranscription())) {
                transcription.setVisibility(View.GONE);
            } else {
                transcription.setText(itemView.getResources().getString(
                        R.string.transcription, item.entry.getTranscription()));
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

            String synonymsText;
            if (item.translation.getSynonyms() != null
                    && item.translation.getSynonyms().length > 0) {
                synonymsText = join(item.translation.getText(),
                        item.translation.getSynonyms());
            } else {
                synonymsText = item.translation.getText();
            }
            synonyms.setText(synonymsText);

            if (item.translation.getMeanings() != null
                    && item.translation.getMeanings().length > 0) {
                meanings.setText(join("", item.translation.getMeanings()));
                meanings.setVisibility(View.VISIBLE);
            } else {
                meanings.setVisibility(View.GONE);
            }

            if (item.translation.getExamples() != null
                    && item.translation.getExamples().length > 0) {
                examples.setText(join("", item.translation.getExamples()));
                examples.setVisibility(View.VISIBLE);
            } else {
                examples.setVisibility(View.GONE);
            }
        }
    }

    private interface AdapterItem {
        int getType();

        void bind(RecyclerView.ViewHolder viewHolder);
    }

    private static class MainItem implements AdapterItem {
        static final int TYPE = 0;

        String text;

        public MainItem(String text) {
            this.text = text;
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

        Entry entry;

        public DefinitionItem(Entry entry) {
            this.entry = entry;
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

    private String join(String value, Translation... items) {
        StringBuilder sb = new StringBuilder(value);
        for (Translation item : items) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(item.getText());
        }
        return sb.toString();
    }
}
