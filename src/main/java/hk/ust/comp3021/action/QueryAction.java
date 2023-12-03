package hk.ust.comp3021.action;

import hk.ust.comp3021.constants.*;

public class QueryAction<T> {
    private final T condition;
    private final QueryActionType action;

    public QueryAction(QueryActionType action, T condition) {
        this.action = action;
        this.condition = condition;
    }

    public T getCondition() {
        return condition;
    }

    public QueryActionType getAction() {
        return action;
    }
}
