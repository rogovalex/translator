package ru.rogovalex.translator.presentation.main.history;

import java.util.List;

import ru.rogovalex.translator.domain.model.Translation;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 02.04.2017
 * Time: 15:29
 */
public interface HistoryView {
    void onHistoryLoading();

    void onHistoryLoaded(List<Translation> items);

    void onHistoryLoadError(Throwable e);

    void onHistoryCleared();
}
