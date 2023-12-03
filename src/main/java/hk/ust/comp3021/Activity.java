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
    private final ArrayList<Student> waitlist;


    public Activity(String activityID, String ServiceUnit, List<String> targetStudentDepartments,
                    int duration, int capacity) {
        this.activityID = activityID;
        this.ServiceUnit = ServiceUnit;
        this.targetStudentDepartments = targetStudentDepartments;
        this.duration = duration;
        this.capacity = capacity;
        this.registeredStudents = new ArrayList<>();
        this.state = ActivityState.OPEN;
        this.waitlist = new ArrayList<>();
    }

    /**
     * TODO: Part 1 Task 1: Implement this method to enroll a student to this activity
     * You should also enroll the activity to the student
     * Make sure no duplicate student is enrolled
     * You need to check whether the student is eligible to enroll this activity
     * You should also check whether the activity is full
     * */
    public synchronized boolean enroll(Student s){

        if (s.getStudentID().equals("s1025") || s.getStudentID().equals("s1021") || s.getStudentID().equals("s1004")){
            int b = 0;
        }
        // 检查活动是否已满
        if (this.isFull()) {
            if (!waitlist.contains(s)) {  // 避免重复添加到等候名单
                waitlist.add(s);
            }
            return false;
        }

        // 检查学生是否符合条件
        if (!targetStudentDepartments.isEmpty() && !targetStudentDepartments.contains(s.getDepartment())) {
            return false;
        }

        // 检查是否重复报名
        if (registeredStudents.contains(s)) {
            return false;
        }

        // 添加学生
        registeredStudents.add(s);
        s.registerActivity(this);
        //System.out.println("Student Enrolled!");
        return true;

    }

    /**
    * TODO: Part 1 Task 1: Implement this method to drop a student from this activity
    * You should also drop the activity from the student
    * */
    public synchronized void drop(Student s){
        if (registeredStudents.contains(s)) {
            registeredStudents.remove(s);
            s.dropActivity(this);

            // 检查等候名单并添加学生到活动中（如果有空位）
            if (!waitlist.isEmpty()) {
                Student nextStudent = waitlist.remove(0); // 移除并获取等候名单中的第一个学生
                registeredStudents.add(nextStudent);
                nextStudent.registerActivity(this);
            }
        } else if (waitlist.contains(s)) {
            waitlist.remove(s);  // 如果学生在等候名单中，直接移除
        }
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

    @Override
    public String toString() {
        return this.activityID;
    }

    public void print(){
        System.out.println(activityID+", "+ServiceUnit+", "+duration+", "+capacity+", "+targetStudentDepartments.toString().replaceAll(", "," ")+", "+registeredStudents.toString().replace(", "," "));
    }
}
