# Employee Attendance System

## Overview

This is a command-line based Employee Attendance System written in Kotlin. It allows for managing employees, tracking their attendance (check-in/check-out), and viewing various attendance reports.

The project is structured into several files, each with a specific responsibility:

* `Main.kt`: Contains the main entry point of the application and handles all user interactions through the command-line menu.
* `EmployeeManager.kt`: Acts as the central controller for the system. It coordinates between the `EmployeeList` and `AttendanceList`.
* `Employee.kt`: Defines the `Employee` data class, including its properties and validation logic.
* `EmployeeList.kt`: A custom list class to manage the collection of `Employee` objects.
* `Attendance.kt`: Defines the `Attendance` data class for storing check-in/check-out records.
* `AttendanceList.kt`: A custom list class to manage the collection of `Attendance` records.
* `Enum.kt`: Contains the `Role` and `Department` enumerations.
