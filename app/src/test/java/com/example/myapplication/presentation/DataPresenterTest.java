package com.example.myapplication.presentation;

import static org.mockito.ArgumentMatchers.any;

import com.example.myapplication.domain.usecase.LinkDataExtractionUseCase;
import com.example.myapplication.domain.usecase.MentionDataExtractionUseCase;
import com.example.myapplication.presentation.presenter.DataPresenter;
import com.example.myapplication.presentation.view.DataFragmentView;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.concurrent.TimeoutException;

import io.reactivex.Observable;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.schedulers.Schedulers;

public class DataPresenterTest {
    DataPresenter target;
    private DataFragmentView view;
    private MentionDataExtractionUseCase mentionUseCase;
    private LinkDataExtractionUseCase linkUseCase;


    @Before
    public void setUp() {

        view = Mockito.mock(DataFragmentView.class);
        mentionUseCase = Mockito.mock(MentionDataExtractionUseCase.class);
        linkUseCase = Mockito.mock(LinkDataExtractionUseCase.class);

        target = new DataPresenter(view, mentionUseCase, linkUseCase);
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(scheduler -> Schedulers.trampoline());

    }

    @Test
    public void handleMentionData_Success() {
        String str = "@abc @1221";

        Mockito.when(mentionUseCase.extractData()).thenReturn("result");

        target.handleMentionData(str);

        Mockito.verify(mentionUseCase).saveMentionData(str);
        Mockito.verify(view).displayResult("result");
        Mockito.verify(view, Mockito.times(0)).displayError();
    }

    @Test
    public void handleMentionData_Failure() {
        String str = "@abc @1221";

        Mockito.when(mentionUseCase.extractData()).thenReturn(null);

        target.handleMentionData(str);

        Mockito.verify(mentionUseCase).saveMentionData(str);
        Mockito.verify(view, Mockito.times(0)).displayResult(any());
        Mockito.verify(view).displayError();
    }

    @Test
    public void handleLinkData_Success() {
        String str = "https://www.abc.com";

        Mockito.when(linkUseCase.extractData()).thenReturn(Observable.just("result"));

        target.handleLinkData(str);

        Mockito.verify(view).showProgress();
        Mockito.verify(linkUseCase).saveLinkData(str);
        Mockito.verify(view, Mockito.timeout(2000)).displayResult("result");
        Mockito.verify(view, Mockito.times(0)).displayError();
        Mockito.verify(view, Mockito.timeout(2000)).hideProgress();
    }

    @Test
    public void handleLinkData_Failure() {
        String str = "https://www.abc.com";

        Mockito.when(linkUseCase.extractData()).thenReturn(Observable.error(new TimeoutException()));

        target.handleLinkData(str);

        Mockito.verify(view).showProgress();
        Mockito.verify(linkUseCase).saveLinkData(str);
        Mockito.verify(view, Mockito.times(0)).displayResult("result");
        Mockito.verify(view).displayError();
        Mockito.verify(view, Mockito.timeout(2000)).hideProgress();
    }

    @After
    public void tearDown() {

    }
}
