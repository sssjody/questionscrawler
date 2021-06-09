package com.example.questionscrawler.service.impl;

import com.example.questionscrawler.bean.QuestionItem;
import com.example.questionscrawler.dao.QuestionDao;
import com.example.questionscrawler.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionDao questionDao;

    @Override
    public void insertQuestions(List<QuestionItem> itemList) {
        for (QuestionItem item : itemList){
            questionDao.insertQuestion(item);
        }
    }
}
