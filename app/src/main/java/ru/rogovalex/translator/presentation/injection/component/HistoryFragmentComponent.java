package ru.rogovalex.translator.presentation.injection.component;

import dagger.Component;
import ru.rogovalex.translator.HistoryFragment;
import ru.rogovalex.translator.presentation.injection.module.HistoryPresentationModule;
import ru.rogovalex.translator.presentation.injection.scope.ViewScope;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 01.04.2017
 * Time: 19:49
 */
@ViewScope
@Component(dependencies = AppComponent.class,
        modules = {HistoryPresentationModule.class})
public interface HistoryFragmentComponent {
    void inject(HistoryFragment fragment);
}
