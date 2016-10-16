package seedu.address.model.person;


import java.util.List;

import seedu.address.commons.exceptions.IllegalValueException;

/**
 * Represents a Task's Date in the to-do-list.
 * Guarantees: immutable; is valid as declared in {@link #isValidDate(String)}
 */
public class Date {

    public static final String MESSAGE_DATE_CONSTRAINTS =
            "Date should be in DD.MM.YY format";
    public static final String DATE_VALIDATION_REGEX = "(0?[1-9]|[12][0-9]|3[01])"
                                                        + "\\."
                                                        + "(0?[1-9]|1[012])"
                                                        + "\\."
                                                        + "\\d{2}";

    public final String value;
    public final java.util.Date startDate;
    public final java.util.Date endDate;

    
    public Date(String date) throws IllegalValueException {
        if (date == null){
            this.value = "";
            startDate = null;
            endDate = null;
            return;
        }
            
        date = date.trim();
        if (!isValidDate(date)) {
            throw new IllegalValueException(MESSAGE_DATE_CONSTRAINTS);
        }
        this.value = date;
        
        startDate = null;
        endDate = null;
    }
    
    /**
     * Validates given date.
     *
     * @throws IllegalValueException if given date(s) are invalid.
     */
    public Date(List<java.util.Date> dateList) throws IllegalValueException {
        String value = "";
    	for (int i = 0; i < dateList.size(); i++){
        	java.util.Date date = dateList.get(i);
        	String dateString = date.getDay() + "." + date.getMonth() + "." + date.getYear();
        	    	
        	value += dateString + " ";
        	
        	if (!isValidDate(dateString)) {
                throw new IllegalValueException(MESSAGE_DATE_CONSTRAINTS);
            }
        }
    	
    	this.value = value;
        
        if (dateList.size() == 1){
        	startDate = dateList.get(0);
        	endDate = null;
        }
        else if (dateList.size() == 2){
        	startDate = dateList.get(0);
        	endDate = dateList.get(1);
        }
        else {
        	startDate = null;
        	endDate = null;
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
