import java.time.format.DateTimeFormatter
import java.time.LocalDateTime

class EmployeeManager {
    private val employeeList = EmployeeList()
    private val attendanceList = AttendanceList()

    private val dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")

    // to initiate data
    init {
        loadSampleData()
    }

    private fun loadSampleData() {
        val e1 = Employee("Dhivyasri", "S", "MANAGER", "IT", "0")
        val e2 = Employee("Harinee", "S", "INTERN", "SALES", "PIEQ0001")
        val e3 = Employee("Shanmugam", "T", "HR", "ADMIN", "PIEQ0001")

        employeeList.addEmployee(e1)
        employeeList.addEmployee(e2)
        employeeList.addEmployee(e3)

        attendanceList.add(Attendance(e1.employeeId, LocalDateTime.of(2025, 7, 23, 9, 0)).apply {
            checkOut(LocalDateTime.of(2025, 7, 23, 17, 0))
        })
        attendanceList.add(Attendance(e2.employeeId, LocalDateTime.of(2025, 7, 23, 8, 30)).apply {
            checkOut(LocalDateTime.of(2025, 7, 23, 18, 5))
        })
        attendanceList.add(Attendance(e1.employeeId, LocalDateTime.of(2025, 7, 24, 9, 10)).apply {
            checkOut(LocalDateTime.of(2025, 7, 24, 17, 5))
        })
        attendanceList.add(Attendance(e1.employeeId, LocalDateTime.of(2025, 7, 25, 8, 58)))
        attendanceList.add(Attendance(e2.employeeId, LocalDateTime.of(2025, 7, 24, 8, 25)).apply {
            checkOut(LocalDateTime.of(2025, 7, 24, 18, 30))
        })
        attendanceList.add(Attendance(e2.employeeId, LocalDateTime.of(2025, 7, 25, 8, 40)).apply {
            checkOut(LocalDateTime.of(2025, 7, 25, 17, 55))
        })
        attendanceList.add(Attendance(e3.employeeId, LocalDateTime.of(2025, 7, 23, 9, 30)).apply {
            checkOut(LocalDateTime.of(2025, 7, 23, 17, 45))
        })
        attendanceList.add(Attendance(e3.employeeId, LocalDateTime.of(2025, 7, 24, 9, 22)).apply {
            checkOut(LocalDateTime.of(2025, 7, 24, 17, 30))
        })
        attendanceList.add(Attendance(e3.employeeId, LocalDateTime.of(2025, 7, 25, 9, 15)))
    }

    // calls addEmployee from EmployeeList
    // --- In EmployeeManager.kt ---

    fun handleAddEmployee(firstName: String, lastName: String, role: String, department: String, reportingTo: String): String {
        return try {
            val newEmployee = Employee(
                firstNameInput = firstName,
                lastNameInput = lastName,
                roleInput = role,
                departmentInput = department,
                reportingToInput = reportingTo
            )
            employeeList.addEmployee(newEmployee)
            "Employee added: ${newEmployee.employeeId}"
        } catch (e: IllegalArgumentException) {
            "Error: ${e.message}"
        }
    }

    // Replace the old deleteEmployee function with this one
    fun handleDeleteEmployee(id: String): String {
        if (employeeList.findById(id) == null) {
            return "Employee not found."
        }
        attendanceList.deleteRecordsForEmployee(id)
        employeeList.deleteEmployee(id)
        return "Employee and all their attendance records have been deleted."
    }

    // calls listAll from EmployeeList
    fun handleListEmployees(): List<String> {
        return employeeList.listAll().map { it.toString() }
    }

    // calls checkIn from AttendanceList
    fun handleCheckIn(id: String, time: LocalDateTime): String {
        if (employeeList.findById(id) == null) return "Invalid Employee ID."
        val formattedTime = time.format(dateTimeFormatter)
        return attendanceList.checkIn(id, time) ?: "Checked in at $formattedTime"
    }

    // calls checkOut from AttendanceList
    fun handleCheckOut(id: String, time: LocalDateTime): String {
        if (employeeList.findById(id) == null) return "Invalid Employee ID."
        val formattedTime = time.format(dateTimeFormatter)
        return attendanceList.validateCheckOut(id, time) ?: "Checked out at $formattedTime"
    }

    // calls getAll from AttendanceList
    fun handleViewAttendance(): List<String> {
        return attendanceList.getAll().map { it.toString() }
    }

    // calls getByDateRange from  AttendanceList
    fun handleViewAttendanceByDate(from: LocalDateTime, to: LocalDateTime): List<String> {
        return attendanceList.getByDateRange(from, to).map { it.toString() }
    }

    // calls getWorkingHoursSummaryByDate from AttendanceList
    fun handleViewWorkingHoursSummaryByDate(from: LocalDateTime, to: LocalDateTime): String {
        return attendanceList.getWorkingHoursSummaryByDate(from, to, employeeList.listAll())
    }

    // calls getCurrentlyCheckedIn from AttendanceList
    fun handleViewCurrentlyCheckedInEmployees(): List<String> {
        val ids = attendanceList.getCurrentlyCheckedIn()
        return ids.map {
            val emp = employeeList.findById(it)
            val name = emp?.let { "${it.firstName} ${it.lastName}" } ?: "Unknown"
            "%-10s %-20s".format(it, name)
        } // formats with neat spacing
    }
}
