# Class Interactions

## Overview

The **Gym Management System** is designed using a modular architecture, where each class represents a specific entity or functionality. These classes interact seamlessly to provide the various features of the system, including user management, membership handling, and workout class scheduling. This document outlines the core classes, their responsibilities, and their relationships.

---

## Core Classes and Interactions

### 1. **App.java**
- Acts as the entry point for the application.
- Manages user login, registration, and initializes the system.
- Directs users to their respective role-specific menus based on login credentials.

---

### 2. **Menu Classes**
#### Role-specific menus provide tailored functionality for different types of users:
- **AdminMenu.java**:
  - Allows administrators to manage users, memberships, and classes.
  - Features include viewing and deleting users, tracking revenue, and managing classes.
- **TrainerMenu.java**:
  - Enables trainers to view their assigned classes or manage any class in the system.
  - Features include creating, updating, and deleting classes.
- **MemberMenu.java**:
  - Provides members with access to browse and enroll in classes, manage memberships, and track enrolled classes.

---

### 3. **Service Classes**
#### These classes encapsulate business logic, bridging the menus and data access layers:
- **UserService.java**:
  - Handles operations related to user authentication, login, and registration.
  - Provides methods for updating user profiles and roles.
- **WorkoutClassService.java**:
  - Manages workout class operations, including creation, updating, deletion, and retrieval.
  - Contains logic to fetch classes for specific trainers or display all available classes.
- **MembershipService.java**:
  - Manages memberships, including creating, renewing, and tracking revenue from memberships.

---

### 4. **DAO Classes**
#### DAO (Data Access Object) classes interact directly with the database:
- **UserDAO.java**:
  - Handles database operations for the `users` table.
  - Includes methods for user registration, deletion, and authentication.
- **WorkoutClassDAO.java**:
  - Performs CRUD (Create, Read, Update, Delete) operations on the `workout_classes` table.
  - Retrieves workout class data for trainers or members.
- **MembershipDAO.java**:
  - Manages the `memberships` table, handling operations for membership purchases and updates.

---

### 5. **Model Classes**
#### Model classes represent the system's entities and serve as data carriers:
- **User.java**:
  - Represents a user in the system, including their role (Admin, Trainer, or Member), username, and contact details.
- **WorkoutClass.java**:
  - Represents a workout class, including its name, description, type, schedule, duration, and capacity.
  - Contains a trainer ID to associate the class with a specific trainer.
- **Membership.java**:
  - Represents a membership plan, including its type, cost, and expiration date.

---

## Class Diagram

Below is a class diagram illustrating the relationships between key entities in the system:

[Insert class diagram image here]

> **Note**: To create and embed your class diagram, you can use tools like:
> - [PlantUML](https://plantuml.com/)
> - [Lucidchart](https://www.lucidchart.com/)
> - [Draw.io](https://app.diagrams.net/)

Example Markdown for adding the diagram:
```markdown
![Class Diagram](./class-diagram.png)


