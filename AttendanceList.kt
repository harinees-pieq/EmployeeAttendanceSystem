import java.time.Duration
import java.time.LocalDateTime

class AttendanceList : ArrayList<Attendance>() {

    // check-in for an employee
    fun checkIn(employeeId: String, checkInTime: LocalDateTime = LocalDateTime.now()): String? {
        val newAttendance = Attendance(employeeId, checkInTime)
        val added = add(newAttendance)
        return if (added) null else "Check-in failed: already checked in today or invalid time."
    }

    // add method - prevents duplicate check-in on same day
    override fun add(element: Attendance): Boolean {
        val employeeId = element.employeeId
        val checkInDate = element.checkInDateTime.toLocalDate()

        if (element.checkInDateTime.isAfter(LocalDateTime.now())) {
            return false
        }

        val alreadyCheckedInToday = this.any {
            it.employeeId == employeeId &&
                    it.checkInDateTime.toLocalDate() == checkInDate &&
                    it.checkOutDateTime == null
        }
        if (alreadyCheckedInToday) {
            return false
        }

        return super.add(element)
    }

    // check-out for an employee
    fun validateCheckOut(employeeId: String, checkOutTime: LocalDateTime = LocalDateTime.now()): String? {
        val record = this.findLast { it.employeeId == employeeId && it.checkOutDateTime == null }
            ?: return "No active check-in found."

        if (checkOutTime.isBefore(record.checkInDateTime)) {
            return "Check-out time cannot be before check-in time."
        }
        if (checkOutTime.isAfter(LocalDateTime.now())) {
            return "Check-out time cannot be in the future."
        }

        return record.checkOut(checkOutTime)
    }

    // returns all attendance records
    fun getAll(): List<Attendance> = this.toList()

    // returns attendance based on date
    fun getByDateRange(from: LocalDateTime, to: LocalDateTime): List<Attendance> =
        this.filter { it.isWithin(from, to) }

    fun getWorkingHoursSummaryByDate(
        from: LocalDateTime,
        to: LocalDateTime,
        employeeList: List<Employee>
    ): String {

        val employeeIdSorter = Comparator<String> { id1, id2 ->
            val num1 = id1.substringAfter("PIEQ").toIntOrNull() ?: 0
            val num2 = id2.substringAfter("PIEQ").toIntOrNull() ?: 0
            num1.compareTo(num2)
        }

        val sortedList = this.sortedWith(compareBy(employeeIdSorter) { it.employeeId })

        if (sortedList.isEmpty()) {
            return "No data found."
        }

        val resultBuilder = StringBuilder()
        resultBuilder.append("%-10s %-20s %-10s\n".format("Emp ID", "Name", "Total Hours"))

        var currentEmpId: String? = null
        var currentEmpName: String = "Unknown"
        var currentTotalMinutes: Long = 0

        for (record in sortedList) {
            if (record.employeeId != currentEmpId) {
                if (currentEmpId != null) {
                    val hours = currentTotalMinutes / 60
                    val rem = currentTotalMinutes % 60
                    val formattedHours = String.format("%02d:%02d", hours, rem)
                    resultBuilder.append("%-10s %-20s %-10s\n".format(currentEmpId, currentEmpName, formattedHours))
                }

                currentEmpId = record.employeeId
                currentEmpName = employeeList.find { it.employeeId == currentEmpId }
                    ?.let { "${it.firstName} ${it.lastName}" } ?: "Unknown"
                currentTotalMinutes = 0
            }

            val checkOut = record.checkOutDateTime
            if (checkOut != null && record.isWithin(from, to)) {
                val minutes = Duration.between(record.checkInDateTime, checkOut).toMinutes()
                currentTotalMinutes += minutes
            }
        }

        if (currentEmpId != null) {
            val hours = currentTotalMinutes / 60
            val rem = currentTotalMinutes % 60
            val formattedHours = String.format("%02d:%02d", hours, rem)
            resultBuilder.append("%-10s %-20s %-10s\n".format(currentEmpId, currentEmpName, formattedHours))
        }

        return resultBuilder.toString()
    }

    // returns checked in without checked out
    fun getCurrentlyCheckedIn(): List<String> {
        return this.filter { it.checkOutDateTime == null }
            .map { it.employeeId }
            .distinct()
    }

    // delete attendance records of deleted employee
    fun deleteRecordsForEmployee(employeeId: String) {
        this.removeIf { it.employeeId == employeeId }
    }

    // prints in a format
    override fun toString(): String {
        if (this.isEmpty()) return "No attendance records found."
        return this.joinToString(separator = "\n") { it.toString() }
    }
}
