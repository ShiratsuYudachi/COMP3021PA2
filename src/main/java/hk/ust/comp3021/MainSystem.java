//completed with GPT4,
package hk.ust.comp3021;

import hk.ust.comp3021.constants.*;
import hk.ust.comp3021.action.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class MainSystem {
    private final List<Student> students;
    private final List<Activity> activities;
    private final List<EventHandler> listeners;
    private CountDownLatch latch;

    public MainSystem() {
        this.students = new ArrayList<>();
        this.activities = new ArrayList<>();
        this.listeners = new ArrayList<>();
    }


    /**
     * TODO Part 1 Task 1: Implement enroll() and drop() in Activity.java
     */
    /**
     * TODO Part 1 Task 2: Implement this method
     */
    public void concurrentRegistration(List<RegistrationAction> actions, CountDownLatch latch) {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        for (RegistrationAction action : actions) {
            executor.submit(() -> {
                processAction(action);
                latch.countDown();
            });
        }

        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void processAction(RegistrationAction action) {
        // 根据 action 的具体类型（ENROLL 或 DROP）来处理
        Activity activity = action.getActivity();
        Student student = action.getStudent();
        synchronized (activity) {
            if (action.getAction() == RegistrationActionType.ENROLL) {
                activity.enroll(student);
            } else if (action.getAction() == RegistrationActionType.DROP) {
                activity.drop(student);
            }
        }
        action.setCompleted(true);
    }


    /**
     * TODO: Part 2 Task 3: Define interface EventHandler and make correct class implement it
     * */
    /**
     * TODO: Part 2 Task 4: Define and implement class ActivityEvent that implements Event
     * */
    public void addListener(EventHandler listener) {
        listeners.add(listener);
    }

    public void dispatchEvent(Event event) {
        listeners.forEach(listener -> listener.onEvent(event));
    }

    /**
     * TODO: Part 2 Task 5: Implement the following method for event handling.
     * You need to call dispatchEvent() in this method.
     * */

    public void studentGetUpdate(ManagementAction action) {
        ActivityEvent event = new ActivityEvent(action.getActivity(), action.getAction());
        dispatchEvent(event);
    }
    /**
     * TODO: Part 3 Task 6: Implement getStudent(String studentID) and getActivity(String activityID) use lambda expression and/or functional programming
    * */
    // Start from here
    public Student getStudent(String studentID) {
        return students.stream()
                .filter(s -> s.getStudentID().equals(studentID))
                .findFirst()
                .orElse(null);
    }

    public Activity getActivity(String activityID) {
        return activities.stream()
                .filter(a -> a.getActivityID().equals(activityID))
                .findFirst()
                .orElse(null);
    }


    /**
    * TODO: Part 3 Task 7: Implement following methods using lambda expression and/or functional programming
    * */
    public List<Student>  searchStudentByIntegerConditionLambda(QueryAction<Integer> query) {
        return students.stream()
                .filter(s -> {
                    switch (query.getAction()) {
                        case YEAR_IS:
                            return query.getCondition().equals(s.getYearOfStudy());
                        case REMAINING_DURATION_LARGER_THAN:
                            return (s.getTotalDurationRequired() - s.getPassedDuration()) > query.getCondition();
                        case REMAINING_DURATION_SMALLER_THAN:
                            return (s.getTotalDurationRequired() - s.getPassedDuration()) < query.getCondition();
                        default:
                            return true;
                    }
                })
                .collect(Collectors.toList());
    }

    /**
     * TODO: Part 3 Task 7
     * */
    public List<Activity> searchActivityByStringConditionLambda(QueryAction<String> query){
        return activities.stream()
                .filter(a -> {
                    switch (query.getAction()) {
                        case ID_CONTAINS:
                            return a.getActivityID().contains(query.getCondition());
                        case SERVICE_UNIT_IS:
                            return a.getServiceUnit().equals(query.getCondition());
                        case PREREQUISITE_CONTAINS:
                            return a.getTargetStudentDepartments().contains(query.getCondition());
                        default:
                            return false;
                    }
                })
                .collect(Collectors.toList());
    }

    /**
     * TODO: Part 3 Task 7
     * */
    public List<Activity> sortActivityByBooleanConditionByLambda(QueryAction<Boolean> query) {
        Comparator<Activity> comparator;
        if (query.getAction() == QueryActionType.DURATION_CAPACITY) {
            comparator = Comparator.comparing(Activity::getDuration)
                    .thenComparing(Activity::getCapacity);
        } else if (query.getAction() == QueryActionType.CAPACITY_DURATION) {
            comparator = Comparator.comparing(Activity::getCapacity)
                    .thenComparing(Activity::getDuration);
        } else {
            return new ArrayList<>(); // 如果不是排序操作，返回空列表
        }

        if (query.getCondition()) {
            return activities.stream()
                    .sorted(comparator)
                    .collect(Collectors.toList());
        } else {
            return activities.stream()
                    .sorted(comparator.reversed())
                    .collect(Collectors.toList());
        }

    }

    public void parseStudents(String fileName) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(", ");
                String studentID = parts[0];
                int yearOfStudy = Integer.parseInt(parts[1]);
                String department = parts[2];
                int totalDurationRequired = Integer.parseInt(parts[3]);
                Student student = new Student(studentID, yearOfStudy, department, totalDurationRequired);
                students.add(student);
            }
        }
    }

    public void parseActivities(String fileName) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(", ");
                String activityID = parts[0];
                String serviceUnit = parts[1];
                int duration = Integer.parseInt(parts[2]);
                int capacity = Integer.parseInt(parts[3]);
                List<String> preferences;
                if (parts[4].length() > 2){
                    preferences = Arrays.asList(parts[4].substring(1, parts[4].length() - 1).split(" "));
                }
                else {
                    preferences = new ArrayList<>();
                }
                Activity activity = new Activity(activityID, serviceUnit, preferences, duration, capacity);
                activities.add(activity);
            }
        }
    }

    public void processRegistration(String fileName) throws IOException {
        List<RegistrationAction> actions = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(", ");
                String studentID = parts[0];
                Student student = getStudent(studentID);
                String activityCode = parts[1];
                Activity activity = getActivity(activityCode);
                RegistrationActionType actType = RegistrationActionType.valueOf(parts[2]);
                actions.add(new RegistrationAction(student, activity, actType));
            }
        }
        latch = new CountDownLatch(actions.size());
        concurrentRegistration(actions,latch);

        try {
            latch.await();
        } catch (InterruptedException e){

        }


    }

    public void processManagement(String fileName) throws IOException {
        List<ManagementAction> actions = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(", ");
                String activityCode = parts[0];
                Activity activity = getActivity(activityCode);
                ManagementActionType actType = ManagementActionType.valueOf(parts[1]);
                actions.add(new ManagementAction(activity, actType));
            }
        }
        // You need to have your implementation done to allow the following code to work
        this.students.forEach(this::addListener);
        actions.forEach(this::studentGetUpdate);
    }

    /**
     * TODO: Part 3 Task 8: Finish this method
     */
    public void processQuery(String fileName) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(", ");
                QueryActionType actType = QueryActionType.valueOf(parts[0]);

                String log = "";
                switch (actType) {
                    case YEAR_IS:
                    case REMAINING_DURATION_LARGER_THAN:
                    case REMAINING_DURATION_SMALLER_THAN:
                        int conditionInt = Integer.parseInt(parts[1]);
                        List<Student> studentsResult = searchStudentByIntegerConditionLambda(new QueryAction<>(actType, conditionInt));
                        log = studentsResult.toString();
                        log = log.substring(1,log.length()-1);
                        System.out.println(log);
                        break;
                    case DURATION_CAPACITY:
                    case CAPACITY_DURATION:
                        boolean conditionBoolean = Boolean.parseBoolean(parts[1]);
                        List<Activity> activitiesResult = sortActivityByBooleanConditionByLambda(new QueryAction<>(actType, conditionBoolean));
                        log = activitiesResult.toString();
                        log = log.substring(1,log.length()-1);
                        System.out.println(log);
                        break;
                    case ID_CONTAINS:
                    case SERVICE_UNIT_IS:
                    case PREREQUISITE_CONTAINS:
                        String conditionString = parts[1];
                        List<Activity> filteredActivities = searchActivityByStringConditionLambda(new QueryAction<>(actType, conditionString));
                        log = filteredActivities.toString();
                        log = log.substring(1,log.length()-1);
                        System.out.println(log);
                        break;
                    default:
                        System.out.println("Unknown query action type: " + actType);
                        break;
                }
            }
        }
    }
    
    public void printActivities(){
        for (Activity activity: activities) {
            activity.print();
        }
    }
    public static void main(String[] args) throws IOException {
        System.out.println("=============== System Start ===============");
        MainSystem system = new MainSystem();
        system.parseStudents("input/student.txt");
        //system.parseStudents("input/testStudents.txt");
        system.parseActivities("input/activity.txt");
        //system.parseActivities("input/testActivity.txt");
        // Part 1: Parallel Registration
        System.out.println("=============== Part 1 ===============");
        system.processRegistration("input/registrationActions.txt");
        //system.processRegistration("input/testReg.txt");
        System.out.println("Part 1 Finished");
        //system.printActivities();

        // Part 2: Sequential Event Management
        System.out.println("=============== Part 2 ===============");
        system.processManagement("input/managementActions.txt");
        //system.processManagement("input/testMGMT.txt");


        // Part 3: Functional Information Retrieval
        System.out.println("=============== Part 3 ===============");
        system.processQuery("input/queryActions.txt");
        //system.processQuery("input/testQuery.txt");


    }
}
