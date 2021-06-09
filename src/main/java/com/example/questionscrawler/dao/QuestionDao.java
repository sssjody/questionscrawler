package com.example.questionscrawler.dao;

import com.example.questionscrawler.bean.QuestionItem;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionDao {
    void insertQuestion(QuestionItem item);
}
