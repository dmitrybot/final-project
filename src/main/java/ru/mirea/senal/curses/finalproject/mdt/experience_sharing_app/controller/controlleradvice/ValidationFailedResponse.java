package ru.mirea.senal.curses.finalproject.mdt.experience_sharing_app.controller.controlleradvice;

import java.util.ArrayList;
import java.util.List;

public class ValidationFailedResponse {

    private List<ViolationErrors> violations = new ArrayList<>();

    public List<ViolationErrors> getViolations() {
        return violations;
    }

    public void setViolations(List<ViolationErrors> violations) {
        this.violations = violations;
    }
}