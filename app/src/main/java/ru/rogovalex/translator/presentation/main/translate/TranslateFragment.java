package ru.rogovalex.translator.presentation.main.translate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import butterknife.BindView;
import butterknife.OnClick;
import ru.rogovalex.translator.R;
import ru.rogovalex.translator.api.ApiException;
import ru.rogovalex.translator.domain.model.Language;
import ru.rogovalex.translator.domain.model.Translation;
import ru.rogovalex.translator.presentation.common.BaseFragment;
import ru.rogovalex.translator.presentation.injection.component.TranslateFragmentComponent;
import ru.rogovalex.translator.presentation.language.LanguageActivity;

public class TranslateFragment extends BaseFragment
        implements TranslateView,
        TranslationAdapter.OnFavoriteChangedListener {

    private static final int REQUEST_LANG = 1;

    @BindView(R.id.source_lang_view)
    TextView mSource;
    @BindView(R.id.translation_lang_view)
    TextView mTranslation;
    @BindView(R.id.translation_input)
    EditText mTextInput;
    @BindView(R.id.clear_input_btn)
    View mClear;
    @BindView(R.id.progress_view)
    View mProgress;
    @BindView(R.id.translate_btn)
    View mTranslate;
    @BindView(R.id.error_message)
    TextView mErrorMessage;
    @BindView(R.id.translation_output)
    RecyclerView mRecyclerView;

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
        return inflater.inflate(R.layout.fragment_translate, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTextInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mPresenter.onTextChanged(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new TranslationAdapter();
        mAdapter.setFavoriteChangedListener(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mCallbacks.getTranslateFragmentComponent().inject(this);
        mPresenter.setUiLanguageCode(getString(R.string.ui_lang_code));
        mPresenter.updateLanguages();
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
            mPresenter.updateLanguages();
        }
    }

    @Override
    protected void cancelLoading() {
        mPresenter.cancel();
    }

    @Override
    public void onTranslating() {
        mProgress.setVisibility(View.VISIBLE);
        mTranslate.setVisibility(View.GONE);
        mErrorMessage.setVisibility(View.GONE);
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
        mAdapter.clear();
        mErrorMessage.setVisibility(View.VISIBLE);
        String message = e instanceof ApiException
                ? e.getMessage()
                : getString(R.string.error_default);
        mErrorMessage.setText(message);
    }

    @Override
    public void onTranslationDirectionChanged(String text, Language source, Language translation) {
        setLanguages(source, translation);
        mTextInput.setText(text);
        mAdapter.clear();
    }

    @Override
    public void onTextEmpty() {
        mClear.setVisibility(View.GONE);
    }

    @Override
    public void onTextNotEmpty() {
        mClear.setVisibility(View.VISIBLE);
    }

    @Override
    public void setLanguages(Language source, Language translation) {
        mSource.setText(source.getName());
        mTranslation.setText(translation.getName());
    }

    @Override
    public void onFavoriteChanged(Translation item) {
        mPresenter.updateFavorite(item);
    }

    @OnClick(R.id.source_lang_view)
    void sourceLanguageClick() {
        changeLanguage(true);
    }

    @OnClick(R.id.translation_lang_view)
    void translationLanguageClick() {
        changeLanguage(false);
    }

    @OnClick(R.id.swap_lang_btn)
    void swapLanguageClick() {
        mPresenter.swapLanguages();
    }

    @OnClick(R.id.translate_btn)
    void translateTextClick() {
        mPresenter.translate();
    }

    @OnClick(R.id.clear_input_btn)
    void clearTextClick() {
        mPresenter.cancelTranslate();
        mTextInput.setText("");
        mAdapter.clear();
        showKeyboard();
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

    private void changeLanguage(boolean changeSourceLanguage) {
        Intent intent = LanguageActivity.newIntent(getContext(), changeSourceLanguage);
        startActivityForResult(intent, REQUEST_LANG);
    }

    public interface Callbacks {
        TranslateFragmentComponent getTranslateFragmentComponent();
    }
}
