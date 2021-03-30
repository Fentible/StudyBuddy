# StudyBuddy
Software Engineering Coursework - Study Planner

*** Current Progress - 10/03/21
+ Task 0 :: github set up 
+ Task 1 :: ideas and sketches added to trello
+ Task 2 :: sketches of layouts and designs
+ Task 3 :: implementation of model classes
  - what classes should be there
  - start of semester profile attributes and basic methods
  - ideas of what should be added to be added in comments
  - added an example of how we may define the semester profile structure
    - Type (M, E or A), Title (e.g. SoftwareEngineering),  Additional info {Due date, course code, etc}
    - deadlines (exams and assigments) come after the module line so that associated them is done easily in the constructor
  - exams and assignments now extend deadline, generic/user made deadlines may be required later on
+ Task 4 :: implementation of sketch designs (layouts, window, parent windows) - **Now allowed to use scene builder**
+ Task 5 :: create dashboard
  - displays each month and elements that end on that day
  - functionality to choose between viewing tasks, activites, milestones or deadlines
+ Task 6 :: create profile class
  - basics implemented, will find more methods needed as other tasks progress
  - saving and loading is currently done with basic serialising for testing
+ Task 7 :: create 'add' classes
  - add Task class - basic blank checking, needs more incorrect input checking (+ error messages/alerts)
  - module must be selected before choosing related assignment and/or exam
  - select module to view tasks, deadlines, etc
    - show completion of these tasks and deadlines
    - colour code modules - display colour on dashboard

+ View module class: 



**Notes:**
+ Need to be able to edit deadlines as well as tasks
+ Deadlines and milestones linked to tasks
  - Show completetion of this deadline/milestone based on how much the tasks have been completed
+ Milestones must be linked to a deadline
+ assignments and exams are stored seperately along with a list called 'deadlines' 
  - deadlines doesnt directly store anything at the moment but is used to return a list of the above two
+ **Currently using static methods and variables to return and set scene when switching windows, as features is supposedly more important in this coursework than code quality. If anyone is familiar with JavaFX and knows a better way (perhaps a FXML controller?) let me know**
