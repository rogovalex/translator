package ru.rogovalex.translator;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewAfterTextChangeEvent;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import ru.rogovalex.translator.domain.translate.TranslateParams;
import ru.rogovalex.translator.presentation.injection.component.TranslateFragmentComponent;
import ru.rogovalex.translator.presentation.translate.TranslateView;
import ru.rogovalex.translator.presentation.translate.TranslateViewPresenter;

public class TranslateFragment extends Fragment implements TranslateView {

    private static final long DEBOUNCE_MILLIS = 1000;

    private EditText mTextInput;
    private TextView mTextOutput;

    private CompositeDisposable mSubscription = new CompositeDisposable();

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

        mTextInput = (EditText) view.findViewById(R.id.text_input);
        mTextOutput = (TextView) view.findViewById(R.id.text_output);
        view.findViewById(R.id.clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextInput.setText("");
                mTextOutput.setText("");
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
        subscribeToTextChanges();
    }

    @Override
    public void onStop() {
        super.onStop();
        mSubscription.clear();
        mPresenter.setView(null);
    }

    @Override
    public void onTranslating() {

    }

    @Override
    public void onTranslated(String translation) {
        mTextOutput.setText(translation);
    }

    @Override
    public void onTranslateError(Throwable e) {

    }

    private void subscribeToTextChanges() {
        mSubscription.add(RxTextView.afterTextChangeEvents(mTextInput)
                .debounce(DEBOUNCE_MILLIS, TimeUnit.MILLISECONDS)
                .map(new Function<TextViewAfterTextChangeEvent, String>() {
                    @Override
                    public String apply(TextViewAfterTextChangeEvent event) throws Exception {
                        return event.view().getText().toString();
                    }
                })
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String s) throws Exception {
                        return !s.isEmpty();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String text) throws Exception {
                        mPresenter.translate(new TranslateParams(text, "ru", "en"));
                    }
                }));
    }

    public interface Callbacks {
        TranslateFragmentComponent getTranslateFragmentComponent();
    }
}
