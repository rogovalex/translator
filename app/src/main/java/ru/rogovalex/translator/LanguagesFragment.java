package ru.rogovalex.translator;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.Filter;

import java.util.List;

import javax.inject.Inject;

import ru.rogovalex.translator.domain.model.Language;
import ru.rogovalex.translator.presentation.injection.component.LanguagesFragmentComponent;
import ru.rogovalex.translator.presentation.translate.LanguagesView;
import ru.rogovalex.translator.presentation.translate.LanguagesViewPresenter;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 18.04.2017
 * Time: 0:05
 */
public class LanguagesFragment extends SearchableListFragment
        implements LanguagesAdapter.OnItemClickListener, LanguagesView {

    private LanguagesAdapter mAdapter;

    private Callbacks mCallbacks;

    @Inject
    LanguagesViewPresenter mPresenter;

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
        mAdapter = new LanguagesAdapter();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ((EditText) view.findViewById(R.id.search_input))
                .setHint(R.string.search_language_hint);
        mAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mCallbacks.getLanguagesFragmentComponent().inject(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.setView(this);
        mPresenter.loadLanguages();
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
    protected LanguagesAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    protected Filter getFilter() {
        return mAdapter.getFilter();
    }

    @Override
    protected void onErrorButtonClick() {
        mPresenter.loadLanguages();
    }

    @Override
    public void onLanguagesLoading() {
        showLoadingView();
    }

    @Override
    public void onLanguagesLoaded(List<Language> items) {
        mAdapter.setItems(items, getQuery());
        showListView();
    }

    @Override
    public void onLanguagesLoadError(Throwable e) {
        showErrorView(e.getMessage());
    }

    @Override
    public void onItemClick(Language item) {
        mCallbacks.onLanguageSelected(item);
    }

    public interface Callbacks {
        LanguagesFragmentComponent getLanguagesFragmentComponent();

        void onLanguageSelected(Language item);
    }
}
