package com.example.myapplication.domain.usecase;

import com.example.myapplication.domain.entity.MentionEntity;
import com.example.myapplication.infra.repository.DataRepository;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MentionDataExtractionUseCase {

    private final DataRepository repository;

    public MentionDataExtractionUseCase(DataRepository repo) {
        repository = repo;
    }

    public void saveMentionData(String data) {
        repository.setMentionData(data);
    }

    public String extractData() {
        String originalStr = repository.getMentionData();
        if (originalStr.isEmpty()) {
            return null;
        }
        ArrayList<String> data = new ArrayList<>();

        String MENTION_REGEX_PATTERN = "@(.*?)(\\W|$)";
        Pattern pattern = Pattern.compile(MENTION_REGEX_PATTERN);
        Matcher matcher = pattern.matcher(originalStr);
        while (matcher.find()) {
            data.add(matcher.group(1));
        }
        if (data.isEmpty()) {
            return null;
        }
        return convertToJson(data);
    }

    private String convertToJson(ArrayList<String> data) {
        MentionEntity entity = new MentionEntity();
        entity.setMentions(data);
        Gson gson = new Gson();
        return gson.toJson(entity);
    }
}
