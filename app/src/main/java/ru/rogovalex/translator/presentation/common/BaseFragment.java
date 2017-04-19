package ru.rogovalex.translator.presentation.common;

import android.support.v4.app.Fragment;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 18.04.2017
 * Time: 12:29
 */
public abstract class BaseFragment extends Fragment {

    private boolean mLoadingCanceled = false;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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
