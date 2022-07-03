package com.example.myapplication.presentation.presenter;

import androidx.annotation.NonNull;

import com.example.myapplication.domain.usecase.LinkDataExtractionUseCase;
import com.example.myapplication.domain.usecase.MentionDataExtractionUseCase;
import com.example.myapplication.presentation.view.DataFragmentView;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DataPresenter {
    private final MentionDataExtractionUseCase mentionUseCase;
    private final LinkDataExtractionUseCase linkUseCase;
    private DataFragmentView view;


    public DataPresenter(DataFragmentView view, MentionDataExtractionUseCase mentionUseCase, LinkDataExtractionUseCase linkUseCase) {
        this.mentionUseCase = mentionUseCase;
        this.linkUseCase = linkUseCase;
        this.view = view;
    }

    public void handleMentionData(@NotNull String originalStr) {
        mentionUseCase.saveMentionData(originalStr);

        String result = mentionUseCase.extractData();
        display(result);
    }

    public void handleLinkData(@NotNull String originalStr) {
        if(view != null) {
            view.showProgress();
        }
        linkUseCase.saveLinkData(originalStr);

        linkUseCase.extractData()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull String s) {
                        if(view != null) {
                            view.hideProgress();
                        }
                        display(s);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if(view != null) {
                            view.hideProgress();
                        }
                        display(null);
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private void display(String result) {
        if (result != null) {
            if (view != null) {
                view.displayResult(result);
            }
        } else {
            if (view != null) {
                view.displayError();
            }
        }
    }

    public void destroy() {
        //do clear
        view = null;
    }
}
