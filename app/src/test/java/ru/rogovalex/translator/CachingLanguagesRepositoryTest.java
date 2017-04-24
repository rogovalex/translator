package ru.rogovalex.translator;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import ru.rogovalex.translator.data.CachingLanguagesRepository;
import ru.rogovalex.translator.data.LanguagesProvider;
import ru.rogovalex.translator.data.database.Database;
import ru.rogovalex.translator.domain.model.Language;

import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 24.04.2017
 * Time: 17:07
 */
public class CachingLanguagesRepositoryTest {

    @Test
    public void getLanguages_fromDatabase() throws Exception {
        Database database = mock(Database.class);
        when(database.getLanguages("ru")).thenReturn(new ArrayList<Language>() {{
            add(new Language("test", "fromDatabase"));
        }});

        LanguagesProvider provider = mock(LanguagesProvider.class);
        when(provider.languages("ru")).thenReturn(Observable.just(Collections.emptyList()));

        CachingLanguagesRepository repository = new CachingLanguagesRepository(database, provider);

        TestObserver<List<Language>> observer = repository.getLanguages("ru").test();

        observer.assertNoErrors();

        verify(database, times(1)).getLanguages("ru");
        verify(database, times(0)).saveLanguages(anyString(), anyListOf(Language.class));
        Assert.assertEquals(1, observer.values().size());
        Assert.assertEquals(1, observer.values().get(0).size());
        Language lang = observer.values().get(0).get(0);
        Assert.assertEquals("test", lang.getCode());
        Assert.assertEquals("fromDatabase", lang.getName());
    }

    @Test
    public void getLanguages_fromProvider() throws Exception {
        List<Language> langs = new ArrayList<Language>() {{
            add(new Language("test", "fromProvider"));
        }};

        boolean[] saved = new boolean[1];

        Database database = mock(Database.class);

        when(database.getLanguages("ru")).thenAnswer(new Answer<List<Language>>() {
            @Override
            public List<Language> answer(InvocationOnMock invocation) throws Throwable {
                return saved[0] ? langs : Collections.emptyList();
            }
        });
        when(database.saveLanguages(anyString(), anyListOf(Language.class))).then(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                saved[0] = true;
                return null;
            }
        });

        LanguagesProvider provider = mock(LanguagesProvider.class);
        when(provider.languages("ru")).thenReturn(Observable.just(langs));

        CachingLanguagesRepository repository = new CachingLanguagesRepository(database, provider);

        TestObserver<List<Language>> observer = repository.getLanguages("ru").test();

        observer.assertNoErrors();

        // 1й вызов - проверяет наличие в бд,
        // 2й вызов - возвращает отсортированный список после сохранения
        verify(database, times(2)).getLanguages("ru");
        verify(database, times(1)).saveLanguages(anyString(), anyListOf(Language.class));
        Assert.assertEquals(1, observer.values().size());
        Assert.assertEquals(1, observer.values().get(0).size());
        Language lang = observer.values().get(0).get(0);
        Assert.assertEquals("test", lang.getCode());
        Assert.assertEquals("fromProvider", lang.getName());
    }
}
