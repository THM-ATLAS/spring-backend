# ATLAS Backend
## Installing the project locally
- We recommend using [IntelliJ IDEA](https://www.jetbrains.com/idea/download/) because of its native Kotlin and Spring support
  - In IntelliJ IDEA you can clone the project using `File > New > Project from Version Control...`, then enter the URL https://github.com/THM-ATLAS/spring-backend
- Alternatively clone the repository using `git clone https://github.com/THM-ATLAS/spring-backend/` or `git clone git@github.com:THM-ATLAS/spring-backend` and open it manually
- Set the environment variables `db_source`, `db_username` and `db_password` to the url, the username and the password of the user respectively
- Start the project by clicking the play button in the upper right corner, the api starts on http://localhost:8080/api
- The documentation automatically starts on http://localhost:8080/api/docs
### Include the Frontend
- Clone the frontend into `/spring-backend/vue-frontend/` using `git clone https://github.com/THM-ATLAS/vue-frontend` or `git clone git@github.com:THM-ATLAS/vue-frontend`
- Run `gradlew buildFrontend` (`./gradlew.bat` on Windows) to build the frontend, then run `gradlew copyFrontend` to copy all necessary files
- Starting the project will now start the frontend on http://localhost:8080 and the backend on http://localhost:8080/api


## Additional links
- [ATLAS Frontend](https://github.com/THM-ATLAS/vue-frontend)
- [API Documentation](http://atlas.mni.thm.de/api/docs) for this implementation (only available from the THM network).
