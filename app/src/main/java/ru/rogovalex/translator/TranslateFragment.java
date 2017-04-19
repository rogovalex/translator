package ru.rogovalex.translator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;

import javax.inject.Inject;

import ru.rogovalex.translator.domain.translate.Language;
import ru.rogovalex.translator.domain.translate.Translation;
import ru.rogovalex.translator.presentation.injection.component.TranslateFragmentComponent;
import ru.rogovalex.translator.presentation.translate.TranslateView;
import ru.rogovalex.translator.presentation.translate.TranslateViewPresenter;

public class TranslateFragment extends Fragment
        implements TranslateView,
        TranslationAdapter.OnFavoriteChangedListener,
        View.OnClickListener {

    private static final int REQUEST_LANG = 1;

    private TextView mOrigin;
    private TextView mTranslation;
    private EditText mTextInput;
    private View mProgress;
    private View mTranslate;
    private TranslationAdapter mAdapter;

    private Language mOriginLang;
    private Language mTranslationLang;
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

        mOrigin = (TextView) view.findViewById(R.id.text_origin);
        mOrigin.setOnClickListener(this);
        mTranslation = (TextView) view.findViewById(R.id.text_translation);
        mTranslation.setOnClickListener(this);
        View swap = view.findViewById(R.id.swap);
        swap.setOnClickListener(this);

        final CardView cardView = (CardView) view.findViewById(R.id.translation_input);
        mTextInput = (EditText) cardView.findViewById(R.id.text_input);
        final View clear = cardView.findViewById(R.id.clear);
        clear.setOnClickListener(this);
        clear.setVisibility(mTextInput.getText().length() == 0
                ? View.GONE : View.VISIBLE);

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
        mTranslate.setOnClickListener(this);
        mProgress = cardView.findViewById(R.id.progress);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.translation_output);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        mAdapter = new TranslationAdapter();
        mAdapter.setFavoriteChangedListener(this);
        recyclerView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mCallbacks.getTranslateFragmentComponent().inject(this);
        updateLanguages();
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.setView(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.setView(null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_LANG && resultCode == Activity.RESULT_OK) {
            updateLanguages();
        }
    }

    @Override
    public void onTranslating() {
        mProgress.setVisibility(View.VISIBLE);
        mTranslate.setVisibility(View.GONE);
        dismissKeyboard();
    }

    @Override
    public void onTranslated(Translation translation) {
        mProgress.setVisibility(View.GONE);
        mTranslate.setVisibility(View.VISIBLE);
        mAdapter.setTranslation(translation);
    }

    @Override
    public void onTranslateError(Throwable e) {
        mProgress.setVisibility(View.GONE);
        mTranslate.setVisibility(View.VISIBLE);
    }

    @Override
    public void onTranslationDirectionChanged(String text) {
        mTextInput.setText(text);
        mAdapter.clear();
    }

    @Override
    public void onFavoriteChanged(Translation item) {
        mPresenter.updateFavorite(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clear:
                mPresenter.cancel();
                mTextInput.setText("");
                mAdapter.clear();
                showKeyboard();
                break;
            case R.id.translate:
                mPresenter.translate(mTextInput.getText().toString().trim());
                break;
            case R.id.text_origin:
                changeLanguage(true);
                break;
            case R.id.text_translation:
                changeLanguage(false);
                break;
            case R.id.swap:
                swapLanguages();
                break;
        }
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

    private void updateLanguages() {
        mOriginLang = PreferencesHelper.getOriginLanguage(getContext());
        mTranslationLang = PreferencesHelper.getTranslationLanguage(getContext());
        updateLanguagesDependencies();
    }

    private void updateLanguagesDependencies() {
        mOrigin.setText(mOriginLang.getName());
        mTranslation.setText(mTranslationLang.getName());
        mPresenter.setTranslationDirection(mTextInput.getText().toString().trim(),
                mOriginLang.getCode(), mTranslationLang.getCode());
    }

    private void swapLanguages() {
        Language tmp = mOriginLang;
        mOriginLang = mTranslationLang;
        mTranslationLang = tmp;
        PreferencesHelper.setLanguages(getContext(), mOriginLang, mTranslationLang);
        updateLanguagesDependencies();
    }

    private void changeLanguage(boolean origin) {
        Intent intent = LanguagesActivity.newIntent(getContext(), origin);
        startActivityForResult(intent, REQUEST_LANG);
    }

    public interface Callbacks {
        TranslateFragmentComponent getTranslateFragmentComponent();
    }
}
