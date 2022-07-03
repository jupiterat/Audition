package com.example.myapplication.presentation.view;

public interface DataFragmentView {
    void displayResult(String result);

    void displayError();

    void showProgress();

    void hideProgress();

}
