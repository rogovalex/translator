package ru.rogovalex.translator;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import ru.rogovalex.translator.api.ApiException;
import ru.rogovalex.translator.api.DictionaryApiService;
import ru.rogovalex.translator.api.response.DictionaryEntry;
import ru.rogovalex.translator.api.response.DictionaryResponse;
import ru.rogovalex.translator.api.response.DictionaryTranslation;
import ru.rogovalex.translator.data.YandexDictionaryProvider;
import ru.rogovalex.translator.domain.model.Definition;
import ru.rogovalex.translator.domain.model.DefinitionOption;
import ru.rogovalex.translator.domain.model.TranslateParams;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class YandexDictionaryProviderTest {

    @Test
    public void lookup_responseEmpty() throws Exception {
        DictionaryApiService service = mock(DictionaryApiService.class);

        when(service.lookup(anyString(), anyString(), anyString(), anyString(), anyInt()))
                .thenReturn(Observable.just(new DictionaryResponse()));

        YandexDictionaryProvider provider = new YandexDictionaryProvider(service);

        TranslateParams p = new TranslateParams("test", "en", "ru", "ru");
        TestObserver<List<Definition>> observer = provider.lookup(p).test();

        observer.assertNoErrors();

        Assert.assertEquals(1, observer.values().size());
        Assert.assertTrue(observer.values().get(0).isEmpty());
    }

    @Test
    public void lookup_responseCode401() throws Exception {
        DictionaryApiService service = mock(DictionaryApiService.class);

        when(service.lookup(anyString(), anyString(), anyString(), anyString(), anyInt()))
                .thenReturn(Observable.just(new DictionaryResponse() {{
                    setCode(401);
                }}));

        YandexDictionaryProvider provider = new YandexDictionaryProvider(service);

        TranslateParams p = new TranslateParams("test", "en", "ru", "ru");
        TestObserver<List<Definition>> observer = provider.lookup(p).test();

        Assert.assertEquals(1, observer.errors().size());
        //noinspection ThrowableResultOfMethodCallIgnored
        ApiException e = (ApiException) observer.errors().get(0);
        Assert.assertEquals(401, e.getCode());
    }

    @Test
    public void lookup_requestError() throws Exception {
        DictionaryApiService service = mock(DictionaryApiService.class);

        when(service.lookup(anyString(), anyString(), anyString(), anyString(), anyInt()))
                .thenReturn(Observable.error(new Exception("test")));

        YandexDictionaryProvider provider = new YandexDictionaryProvider(service);

        TranslateParams p = new TranslateParams("test", "en", "ru", "ru");
        TestObserver<List<Definition>> observer = provider.lookup(p).test();

        Assert.assertEquals(1, observer.errors().size());
        //noinspection ThrowableResultOfMethodCallIgnored
        Exception e = (Exception) observer.errors().get(0);
        Assert.assertEquals("test", e.getMessage());
    }

    @Test
    public void lookup_singleEntry() throws Exception {
        DictionaryEntry entry = new DictionaryEntry();
        entry.setText("тест");
        entry.setPos("сущ");
        entry.setTranscription("тЭст");
        entry.setTranslations(new DictionaryTranslation[]{
                new DictionaryTranslation() {{
                    setText("тест1");
                    setMeanings(new DictionaryTranslation[]{
                            new DictionaryTranslation() {{
                                setText("значение1");
                            }},
                            new DictionaryTranslation() {{
                                setText("значение2");
                            }}
                    });
                    setSynonyms(new DictionaryTranslation[]{
                            new DictionaryTranslation() {{
                                setText("синоним1");
                            }},
                            new DictionaryTranslation() {{
                                setText("синоним2");
                            }}
                    });
                    setExamples(new DictionaryTranslation[]{
                            new DictionaryTranslation() {{
                                setText("пример1");
                            }},
                            new DictionaryTranslation() {{
                                setText("пример2");
                            }}
                    });
                }}
        });

        DictionaryApiService service = mock(DictionaryApiService.class);
        when(service.lookup(anyString(), anyString(), anyString(), anyString(), anyInt()))
                .thenReturn(Observable.just(new DictionaryResponse() {{
                    setEntries(new DictionaryEntry[]{entry});
                }}));

        YandexDictionaryProvider provider = new YandexDictionaryProvider(service);

        TranslateParams p = new TranslateParams("test", "en", "ru", "ru");
        TestObserver<List<Definition>> observer = provider.lookup(p).test();

        observer.assertNoErrors();

        Assert.assertEquals(1, observer.values().size());
        Assert.assertEquals(1, observer.values().get(0).size());
        Definition item = observer.values().get(0).get(0);
        Assert.assertEquals("тест", item.getText());
        Assert.assertEquals("сущ", item.getPos());
        Assert.assertEquals("тЭст", item.getTranscription());
        Assert.assertEquals(1, item.getDefinitionOptions().size());
        DefinitionOption opt = item.getDefinitionOptions().get(0);
        Assert.assertEquals("тест1, синоним1, синоним2", opt.getSynonyms());
        Assert.assertEquals("значение1, значение2", opt.getMeanings());
        Assert.assertEquals("пример1, пример2", opt.getExamples());
    }

    @Test
    public void lookup_multipleEntries() throws Exception {
        DictionaryEntry entry1 = new DictionaryEntry();
        entry1.setText("первый");
        entry1.setPos("прил");
        DictionaryEntry entry2 = new DictionaryEntry();
        entry2.setText("два");
        entry2.setPos("сущ");
        DictionaryEntry entry3 = new DictionaryEntry();

        DictionaryApiService service = mock(DictionaryApiService.class);
        when(service.lookup(anyString(), anyString(), anyString(), anyString(), anyInt()))
                .thenReturn(Observable.just(new DictionaryResponse() {{
                    setEntries(new DictionaryEntry[]{entry1, entry2, entry3});
                }}));

        YandexDictionaryProvider provider = new YandexDictionaryProvider(service);

        TranslateParams p = new TranslateParams("test", "en", "ru", "ru");
        TestObserver<List<Definition>> observer = provider.lookup(p).test();

        observer.assertNoErrors();

        Assert.assertEquals(1, observer.values().size());
        Assert.assertEquals(3, observer.values().get(0).size());
        Definition item1 = observer.values().get(0).get(0);
        Assert.assertEquals("первый", item1.getText());
        Assert.assertEquals("прил", item1.getPos());
        Assert.assertEquals("", item1.getTranscription());
        Assert.assertTrue(item1.getDefinitionOptions().isEmpty());
        Definition item2 = observer.values().get(0).get(1);
        Assert.assertEquals("два", item2.getText());
        Assert.assertEquals("сущ", item2.getPos());
        Assert.assertEquals("", item2.getTranscription());
        Assert.assertTrue(item2.getDefinitionOptions().isEmpty());
        Definition item3 = observer.values().get(0).get(2);
        Assert.assertNull(item3.getText());
        Assert.assertEquals("", item3.getPos());
        Assert.assertEquals("", item3.getTranscription());
        Assert.assertTrue(item3.getDefinitionOptions().isEmpty());
    }
}