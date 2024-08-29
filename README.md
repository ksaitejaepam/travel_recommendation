# Project Title: Travel Recommendation Platform

## Project Description:
Our travel recommendation platform is designed to connect colleagues from different countries and cultures through their shared love of travel. The platform will offer a range of features to help users discover new destinations, plan their trips, and share their experiences with each other.
Users can create profiles on the platform, where they can share their travel experiences and connect with other travelers. They can recommend and review travel destinations, including places to visit, eat, and stay. Users can also rate and review destinations that they have visited, and share tips and recommendations for other travelers.
The platform will include a map of recommended destinations, where users can view and filter destinations by location, rating, and other criteria. Users can also plan and organize their travel itineraries using the website, adding destinations to their itinerary and creating a schedule for their trip.
In addition to these features, the platform will offer travel blogs, where users can create and share articles about their experiences, recommendations, and tips. Users can also learn about the culture and customs of different destinations, including information about national holidays, traditions, and cuisine.
The social features of the platform will allow users to connect with each other through private messaging and public discussion forums. They can also organize virtual or in-person meetups and events.
Our travel recommendation platform will provide a unique social network for travelers, connecting colleagues from different countries and cultures and helping them to discover new destinations and share their experiences with each other.

## Key Features:
- **Registration form** for new users to create an account.
- **Login page** for users to access their accounts.
- **User profile page** to display and edit personal information.
- **Main page** displays a map of recommended destinations.
- **Search bar** to search for specific destinations or filter for specific types of destinations (e.g., food, culture, attractions).
- **Option to add a new destination** to the map.
- **Ability to leave reviews and ratings** for destinations.
- **Travel blog section** for users to share their travel experiences and recommendations.
- **Itinerary planner** to help users plan their trips.
- **Admin dashboard** to manage user accounts and destination information.

# Technologies Used

## Languages:
- **Java 17** - High-performance, scalable, and secure programming language.
## Frameworks & Libraries:
- **Spring Framework** - Comprehensive framework for building enterprise-grade applications.
- **Spring Boot** - Simplified Spring-based application development with embedded server support.
- **JUnit** - A unit testing framework for Java applications.
- **Mockito** - Mocking framework for unit tests in Java.

## Tools:
- **Git** - Version control system for tracking changes, managing code collaboration, and maintaining project history.
- **Maven** - Build automation tool for managing dependencies and building applications.


## Databases:
- **MySQL** - Relational database management system for storing and managing data.

## Project Structure

PBE: Root directory of the multi-module Maven project.
- **user-management-service/**: Module dedicated to user management functionalities.
    - src/main/java/com/epam/user/management/application/: Contains main application code including configuration, controllers, services, and repositories etc.
    - src/main/resources/: Includes application properties and logging configurations.
    - src/test/java/com/epam/user/management/: Contains unit and integration tests for the user management service.
- travel-recommendation-service/: Module dedicated to travel recommendation functionalities.
    - src/main/java/com/epam/travel/management/application/: Contains main application code including configuration, controllers, services, and repositories etc.
    - src/main/resources/: Includes application properties and logging configurations.
    - src/test/java/com/epam/travel/management/application/: Contains unit and integration tests for the travel recommendation service.
- .mvn/wrapper/: Contains Maven wrapper files for consistent build environments.
- pom.xml:Parent Maven build script that manages dependencies and builds configurations for all modules.
- .gitignore: Specifies files and directories to be ignored by Git version control.

