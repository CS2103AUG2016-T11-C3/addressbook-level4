package seedu.address.model.person;


import java.util.List;
import java.util.TimeZone;

import com.joestelmach.natty.DateGroup;

import seedu.address.commons.exceptions.IllegalValueException;

/**
 * Represents a Task's Date in the to-do-list.
 * Guarantees: immutable; is valid as declared in {@link #isValidDate(String)}
 */
public class Date {

    public static final String MESSAGE_DATE_CONSTRAINTS =
            "Date should be in MM-DD-YYYY format";
    public static final String DATE_VALIDATION_REGEX = "(0?[1-9]|[12][0-9]|3[01])"
                                                        + "-"
                                                        + "(0?[1-9]|1[012])"
                                                        + "-"
                                                        + "\\d{4}";

    public final String value;
    public final java.util.Date startDate;
    public final java.util.Date endDate;

    //TODO: To streamline the method
    public Date(String dateStr) throws IllegalValueException {
        // allow dateList to be null in Date constructor when user doesn't input "date/"
        assert dateStr != null;
        
        TimeZone tz = TimeZone.getDefault();    
        com.joestelmach.natty.Parser natty = new com.joestelmach.natty.Parser(tz);
        List<java.util.Date> dateList;
        List<DateGroup> groupList = natty.parse(dateStr);
        dateList = (groupList.isEmpty()) ? null : groupList.get(0).getDates();
        if (dateList == null){
            this.value = "";
            startDate = null;
            endDate = null;
            return;
        }

        String [] dateStrings = new String [2];

        for (int i = 0; i < dateList.size(); i++){
            java.util.Date date = dateList.get(i);
            dateStrings[i] = date.getDate() + "-" + (date.getMonth() + 1) + "-" + (date.getYear() + 1900);

            if (!isValidDate(dateStrings[i])) {
                throw new IllegalValueException(MESSAGE_DATE_CONSTRAINTS);
            }
        }

        // floating task
        if (dateList.size() == 0){
            this.value = "";
            startDate = null;
            endDate = null;
        }
        // deadline
        else if (dateList.size() == 1){
            this.value = dateStrings[0];
            startDate = dateList.get(0);
            endDate = null;
        }
        // event 
        else if (dateList.size() == 2){
            if (dateStrings[0].equals(dateStrings[1])){
                this.value = dateStrings[0];
            }
            else {
                this.value = dateStrings[0] + " to " + dateStrings[1];
            }
            startDate = dateList.get(0);
            endDate = dateList.get(1);
        }
        // 3 or more arguments: not something we handle
        else {
            throw new IllegalValueException(MESSAGE_DATE_CONSTRAINTS);
        }
    }
    
    /**
     * Validates given date.
     *
     * @throws IllegalValueException if given date(s) are invalid.
     */
    public Date(List<java.util.Date> dateList) throws IllegalValueException {
    	// allow dateList to be null in Date constructor when user doesn't input "date/"
    	if (dateList == null){
    		this.value = "";
            startDate = null;
            endDate = null;
            return;
    	}

        String [] dateStrings = new String [2];

    	for (int i = 0; i < dateList.size(); i++){
        	java.util.Date date = dateList.get(i);
        	dateStrings[i] = date.getDate() + "-" + (date.getMonth() + 1) + "-" + (date.getYear() + 1900);
        	
        	if (!isValidDate(dateStrings[i])) {
                throw new IllegalValueException(MESSAGE_DATE_CONSTRAINTS);
            }
        }
        
    	// floating task
    	if (dateList.size() == 0){
        	this.value = "";
        	startDate = null;
        	endDate = null;
        }
    	// deadline
    	else if (dateList.size() == 1){
        	this.value = dateStrings[0];
        	startDate = dateList.get(0);
        	endDate = null;
        }
    	// event 
        else if (dateList.size() == 2){
        	if (dateStrings[0].equals(dateStrings[1])){
        		this.value = dateStrings[0];
        	}
        	else {
        		this.value = dateStrings[0] + " to " + dateStrings[1];
        	}
        	startDate = dateList.get(0);
        	endDate = dateList.get(1);
        }
    	// 3 or more arguments: not something we handle
        else {
        	throw new IllegalValueException(MESSAGE_DATE_CONSTRAINTS);
        }
        
        
    }

    /**
     * Returns if a given string is a valid person email.
     */
    public static boolean isValidDate(String test) {
        return test.equals("") || test.matches(DATE_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Date // instanceof handles nulls
                && this.value.equals(((Date) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
