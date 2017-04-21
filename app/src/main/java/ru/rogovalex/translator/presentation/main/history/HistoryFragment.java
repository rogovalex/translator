package ru.rogovalex.translator.presentation.main.history;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Filter;

import java.util.ArrayList;
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
    private Dialog mDialog;

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
        setHasOptionsMenu(true);
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
    public void onDestroyView() {
        super.onDestroyView();
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.history, menu);

        menu.findItem(R.id.clear).setOnMenuItemClickListener(item -> {
            showConfirmDialog();
            return true;
        });
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
    public void onHistoryCleared() {
        mAdapter.setItems(new ArrayList<>(), getQuery());
    }

    @Override
    public void onFavoriteChanged(Translation item) {
        mPresenter.updateFavorite(item);
    }

    private void showConfirmDialog() {
        if (mDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage(R.string.confirm_clear_history);
            builder.setPositiveButton(R.string.action_clear,
                    ((dialog, which) -> mPresenter.clearHistory()));
            builder.setNegativeButton(R.string.action_cancel, null);
            mDialog = builder.create();
        }
        mDialog.show();
    }

    public interface Callbacks {
        HistoryFragmentComponent getHistoryFragmentComponent();
    }
}
