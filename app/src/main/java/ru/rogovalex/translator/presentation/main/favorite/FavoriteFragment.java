package ru.rogovalex.translator.presentation.main.favorite;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.Filter;

import java.util.List;

import javax.inject.Inject;

import ru.rogovalex.translator.R;
import ru.rogovalex.translator.domain.model.Translation;
import ru.rogovalex.translator.presentation.common.SearchableListFragment;
import ru.rogovalex.translator.presentation.injection.component.FavoriteFragmentComponent;
import ru.rogovalex.translator.presentation.main.common.ListAdapter;

public class FavoriteFragment extends SearchableListFragment
        implements ListAdapter.OnFavoriteChangedListener, FavoriteView {

    private ListAdapter mAdapter;

    private Callbacks mCallbacks;

    @Inject
    FavoriteViewPresenter mPresenter;

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
        ((EditText) view.findViewById(R.id.search_input))
                .setHint(R.string.search_favorite_hint);
        ((RecyclerView) view.findViewById(R.id.recycler_view))
                .addItemDecoration(new DividerItemDecoration(getContext(),
                        DividerItemDecoration.VERTICAL));
        mAdapter.setFavoriteChangedListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mCallbacks.getFavoriteFragmentComponent().inject(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.setView(this);
        mPresenter.loadFavorite();
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
        mPresenter.loadFavorite();
    }

    @Override
    public void onFavoriteLoading() {
        showLoadingView();
    }

    @Override
    public void onFavoriteLoaded(List<Translation> items) {
        mAdapter.setItems(items, getQuery());
        showListView();
    }

    @Override
    public void onFavoriteLoadError(Throwable e) {
        showErrorView(e.getMessage());
    }

    @Override
    public void onFavoriteChanged(Translation item) {
        mPresenter.updateFavorite(item);
    }

    public interface Callbacks {
        FavoriteFragmentComponent getFavoriteFragmentComponent();
    }
}
