package ru.rogovalex.translator.presentation.translate;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;
import ru.rogovalex.translator.domain.translate.Language;
import ru.rogovalex.translator.domain.translate.LanguagesInteractor;
import ru.rogovalex.translator.presentation.common.BasePresenter;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 02.04.2017
 * Time: 15:29
 */
public class LanguagesViewPresenter extends BasePresenter<LanguagesView> {

    private final LanguagesInteractor mInteractor;

    private List<Language> mItems;

    private boolean mLoading;

    @Inject
    public LanguagesViewPresenter(LanguagesInteractor interactor) {
        mInteractor = interactor;
        super.setView(sStubView);
    }

    public void loadLanguages() {
        if (mItems != null) {
            getView().onLanguagesLoaded(mItems);
            return;
        }

        getView().onLanguagesLoading();

        if (mLoading) {
            return;
        }

        mLoading = true;
        mInteractor.execute("ru", new Consumer<List<Language>>() {
            @Override
            public void accept(List<Language> items) throws Exception {
                mLoading = false;
                mItems = items;
                getView().onLanguagesLoaded(items);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable e) throws Exception {
                mLoading = false;
                getView().onLanguagesLoadError(e);
            }
        });
    }

    public void cancel() {
        mLoading = false;
        mInteractor.cancel();
    }

    private static LanguagesView sStubView = new LanguagesView() {
        @Override
        public void onLanguagesLoading() {
        }

        @Override
        public void onLanguagesLoaded(List<Language> items) {
        }

        @Override
        public void onLanguagesLoadError(Throwable e) {
        }
    };
}
