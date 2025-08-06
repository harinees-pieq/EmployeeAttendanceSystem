import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
val manager = EmployeeManager()

fun main() {
    while (true) {
        println(
            "\nMenu:\n1. Add Employee\n2. Delete Employee\n3. List Employees" +
                    "\n4. Check-In\n5. Check-Out\n6. View All Attendance" +
                    "\n7. View Attendance by Date\n8. View Working Hours Summary By Date" +
                    "\n9. View Currently Checked-In Employees\n10. Exit\n"
        )

        when (prompt("Enter your choice: ")) {
            "1" -> addEmployee()
            "2" -> deleteEmployee()
            "3" -> listEmployees()
            "4" -> checkIn()
            "5" -> checkOut()
            "6" -> viewAllAttendance()
            "7" -> viewAttendanceByDate()
            "8" -> viewWorkingHoursSummary()
            "9" -> viewCurrentlyCheckedIn()
            "10" -> {
                println("Exiting...")
                return
            }
            else -> println("Invalid choice.")
        }
    }
}

// prints and gets value
fun prompt(msg: String): String {
    print(msg)
    return readln().trim()
}

// gets input to add and calls addEmployee from EmployeeManager
fun addEmployee() {
    val firstName = prompt("Enter first name: ")
    val lastName = prompt("Enter last name: ")
    val role = prompt("Enter role (INTERN, DEVELOPER, MANAGER, HR, QA): ")
    val dept = prompt("Enter department (IT, ADMIN, SALES, MARKETING): ")
    val reportingTo = prompt("Enter reporting manager ID: ")
    println(manager.handleAddEmployee(firstName, lastName, role, dept, reportingTo))
}

// get id to delete and calls deleteEmployee from EmployeeManager
fun deleteEmployee() {
    val id = prompt("Enter Employee ID to delete: ")
    println(manager.handleDeleteEmployee(id))
}

// gets list and prints
fun listEmployees() {
    manager.handleListEmployees().forEach { println(it) }
}

// calls checkIn from EmployeeManager
fun checkIn() {
    val id = prompt("Enter Employee ID: ")
    val dateTime = parseDateTimeOrNow("Enter check-in time (dd-MM-yyyy HH:mm) or leave blank: ")
    println(manager.handleCheckIn(id, dateTime))
}

// calls checkOut from EmployeeManager
fun checkOut() {
    val id = prompt("Enter Employee ID: ")
    val dateTime = parseDateTimeOrNow("Enter check-out time (dd-MM-yyyy HH:mm) or leave blank: ")
    println(manager.handleCheckOut(id, dateTime))
}

// viewAttendance will be printed
fun viewAllAttendance() {
    manager.handleViewAttendance().forEach { println(it) }
}

// viewAttendanceByDate will be printed
fun viewAttendanceByDate() {
    val fromDate = parseDateOnly("Enter start date (dd-MM-yyyy): ")
    val toDate = parseDateOnly("Enter end date (dd-MM-yyyy): ")
    val from = fromDate.atStartOfDay()
    val to = toDate.atTime(23, 59, 59)
    manager.handleViewAttendanceByDate(from, to).forEach { println(it) }
}

// viewWorkingHoursSummaryByDate will be called
fun viewWorkingHoursSummary() {
    val fromDate = parseDateOnly("Enter start date (dd-MM-yyyy): ")
    val toDate = parseDateOnly("Enter end date (dd-MM-yyyy): ")
    val from = fromDate.atStartOfDay()
    val to = toDate.atTime(23, 59, 59)

    val summaryString = manager.handleViewWorkingHoursSummaryByDate(from, to)

    println()
    println(summaryString)
}

// checked in and not yet checked out
fun viewCurrentlyCheckedIn() {
    println("\n%-10s %-20s".format("Emp ID", "Name"))
    manager.handleViewCurrentlyCheckedInEmployees().forEach { println(it) }
}

// parser for datetime
fun parseDateTimeOrNow(message: String): LocalDateTime {
    val input = prompt(message)
    return if (input.isEmpty()) {
        LocalDateTime.now()
    } else {
        try {
            LocalDateTime.parse(input, formatter)
        } catch (e: DateTimeParseException) {
            println("Invalid format. Using current time.")
            LocalDateTime.now()
        }
    }
}

// parser for date
private fun parseDateOnly(message: String): LocalDate {
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    while (true) {
        try {
            val input = prompt(message)
            return LocalDate.parse(input, formatter)
        } catch (e: Exception) {
            println("Invalid format. Use dd-MM-yyyy")
        }
    }
}
