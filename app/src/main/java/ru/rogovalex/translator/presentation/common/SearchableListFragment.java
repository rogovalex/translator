package ru.rogovalex.translator.presentation.common;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.TextView;

import ru.rogovalex.translator.R;

public abstract class SearchableListFragment extends BaseFragment
        implements TextWatcher {

    private EditText mSearchInput;
    private View mClear;
    private RecyclerView mRecyclerView;
    private View mProgressView;
    private View mErrorView;
    private TextView mErrorMessage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        final CardView cardView = (CardView) view.findViewById(R.id.search_bar);
        mSearchInput = (EditText) cardView.findViewById(R.id.search_input);
        mClear = cardView.findViewById(R.id.clear_input_btn);
        mClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchInput.setText("");
            }
        });

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(getAdapter());

        mProgressView = view.findViewById(R.id.progress_view);
        mErrorView = view.findViewById(R.id.error_view);
        mErrorMessage = (TextView) mErrorView.findViewById(R.id.error_message);
        View errorButton = mErrorView.findViewById(R.id.error_button);
        errorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onErrorButtonClick();
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mSearchInput.addTextChangedListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        mSearchInput.removeTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mClear.setVisibility(s.length() == 0 ? View.GONE : View.VISIBLE);
        getFilter().filter(s);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    protected String getQuery() {
        return mSearchInput.getText().toString();
    }

    protected void showLoadingView() {
        mRecyclerView.setVisibility(View.GONE);
        mProgressView.setVisibility(View.VISIBLE);
        mErrorView.setVisibility(View.GONE);
    }

    protected void showListView() {
        mRecyclerView.setVisibility(View.VISIBLE);
        mProgressView.setVisibility(View.GONE);
        mErrorView.setVisibility(View.GONE);
    }

    protected void showErrorView(String message) {
        mRecyclerView.setVisibility(View.GONE);
        mProgressView.setVisibility(View.GONE);
        mErrorView.setVisibility(View.VISIBLE);
        mErrorMessage.setText(message);
    }

    protected abstract RecyclerView.Adapter getAdapter();

    protected abstract Filter getFilter();

    protected abstract void onErrorButtonClick();
}
