package seedu.address.model;

import java.util.Date;
import java.util.Set;

import seedu.address.commons.core.UnmodifiableObservableList;
import seedu.address.model.task.ReadOnlyTask;
import seedu.address.model.task.Task;
import seedu.address.model.task.UniqueTaskList;
import seedu.address.model.undo.UndoTask;

/**
 * The API of the Model component.
 */
public interface Model {
    /** Clears existing backing model and replaces with the provided new data. */
    void resetData(ReadOnlyTaskBook newData);

    /** Returns the TaskBook */
    ReadOnlyTaskBook getTaskBook();

    /** Deletes the given task. */
    void deleteTask(ReadOnlyTask target) throws UniqueTaskList.TaskNotFoundException;

    /** Adds the given task */
    boolean addTask(Task toAdd);
    
    /** Adds the given undo */
    void addUndo(String command, ReadOnlyTask data);
    
    /** Adds the given undo */
    void addUndo(String command, ReadOnlyTask postData, ReadOnlyTask preData);

    /** Marks the given task as completed */
    void completeTask(ReadOnlyTask target) throws UniqueTaskList.TaskNotFoundException;
    
    /** Marks the given task as uncompleted */
    void uncompleteTask(ReadOnlyTask target) throws UniqueTaskList.TaskNotFoundException;
    
    /** Undoes the last reversible action */
    UndoTask undoTask();
    
    /** Returns the filtered dated task list as an {@code UnmodifiableObservableList<ReadOnlyTask>} */
    UnmodifiableObservableList<ReadOnlyTask> getFilteredDatedTaskList();
    
    /** Returns the filtered undated task list as an {@code UnmodifiableObservableList<ReadOnlyTask>} */
    UnmodifiableObservableList<ReadOnlyTask> getFilteredUndatedTaskList();

    /** Updates the filter of the filtered tasks list to show all persons */
    void updateFilteredListToShowAll();

    /** Updates the filter of the filtered tasks list to filter by the given date*/
    void updateFilteredTaskListByDate(Date date);
    
    /** Updates the filter of the filtered tasks list to filter by the given keywords*/
    void updateFilteredTaskListByKeywords(Set<String> keywords);

    /** Updates the filter of the filtered tasks list to filter by the given keyword (od/done)*/
    void updateFilteredTaskListByStatus(String... keyword);
    
    /** Marks task as overdue as compared to system current Datetime */
    void overdueTask(ReadOnlyTask target) throws UniqueTaskList.TaskNotFoundException;
}
