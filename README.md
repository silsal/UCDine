**********************************************************************************

Where to find the elements we were asked to implement in the project brief

**********************************************************************************
Default users:
-username: neverland password: iloveandroid
-username: sheep password: sheeppy


Activities (minimum 5):
Login →  Main Activity
Display →  "Home" Page for routing to primary activities
Attend Event →  User browse available Events
|→  Event Details →  Gives full details of Event and Allows user to select to attend.
Host Event →  User create new Event
Find Recipe →  User interact with API to search for recipe suggestions
Profile →  Summary of User Details
|→  Edit Profile →  Allows user to make changes to profile details



Local Database →  Name: "foodshareDB".
Tables:
"User" →  Contains details of the App Users.
"My_Events" →  Contains details (Event ID) of the events that the current user has selected to attend.
"Events" →  This would (in reality) be on a server-based database. It contains details of all events that have been created by all users. This is where any "Create New Events" are stored.


Buttons →  All Acticities incorporate button
Lists →  Recipe Finder | Search Events | Profile
Dialogues →  Create Events (select images) | Edit Profile (select images) | Search Events (SHAUNA)
Action Bar →  All pages include drop-down. Profile also includes visible action-bar buttons.
Toasts →  On several activities when funtionality is implemented by user. E.g. On Camera Take Picture / On Attend Event Complete.


Connect with two Sensors or Web Services:
Sensor →  GPS →  Edit Profile (set location)
API →  Food/Recipe → Third Party Website

Connect with one activity from another android application:
Calendar --> Create Event
Camera --> Create Event | Edit Profile
Gallery --> Create Event | Edit Profile