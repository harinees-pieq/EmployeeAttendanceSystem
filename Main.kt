import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
val manager = EmployeeManager()

fun main() {
    while (true) {
        println(
            "\nMenu:\n1. Add Employee\n2. Update Employee\n3. Delete Employee\n4. List Employees" +
                    "\n5. Check-In\n6. Check-Out\n7. View All Attendance" +
                    "\n8. View Attendance by Date\n9. View Working Hours Summary By Date" +
                    "\n10. View Currently Checked-In Employees\n11. Exit\n"
        )

        when (prompt("Enter your choice: ")) {
            "1" -> addEmployee()
            "2" -> updateEmployee()
            "3" -> deleteEmployee()
            "4" -> listEmployees()
            "5" -> checkIn()
            "6" -> checkOut()
            "7" -> viewAllAttendance()
            "8" -> viewAttendanceByDate()
            "9" -> viewWorkingHoursSummary()
            "10" -> viewCurrentlyCheckedIn()
            "11" -> {
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
    println(manager.addEmployee(firstName, lastName, role, dept, reportingTo))
}

// gets input to update and calls updateEmployee from EmployeeManager
fun updateEmployee() {
    val id = prompt("Enter Employee ID to update: ")
    val firstName = prompt("Enter new first name: ")
    val lastName = prompt("Enter new last name: ")
    val role = prompt("Enter new role (INTERN, DEVELOPER, MANAGER, HR, QA): ")
    val dept = prompt("Enter new department (IT, ADMIN, SALES, MARKETING): ")
    val reportingTo = prompt("Enter new reporting manager ID: ")
    println(manager.updateEmployee(id, firstName, lastName, role, dept, reportingTo))
}

// get id to delete and calls deleteEmployee from EmployeeManager
fun deleteEmployee() {
    val id = prompt("Enter Employee ID to delete: ")
    println(manager.deleteEmployee(id))
}

// gets list and prints
fun listEmployees() {
    manager.listEmployees().forEach { println(it) }
}

// calls checkIn from EmployeeManager
fun checkIn() {
    val id = prompt("Enter Employee ID: ")
    val dateTime = parseDateTimeOrNow("Enter check-in time (dd-MM-yyyy HH:mm) or leave blank: ")
    println(manager.checkIn(id, dateTime))
}

// calls checkOut from EmployeeManager
fun checkOut() {
    val id = prompt("Enter Employee ID: ")
    if (!Employee.isValidEmployeeId(id)) {
        println("Invalid Employee ID format.")
        return
    }
    val dateTime = parseDateTimeOrNow("Enter check-out time (dd-MM-yyyy HH:mm) or leave blank: ")
    println(manager.checkOut(id, dateTime))
}

// viewAttendance will be printed
fun viewAllAttendance() {
    manager.viewAttendance().forEach { println(it) }
}

// viewAttendanceByDate will be printed
fun viewAttendanceByDate() {
    val fromDate = parseDateOnly("Enter start date (dd-MM-yyyy): ")
    val toDate = parseDateOnly("Enter end date (dd-MM-yyyy): ")
    val from = fromDate.atStartOfDay()
    val to = toDate.atTime(23, 59, 59)
    manager.viewAttendanceByDate(from, to).forEach { println(it) }
}

// viewWorkingHoursSummaryByDate will be called
fun viewWorkingHoursSummary() {
    val fromDate = parseDateOnly("Enter start date (dd-MM-yyyy): ")
    val toDate = parseDateOnly("Enter end date (dd-MM-yyyy): ")
    val from = fromDate.atStartOfDay()
    val to = toDate.atTime(23, 59, 59)

    val summary = manager.viewWorkingHoursSummaryByDate(from, to)

    if (summary.isEmpty()) {
        println("No data found.")
    } else {
        println("\n%-10s %-20s %-10s".format("Emp ID", "Name", "Total Hours"))
        summary.forEach(::println)
    }
}

// checked in and not yet checked out
fun viewCurrentlyCheckedIn() {
    println("\n%-10s %-20s".format("Emp ID", "Name"))
    manager.viewCurrentlyCheckedInEmployees().forEach { println(it) }
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