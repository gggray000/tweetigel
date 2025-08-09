# tweetigel

<img src="tweetigel-frontend/public/tweetigel_logo.png" alt="Tweetigel Logo" width="100"/>

## Introduction
This is a project for the course "Web Technology Project". <br>
Technology stack: <br>
- Backend:Java Spring Boot
- Frontend: React.js
- Database: MariaDB
- Deployment: Docker<br>
#### Screenshots:<br>
##### Landing Page
<img width="800" height="464" alt="Screenshot 2025-08-09 at 17 12 40" src="https://github.com/user-attachments/assets/eb0a5a45-a4fb-441d-bb08-e93caca18def" /> <br>
##### Homgpage
<img width="800" height="706" alt="Screenshot 2025-08-09 at 17 13 57" src="https://github.com/user-attachments/assets/dfe392ac-34af-4e30-8ae9-1991a292a1fc" />


## Usage
- run <code>docker-compose up -d --build</code>, and access the frontend via <code>localhost:80</code>.
- Test data can be populated to the application by running <code>testData.sql</code>.

## Features
1. Account register and login.
2. Sending posts.
3. Searching other users.
4. Following and unfollowing other users.
5. Liking and unliking posts.
6. Managing one's own user profile.
7. Displaying user profile of another user, including their posts.
8. Presenting the timelines of posts as a paginated view.
9. Deleting one's own posts.
10. Commenting on posts.
11. Tagging posts using a hashtag mechanism.
12. Retrieving posts based on hashtags.
