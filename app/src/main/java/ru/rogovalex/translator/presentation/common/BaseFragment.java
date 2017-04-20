package ru.rogovalex.translator.presentation.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 18.04.2017
 * Time: 12:29
 */
public abstract class BaseFragment extends Fragment {

    private Unbinder mUnbinder;

    private boolean mLoadingCanceled = false;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mUnbinder = ButterKnife.bind(this, view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
        cancelLoadingIfRequired();
    }

    @Override
    public void onStop() {
        super.onStop();
        cancelLoadingIfRequired();
    }

    private void cancelLoadingIfRequired() {
        if (!mLoadingCanceled && (isRemoving() || getActivity().isFinishing())) {
            cancelLoading();
            mLoadingCanceled = true;
        }
    }

    protected void cancelLoading() {
        // override this
    }
}
