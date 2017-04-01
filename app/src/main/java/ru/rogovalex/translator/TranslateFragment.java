package ru.rogovalex.translator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewAfterTextChangeEvent;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

public class TranslateFragment extends Fragment {

    private static final long DEBOUNCE_MILLIS = 300;

    private CompositeDisposable mSubscription = new CompositeDisposable();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_translate, container, false);

        final EditText textInput = (EditText) view.findViewById(R.id.text_input);
        mSubscription.add(RxTextView.afterTextChangeEvents(textInput)
                .debounce(DEBOUNCE_MILLIS, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<TextViewAfterTextChangeEvent>() {
                    @Override
                    public void accept(TextViewAfterTextChangeEvent event) throws Exception {
                        onTextChanged(event.view().getText().toString());
                    }
                }));

        view.findViewById(R.id.clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textInput.setText("");
            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mSubscription.clear();
    }

    private void onTextChanged(String text) {
        Log.i("REQUEST", text);
    }

}
