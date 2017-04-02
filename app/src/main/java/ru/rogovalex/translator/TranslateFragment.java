package ru.rogovalex.translator;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import javax.inject.Inject;

import ru.rogovalex.translator.domain.translate.TranslateParams;
import ru.rogovalex.translator.domain.translate.TranslateResult;
import ru.rogovalex.translator.presentation.injection.component.TranslateFragmentComponent;
import ru.rogovalex.translator.presentation.translate.TranslateView;
import ru.rogovalex.translator.presentation.translate.TranslateViewPresenter;

public class TranslateFragment extends Fragment implements TranslateView {

    private EditText mTextInput;
    private View mProgress;
    private View mTranslate;
    private TranslationAdapter mAdapter;

    private Callbacks mCallbacks;

    @Inject
    TranslateViewPresenter mPresenter;

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
        View view = inflater.inflate(R.layout.fragment_translate, container, false);

        final CardView cardView = (CardView) view.findViewById(R.id.translation_input);
        mTextInput = (EditText) cardView.findViewById(R.id.text_input);
        final View clear = cardView.findViewById(R.id.clear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.cancel();
                mTextInput.setText("");
                mAdapter.clear();
                showKeyboard();
            }
        });
        clear.setVisibility(mTextInput.getText().length() == 0 ? View.GONE : View.VISIBLE);

        mTextInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                clear.setVisibility(s.length() == 0 ? View.GONE : View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mTranslate = cardView.findViewById(R.id.translate);
        mTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.translate(new TranslateParams(
                        mTextInput.getText().toString().trim(), "ru", "en"));
            }
        });
        mProgress = cardView.findViewById(R.id.progress);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.translation_output);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        mAdapter = new TranslationAdapter();
        recyclerView.setAdapter(mAdapter);

        final float elevation = getResources().getDimension(R.dimen.default_elevation);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int position = layoutManager.findFirstCompletelyVisibleItemPosition();
                cardView.setCardElevation(position > 0 ? elevation : 0);
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mCallbacks.getTranslateFragmentComponent().inject(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.setView(this);
        mPresenter.translate(new TranslateParams(
                mTextInput.getText().toString().trim(), "ru", "en"));
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.setView(null);
    }

    @Override
    public void onTranslating() {
        mProgress.setVisibility(View.VISIBLE);
        mTranslate.setVisibility(View.GONE);
        dismissKeyboard();
    }

    @Override
    public void onTranslated(TranslateResult translation) {
        mProgress.setVisibility(View.GONE);
        mTranslate.setVisibility(View.VISIBLE);
        mAdapter.setTranslation(translation);
    }

    @Override
    public void onTranslateError(Throwable e) {
        mProgress.setVisibility(View.GONE);
        mTranslate.setVisibility(View.VISIBLE);
    }

    private void dismissKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mTextInput, InputMethodManager.SHOW_FORCED);
    }

    public interface Callbacks {
        TranslateFragmentComponent getTranslateFragmentComponent();
    }
}
