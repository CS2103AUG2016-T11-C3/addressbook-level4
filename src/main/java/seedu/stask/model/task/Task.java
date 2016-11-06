package seedu.stask.model.task;

import java.util.Comparator;
import java.util.Objects;
import java.util.logging.Logger;

import seedu.stask.commons.core.LogsCenter;
import seedu.stask.commons.util.CollectionUtil;
import seedu.stask.model.tag.UniqueTagList;
import seedu.stask.model.task.Status.State;

//@@author A0139145E
/**
 * Represents a DatedTask in the to-do-list. Guarantees: details are present and
 * not null, field values are validated.
 */
public class Task implements ReadOnlyTask, Comparable<Task> {

    private static final Logger logger = LogsCenter.getLogger(Task.class);

    private Name name;
    private Description description;
    private Datetime datetime;
    private Status status;
    private UniqueTagList tags;

    /**
     * Every field must be present and not null.
     */
    public Task(Name name, Description description, Datetime datetime, Status status, UniqueTagList tags) {
        assert !CollectionUtil.isAnyNull(name, description, datetime, status, tags);
        this.name = name;
        this.description = description;
        this.datetime = datetime;
        this.status = status;
        this.tags = new UniqueTagList(tags); // protect internal tags from
        // changes in the arg list
        logger.fine("Task successfully created: " + this.toString());
    }

    /**
     * Copy constructor.
     */
    public Task(ReadOnlyTask source) {
        this(source.getName(), source.getDescription(), source.getDatetime(), source.getStatus(), source.getTags());
    }

    @Override
    public Name getName() {
        return name;
    }

    @Override
    public Description getDescription() {
        return description;
    }

    @Override
    public Datetime getDatetime() {
        return datetime;
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public UniqueTagList getTags() {
        return new UniqueTagList(tags);
    }

    /**
     * Replaces this task's tags with the tags in the argument tag list.
     */
    public void setTags(UniqueTagList replacement) {
        tags.setTags(replacement);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ReadOnlyTask // instanceof handles nulls
                        && this.isSameStateAs((ReadOnlyTask) other));
    }

    public void setName(Name name) {
        this.name = name;
    }

    public void setDescription(Description description) {
        this.description = description;
    }

    public void setDatetime(Datetime datetime) {
        this.datetime = datetime;
    }

    public boolean setAsDone() {
        this.setStatus(new Status(State.DONE));
        return true;
    }

    public boolean setAsOverdue() {
        this.setStatus(new Status(State.OVERDUE));
        return true;
    }

    public boolean setAsNorm() {
        this.setStatus(new Status(State.NONE));
        return true;
    }

    public boolean setAsExpire() {
        this.setStatus(new Status(State.EXPIRE));
        return true;
    }

    private void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing
        // your own
        return Objects.hash(name, description, datetime, status, tags);
    }

    @Override
    public String toString() {
        return getAsText();
    }

    //@@author A0143884W
    @Override
    public int compareTo(Task other) {
        return Comparators.NAME.compare(this, other);
    }

    /** 
     * Compares between tasks
     */
    public static class Comparators {
        /**
         * Compares tasks by alphabetical order of NAME
         */
        public static Comparator<Task> NAME = new Comparator<Task>() {
            @Override
            public int compare(Task t1, Task t2) {
                String task1name = t1.getName().toString().toLowerCase();
                String task2name = t2.getName().toString().toLowerCase();
                return task1name.compareTo(task2name);
            }
        };

        /**
         * Compares tasks by chronological order of DATE
         * Compares start time first before comparing end time
         */
        public static Comparator<Task> DATE = new Comparator<Task>() {
            @Override
            public int compare(Task t1, Task t2) {
                Datetime d1 = t1.getDatetime();
                Datetime d2 = t2.getDatetime();
                if (d1.getStart().compareTo(d2.getStart()) == 0) {
                    if (d1.getEnd() != null && d2.getEnd() != null) {
                        return d1.getEnd().compareTo(d2.getEnd());
                    } else {
                        return 0;
                    }
                } else {
                    return d1.getStart().compareTo(d2.getStart());
                }
            }
        };
    }
}
