package ru.rogovalex.translator;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import ru.rogovalex.translator.data.CachingTranslationRepository;
import ru.rogovalex.translator.data.DictionaryProvider;
import ru.rogovalex.translator.data.TranslationProvider;
import ru.rogovalex.translator.data.database.Database;
import ru.rogovalex.translator.domain.model.Translation;
import ru.rogovalex.translator.domain.model.TranslationParams;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 24.04.2017
 * Time: 17:52
 */
public class CachingTranslationRepositoryTest {

    @Test
    public void getTranslation_fromDatabase() throws Exception {
        TranslationParams params = new TranslationParams("test", "en", "ru", "ru");
        Database database = mock(Database.class);
        when(database.getTranslations(params)).thenReturn(new ArrayList<Translation>() {{
            add(new Translation("test", "en", "тест", "ru", Collections.emptyList()));
        }});

        TranslationProvider provider = mock(TranslationProvider.class);
        when(provider.translate(params)).thenReturn(Observable.empty());

        DictionaryProvider dProvider = mock(DictionaryProvider.class);
        when(dProvider.lookup(params)).thenReturn(Observable.empty());

        CachingTranslationRepository repository = new CachingTranslationRepository(database, provider, dProvider);

        TestObserver<Translation> observer = repository.getTranslation(params).test();

        observer.assertNoErrors();

        verify(database, times(1)).getTranslations(params);
        verify(database, times(1)).saveRecentTranslation(any(Translation.class), anyString());
        Assert.assertEquals(1, observer.values().size());

        Translation translation = observer.values().get(0);
        Assert.assertEquals("test", translation.getText());
        Assert.assertEquals("en", translation.getTextLang());
        Assert.assertEquals("тест", translation.getTranslation());
        Assert.assertEquals("ru", translation.getTranslationLang());
        Assert.assertEquals(0, translation.getDefinitions().size());
    }

    @Test
    public void getTranslation_fromProvider() throws Exception {
        TranslationParams params = new TranslationParams("test", "en", "ru", "ru");
        Database database = mock(Database.class);
        when(database.getTranslations(params)).thenReturn(Collections.emptyList());

        TranslationProvider provider = mock(TranslationProvider.class);
        when(provider.translate(params)).thenReturn(Observable.just("тест"));

        DictionaryProvider dProvider = mock(DictionaryProvider.class);
        when(dProvider.lookup(params)).thenReturn(Observable.just(Collections.emptyList()));

        CachingTranslationRepository repository = new CachingTranslationRepository(database, provider, dProvider);

        TestObserver<Translation> observer = repository.getTranslation(params).test();

        observer.assertNoErrors();

        verify(database, times(1)).getTranslations(params);
        verify(database, times(1)).saveRecentTranslation(any(Translation.class), anyString());
        Assert.assertEquals(1, observer.values().size());

        Translation translation = observer.values().get(0);
        Assert.assertEquals("test", translation.getText());
        Assert.assertEquals("en", translation.getTextLang());
        Assert.assertEquals("тест", translation.getTranslation());
        Assert.assertEquals("ru", translation.getTranslationLang());
        Assert.assertEquals(0, translation.getDefinitions().size());
    }
}
