package ru.rogovalex.translator.presentation.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
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

import butterknife.BindView;
import butterknife.OnClick;
import ru.rogovalex.translator.R;

public abstract class SearchableListFragment extends BaseFragment
        implements TextWatcher {

    @BindView(R.id.search_input)
    EditText mSearchInput;
    @BindView(R.id.clear_input_btn)
    View mClear;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.progress_view)
    View mProgressView;
    @BindView(R.id.error_view)
    View mErrorView;
    @BindView(R.id.error_message)
    TextView mErrorMessage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(getAdapter());
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

    @OnClick(R.id.clear_input_btn)
    void clearButtonClick() {
        mSearchInput.setText("");
    }

    @OnClick(R.id.error_button)
    void errorButtonClick() {
        onErrorButtonClick();
    }

    protected EditText getSearchInput() {
        return mSearchInput;
    }

    protected RecyclerView getRecyclerView() {
        return mRecyclerView;
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
