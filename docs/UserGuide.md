<!--@@author A0139024M-->
# User Guide

1. [Introduction](#1-introduction)
2. [Getting Started](#2-getting-started)
3. [Features](#3-features)
4. [FAQ](#4-faq)
5. [Command Summary](#5-command-summary)
6. [Appendix](#6-appendix)

## 1. Introduction 

&nbsp;&nbsp;&nbsp;&nbsp; Have you ever felt like there are too many tasks to do, and you are unable to remember all of them? Or feel that your calendar is overflowing with sticky notes on the tasks to be done each day? Have no fear, as our all-in-one task management application, sTask, is here to save your day!

sTask manages the different types of tasks that you will encounter in your daily life, be it a deadline for submission, or even a date with your significant other, this application can show you what all the tasks you have in a month in one glance, or even a list of tasks for a specific day. sTask also manages your list of ad-hoc tasks, which are non-dated tasks such as "Read the new Harry Potter book!", and displays them beautifully and neatly at the side of the application, so you can refer to them any time you have some free time.

Love typing? You will love sTask, as you only need to use the keyboard to type simple commands to manage your tasks.

#### 

## 2. Getting Started

### 2.1 Setting Up

&nbsp;&nbsp;&nbsp;&nbsp; <b>2.1.1</b> Please ensure that you have Java version `1.8.0_60` or later installed in your Computer.<br>
 >> Having any Java 8 version is not enough. <br>
   This app will not work with earlier versions of Java 8.
   
&nbsp;&nbsp;&nbsp;&nbsp; <b>2.1.2</b> You can download the latest `sTask.jar` from our 'releases' tab. <br>
&nbsp;&nbsp;&nbsp;&nbsp; <b>2.1.3</b> You can then copy the file to the folder you want to use as the home folder for sTask. <br>
&nbsp;&nbsp;&nbsp;&nbsp; <b>2.1.4</b> You can double-click the file to start the app. The GUI should appear in a few seconds. <br>

### 2.2 The User Interface
<br>
       <img src="images/Ui.png" width="600"> <br>

&nbsp;&nbsp;&nbsp;&nbsp;<b>2.2.1</b> You can type your commands in the Smart Command Input Bar. <br>
&nbsp;&nbsp;&nbsp;&nbsp;<b>2.2.2</b> This is place where sTask gives you feedback and interacts with you. <br>
&nbsp;&nbsp;&nbsp;&nbsp;<b>2.2.3</b> This is the To Do Task Panel where all your tasks without date and time are displayed in alphabetical order. <br>
&nbsp;&nbsp;&nbsp;&nbsp;<b>2.2.4</b> This is the Dated Task Panel where all your events and deadlines are displayed in chronological order. <br>   

>> You can refer to the [Features](#3-features) section below for details of each command or you can refer to the [Command Summary](#5-command-summary) below<br>


## 3. Features

&nbsp;&nbsp;&nbsp;&nbsp; You can learn more about the different features of sTask: kjfkdsfjs<br>

&nbsp;&nbsp;&nbsp;&nbsp;<b>3.1 Looking for Help </b><br><br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;You can type `help`to glance through the list of commands and its formats.<br><br>

&nbsp;&nbsp;&nbsp;&nbsp;<b>3.2 Adding a Task </b><br><br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; You can add a To Do by typing<br>
>> `add TASKNAME d/TASK_DESCRIPTION [t/TAG...]` <br>
* add Buy coffee d/Starbucks

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; You can add a Deadline by typing<br>
>> `add TASKNAME d/TASK_DESCRIPTION date/DATE_TIME [t/TAG...]` <br>
* add Finish project proposal date/next friday 5pm t/important

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; You can add an Event by typing<br>
>> `add TASKNAME d/TASK_DESCRIPTION date/DATE_TIME to DATE_TIME[t/TAG...]` <br>
* add Meeting with client d/Prepare docunments date/28-10-2016 10am to 28-10-2016 12pm <br><br>

&nbsp;&nbsp;&nbsp;&nbsp; <b>3.3 Listing your Tasks </b><br><br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; You can list all your uncompleted tasks by typing<br>
>> `list all` <br>

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; You can list all your completed tasks by typing<br>
>> `list done` <br>

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; You can list all your overdue and expired tasks by typing<br>
>> `list od` <br><br>

&nbsp;&nbsp;&nbsp;&nbsp;<b>3.4 Editing your Task </b><br><br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; You can edit one or more field(s) of an existing task by typing<br>
>> `edit INDEX TASKNAME d/TASK_DESCRIPTION date/DATE_TIME [t/TAG...]` <br>
* edit A1 Buy iced tea during lunch d/add lemon date/today noon <br>
* edit B1 date/next friday 8pm <br>
* edit B2 t/urgent<br><br>

&nbsp;&nbsp;&nbsp;&nbsp;<b>3.5 Finding your Tasks </b><br><br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; You can find your tasks by typing<br>
>> `find [KEYWORD...]` <br>
* find meeting <br><br>
sTask searches through all the fields and returns all the tasks that contains your KEYWORD.<br><br>


&nbsp;&nbsp;&nbsp;&nbsp;<b>3.6 Viewing your tasks on a specific date </b><br><br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; You can see all your tasks on a specific date by typing<br>
>> `view DATE` <br>
* view today <br>
* view 22-10-2017 <br>
* view valentine day<br><br>
  
&nbsp;&nbsp;&nbsp;&nbsp;<b>3.7 Deleting your Task </b><br><br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; You can delete your task by typing<br>
>> `delete INDEX` <br>
* delete A1 <br><br>

&nbsp;&nbsp;&nbsp;&nbsp;<b>3.8 Undoing an action </b><br><br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; You can undo your previous action by typing<br>
>> `undo` <br><br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;You can only undo the following commands: `add`, `edit`, `delete`, `done`.<br><br>

&nbsp;&nbsp;&nbsp;&nbsp;<b>3.9 Redoing an action </b><br><br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; You can redo your previous `undo` by typing<br>
>> `redo` <br><br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;You can only redo if you do not use the following commands: `add`, `edit`, `delete`, `done`.<br><br>


&nbsp;&nbsp;&nbsp;&nbsp;<b>3.10 Completing a task </b><br><br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; You can mark a task as complete by typing<br>
>> `done INDEX`<br>
* done B10 <br>

&nbsp;&nbsp;&nbsp;&nbsp;<b>3.11 Selecting a task </b><br><br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; You can select a task by typing<br>
>> `select INDEX`<br>
* select A11 <br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;You can use this command to navigate through your list of tasks.<br><br>

&nbsp;&nbsp;&nbsp;&nbsp;<b>3.12 Changing saved data location </b><br><br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; You can change the location of your saved data by typing<br>
>> `save FOLDERPATH`<br>
* save C:\Users\Jim\Dropbox<br><br>

&nbsp;&nbsp;&nbsp;&nbsp;<b>3.13 Exiting sTask </b><br><br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; You can exit the program by typing<br>
>> `exit`<br><br>
  
## 4. FAQ

###General:<br>

**Q**: Do I need to save manually?<br>
**A**: Data is saved in the hard disk automatically after any command that changes the data. There is no need to save manually.<br><br>

**Q**: How do I transfer my data to another computer?<br>
**A**: Install sTask in the other computer by following the steps in Getting Started, and replace the newly created data file with your existing one.<br><br>

**Q**: Can sTask integrate with my Google Calendar?<br>
**A**: Currently, sTask is not able to synchronise your data with Google Calendar, as our application is optimised to work without an internet connection. However, we will be looking at implementing this as an optional feature in the near future, so be sure to keep a lookout for our future updates!<br><br>

**Q**: Will the status of my tasks get updated in real time?<br>
**A**: Currently, sTask only updates the statuses of your tasks when an action is made.<br><br>

###Commands:<br>

**Q**: Can I convert a To Do task to a deadline without deleting and adding it again?<br>
**A**: You can use the edit command to add a date to your To Do task and it will be accepted as a deadline.<br><br>

**Q**: Can I remove any of the fields in a specific task?<br>
**A**: You can use the edit command and just specify the field without any data and it will remove the corresponding the field. For example, to remove the description of the task at index A1, just type "edit A1 d/".<br><br>

**Q**: How do I transfer my data to another Computer?<br>
**A**: Install the app in the other computer and overwrite the empty data file it creates with 
       the file that contains the data of your previous sTask.<br><br>

If you have any further enquiries, drop us an email at sTask@sTask.com.sg!
       
## 5. Command Summary

Command | Format  
-------- | -------- 

Help | `help`
- Shows a help file

Add | `add TASKNAME d/TASK_DESCRIPTION @/DATE TIME [t/TAG...]`
- Inserts a task into sTask

Delete | `delete INDEX`
- Deletes a task from sTask

Edit | `edit INDEX [FIELDS]`
- Edits the field of the task at the specified `INDEX`

Find | `find KEYWORD [MORE_KEYWORDS]`
- Finds an existing task based on the TASK_NAME or TASK_TAG

List | `list all` , `list od`, `list done`
- Lists all/overdue/completed tasks in sTask

View | `view DATE`
- Populates the list of deadlines and events of the selected DATE

Done | `done INDEX`
- Marks the selected task as completed

Save | `save FOLDERPATH`
- Changes location of storage data to specified folder

Undo | `undo`
- Reverts the last reversible action (up to 10)

## 6. Appendix

Possible Date formats
DD-MM-YY 	: 18-10-16 
DD-MM-YYYY  : 27-2-2101
DD MMM YYYY : 15 MAY 2103
relative 	: today || tmr || next tuesday

Not accepted Date formats
DD-MM
DD.MM
DD.MM.YY
DD.MM.YYYY

Possible Time formats
24HR : 2359
am/pm : 2.30pm
relative : 2 hours later || 30 minutes later 

Not accepted Time formats 
2500
230pm

This is not an exhaustive list of formats, please visit the following website for more information
http://natty.joestelmach.com/doc.jsp
