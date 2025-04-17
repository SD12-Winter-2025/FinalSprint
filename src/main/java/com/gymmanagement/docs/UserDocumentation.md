# User Guide

## Overview

The **Gym Management System** is a console-based application designed to streamline gym operations for administrators, trainers, and members. Each role has a unique set of functionalities, allowing users to manage memberships, create and update classes, and enroll in workout programs.

### Features by User Role:
- **Administrators**:
  - Manage all users within the system.
  - Monitor and track membership revenue.
  - View, create, update, and delete workout classes.

- **Trainers**:
  - View their assigned classes.
  - Create new classes and update or delete existing ones.
  - Purchase memberships.

- **Members**:
  - Browse and enroll in available workout classes.
  - View their memberships and renew them.
  - Track the workout classes they’ve joined.

---

## Role-Specific Instructions

### **For Administrators**
#### Features:
- **View All Users**:
  - From the Admin Menu, select option `1`. 
  - A table of all registered users will be displayed, showing IDs, usernames, roles, and other details.
  
- **Delete User**:
  - Select option `2` from the Admin Menu.
  - View the table of users, then enter the user ID you wish to delete.

- **View Membership Revenue**:
  - Select option `3` from the Admin Menu.
  - The program will calculate and display total revenue from memberships.

- **View All Classes**:
  - Choose option `4` to see a list of all classes available, including schedules and capacities.

---

### **For Trainers**
#### Features:
- **View Assigned Classes**:
  - From the Trainer Menu, select option `1`. 
  - The program will display only the classes assigned to you as a trainer.

- **Create New Class**:
  - Choose option `2` from the Trainer Menu. 
  - Enter the required class details, such as:
    - **Name**: Enter the name of the class (e.g., "Yoga Session").
    - **Description**: Provide a short description (e.g., "Relaxing yoga session for beginners").
    - **Type**: Specify the type of class (e.g., "Yoga").
    - **Schedule**: Provide the date and time using the format `YYYY-MM-DDTHH:MM` (e.g., `2025-10-03T04:30`).
    - **Duration**: Enter the duration in minutes (e.g., `60`).
    - **Max Capacity**: Specify the maximum number of participants (e.g., `15`).

- **Update Class**:
  - Select option `3` from the Trainer Menu.
  - The program will display all classes. Enter the class ID to update the desired class, then follow the prompts to modify details.

- **Delete Class**:
  - Select option `4`. View all available classes, then enter the class ID for deletion. Confirm the deletion when prompted.

- **Purchase Membership**:
  - Select option `5` from the Trainer Menu. Follow the prompts to purchase or renew memberships.

---

### **For Members**
#### Features:
- **Browse Classes**:
  - From the Member Menu, select option `1`. View all available classes and schedules.

- **View My Memberships**:
  - Choose option `2`. Membership details, including expiration dates, will be displayed.

- **Purchase Membership**:
  - Select option `3`. Follow the instructions to purchase a new membership plan or renew an existing membership.

- **Enroll in Class**:
  - Select option `4`. Browse classes, then enter the Class ID to enroll.

- **View Enrolled Classes**:
  - Choose option `5`. All classes you’ve joined will be listed with their schedules.

---

## Instructions on Starting the System

1. **Setup**:
   - Ensure Java is installed on your machine (JDK version 11 or higher).
   - Import the database schema using the provided `schema.sql` file.
   - Populate test data using the `data.sql` file (optional).

2. **Compile**:
   - Open a terminal or command prompt in the project directory.
   - Run the following command to compile the program:
     ```bash
     javac App.java
     ```

3. **Run the Application**:
   - Execute the compiled program using:
     ```bash
     java App
     ```

4. **Follow Menu Prompts**:
   - Based on your role (Admin, Trainer, or Member), select an option from the menu displayed.
   - Enter data as prompted (e.g., dates in `YYYY-MM-DDTHH:MM` format, numeric IDs).

---

## Notes on Error Handling
- **Invalid Input**:
  - If you enter invalid data (e.g., text instead of numbers), the program will display an error message and prompt you to try again.
- **Date Formatting**:
  - Ensure dates follow the `YYYY-MM-DDTHH:MM` format (e.g., `2025-10-03T04:30`) to avoid parsing errors.
- **Missing Data**:
  - If no relevant data exists (e.g., no classes for a trainer), the program will display a clear message.

---

## Conclusion

The Gym Management System provides an intuitive interface for managing gym operations. By following the menus and prompts, administrators, trainers, and members can effortlessly navigate their tasks. For additional help, please refer to the [README.md](./README.md) or contact the repository owner.
