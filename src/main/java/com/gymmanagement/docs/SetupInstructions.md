# Setup Instructions

## Requirements

To set up and run the **Gym Management System** on your local machine, ensure you have the following:

1. **Java Development Kit (JDK)**:
   - Version: 11 or higher is required.
   - You can download it from [Oracle](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html) or [OpenJDK](https://openjdk.org/).

2. **Database**:
   - A relational database such as **MySQL** or **PostgreSQL** is supported.
   - Ensure the database is installed, running, and accessible.
   - The application expects a schema defined in `schema.sql` and optionally pre-populated data from `data.sql`.

---

## Installation and Setup

    Follow these steps to install and run the application locally:

### 1. Clone the Repository
    Clone the repository to your local machine using Git:
    
    git clone <repository-url>
    cd gym-management-system

## 2. Configure the Database

### Step 1: Create the Database
    Open your database client (e.g., MySQL Workbench, pgAdmin, or the terminal).

    Create a new database:

        CREATE DATABASE gym_management;


### Step 2: Import the Schema
    Use the provided `schema.sql` file to set up the database structure:

        mysql -u <username> -p gym_management < schema.sql

    Replace <username> with your database username.

    (Optional) Import sample data from data.sql to populate the database:

        mysql -u <username> -p gym_management < data.sql


### Step 3: Update Database Credentials
    Open DatabaseConfig.java in the project and update the database connection details to match your setup:

        private static final String DB_URL = "jdbc:mysql://localhost:3306/gym_management";
        private static final String DB_USER = "your_username";
        private static final String DB_PASSWORD = "your_password";


## 3. Compile the Code
    Compile the Java code to generate .class files:

        javac App.java

## 4. Run the Application
    Run the compiled application:

        java App

    Once the program is running, you will be presented with the main menu. Follow the prompts to log in or register based on your role.

## Troubleshooting

If you encounter issues during setup or runtime, follow these troubleshooting steps:

---

### 1. Database Connection Errors
- **Cause**: The program is unable to connect to the database.
- **Solution**:
  - Verify that the database server is running.
  - Check your database credentials in `DatabaseConfig.java` (e.g., username, password, database URL).
  - Ensure that the database schema has been set up correctly using `schema.sql`.

---

### 2. Missing Tables or Data
- **Cause**: The database schema or data is incomplete.
- **Solution**:
  - Re-import the schema using `schema.sql` to ensure the database structure is correct.
  - Optionally, import the sample data using `data.sql` to populate test data.

---

### 3. Java Errors
- **Cause**: Compilation or runtime errors in the application.
- **Solution**:
  - Ensure that Java (JDK) is installed and added to your system PATH.
  - Verify installation by running:
    ```bash
    java -version
    javac -version
    ```
  - Recompile the application using:
    ```bash
    javac App.java
    ```
  - Ensure all `.java` files are located in the same directory or properly referenced.

---

### 4. Invalid Inputs in the Application
- **Cause**: Entering incorrect input formats (e.g., invalid dates or numbers).
- **Solution**:
  - Follow the program's on-screen instructions carefully.
  - Use the format `YYYY-MM-DDTHH:MM` for dates (e.g., `2025-10-03T04:30`).

---
