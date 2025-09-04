package com.prescriptionservice.repositories;

import com.prescriptionservice.models.entity.Question;
import org.springframework.stereotype.Repository;
import jakarta.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * In-memory implementation of QuestionRepository using HashMap storage.
 * Initializes with reusable consultation questions that can be associated with multiple products.
 */
@Repository
public class QuestionRepositoryImpl implements QuestionRepository {
    
    private final Map<String, Question> questions = new ConcurrentHashMap<>();
    
    @PostConstruct
    public void initializeQuestions() {
        addQuestion("Q1", "Do you experience nose itchiness when near pears?", Question.QuestionType.YES_NO, true);
        addQuestion("Q2", "Have you ever sneezed uncontrollably in a fruit market?", Question.QuestionType.YES_NO, true);
        addQuestion("Q3", "Does your throat feel scratchy after eating pear-flavored items?", Question.QuestionType.YES_NO, true);
        addQuestion("Q4", "Do you avoid pear orchards during blooming season?", Question.QuestionType.YES_NO, false);
        addQuestion("Q5", "Have you previously had an adverse reaction to allergy medication?", Question.QuestionType.YES_NO, true);
    }
    
    private void addQuestion(String id, String text, Question.QuestionType type, boolean required) {
        questions.put(id, new Question(id, text, type, required));
    }
    
    @Override
    public List<Question> findByIds(List<String> questionIds) {
        return questionIds.stream()
                .map(questions::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Question> findAll() {
        return new ArrayList<>(questions.values());
    }
    
    @Override
    public Optional<Question> findById(String id) {
        return Optional.ofNullable(questions.get(id));
    }
}