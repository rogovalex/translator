package ru.rogovalex.translator.presentation.injection.component;

import dagger.Component;
import ru.rogovalex.translator.presentation.injection.module.TranslateFragmentModule;
import ru.rogovalex.translator.presentation.injection.scope.ActivityScope;
import ru.rogovalex.translator.presentation.main.translate.TranslateFragment;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 01.04.2017
 * Time: 19:49
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = {TranslateFragmentModule.class})
public interface TranslateFragmentComponent {
    void inject(TranslateFragment fragment);
}
