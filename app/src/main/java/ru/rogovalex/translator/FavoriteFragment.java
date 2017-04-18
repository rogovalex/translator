package ru.rogovalex.translator;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.Filter;

import java.util.List;

import javax.inject.Inject;

import ru.rogovalex.translator.domain.translate.TranslateResult;
import ru.rogovalex.translator.presentation.injection.component.FavoriteFragmentComponent;
import ru.rogovalex.translator.presentation.translate.FavoriteView;
import ru.rogovalex.translator.presentation.translate.FavoriteViewPresenter;

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
    protected ListAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    protected Filter getFilter() {
        return mAdapter.getFilter();
    }

    @Override
    public void onFavoriteLoading() {

    }

    @Override
    public void onFavoriteLoaded(List<TranslateResult> items) {
        mAdapter.setItems(items, getQuery());
    }

    @Override
    public void onFavoriteLoadError(Throwable e) {

    }

    @Override
    public void onFavoriteChanged(TranslateResult item) {
        mPresenter.updateFavorite(item);
    }

    public interface Callbacks {
        FavoriteFragmentComponent getFavoriteFragmentComponent();
    }
}
