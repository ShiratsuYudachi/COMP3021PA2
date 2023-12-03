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

    @Override
    public String toString() {
        return this.StudentID;
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof ActivityEvent) {
            ActivityEvent activityEvent = (ActivityEvent) event;
            Activity activity = activityEvent.getActivity();
            if (!this.registeredActivities.contains(activity)) return;

            switch (activityEvent.getActionType()) {
                case ACTIVITY_POSTPONED:
                    System.out.println("Student " + StudentID + " is notified that Activity " + activity.getActivityID() + " has postponed.");
                    break;
                case ACTIVITY_FINISHED:
                    // 更新学生状态，例如移动活动到完成列表
                    finishedActivities.add(activity);
                    registeredActivities.remove(activity);
                    passedDuration += activity.getDuration();
                    System.out.println("Student " + StudentID + " is notified that Activity " + activity.getActivityID() + " has finished.");
                    break;
                case ACTIVITY_CANCELLED:
                    // 从注册活动中移除
                    registeredActivities.remove(activity);
                    System.out.println("Student " + StudentID + " is notified that Activity " + activity.getActivityID() + " has cancelled.");
                    break;
            }
        }
    }
}
