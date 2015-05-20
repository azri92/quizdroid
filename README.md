# QuizDroid
INFO 498 Android Development | Homework 6

An application that will allow users to take multiple-choice quizzes

# PART 1
## Stories
* As a user, when I start the app, I should see a list of different topics on which to take a quiz. (For now, these should be hard-coded to read "Math", "Physics", and "Marvel Super Heroes", as well as any additional topics you feel like adding into the mix.)
* As a user, when I select a topic from the list, it should take me to the "topic overview" page, which displays a brief description of this topic, the total number of questions in this topic, and a "Begin" button taking me to the first question.
* As a user, when I press the "Begin" button from the topic overview page, it should take me to the first question page, which will consist of a question (TextView), four radio buttons each of which consist of one answer, and a "Submit" button.
* As a user, when I am on a question page and I press the "Submit" button, if no radio button is selected, it should do nothing. If a radio button is checked, it should take me to the "answer" page. (Ideally, the Submit button should not be visible until a radio button is selected.)
* As a user, when I am on the "answer" page, it should display the answer I gave, the correct answer, and tell me the total number of correct vs incorrect answers so far ("You have 5 out of 9 correct"), and display a "Next" button taking me to the next question page, or else display a "Finish" button if that is the last question in the topic.
* As a user, when I am on the "answer" page, and it is the last question of the topic, the "Finish" button should take me back to the first topic-list page, so that I can take a new quiz.

## Grading
* All your code should be in a GitHub repo under your account
* repo should be called 'quizdroid' in a branch called 'part1'
* repo should contain all necessary build artifacts
* include a directory called "screenshots", including:
    * screenshot of app running on emulator
    * pic or screenshot or movie of app running on a device

# PART 2
In this part, switch from using Activities to Fragments

## Stories
* As a developer, the "topic overview", "question" and "answer" pages should be merged into a single Activity, using Fragments to swap between the question UI and the answer UI.
* This means, then, that there will be only two Activities in the entire system:
    * the list of topics at the front of the application,
    * and the multi-use activity that serves for the topic overview, the question, and answer pages.

## Grading
* All your code should be in a GitHub repo under your account
    * repo should be called 'quizdroid' in branch called "part2"
    * repo should contain all necessary build artifacts
    * include a directory called "screenshots", including:
* screenshot of app running on emulator
    * pic or screenshot or movie of app running on a device
    * We will clone and build it from the GH repo
* 5 points, one for each satisfied story from the previous (pt1) version of the assignment refactored to use fragments

## BONUS
* use animation transitions to "slide" the fragments and and out; when going from question to answer, for example, slide the answer fragment in from the right towards the left. (1 pt)

# PART 3

## Stories
* Create a class called QuizApp extending android.app.Application and make sure it is referenced from the app manifest; override the onCreate() method to emit a message to the diagnostic log to ensure it is being loaded and run
* Use the "Repository" pattern to create a TopicRepository interface; create one implementation that simply stores elements in memory from a hard-coded list initialized on startup. Create domain objects for Topic and Quiz, where a Quiz is question text, four answers, and an integer saying which of the four answers is correct, and Topic is a title, short description, long description, and a collection of Question objects.
* Make the QuizApp object a singleton, and provide a method for accessing the TopicRepository.
* Refactor the activities in the application to use the TopicRepository. On the topic list page, the title and the short description should come from the similar fields in the Topic object. On the topic overview page, the title and long description should come from the similar fields in the Topic object. The Question object should be similarly easy to match up to the UI.
* Refactor the TopicRepository to read a JSON file ("assets/questions.json") to use as the source of the Topics and Questions. Use a hard-coded file (available at http://tednewardsandbox.site44.com/questions.json).

## Grading
* All your code should be in a GitHub repo under your account
    * repo should be called 'quizdroid' in branch called 'part3'
    * repo should contain all necessary build artifacts
* include a directory called "screenshots", including:
    * screenshot of app running on emulator
    * pic or screenshot or movie of app running on a device

## BONUS
* In the next part, we will need this application to need to access the Internet, among other things. Look through the list of permissions in the Android documentation, and add uses-permission elements as necessary to enable that now. (1 pt)
* Refactor the domain model so that Topics can have an icon to go along with the title and descriptions. (Use the stock Android icon for now if you don't want to test your drawing skills.) Refactor the topic list ListView to use the icon as part of the layout for each item in the list view. Display the icon on the topic overview page.

# PART 4

Now we will write the code to check the questions periodically, store the data, and allow for preferences

## Tasks:
* The application should provide a "Preferences" action bar item that brings up a "Preferences" activity containing the application's configurable settings: URL to use for question data, and how often to check for new downloads measured in minutes. If a download is currently under way, these settings should not take effect until the next download starts.
* The application should create some background operation (Thread, AlarmManager or Service) that will (eventually) attempt to download a JSON file containing the questions from the server every "N" minutes/hours. For now, pop a Toast message displaying the URL that will eventually be hit. Make sure this URL is what's defined in the Preferences.