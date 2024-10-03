This is ProjectX!

A place where you can create, store and keep track of your projects in this busy world of ours.
(Note that the application is pretty basic at the moment, new features coming)

Notes:
You'll have to have an active GitHub account to login to the site.
Uses OAuth2 authentication with GitHub.
The site will automaticly create a unique user-account based on your GitHub username, and only you can access your projects.
Standard CRUD.
The webservice is deployed on Render, and uses their PostgreSQL database service there to store users and projects.
The projects uses a CI/CD pipeline through GitHub Actions where all changes to the application code is tested.
If the tests go through, the application is rebuilt with the changes and pushed to Docker Hub as an image, which Render instantly redeploys.
