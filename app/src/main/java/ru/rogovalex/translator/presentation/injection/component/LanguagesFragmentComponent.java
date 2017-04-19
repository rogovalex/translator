package ru.rogovalex.translator.presentation.injection.component;

import dagger.Component;
import ru.rogovalex.translator.LanguagesFragment;
import ru.rogovalex.translator.presentation.injection.module.LanguagesFragmentModule;
import ru.rogovalex.translator.presentation.injection.scope.ActivityScope;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 18.04.2017
 * Time: 11:32
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = {LanguagesFragmentModule.class})
public interface LanguagesFragmentComponent {
    void inject(LanguagesFragment fragment);
}
