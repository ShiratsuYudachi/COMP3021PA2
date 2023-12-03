package hk.ust.comp3021.action;
import hk.ust.comp3021.constants.*;
import hk.ust.comp3021.*;

public class ManagementAction {
    private final Activity activity;
    private final ManagementActionType action;

    public ManagementAction(Activity activity, ManagementActionType action) {
        this.activity = activity;
        this.action = action;
    }

    public ManagementActionType getAction() {
        return action;
    }

    public Activity getActivity() {
        return activity;
    }
}
