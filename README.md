# QuizDroid
INFO 498 Android Development | Homework 6

An application that will allow users to take multiple-choice quizzes.

## NOTE
This assignment has 5 parts, each in its own branch (Parts 1-5).

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
