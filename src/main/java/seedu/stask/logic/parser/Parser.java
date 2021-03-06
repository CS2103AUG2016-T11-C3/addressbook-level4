package seedu.stask.logic.parser;

import static seedu.stask.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.stask.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.stask.commons.exceptions.IllegalValueException;
import seedu.stask.commons.util.StringUtil;
import seedu.stask.logic.commands.AddCommand;
import seedu.stask.logic.commands.ClearCommand;
import seedu.stask.logic.commands.Command;
import seedu.stask.logic.commands.DeleteCommand;
import seedu.stask.logic.commands.DoneCommand;
import seedu.stask.logic.commands.EditCommand;
import seedu.stask.logic.commands.ExitCommand;
import seedu.stask.logic.commands.FindCommand;
import seedu.stask.logic.commands.HelpCommand;
import seedu.stask.logic.commands.IncorrectCommand;
import seedu.stask.logic.commands.ListCommand;
import seedu.stask.logic.commands.RedoCommand;
import seedu.stask.logic.commands.SaveCommand;
import seedu.stask.logic.commands.SelectCommand;
import seedu.stask.logic.commands.UndoCommand;
import seedu.stask.logic.commands.ViewCommand;

/**
 * Parses user input.
 */
public class Parser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");

    private static final Pattern PERSON_INDEX_ARGS_FORMAT = Pattern.compile("(?<targetIndex>[a-z|A-Z]\\d+)");

    private static final Pattern KEYWORDS_ARGS_FORMAT =
            Pattern.compile("(?<keywords>\\S+(?:\\s+\\S+)*)"); // one or more keywords separated by whitespace

    private static final Pattern PERSON_DATA_ARGS_FORMAT = // '/' forward slashes are reserved for delimiter prefixes
            Pattern.compile("(?<name>[^/]+)"
                    + "( d/(?<description>[^/]+)){0,1}"
                    + "( date/(?<date>[^/]+)){0,1}"
                    + "(?<tagArguments>(?: t/[^/]+)*)"); // variable number of tags

    private static final Pattern EDIT_DATA_ARGS_FORMAT = // '/' forward slashes are reserved for delimiter prefixes
            Pattern.compile("(?<index>[a-z|A-Z][\\d]+)"
                    + "( (?<name>[^/]+)){0,1}"
                    + "( d/(?<description>[^/]*)){0,1}"
                    + "( date/(?<date>[^/]*)){0,1}" // group <date> can be blank to edit DatedTask -> UndatedTask
                    + "(?<tagArguments>(?: t/[^/]*)*)"); // variable number of tags

    public Parser() {}

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     */
    public Command parseCommand(String userInput) {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");
        switch (commandWord.toLowerCase()) {

        case AddCommand.COMMAND_WORD:
            return prepareAdd(arguments);

        case SelectCommand.COMMAND_WORD:
        case SelectCommand.COMMAND_ALIAS:
            return prepareSelect(arguments);

        case DoneCommand.COMMAND_WORD:
            return prepareDone(arguments);

        case DeleteCommand.COMMAND_WORD:
        case DeleteCommand.COMMAND_ALIAS:
            return prepareDelete(arguments);

        case EditCommand.COMMAND_WORD:
            return prepareEdit(arguments);

        case ClearCommand.COMMAND_WORD:
            return new ClearCommand();

        case UndoCommand.COMMAND_WORD:
            return new UndoCommand();

        case RedoCommand.COMMAND_WORD:
            return new RedoCommand();

        case FindCommand.COMMAND_WORD:
            return prepareFind(arguments);

        case ListCommand.COMMAND_WORD:
        case ListCommand.COMMAND_ALIAS:
            return prepareList(arguments);

        case SaveCommand.COMMAND_WORD:
            return prepareSave(arguments);

        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();

        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();

        case ViewCommand.COMMAND_WORD:
            return prepareView(arguments); 

        default:
            return new IncorrectCommand(MESSAGE_UNKNOWN_COMMAND);
        }
    }

    //@@author A0143884W
    /**
     * Parses arguments in the context of the view task command.
     *
     * @param arguments full command arguments string
     * @return the prepared command
     */
    private Command prepareView(String arguments) {
        try {
            return new ViewCommand(arguments);
        } catch (IllegalValueException ive) {
            return new IncorrectCommand(ive.getMessage());
        }
    }

    /**
     * Parses arguments in the context of the add task command.
     *
     * @param arguments full command arguments string
     * @return the prepared command
     */
    private Command prepareAdd(String arguments){
        final Matcher matcher = PERSON_DATA_ARGS_FORMAT.matcher(arguments.trim());

        // Validate arguments string format
        if (!matcher.matches()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }	

        try {
            return new AddCommand(
                    matcher.group("name"),
                    matcher.group("description"),
                    matcher.group("date"),
                    getTagsFromArgs(matcher.group("tagArguments"))
                    );
        } catch (IllegalValueException ive) {
            return new IncorrectCommand(ive.getMessage());
        }
    }
    //@@author

    /**
     * Extracts the new person's tags from the add command's tag arguments string.
     * Merges duplicate tag strings.
     */
    private static Set<String> getTagsFromArgs(String tagArguments) throws IllegalValueException {
        // no input tags
        if (tagArguments.isEmpty()) {
            return null;
        } else {
            // replace first delimiter prefix, then split
            final Collection<String> tagStrings = Arrays.asList(tagArguments.replaceFirst(" t/", "").split(" t/"));
            return new HashSet<>(tagStrings);
        }
    }

    /**
     * Parses arguments in the context of the delete person command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    private Command prepareDelete(String args) {

        Optional<String> index = parseIndex(args);
        if(!index.isPresent()){
            return new IncorrectCommand(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }

        return new DeleteCommand(index.get());
    }

    //@@author A0139145E
    /**
     * Parses arguments in the context of the done task command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    private Command prepareDone(String args) {
        Optional<String> index = parseIndex(args);
        if (!index.isPresent()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, 
                            DoneCommand.MESSAGE_USAGE));
        }
        return new DoneCommand(index.get());
    }
    //@@author 

    //@@author A0143884W
    /**
     * Parses arguments in the context of the edit person command.
     *
     * @param arguments full command arguments string
     * @return the prepared command
     */
    private Command prepareEdit(String arguments) {
        final Matcher matcher = EDIT_DATA_ARGS_FORMAT.matcher(arguments.trim());
        
        // Validate arguments string format
        if (!matcher.matches()) {
            return new IncorrectCommand(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));
        }

        try {
            return new EditCommand(
                    (matcher.group("index").toUpperCase()),
                    matcher.group("name"),
                    matcher.group("description"),
                    matcher.group("date"),
                    getTagsFromArgs(matcher.group("tagArguments"))
                    );
        } catch (IllegalValueException ive) {
            return new IncorrectCommand(ive.getMessage());
        }
    }
    //@@author

    /**
     * Parses arguments in the context of the select person command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    private Command prepareSelect(String args) {
        Optional<String> index = parseIndex(args);
        if(!index.isPresent()){
            return new IncorrectCommand(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, SelectCommand.MESSAGE_USAGE));
        }

        return new SelectCommand(index.get());
    }

    /**
     * Returns the specified index in the {@code command} IF a letter followed by
     * a positive unsigned integer is given as the index.
     *   Returns an {@code Optional.empty()} otherwise.
     */
    private Optional<String> parseIndex(String command) {
        final Matcher matcher = PERSON_INDEX_ARGS_FORMAT.matcher(command.trim());
        if (!matcher.matches()) {
            return Optional.empty();
        }

        String index = matcher.group("targetIndex");
        if ((index.split(" ")).length == 1 && StringUtil.isUnsignedInteger(index.substring(1))) {
            return Optional.of(index.toUpperCase());
        }
        else {
            return Optional.empty();
        }

    }

    /**
     * Parses arguments in the context of the find person command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    private Command prepareFind(String args) {
        final Matcher matcher = KEYWORDS_ARGS_FORMAT.matcher(args.trim());
        if (!matcher.matches()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    FindCommand.MESSAGE_USAGE));
        }

        // keywords delimited by whitespace
        final String[] keywords = matcher.group("keywords").split("\\s+");
        final Set<String> keywordSet = new HashSet<>(Arrays.asList(keywords));
        return new FindCommand(keywordSet);
    }

    //@@author A0139145E
    /**
     * Parses arguments in the context of the list task command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    private Command prepareList (String args) {
        final Matcher matcher = KEYWORDS_ARGS_FORMAT.matcher(args.trim());
        if (!matcher.matches()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                            ListCommand.MESSAGE_LIST_USAGE));
        }
        final String[] keywords = matcher.group("keywords").split("\\s+");
        
        if (keywords.length > 1) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                            ListCommand.MESSAGE_LIST_USAGE));
        }
        try {
            return new ListCommand(keywords[0]);
        } catch (IllegalValueException ive) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                            ive.getMessage()));
        }
    }
    //@@author 


    //@@author A0139528W
    /**
     * Parses arguments in the context of the save folder command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    private Command prepareSave(String args) {
        if (args.trim().length() == 0) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    SaveCommand.MESSAGE_USAGE));
        }

        return new SaveCommand(args);
    }
    //@@author
}
