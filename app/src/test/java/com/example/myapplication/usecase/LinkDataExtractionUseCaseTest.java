package com.example.myapplication.usecase;

import static org.junit.Assert.assertEquals;

import com.example.myapplication.domain.usecase.LinkDataExtractionUseCase;
import com.example.myapplication.infra.repository.DataRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import io.reactivex.Observable;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;

public class LinkDataExtractionUseCaseTest {

    LinkDataExtractionUseCase useCase;
    DataRepository repository;

    @Before
    public void setUp() {
        repository = Mockito.mock(DataRepository.class);
        useCase = new LinkDataExtractionUseCase(repository);
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(scheduler -> Schedulers.trampoline());
    }

    @Test
    public void saveLinkData_Success() {
        String str = "title;url";
        useCase.saveLinkData(str);

        Mockito.verify(repository).setLinkData("url");
    }

    @Test
    public void saveLinkData_Failure() {
        String str = "title-url";
        useCase.saveLinkData(str);

        Mockito.verify(repository, Mockito.times(0)).setLinkData("url");
    }

    @Test
    public void extractData_Success() {
        String url = "https://www.abc.com";
        Mockito.when(repository.getLinkData()).thenReturn(url);
        Mockito.when(repository.getHtml(url)).thenReturn(Observable.just("<title>title123</title>"));

        TestObserver<String> testObserver = useCase.extractData().test();
        testObserver.awaitTerminalEvent();
        testObserver.assertNoErrors();
        assertEquals("title123", testObserver.getEvents().get(0).get(0));
    }

    @Test
    public void extractData_Failure_InvalidUrl() {
        Mockito.when(repository.getLinkData()).thenReturn("");

        TestObserver<String> testObserver = useCase.extractData().test();
        testObserver.awaitTerminalEvent();
        testObserver.assertError(Throwable.class);

        Throwable exception = (Throwable) testObserver.getEvents().get(1).get(0);
        assertEquals("link is invalid", exception.getMessage());
    }

    @Test
    public void extractData_Failure_NotContainTitle() {
        String url = "https://www.abc.com";
        Mockito.when(repository.getLinkData()).thenReturn(url);
        Mockito.when(repository.getHtml(url)).thenReturn(Observable.just("<message>title123</message>"));

        TestObserver<String> testObserver = useCase.extractData().test();
        testObserver.awaitTerminalEvent();
        testObserver.assertError(Throwable.class);

        Throwable exception = (Throwable) testObserver.getEvents().get(1).get(0);
        assertEquals("no link", exception.getMessage());
    }

}
