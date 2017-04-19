package ru.rogovalex.translator.presentation.injection.component;

import dagger.Component;
import ru.rogovalex.translator.LanguageFragment;
import ru.rogovalex.translator.presentation.injection.module.LanguageFragmentModule;
import ru.rogovalex.translator.presentation.injection.scope.ActivityScope;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 18.04.2017
 * Time: 11:32
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = {LanguageFragmentModule.class})
public interface LanguageFragmentComponent {
    void inject(LanguageFragment fragment);
}
