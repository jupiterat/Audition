package com.example.myapplication.usecase;

import static org.junit.Assert.assertEquals;

import com.example.myapplication.domain.usecase.MentionDataExtractionUseCase;
import com.example.myapplication.infra.repository.DataRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.schedulers.Schedulers;

public class MentionDataExtractionUseCaseTest {
    MentionDataExtractionUseCase useCase;
    DataRepository repository;

    @Before
    public void setUp() {
        repository = Mockito.mock(DataRepository.class);
        useCase = new MentionDataExtractionUseCase(repository);
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(scheduler -> Schedulers.trampoline());
    }

    @Test
    public void extractData() {
        ArrayList<String> list = new ArrayList<>();
        list.add("@billgates do you know where is @elonmusk?");
        list.add("@billgates where is @elonmusk");

        for (int i = 0; i < list.size(); i++) {
            Mockito.when(repository.getMentionData()).thenReturn(list.get(i));
            assertEquals("{\"mentions\":[\"billgates\",\"elonmusk\"]}", useCase.extractData());
        }
    }


}
