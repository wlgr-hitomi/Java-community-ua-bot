package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

public class QuestionBank {
    private final List<Question> questions;
    private final Random random = new Random();

    public QuestionBank() {
        this.questions = loadQuestionsFromFile("questions.json");
    }

    private List<Question> loadQuestionsFromFile(String fileName) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return List.of(objectMapper.readValue(new File(fileName), Question[].class));
        } catch (IOException e) {
            throw new RuntimeException("Помилка завантаження питань: " + e.getMessage());
        }
    }

    public Question getRandomQuestion() {
        return questions.get(random.nextInt(questions.size()));
    }
}
