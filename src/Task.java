import java.util.Date;

public class Task implements Comparable<Task> {
    private String _message;
    private Date _deadline;
    private boolean _isDone;

    public Task(String message, Date deadline) {
        _message = message;
        _deadline = deadline;
        _isDone = false;
    }

    @Override
    /** Allows for comparisons of tasks in terms of their deadlines. */
    public int compareTo(Task o) {
        if (o.hasDeadline() && !hasDeadline()) {
            return -1*Integer.MAX_VALUE;
        } else if (!o.hasDeadline() && hasDeadline()) {
            return Integer.MAX_VALUE;
        }
        return -1*getDeadline().compareTo(o.getDeadline());
    }

    /** Returns the message of the task. */
    public String getMessage() {
        return _message;
    }

    /** Returns the deadline of the task, or null if no deadline. */
    public Date getDeadline() {
        return _deadline;
    }

    /** Returns whether the task has a deadline or not. */
    public boolean hasDeadline() {
        return _deadline == null;
    }

    /** Returns whether the task is done. */
    public boolean isDone() {
        return _isDone;
    }

    /** Marks the task as completed. */
    public void complete() {
        _isDone = true;
    }
}
