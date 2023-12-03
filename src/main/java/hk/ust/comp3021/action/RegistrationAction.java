package hk.ust.comp3021.action;

import hk.ust.comp3021.constants.*;
import hk.ust.comp3021.*;
import java.util.*;

public class RegistrationAction {
    private final RegistrationActionType action;
    private final Student student;
    private final Activity activity;
    private boolean completed = false;

    public RegistrationAction(Student student, Activity activity, RegistrationActionType action)  {
        this.student = student;
        this.activity = activity;
        this.action = action;
    }

    public RegistrationActionType getAction() {
        return action;
    }

    public Student getStudent() {
        return student;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public boolean isCompleted() {
        return completed;
    }
}
