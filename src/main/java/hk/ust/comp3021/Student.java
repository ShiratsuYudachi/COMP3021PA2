package hk.ust.comp3021;

import java.util.*;

public class Student implements EventHandler {
    private final String StudentID;
    private final int YearOfStudy;
    private final String department;
    private final int totalDurationRequired;
    private int passedDuration;
    private final List<Activity> registeredActivities;
    private final List<Activity> finishedActivities;

    public Student(String studentID, int yearOfStudy, String department, int totalDurationRequired) {
        this.StudentID = studentID;
        this.YearOfStudy = yearOfStudy;
        this.registeredActivities = new ArrayList<>();
        this.finishedActivities = new ArrayList<>();
        this.department = department;
        this.totalDurationRequired = totalDurationRequired;
        this.passedDuration = 0;
    }

    public String getStudentID() {
        return StudentID;
    }

    public int getYearOfStudy() {
        return YearOfStudy;
    }

    public String getDepartment() {
        return department;
    }

    public int getTotalDurationRequired() {
        return totalDurationRequired;
    }

    public int getPassedDuration() {
        return passedDuration;
    }

    /**The check of repeated registration would be in Activity Class, not this class
     * So this method only do simple registration
    * */
    public void registerActivity(Activity a){
        registeredActivities.add(a);
    }

    public void dropActivity(Activity a){
        registeredActivities.remove(a);
    }
}
