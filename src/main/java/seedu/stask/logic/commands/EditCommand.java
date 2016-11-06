package seedu.stask.logic.commands;

import java.util.HashSet;
import java.util.Set;

import seedu.stask.commons.core.Messages;
import seedu.stask.commons.core.UnmodifiableObservableList;
import seedu.stask.commons.exceptions.IllegalValueException;
import seedu.stask.commons.util.CommandUtil;
import seedu.stask.model.tag.Tag;
import seedu.stask.model.tag.UniqueTagList;
import seedu.stask.model.task.Datetime;
import seedu.stask.model.task.Description;
import seedu.stask.model.task.Name;
import seedu.stask.model.task.ReadOnlyTask;
import seedu.stask.model.task.Task;
import seedu.stask.model.task.UniqueTaskList.TaskNotFoundException;


//@@author A0143884W
/**
 * Edits a task identified using it's last displayed index from the task book.
 */
public class EditCommand extends Command implements Undoable{

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Edits a task identified using it's last displayed index from the task book.\n"
            + "Parameters: INDEX FIELDS_TO_EDIT\n"
            + "Example: " + COMMAND_WORD + " A1 Do that instead d/new description date/13-10-2016";

    public static final String MESSAGE_EDIT_TASK_SUCCESS = "Edited Task: %1$s";

    private ReadOnlyTask toEdit;
    private Task toAdd;

    private String targetIndex;
    private Name name;
    private Description description;
    private Datetime datetime;
    private UniqueTagList tags;

    /**
     * Convenience constructor using raw values.
     *
     * @throws IllegalValueException if any of the raw values are invalid
     */
    public EditCommand(String targetIndex, String name, String description, String datetime, Set<String> tagsList)
            throws IllegalValueException {
        Set<Tag> tagSet = new HashSet<>();
        if (tagsList == null) {
            tagSet = null;
        } else if (!tagsList.contains("")){
            for (String tagName : tagsList) {
                tagSet.add(new Tag(tagName));
            }    
        }
        populateNonNullFields(targetIndex, name, description, datetime, tagSet);
    } 

    @Override
    public CommandResult execute() {
        assert model != null;

        UnmodifiableObservableList<ReadOnlyTask> lastDatedTaskList = model.getFilteredDatedTaskList();
        UnmodifiableObservableList<ReadOnlyTask> lastUndatedTaskList = model.getFilteredUndatedTaskList();

        if (!CommandUtil.isValidIndex(targetIndex, lastUndatedTaskList.size(), lastDatedTaskList.size())){
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }

        toEdit = CommandUtil.getTaskFromCorrectList(targetIndex, lastDatedTaskList, lastUndatedTaskList);

        populateEditedTaskFields();
        
        try {
        	model.deleteTask(toEdit);          	
        } catch (TaskNotFoundException tnfe) {
        	assert false : "The target task cannot be missing";
        }      
        
        int checkForDuplicateOrClash = model.addTask(toAdd);                
        populateUndo();
        return CommandUtil.generateCommandResult(new CommandResult(String.format(MESSAGE_EDIT_TASK_SUCCESS, toAdd)),
        										checkForDuplicateOrClash);
    }

    /**
     * populates non-null instance variables of EditCommand and validates them 
     * 
     * @param targetIndex
     * @param name
     * @param description
     * @param datetime
     * @param tagSet
     * @throws IllegalValueException
     */   
    private void populateNonNullFields(String targetIndex, String name, String description, String datetime,
            final Set<Tag> tagSet) throws IllegalValueException {
        if (name != null){
            this.name = new Name(name);       
        }

        if (description != null){
            this.description = new Description(description);
        }

        if (datetime != null){
            this.datetime = new Datetime(datetime);
        }

        if (tagSet != null){
            this.tags = new UniqueTagList(tagSet);
        }

        this.targetIndex = targetIndex;
    }  

    /**
     * combine edit inputs into task to be added
     */
    private void populateEditedTaskFields() {

        toAdd  = new Task (toEdit.getName(), toEdit.getDescription(), toEdit.getDatetime(), 
                toEdit.getStatus(), toEdit.getTags());

        if (name != null){
            toAdd.setName(name);     
        }
        if (description != null){
            toAdd.setDescription(description);
        }
        if (datetime != null){
            toAdd.setDatetime(datetime);
        }
        //tags == null when no t/ is typed
        if (tags != null){
            //tags is empty when 't/'  is typed (to clear tags)
            if (tags.isEmpty()){
                toAdd.setTags(new UniqueTagList());
            } else {
                toAdd.setTags(tags);
            }
        }
    }

    @Override
    public void populateUndo(){
        assert COMMAND_WORD != null;
        assert toAdd != null;
        assert toEdit != null;
        model.addUndo(COMMAND_WORD, toAdd, toEdit);
        model.clearRedo();
    } 

}