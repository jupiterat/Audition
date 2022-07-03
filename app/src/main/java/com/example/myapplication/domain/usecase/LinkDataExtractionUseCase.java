package com.example.myapplication.domain.usecase;

import com.example.myapplication.infra.repository.DataRepository;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Observable;

public class LinkDataExtractionUseCase {
    private final DataRepository repository;

    public LinkDataExtractionUseCase(DataRepository repo) {
        repository = repo;
    }

    public void saveLinkData(String data) {
        String[] arr = data.split(";");
        if (arr.length >= 2) {
            repository.setLinkData(arr[1].trim());
        }
    }

    public Observable<String> extractData() {
        String originalStr = repository.getLinkData();
        if (originalStr.isEmpty()) {
            return Observable.error(new Throwable("link is invalid"));
        }

        return repository.getHtml(originalStr)
                .flatMap(s -> {
                    if (s.isEmpty()) {
                        return Observable.error(new Throwable("data is invalid"));
                    }
                    Pattern p = Pattern.compile("<title>(.*?)</title>");
                    Matcher m = p.matcher(s);
                    String result = null;
                    while (m.find()) {
                        result = m.group(1);
                    }
                    if (result == null || result.isEmpty()) {
                        return Observable.error(new Throwable("no link"));
                    }
                    return Observable.just(result);
                });

    }
}
