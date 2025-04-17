# FinalSprint
# Gym Management System

Welcome to the **Gym Management System**! This console-based application provides an interactive platform for managing gym users, memberships, and workout classes, catering to administrators, trainers, and members.

## 🚀 Features
- **Role-Specific Menus**:
  - **Admins**: Manage users, memberships, revenue, and gym classes.
  - **Trainers**: View and manage their assigned classes, create, update, or delete classes.
  - **Members**: Browse and enroll in classes, manage memberships, and view enrolled classes.
- **Seamless Class Management**: Create, update, and delete gym classes with ease.
- **Membership Handling**: View, purchase, and manage memberships with accurate tracking.
- **Interactive Menus**: Clean, structured, and visually appealing menus with robust error handling.

---

## 🛠️ Installation & Setup

### Requirements
1. **Java Development Kit (JDK)**: Version 11 or higher.
2. **Database**:
   - MySQL or PostgreSQL database for data storage.
   - Ensure the database contains the required tables.

### Steps to Run the Application
1. **Clone the Repository**:
   ```bash
   git clone <repository-url>
   cd gym-management-system

2. **Setup the Database:**
    Import the provided schema.sql and data.sql files into your database.
    Update the database credentials in the DatabaseConfig.java file.

3. **Compile the Code**
    javac App.java

4. **Run the Program**
    java App

## 📖 Documentation

This repository includes detailed documentation to guide you through using and understanding the Gym Management System:

- **User Guide**: A detailed guide for end users, including instructions on how to navigate and use the system. 
    (./docs/UserDocumentation.md)
- **Class Diagram and Interactions**: Overview of all classes, their roles, and relationships, including a class diagram.
    (./docs/ClassInteractions.md)
- **Setup Instructions**: Comprehensive setup instructions for configuring and running the program.
    (./docs/SetupInstructions.md)

---

## 📜 Role-Based Usage Instructions

### Administrators
Admins can:
- View, delete, and manage all user accounts.
- Monitor revenue from memberships.
- View and manage all available classes.

---

### Trainers
Trainers can:
- View their assigned classes.
- Create, update, or delete classes.
- Purchase memberships for personal use.

---

### Members
Members can:
- Browse and enroll in workout classes.
- View their memberships and manage renewals.
- Track the classes they are enrolled in.


## Visual Menu Design
An example of the clean, structured menus used throughout the program:

╔═══════════════════════════════════╗
║          GYM MANAGEMENT SYSTEM    ║
╠═══════════════════════════════════╣
║  1. Login                         ║
║  2. Register                      ║
║  3. Exit                          ║
╚═══════════════════════════════════╝
Select an option:


## 📂 Project Structure

### Core Application
- **App.java**: The main entry point for the application.

---

### Menus
- **AdminMenu.java**: Admin-specific menu functionalities.
- **TrainerMenu.java**: Trainer-specific menu functionalities.
- **MemberMenu.java**: Member-specific menu functionalities.

---

### Services
- **UserService.java**: Handles user login and registration.
- **WorkoutClassService.java**: Manages class-related logic.
- **MembershipService.java**: Manages memberships and revenue calculations.

---

### DAOs (Data Access Objects)
- **UserDAO.java**: Handles database operations for the `users` table.
- **WorkoutClassDAO.java**: Manages database operations for `workout_classes`.
- **MembershipDAO.java**: Handles membership-related database queries.

---

### Models
- **User.java, WorkoutClass.java, Membership.java**: Represent system entities.

---

## 💡 Troubleshooting

### Database Connection Errors
- Ensure your database server is running and credentials in `DatabaseConfig.java` are correct.

### Java Errors
- Verify that the correct JDK version is installed.
- Ensure all required `.class` files are compiled before running the program.

### Invalid Inputs
- Follow on-screen instructions for expected input formats (e.g., date in `YYYY-MM-DDTHH:MM` format).
