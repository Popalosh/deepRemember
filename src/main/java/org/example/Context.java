package org.example;

import java.util.List;

public class Context {
    private String topic;
    private String goal;
    private List<String> steps;
    private String currentStep;
    private String code;
    private String notes;

    // Конструктор
    public Context(String topic, String goal, List<String> steps, String currentStep, String code, String notes) {
        this.topic = topic;
        this.goal = goal;
        this.steps = steps;
        this.currentStep = currentStep;
        this.code = code;
        this.notes = notes;
    }

    // Геттеры и сеттеры
    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public List<String> getSteps() {
        return steps;
    }

    public void setSteps(List<String> steps) {
        this.steps = steps;
    }

    public String getCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(String currentStep) {
        this.currentStep = currentStep;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}