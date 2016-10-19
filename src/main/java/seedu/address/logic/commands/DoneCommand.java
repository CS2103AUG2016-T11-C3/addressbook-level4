package seedu.address.logic.commands;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.UnmodifiableObservableList;
import seedu.address.model.person.*;
import seedu.address.model.person.UniquePersonList.PersonNotFoundException;
import seedu.address.ui.PersonListPanel;

/**
 * Sets as completed a task identified using it's last displayed index from the address book.
 */
public class DoneCommand extends Command {

    public final int targetIndex;

    public static final String COMMAND_WORD = "done";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Sets as completed the task identified by the index number used in the last person listing.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DONE_TASK_SUCCESS = "Completed Task: %1$s";

    public DoneCommand(int targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute() {

        UnmodifiableObservableList<ReadOnlyTask> lastShownList = model.getFilteredPersonList();
        UnmodifiableObservableList<ReadOnlyTask> lastUndatedTaskList = model.getFilteredUndatedTaskList();

        if ((targetIndex <= PersonListPanel.DATED_DISPLAY_INDEX_OFFSET 
                && lastUndatedTaskList.size() < targetIndex)  // index <= 10 && index > size of list
           || (targetIndex > PersonListPanel.DATED_DISPLAY_INDEX_OFFSET  // index > 10 && index - 10 > size of list 
                   && lastShownList.size() < targetIndex - PersonListPanel.DATED_DISPLAY_INDEX_OFFSET)) {
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        ReadOnlyTask readTaskToComplete;
        if (targetIndex > PersonListPanel.DATED_DISPLAY_INDEX_OFFSET) {
            readTaskToComplete = lastShownList.get(targetIndex - 1 - PersonListPanel.DATED_DISPLAY_INDEX_OFFSET);
        }
        else {
            readTaskToComplete = lastUndatedTaskList.get(targetIndex - 1);
        }
        
        if (!readTaskToComplete.getStatus().equals(new Status(Status.State.DONE))){
            try {
                model.completeTask(readTaskToComplete);
            } catch (PersonNotFoundException pnfe) {
                assert false : "The target task cannot be found";
            }
        }
        
        //TODO look at posting a set as completed event.
        //EventsCenter.getInstance().post(new JumpToListRequestEvent(targetIndex - 1));
        return new CommandResult(String.format(MESSAGE_DONE_TASK_SUCCESS, readTaskToComplete));

    }

}
