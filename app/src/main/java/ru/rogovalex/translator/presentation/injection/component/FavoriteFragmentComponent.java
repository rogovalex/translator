package ru.rogovalex.translator.presentation.injection.component;

import dagger.Component;
import ru.rogovalex.translator.FavoriteFragment;
import ru.rogovalex.translator.presentation.injection.scope.ViewScope;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 01.04.2017
 * Time: 19:49
 */
@ViewScope
@Component(dependencies = AppComponent.class)
public interface FavoriteFragmentComponent {
    void inject(FavoriteFragment fragment);
}