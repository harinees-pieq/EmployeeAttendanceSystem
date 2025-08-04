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
        val e1 = Employee("Harinee", "S", "INTERN", "IT", "PIEQ0001")
        val e2 = Employee("Dhivyasri", "S", "MANAGER", "SALES", "0")
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
    fun addEmployee(firstName: String, lastName: String, role: String, department: String, reportingTo: String): String {
        val emp = Employee(firstName, lastName, role, department, reportingTo)
        employeeList.addEmployee(emp)
        return "Employee added: ${emp.employeeId}"
    }

    // calls updateDetails from Employee
    fun updateEmployee(
        id: String,
        firstName: String,
        lastName: String,
        role: String,
        department: String,
        reportingTo: String
    ): String {
        val emp = employeeList.findById(id) ?: return "Employee not found."
        emp.updateDetails(firstName, lastName, role, department, reportingTo)
        return "Employee updated."
    }

    // calls deleteEmployee from EmployeeList
    fun deleteEmployee(id: String): String {
        return if (employeeList.deleteEmployee(id)) "Employee deleted." else "Employee not found."
    }

    // calls listAll from EmployeeList
    fun listEmployees(): List<String> {
        return employeeList.listAll().map { it.toString() }
    }

    // calls checkIn from AttendanceList
    fun checkIn(id: String, time: LocalDateTime): String {
        if (employeeList.findById(id) == null) return "Invalid Employee ID."
        val formattedTime = time.format(dateTimeFormatter)
        return attendanceList.checkIn(id, time) ?: "Checked in at $formattedTime"
    }

    // calls checkOut from AttendanceList
    fun checkOut(id: String, time: LocalDateTime): String {
        val formattedTime = time.format(dateTimeFormatter)
        return attendanceList.checkOut(id, time) ?: "Checked out at $formattedTime"
    }

    // calls getAll from AttendanceList
    fun viewAttendance(): List<String> {
        return attendanceList.getAll().map { it.toString() }
    }

    // calls getByDateRange from  AttendanceList
    fun viewAttendanceByDate(from: LocalDateTime, to: LocalDateTime): List<String> {
        return attendanceList.getByDateRange(from, to).map { it.toString() }
    }

    // calls getWorkingHoursSummaryByDate from AttendanceList
    fun viewWorkingHoursSummaryByDate(from: LocalDateTime, to: LocalDateTime): List<String> {
        val summary = attendanceList.getWorkingHoursSummaryByDate(from, to, employeeList.listAll())
        return summary.ifEmpty {
            emptyList()
        }
    }

    // calls getCurrentlyCheckedIn from AttendanceList
    fun viewCurrentlyCheckedInEmployees(): List<String> {
        val ids = attendanceList.getCurrentlyCheckedIn()
        return ids.map {
            val emp = employeeList.findById(it)
            val name = emp?.let { "${it.firstName} ${it.lastName}" } ?: "Unknown"
            "%-10s %-20s".format(it, name)
        } // formats with neat spacing
    }
}
