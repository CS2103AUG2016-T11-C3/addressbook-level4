package seedu.stask.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.collections.transformation.FilteredList;
import seedu.stask.commons.core.ComponentManager;
import seedu.stask.commons.core.LogsCenter;
import seedu.stask.commons.core.UnmodifiableObservableList;
import seedu.stask.commons.events.model.TaskBookChangedEvent;
import seedu.stask.commons.events.storage.StorageDataPathChangedEvent;
import seedu.stask.commons.events.ui.JumpToListRequestEvent;
import seedu.stask.commons.events.ui.UpdateListCountEvent;
import seedu.stask.commons.util.StringUtil;
import seedu.stask.logic.commands.AddCommand;
import seedu.stask.model.task.Datetime;
import seedu.stask.model.task.ReadOnlyTask;
import seedu.stask.model.task.Status;
import seedu.stask.model.task.Task;
import seedu.stask.model.task.UniqueTaskList;
import seedu.stask.model.task.UniqueTaskList.TaskNotFoundException;
import seedu.stask.model.undo.UndoList;
import seedu.stask.model.undo.UndoTask;

/**
 * Represents the in-memory model of the task book data.
 * All changes to any model should be synchronized.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final TaskBook taskBook;
    private final FilteredList<Task> filteredDatedTasks;
    private final FilteredList<Task> filteredUndatedTasks;
    private UndoList undoableTasks;
    private UndoList redoableTasks;

    /**
     * Initializes a ModelManager with the given TaskBook
     * TaskBook and its variables should not be null
     */
    public ModelManager(TaskBook src, UserPrefs userPrefs) {
        super();
        assert src != null;
        assert userPrefs != null;

        logger.fine("Initializing with task book: " + src + " and user prefs " + userPrefs);

        taskBook = new TaskBook(src);
        filteredDatedTasks = new FilteredList<>(taskBook.getDatedTasks());
        filteredUndatedTasks = new FilteredList<>(taskBook.getUndatedTasks());
        undoableTasks = new UndoList();
        redoableTasks = new UndoList();
    }

    public ModelManager() {
        this(new TaskBook(), new UserPrefs());
    }

    public ModelManager(ReadOnlyTaskBook initialData, UserPrefs userPrefs) {
        taskBook = new TaskBook(initialData);
        filteredDatedTasks = new FilteredList<>(taskBook.getDatedTasks());
        filteredUndatedTasks = new FilteredList<>(taskBook.getUndatedTasks());
        undoableTasks = new UndoList();
        redoableTasks = new UndoList();
    }

    //@@author A0139024M 
    /**
     * Check the start time and date time of all tasks in application
     */
    public void checkStatus(){
        checkDatedTaskStatus(taskBook.getUniqueDatedTaskList(), getCurrentTime(), DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm"));
        checkUndatedTaskStatus(taskBook.getUniqueUndatedTaskList());
    }

    /**
     * Get the current time of the local machine
     * @return
     */
    private LocalDateTime getCurrentTime() {
        return LocalDateTime.now();
    }

    /**
     * Update the status of all Undated/Floating Tasks in application
     * @param floating
     */
    private void checkUndatedTaskStatus(UniqueTaskList floating) {
        for (Task undatedTarget : floating) {
            if (undatedTarget.getStatus().status == Status.State.OVERDUE || undatedTarget.getStatus().status == Status.State.EXPIRE ) {
                try {
                    taskBook.resetFloatingTaskStatus(undatedTarget);
                } catch (TaskNotFoundException e){}
            }
        }
    }

    /**
     * Update the status of all Dated Tasks in application
     * @param tasks
     * @param currentTime
     * @param formatter
     */
    private void checkDatedTaskStatus(UniqueTaskList tasks, LocalDateTime currentTime, DateTimeFormatter formatter) {
        for (Task target : tasks) {
            assert target.getDatetime().getStart() != null;
            //Check if the task is a Deadline
            if (target.getDatetime().getEnd() == null) {
                updateDeadlineStatus(currentTime, formatter, target);
            } else if (target.getDatetime().getEnd() != null) { //Check if the task is a event Event
                updateEventStatus(currentTime, formatter, target);                                
            }
        }
    }

    /**
     * Updated the status of all Event Tasks in application
     * @param currentTime
     * @param formatter
     * @param target
     */
    private void updateEventStatus(LocalDateTime currentTime, DateTimeFormatter formatter, Task target) {
        String endDateTime = getEventEndTimeInStringFormat(target);
        LocalDateTime dateTime = LocalDateTime.parse(endDateTime,formatter);
        if (dateTime.isBefore(currentTime) && target.getStatus().status != Status.State.DONE) {
            try {
                taskBook.setExpire(target);
            } catch (TaskNotFoundException e) {
                throw new AssertionError("Impossible!");
            }                
        }
        else if (dateTime.isAfter(currentTime) && (target.getStatus().status == Status.State.EXPIRE 
                || target.getStatus().status == Status.State.OVERDUE)) {
            try {
                taskBook.postponeTask(target);
            } catch (TaskNotFoundException e) {
                throw new AssertionError("Impossible!");
            }
        }
    }

    /**
     * Get the End Time of Event in String Format
     * @param target
     * @return
     */
    private String getEventEndTimeInStringFormat(Task target) {
        return target.getDatetime().toString().substring(21);
    }

    /**
     * Updated the status of all Deadline tasks in application
     * @param currentTime
     * @param formatter
     * @param target
     */
    private void updateDeadlineStatus(LocalDateTime currentTime, DateTimeFormatter formatter, Task target) {
        LocalDateTime dateTime = convertDatetimeFormat(formatter, target);
        if (dateTime.isBefore(currentTime) && target.getStatus().status != Status.State.DONE) {
            try {
                taskBook.setTaskOverdue(target);
            } catch (TaskNotFoundException e) {
                throw new AssertionError("Impossible!");
            }                
        }
        else if (dateTime.isAfter(currentTime) && (target.getStatus().status == Status.State.OVERDUE || target.getStatus().status == Status.State.EXPIRE)) {
            try {
                taskBook.postponeTask(target);
            }catch (TaskNotFoundException e) {
                throw new AssertionError("Impossible!");
            }
        }
    }

    private LocalDateTime convertDatetimeFormat(DateTimeFormatter formatter, Task target) {
        return LocalDateTime.parse(target.getDatetime().toString(), formatter);
    }
    //@@author

    @Override
    public void resetData(ReadOnlyTaskBook newData) {
        taskBook.resetData(newData);
        undoableTasks = new UndoList(); 
        indicateTaskBookChanged();
        raise(new UpdateListCountEvent(this));
    }

    @Override
    public ReadOnlyTaskBook getTaskBook() {
        return taskBook;
    }

    /** Raises an event to indicate the model has changed */
    private void indicateTaskBookChanged() {
        checkStatus();
        raise(new TaskBookChangedEvent(taskBook));
    }

    @Override
    public synchronized void deleteTask(ReadOnlyTask target) throws UniqueTaskList.TaskNotFoundException {
        taskBook.removeTask(target);
        indicateTaskBookChanged();
        raise(new UpdateListCountEvent(this));
    }
    
    //@@author A0139024M
    private void filterClashingEvents(Task target) {
        filteredDatedTasks.setPredicate((new PredicateExpression(new ClashingQualifier(target)))::satisfies);
    }
    //@@author

    //@@author A0143884W
    @Override
    public synchronized int addTask(Task target) {
        int checkForDuplicateOrClash = taskBook.addTask(target);
        indicateTaskBookChanged();
        filterClashingEvents(target);
        checkForDuplicateOrClash = checkForClash(checkForDuplicateOrClash);
        scrollToAddedTask(target);
        return checkForDuplicateOrClash;
    }

    /**
     * List all Dated Tasks if no clashing events, else update int checkForDuplicateOrClash
     * 
     * @param checkForDuplicate int that checks for duplicates 
     * @return int that has been updated to check for clashing events 
     */
	private int checkForClash(int checkForDuplicate) {
		if (filteredDatedTasks.size() <= 1) {
            updateFilteredListToShowAll();
        }
        else if (filteredDatedTasks.size() > 1){
            checkForDuplicate = AddCommand.CLASH;
        }
		return checkForDuplicate;
	}

    /**
     * After task is added, scroll to it in the UndatedListPanel || DatedListPanel
     * @param target Task object that is being added
     */ 
    private void scrollToAddedTask(Task target) {
        int [] result = indexOfAddedTask(target);       
        raise (new JumpToListRequestEvent(result[0], result[1]));
    }

    /**
     * Find the task's index and the list it belongs to 
     * @param target Task object that is being added 
     * @return int [] containing the task's index and the list it belongs to 
     */
    private int[] indexOfAddedTask(Task target) {
        int datedTaskIndex = filteredDatedTasks.indexOf(target);
        int undatedTaskIndex = filteredUndatedTasks.indexOf(target);
        int [] result = new int[2];
        // indexOf returns -1 if task not found in the list
        if (datedTaskIndex == -1){
            result[0] = undatedTaskIndex;
            result[1] = JumpToListRequestEvent.UNDATED_LIST;
        } else if (undatedTaskIndex == -1){
            result[0] = datedTaskIndex;
            result[1] = JumpToListRequestEvent.DATED_LIST;
        }
        return result;
    }
    //@@author

    //@@author A0139145E
    @Override
    public synchronized void completeTask (ReadOnlyTask target) 
                                throws UniqueTaskList.TaskNotFoundException {
        taskBook.completeTask(target);
        updateFilteredListToShowAll();
        indicateTaskBookChanged();
    }

    @Override
    public synchronized void uncompleteTask(ReadOnlyTask target) 
                                throws UniqueTaskList.TaskNotFoundException {
        taskBook.uncompleteTask(target);
        updateFilteredListToShowAll();
        indicateTaskBookChanged();
    }

    @Override
    public synchronized UndoTask undoTask() {
        return undoableTasks.retrieve();
    }

    @Override
    public synchronized UndoTask redoTask() {
        return redoableTasks.retrieve();
    }

    @Override
    public synchronized void clearRedo() {
        redoableTasks = new UndoList();
    }

    @Override
    public synchronized void overdueTask (Task target) throws TaskNotFoundException {
        taskBook.setTaskOverdue(target);
        updateFilteredListToShowAll();
        indicateTaskBookChanged();
    }

    @Override
    public void addUndo(String command, ReadOnlyTask postData) {
        undoableTasks.add(command, postData, null);
    }

    @Override
    public void addUndo(String command, ReadOnlyTask postData, ReadOnlyTask preData) {
        undoableTasks.add(command, postData, preData);
    }

    @Override
    public void addUndo(UndoTask target) {
        addUndo(target.getCommand(), target.getPostData(), target.getPreData());
    }

    @Override
    public void addRedo(UndoTask target) {
        addRedo(target.getCommand(), target.getPostData(), target.getPreData());
    }

    private void addRedo(String command, ReadOnlyTask postData, ReadOnlyTask preData) {
        redoableTasks.add(command, postData, preData);
    }

    //@@author 

    //=========== Filtered Task List Accessors =============================================================== 

    @Override
    public UnmodifiableObservableList<ReadOnlyTask> getFilteredDatedTaskList() {
        return new UnmodifiableObservableList<>(filteredDatedTasks);
    }

    @Override
    public UnmodifiableObservableList<ReadOnlyTask> getFilteredUndatedTaskList() {
        return new UnmodifiableObservableList<>(filteredUndatedTasks);
    }

    //@@author A0139145E
    @Override
    public void updateFilteredListToShowAll() {
        updateFilteredTaskListByStatus("NONE", "OVERDUE", "EXPIRE");
    }
    //@@author

    // called by ViewCommand
    @Override 
    public void updateFilteredTaskListByDate(Date date){
        filteredDatedTasks.setPredicate((new PredicateExpression(new DateQualifier(date)))::satisfies);
        raise(new UpdateListCountEvent(this));
    }

    // called by FindCommand
    @Override
    public void updateFilteredTaskListByKeywords(Set<String> keywords){
        updateFilteredTaskList(new PredicateExpression(new TaskQualifier(keywords)));
    }

    // called by ListCommand
    @Override
    public void updateFilteredTaskListByStatus(String... keyword){
        ArrayList<String> listOfKeywords = new ArrayList<>();
        for (String word : keyword){
            listOfKeywords.add(word);
        }
        updateFilteredTaskList(new PredicateExpression(new StatusQualifier(listOfKeywords)));
    }

    private void updateFilteredTaskList(Expression expression) {
        filteredDatedTasks.setPredicate(expression::satisfies);
        filteredUndatedTasks.setPredicate(expression::satisfies);
        raise(new UpdateListCountEvent(this));
    }

    //========== Inner classes/interfaces used for filtering ==================================================

    interface Expression {
        boolean satisfies(ReadOnlyTask task);
        String toString();
    }

    private class PredicateExpression implements Expression {

        private final Qualifier qualifier;

        PredicateExpression(Qualifier qualifier) {
            this.qualifier = qualifier;
        }

        @Override
        public boolean satisfies(ReadOnlyTask task) {
            return qualifier.run(task);
        }

        @Override
        public String toString() {
            return qualifier.toString();
        }
    }

    interface Qualifier {
        boolean run(ReadOnlyTask task);
        String toString();
    }

    //@@author A0139528W
    private class TaskQualifier implements Qualifier {
        private Set<String> taskKeyWords;

        TaskQualifier(Set<String> taskKeyWords) {
            this.taskKeyWords = taskKeyWords;
        }

        @Override
        public boolean run(ReadOnlyTask task) {
            boolean matchTaskNames = taskKeyWords.stream()
                    .filter(keyword -> StringUtil.containsIgnoreCase(task.getName().fullName, keyword))
                    .findAny().isPresent();
            boolean matchDateTime = taskKeyWords.stream()
                    .filter(keyword -> StringUtil.containsIgnoreCase(task.getDatetime().toString(), keyword))
                    .findAny().isPresent();
            boolean matchTaskDescription = taskKeyWords.stream()
                    .filter(keyword -> StringUtil.containsIgnoreCase(task.getDescription().value, keyword))
                    .findAny().isPresent();
            boolean matchListTags = taskKeyWords.stream()
                    .filter(keyword -> StringUtil.containsIgnoreCase(task.getTags().toString(), keyword))
                    .findAny().isPresent();
            return (matchTaskNames || matchDateTime || matchTaskDescription || matchListTags);
        }

        @Override
        public String toString() {
            return "task=" + String.join(", ", taskKeyWords);
        }
    }
    //@@author 

    //@@author A0139145E
    private class StatusQualifier implements Qualifier {
        private ArrayList<Status> statusList;

        StatusQualifier (ArrayList<String> stateKeyWords) {
            this.statusList = new ArrayList<Status>();
            for (String word : stateKeyWords) {
                statusList.add(new Status(word));
            }
        }
        
        @Override
        public boolean run (ReadOnlyTask task) {
            for (Status key : statusList) {
                if (task.getStatus().equals(key)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public String toString() {
            return "status=" + statusList.toString();
        }
    }
    //@@author

    //@@author A0143884W
    /**
     * Qualifies if tasks fall on input date
     */
    private class DateQualifier implements Qualifier {
        private Date inputDate;

        DateQualifier(Date date) {
            this.inputDate = date;
        }

        @Override
        public boolean run(ReadOnlyTask task) {

            Datetime taskDate = task.getDatetime();
            Date startDate = taskDate.getStart();
            Date endDate = taskDate.getEnd();

            if (sameDate(startDate)) { // check start date
                return true;
            } else if (endDate != null && sameDate(endDate)) {  // check end date only
                return true;
            } else if (endDate != null && inputDate.after(startDate) && inputDate.before(endDate)) { // check between start and end date only
                return true;
            } else {
                return false;
            }
        }

        @Override
        public String toString() {
            return "date=" + inputDate.toString();
        }

        /**
         * Checks if the Date other is the same as inputDate
         * @param other Date object that is being compared to
         * @return true if Date objects are the same, excluding time
         */
        private boolean sameDate(Date other){
            return inputDate.getDate() == other.getDate() && inputDate.getMonth() == other.getMonth() 
                    && inputDate.getYear() == other.getYear();
        }
    }
  //@@author
    
    //@@author A0139024M
    private class ClashingQualifier implements Qualifier {
        private ReadOnlyTask newDatedTask;

        ClashingQualifier(ReadOnlyTask target) {
            this.newDatedTask = target;
        }

        @Override
        public boolean run(ReadOnlyTask task) {
            
            //Ensure that the task added and the dated task from the original list are not events
            if (task.getDatetime().getEnd() == null || newDatedTask.getDatetime().getEnd() == null) {
                return false;
            } else if (task.getDatetime().getStart().before(newDatedTask.getDatetime().getEnd())
                    && task.getDatetime().getEnd().after(newDatedTask.getDatetime().getStart())) {
                return true;
            } else {
                return false;
            }
        }

    }
    //@@author

    //@@author A0139528W
    @Subscribe
    public void handleStorageDataChangedEvent(StorageDataPathChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event, "Creating task.xml in a new location."));
        indicateTaskBookChanged();
    }
    //@@author
}