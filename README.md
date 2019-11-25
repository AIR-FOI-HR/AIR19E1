## AIR19E1

# TeamMate
### Members
- Ondrej Jarina
- Zdenko Pečeňa
- Pablo Figueroa Martínez
### What is TeamMate? - Introduction
TeamMate is a project created for the course Software Analysis and Design, whose purpose is to create a fully functional mobile app. Broadly speaking, this application will let the user search and sign up in any sport activity happening nearby in a simple and intuitive way.
### Description of the system
#### Core use cases
To properly define the minimum viable product we need to define the core use cases.
 1. **Browse sports venues**: every logged user must have the ability to browse within the sports venues that are nearby him/her. The user will be able to modify some preferences such us distance from the venue.
 2. **Browse activities**: every logged user must have the ability to browse all the sports that take place in certain sport venue. This includes browsing his/her own activities, that is, browse all the activities in which the user has participated in or has created.
 3. **Create an activity**: every logged user must have the ability to create a new activity in a sport venue.
 4. **Sign up to an activity**: every logged user must have the ability to sign up for an incoming activity.
#### Architecture
The architecture of the project will follow the well-known three-layered architectural pattern, in which there exists three clearly separate layers:
- **UI layer**, where the application will let the user navigate the sport venues and activities, among others.
- **Application layer**, where the application will process more complex functionality, such as letting the user create new activities, sign up for new activities and modify some user preferences.
- **Data management layer**, where the application will process data, managing and storing it.
#### Technologies
The group wanted an up-to-date technology with a wide community to help us on any problem that could arise. That is why we  decided to use **Java (Android)** for the project.

As for the database, we decided to use Google's [**Firebase Cloud Firestore**](https://firebase.google.com/docs/firestore) due to it comprehensive documentation and ease of implementation.
The team is going to use **GitHub** as platform to manage the version control of the application (the repository we are using can be accessed through [this link](https://github.com/AIR-FOI-HR/AIR19E1)); and **Zenhub** to manage our project planning (the board can be found through [this link](https://app.zenhub.com/workspaces/air19e1-5db66c3e73e8f70001d9e693/board?milestones=Milestone%201%20-%20Sprint%201%232019-11-21,Milestone%202%20-%20Sprint%202%23,Milestone%203%20-Sprint%203%23&filterLogic=any&repos=217964850)).
#### Environment
We decided to use **Android Studio** for working on the project, because it is the recommended IDE for creating Android applications. e are also using **Google Cloud Platform** to access APIs and use Google services. Lastly, our team uses **Firebase Console** fFor accessing and managing our database. 
## Project Planning
Our team is following **Scrum** framework during the development process. As said before, wWe are monitoring our progress using tool [**Zenhub** ](https://app.zenhub.com/workspaces/air19e1-5db66c3e73e8f70001d9e693/board?milestones=Milestone%201%20-%20Sprint%201%232019-11-21,Milestone%202%20-%20Sprint%202%23,Milestone%203%20-Sprint%203%23&filterLogic=any&repos=217964850)where we can submit issues, set milestones, review closed issues. We planned to finish our project in three sprints.
## Product backlog
### Features
The main features of the application will be as follows:
| ID |                                    Item                                    |
|:--:|:--------------------------------------------------------------------------:|
| F1 | Browse sport venues near me.                                               |
| F2 | Browse all the sports activities that take place in a certain sport venue. |
| F3 | Create an activity.                                                        |
| F4 | Ability to set and modify some profile-related preferences.                |
| F5 | Sign up to a sport activity.                                               |
| F6 | Browse the activities that I have created or attended.                     |
### Breakdown of the features
|  ID  |                                Item                                | Priority | Estimation of value | Initial estimation of effort | Sprint 1 estimation | Sprint 2 estimation | Sprint 3 estimation |
|:----:|:------------------------------------------------------------------:|:--------:|:-------------------:|:----------------------------:|:-------------------:|:-------------------:|:-------------------:|
|  R1  | Configure Project in Android Studio (modules, dependencies, etc.). |     1    |          2          |               4              |                     |                     |                     |
|  R2  | Configure Google Cloud Platform (add project and get API keys).    |     1    |          2          |               4              |                     |                     |                     |
|  R3  | Integrate Google Places SDK.                                       |     2    |          5          |              10              |                     |                     |                     |
|  R4  | Integrate Google’s Sign-in.                                        |     2    |          5          |               9              |                     |                     |                     |
| F1.1 | Main list of sport venues.                                         |     3    |          7          |               8              |                     |                     |                     |
| F1.2 | Search for venues.                                                 |     4    |          6          |              10              |                     |                     |                     |
| F4.1 | Profile view.                                                      |     5    |          6          |               7              |                     |                     |                     |
| F4.2 | Logout of the application.                                         |     6    |          6          |               8              |                     |                     |                     |
| F2.1 | List of activities in any selected venue.                          |     7    |          7          |               7              |                     |                     |                     |
| F2.2 | Display of an activity.                                            |     8    |          7          |               7              |                     |                     |                     |
| F5.1 | Sign up in an activity.                                            |     9    |          7          |               9              |                     |                     |                     |
| F3.1 | Create an activity.                                                |    10    |          8          |              10              |                     |                     |                     |
| F4.3 | Edit user preferences.                                             |    11    |          6          |               9              |                     |                     |                     |
| F6.1 | List My Activities.                                                |    12    |          5          |               7              |                     |                     |                     |
| F6.2 | List Attending to.                                                 |    13    |          5          |               7              |                     |                     |                     |
| F5.2 | Accept a request to join an activity.                              |    14    |          7          |               8              |                     |                     |                     |
|  R5  | Make the application look fancy.                                   |    15    |          10         |              10              |                     |                     |                     |
