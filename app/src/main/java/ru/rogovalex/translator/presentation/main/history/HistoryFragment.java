package ru.rogovalex.translator.presentation.main.history;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.view.View;
import android.widget.Filter;

import java.util.List;

import javax.inject.Inject;

import ru.rogovalex.translator.R;
import ru.rogovalex.translator.domain.model.Translation;
import ru.rogovalex.translator.presentation.common.SearchableListFragment;
import ru.rogovalex.translator.presentation.injection.component.HistoryFragmentComponent;
import ru.rogovalex.translator.presentation.main.common.ListAdapter;

public class HistoryFragment extends SearchableListFragment
        implements ListAdapter.OnFavoriteChangedListener, HistoryView {

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new ListAdapter();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getSearchInput().setHint(R.string.search_history_hint);
        getRecyclerView().addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));
        mAdapter.setFavoriteChangedListener(this);
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
    protected void cancelLoading() {
        mPresenter.cancel();
    }

    @Override
    protected ListAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    protected Filter getFilter() {
        return mAdapter.getFilter();
    }

    @Override
    protected void onErrorButtonClick() {
        mPresenter.loadHistory();
    }

    @Override
    public void onHistoryLoading() {
        showLoadingView();
    }

    @Override
    public void onHistoryLoaded(List<Translation> items) {
        mAdapter.setItems(items, getQuery());
        showListView();
    }

    @Override
    public void onHistoryLoadError(Throwable e) {
        showErrorView(e.getMessage());
    }

    @Override
    public void onFavoriteChanged(Translation item) {
        mPresenter.updateFavorite(item);
    }

    public interface Callbacks {
        HistoryFragmentComponent getHistoryFragmentComponent();
    }
}
