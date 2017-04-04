package ru.rogovalex.translator;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;

import java.util.List;

import javax.inject.Inject;

import ru.rogovalex.translator.domain.translate.TranslateResult;
import ru.rogovalex.translator.presentation.injection.component.HistoryFragmentComponent;
import ru.rogovalex.translator.presentation.translate.HistoryView;
import ru.rogovalex.translator.presentation.translate.HistoryViewPresenter;

public class HistoryFragment extends SearchableListFragment
        implements HistoryView {

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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ((EditText) view.findViewById(R.id.search_input))
                .setHint(R.string.search_history_hint);
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
        setAdapterItems(items);
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
