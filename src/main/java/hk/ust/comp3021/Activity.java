package hk.ust.comp3021;

import hk.ust.comp3021.constants.ActivityState;

import java.util.ArrayList;
import java.util.List;

public class Activity {
    private final String activityID;
    private final String ServiceUnit;
    private final List<String> targetStudentDepartments;
    private final int duration;
    private final int capacity;
    private final ArrayList<Student> registeredStudents;
    private ActivityState state;

    public Activity(String activityID, String ServiceUnit, List<String> targetStudentDepartments,
                    int duration, int capacity) {
        this.activityID = activityID;
        this.ServiceUnit = ServiceUnit;
        this.targetStudentDepartments = targetStudentDepartments;
        this.duration = duration;
        this.capacity = capacity;
        this.registeredStudents = new ArrayList<>();
        this.state = ActivityState.OPEN;
    }

    /**
     * TODO: Part 1 Task 1: Implement this method to enroll a student to this activity
     * You should also enroll the activity to the student
     * Make sure no duplicate student is enrolled
     * You need to check whether the student is eligible to enroll this activity
     * You should also check whether the activity is full
     * */
    public synchronized boolean enroll(Student s){
        return false;
    }

    /**
    * TODO: Part 1 Task 1: Implement this method to drop a student from this activity
    * You should also drop the activity from the student
    * */
    public synchronized void drop(Student s){
    }

    public String getActivityID() {
        return activityID;
    }

    public String getServiceUnit() {
        return ServiceUnit;
    }

    public List<String> getTargetStudentDepartments() {
        return targetStudentDepartments;
    }

    public int getDuration() {
        return duration;
    }

    public int getCapacity() {
        return capacity;
    }

    public boolean isFull(){
        return registeredStudents.size() == capacity;
    }

    public ActivityState getState(){
        return this.state;
    }

    public void changeState(ActivityState s){
        // Only changeable from OPEN to CLOSED
        if (this.state == ActivityState.OPEN){
            this.state = s;
        }
    }
}
