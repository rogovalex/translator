package ru.rogovalex.translator;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import javax.inject.Inject;

import ru.rogovalex.translator.domain.translate.TranslateResult;
import ru.rogovalex.translator.presentation.injection.component.HistoryFragmentComponent;
import ru.rogovalex.translator.presentation.translate.HistoryView;
import ru.rogovalex.translator.presentation.translate.HistoryViewPresenter;

public class HistoryFragment extends Fragment
        implements HistoryView,
        ListAdapter.OnFavoriteChangedListener {

    private EditText mSearchInput;
    private ListAdapter mAdapter;

    private Callbacks mCallbacks;

    @Inject
    HistoryViewPresenter mPresenter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        final CardView cardView = (CardView) view.findViewById(R.id.search_bar);
        mSearchInput = (EditText) cardView.findViewById(R.id.search_input);
        mSearchInput.setHint(R.string.search_history_hint);
        final View clear = cardView.findViewById(R.id.clear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchInput.setText("");
            }
        });
        clear.setVisibility(mSearchInput.getText().length() == 0
                ? View.GONE : View.VISIBLE);

        mSearchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                clear.setVisibility(s.length() == 0 ? View.GONE : View.VISIBLE);
                mAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mCallbacks.getHistoryFragmentComponent().inject(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.setView(this);
        mPresenter.loadHistory();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.setView(null);
    }

    @Override
    public void onHistoryLoading() {

    }

    @Override
    public void onHistoryLoaded(List<TranslateResult> items) {
        mAdapter.setItems(items, mSearchInput.getText().toString());
    }

    @Override
    public void onHistoryLoadError(Throwable e) {

    }

    @Override
    public void onFavoriteChanged(TranslateResult item) {
        mPresenter.updateFavorite(item);
    }

    public interface Callbacks {
        HistoryFragmentComponent getHistoryFragmentComponent();
    }
}
