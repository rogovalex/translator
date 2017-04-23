package ru.rogovalex.translator.presentation.common;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 01.04.2017
 * Time: 19:32
 */
public abstract class BasePresenter<View> {

    private View mView;

    public View getView() {
        return mView;
    }

    public void setView(View view) {
        if (view == null) {
            view = getStubView();
        }
        mView = view;
    }

    protected View getStubView() {
        return null;
    }
}
