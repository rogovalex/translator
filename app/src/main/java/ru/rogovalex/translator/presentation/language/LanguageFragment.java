package ru.rogovalex.translator.presentation.language;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.Filter;

import java.util.List;

import javax.inject.Inject;

import ru.rogovalex.translator.R;
import ru.rogovalex.translator.domain.model.Language;
import ru.rogovalex.translator.presentation.common.SearchableListFragment;
import ru.rogovalex.translator.presentation.injection.component.LanguageFragmentComponent;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 18.04.2017
 * Time: 0:05
 */
public class LanguageFragment extends SearchableListFragment
        implements LanguageAdapter.OnItemClickListener, LanguagesView {

    private LanguageAdapter mAdapter;

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
        mAdapter = new LanguageAdapter();
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
        mPresenter.setUiLanguageCode(getString(R.string.ui_lang_code));
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
    protected LanguageAdapter getAdapter() {
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
        LanguageFragmentComponent getLanguagesFragmentComponent();

        void onLanguageSelected(Language item);
    }
}
