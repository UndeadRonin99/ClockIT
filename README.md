# ClockIT
ClockIT is an advanced time management and tracking system designed to help users efficiently manage their tasks and time. This README file provides an overview of the features offered by ClockIT, making it accessible to both technical and non-technical stakeholders.


## Features
### Leaderboard (feature 1)
The LeaderBoard feature in the app allows users to see their ranking based on the points they have accumulated from logging activities. Here's how it works:

1. *Data Retrieval*: When you open the leaderboard, the app connects to a Firebase Realtime Database to fetch all logged activity sessions.

2. *Points Calculation*: For each logged session, the app calculates points based on the duration of the activity. Each minute of logged activity counts as one point.

3. *User and Bot Points*: The app distinguishes between real users and predefined bot users. Bot users have fixed points for illustration purposes, making the leaderboard more competitive.

4. *Leaderboard Display*: 
    - The app calculates the total points for the user and combines this with the points of bot users.
    - It then sorts all users by their total points in descending order.
    - The top three users are highlighted at the top of the leaderboard with special backgrounds, prominently showing their names and points.
    - The rest of the users are listed below, showing their rank, names, and points.

5. *Updating the Leaderboard*: The leaderboard dynamically updates each time you open it, reflecting the most recent data from the database. This ensures that your latest activities and points are always included.

Logging your study hours and activities allows you to accumulate points and see your name rise on the leaderboard. The top spots are highlighted, motivating users to engage more and earn more points.

### Colors for categories (feature 2)
Assigning distinct colours to categories enables users to quickly identify and differentiate between various tasks or projects within the app interface. This feature enhances usability and organization, making it easier for users to navigate their time-tracking data and interpret visual cues briefly. By associating specific colours with each category, users can customize their workflow and tailor the app's appearance to suit their preferences and visual preferences.

The color will show up anywhere the category name is displayed (viewing logs, adding goals, viewing goals, adding logs, etc.) and when the user is creating new activities. This allows the user to easily and quickly identify which category they are dealing with.

### Time Tracking
#### Automatic Time Logging: 
Automatically logs the time spent on different tasks based on user activity.
#### Manual Time Entry:
Allows users to enter the time spent on tasks manually.

### Task Management
#### Create Tasks:
Users can create new tasks with specific details such as description, priority, and deadlines.
#### Delete Tasks:
Users can remove tasks that are no longer needed.

### User Management
#### Activity Reports:
Generates detailed reports on user activities, including time spent on each task.
#### Productivity Analysis:
Provides insights into user productivity based on time-tracking data.

## Additional Information
If you need any more information or troubleshooting, please refer to the documentation provided within the project or contact the project maintainers.

## Link to YouTube video
You can find the video of our app running [here](https://www.youtube.com/watch?v=Q9zIdVPtHmU).
