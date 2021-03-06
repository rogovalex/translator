package ru.rogovalex.translator;

import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import ru.rogovalex.translator.data.YandexTranslationProvider;
import ru.rogovalex.translator.data.api.TranslateApiService;
import ru.rogovalex.translator.data.api.YandexApiException;
import ru.rogovalex.translator.data.api.response.LanguagesResponse;
import ru.rogovalex.translator.data.api.response.TranslateResponse;
import ru.rogovalex.translator.domain.model.Language;
import ru.rogovalex.translator.domain.model.TranslationParams;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created with Android Studio.
 * User: rogov
 * Date: 22.04.2017
 * Time: 23:27
 */
public class YandexTranslationProviderTest {

    @Test
    public void translate_responseEmpty() throws Exception {
        TranslateApiService service = mock(TranslateApiService.class);

        when(service.translate(anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(Observable.just(new TranslateResponse()));

        YandexTranslationProvider provider = new YandexTranslationProvider(service);

        TranslationParams p = new TranslationParams("test", "en", "ru", "ru");
        TestObserver<String> observer = provider.translate(p).test();

        Assert.assertEquals(1, observer.errors().size());
        //noinspection ThrowableResultOfMethodCallIgnored
        YandexApiException e = (YandexApiException) observer.errors().get(0);
        Assert.assertEquals(422, e.getCode());
    }

    @Test
    public void translate_responseCode401() throws Exception {
        TranslateApiService service = mock(TranslateApiService.class);

        when(service.translate(anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(Observable.just(new TranslateResponse() {{
                    setCode(401);
                }}));

        YandexTranslationProvider provider = new YandexTranslationProvider(service);

        TranslationParams p = new TranslationParams("test", "en", "ru", "ru");
        TestObserver<String> observer = provider.translate(p).test();

        Assert.assertEquals(1, observer.errors().size());
        //noinspection ThrowableResultOfMethodCallIgnored
        YandexApiException e = (YandexApiException) observer.errors().get(0);
        Assert.assertEquals(401, e.getCode());
    }

    @Test
    public void translate_success() throws Exception {
        TranslateApiService service = mock(TranslateApiService.class);

        when(service.translate(anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(Observable.just(new TranslateResponse() {{
                    setText(new String[]{"тест"});
                }}));

        YandexTranslationProvider provider = new YandexTranslationProvider(service);

        TranslationParams p = new TranslationParams("test", "en", "ru", "ru");
        TestObserver<String> observer = provider.translate(p).test();

        observer.assertNoErrors();
        observer.assertValue("тест");
    }

    @Test
    public void languages_responseEmpty() throws Exception {
        TranslateApiService service = mock(TranslateApiService.class);

        when(service.languages(anyString(), anyString()))
                .thenReturn(Observable.just(new LanguagesResponse()));

        YandexTranslationProvider provider = new YandexTranslationProvider(service);

        TestObserver<List<Language>> observer = provider.languages("ru").test();

        observer.assertNoValues();
    }

    @Test
    public void languages_responseCode401() throws Exception {
        TranslateApiService service = mock(TranslateApiService.class);

        when(service.languages(anyString(), anyString()))
                .thenReturn(Observable.just(new LanguagesResponse() {{
                    setCode(401);
                }}));

        YandexTranslationProvider provider = new YandexTranslationProvider(service);

        TestObserver<List<Language>> observer = provider.languages("ru").test();

        Assert.assertEquals(1, observer.errors().size());
        //noinspection ThrowableResultOfMethodCallIgnored
        YandexApiException e = (YandexApiException) observer.errors().get(0);
        Assert.assertEquals(401, e.getCode());
    }

    @Test
    public void languages_success() throws Exception {
        TranslateApiService service = mock(TranslateApiService.class);

        when(service.languages(anyString(), anyString()))
                .thenReturn(Observable.just(new LanguagesResponse() {{
                    setLangs(new LinkedHashMap<String, String>() {{
                        put("ru", "Русский");
                        put("en", "Английский");
                    }});
                }}));

        YandexTranslationProvider provider = new YandexTranslationProvider(service);

        TestObserver<List<Language>> observer = provider.languages("ru").test();

        observer.assertNoErrors();

        Assert.assertEquals(1, observer.values().size());
        Assert.assertEquals(2, observer.values().get(0).size());
        Language lang1 = observer.values().get(0).get(0);
        Assert.assertEquals("ru", lang1.getCode());
        Assert.assertEquals("Русский", lang1.getName());
        Language lang2 = observer.values().get(0).get(1);
        Assert.assertEquals("en", lang2.getCode());
        Assert.assertEquals("Английский", lang2.getName());
    }
}
