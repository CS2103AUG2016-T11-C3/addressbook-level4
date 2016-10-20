package seedu.address.logic.commands;

import java.util.HashSet;
import java.util.Set;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.UnmodifiableObservableList;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Datetime;
import seedu.address.model.person.Description;
import seedu.address.model.person.Name;
import seedu.address.model.person.ReadOnlyTask;
import seedu.address.model.person.Task;
import seedu.address.model.person.UniquePersonList;
import seedu.address.model.person.UniquePersonList.PersonNotFoundException;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;
import seedu.address.ui.PersonListPanel;

/**
 * Deletes a person identified using it's last displayed index from the address book.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Edits the task identified by the index number given in the most recent listing.\n"
            + "Parameters: INDEX (must be a positive integer) FIELD_TO_EDIT(include delimiter d/, date/, t/ etc)\n"
            + "Example: " + COMMAND_WORD + " 1 do that instead date/13-10-16";

    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Edited Task: %1$s";

    private ReadOnlyTask toEdit;
    private Task toAdd;
    
    private int targetIndex;
    private Name name;
    private Description description;
    private Datetime datetime;
    private UniqueTagList tags;

    public EditCommand(int targetIndex, String name, String description, String datetime, Set<String> tags)
            throws IllegalValueException {
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(new Tag(tagName));
        }    
        
        populateNonNullFields(targetIndex, name, description, datetime, tagSet);
    }

	private void populateNonNullFields(int targetIndex, String name, String description, String datetime,
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
        
        this.tags = new UniqueTagList(tagSet);
        this.targetIndex = targetIndex;
	}  

    @Override
    public CommandResult execute() {

        UnmodifiableObservableList<ReadOnlyTask> lastShownList = model.getFilteredPersonList();
        UnmodifiableObservableList<ReadOnlyTask> lastUndatedTaskList = model.getFilteredUndatedTaskList();

        if (targetIndex <= PersonListPanel.DATED_DISPLAY_INDEX_OFFSET 
                && lastUndatedTaskList.size() >= targetIndex){
            toEdit = lastUndatedTaskList.get(targetIndex - 1);
        }
        else if (targetIndex > PersonListPanel.DATED_DISPLAY_INDEX_OFFSET 
                   && lastShownList.size() >= targetIndex - PersonListPanel.DATED_DISPLAY_INDEX_OFFSET){
            toEdit = lastShownList.get(targetIndex - 1 - PersonListPanel.DATED_DISPLAY_INDEX_OFFSET);
        }
        else {
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }
        
        populateEditedTaskFields();
                
        assert model != null;
        try {
            model.deletePerson(toEdit);
            model.addPerson(toAdd);           
        } catch (UniquePersonList.DuplicatePersonException e) {
                return new CommandResult(AddCommand.MESSAGE_DUPLICATE_PERSON);     
        } catch (PersonNotFoundException pnfe) {
            assert false : "The target task cannot be missing";
        }

        return new CommandResult(String.format(MESSAGE_EDIT_PERSON_SUCCESS, toAdd));
    }

    // use original task as base, insert fields that have been input in edit
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
        
        toAdd.setTags(tags);
    }    

}