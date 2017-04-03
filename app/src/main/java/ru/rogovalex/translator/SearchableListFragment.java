package ru.rogovalex.translator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.List;

import ru.rogovalex.translator.domain.translate.TranslateResult;

public abstract class SearchableListFragment extends Fragment
        implements ListAdapter.OnFavoriteChangedListener, TextWatcher {

    private EditText mSearchInput;
    private View mClear;
    private ListAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        final CardView cardView = (CardView) view.findViewById(R.id.search_bar);
        mSearchInput = (EditText) cardView.findViewById(R.id.search_input);
        mSearchInput.setHint(R.string.search_history_hint);
        mClear = cardView.findViewById(R.id.clear);
        mClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchInput.setText("");
            }
        });

        mSearchInput.addTextChangedListener(this);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));
        mAdapter = new ListAdapter();
        mAdapter.setFavoriteChangedListener(this);
        recyclerView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mClear.setVisibility(s.length() == 0 ? View.GONE : View.VISIBLE);
        mAdapter.getFilter().filter(s);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    protected void setAdapterItems(List<TranslateResult> items) {
        mAdapter.setItems(items, mSearchInput.getText().toString());
    }
}
