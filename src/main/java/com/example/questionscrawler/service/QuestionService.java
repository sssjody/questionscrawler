package com.example.questionscrawler.service;

import com.example.questionscrawler.bean.QuestionItem;

import java.util.List;

public interface QuestionService {
    void insertQuestions(List<QuestionItem> itemList);
}
