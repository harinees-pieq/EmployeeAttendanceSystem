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
    fun checkOut(employeeId: String, checkOutTime: LocalDateTime = LocalDateTime.now()): String? {
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

    // sum working hours of employee based on date
    fun getWorkingHoursSummaryByDate(from: LocalDateTime, to: LocalDateTime, employeeList: List<Employee>): List<String> {
        val filtered = this.filter { att ->
            att.checkOutDateTime != null &&
                    !att.checkInDateTime.isBefore(from) &&
                    !att.checkInDateTime.isAfter(to)
        }

        if (filtered.isEmpty()) {
            return emptyList()
        }

        val summaryMap = mutableMapOf<String, Long>()
        for (record in filtered) {
            val minutes = Duration.between(record.checkInDateTime, record.checkOutDateTime!!).toMinutes()
            summaryMap[record.employeeId] = summaryMap.getOrDefault(record.employeeId, 0) + minutes
        }

        val result = mutableListOf<String>()
        for ((empId, totalMinutes) in summaryMap) {
            val employee = employeeList.find { it.employeeId == empId }
            val name = employee?.let { "${it.firstName} ${it.lastName}" } ?: "Unknown"
            val hours = totalMinutes / 60
            val rem = totalMinutes % 60
            result.add("%-10s %-20s %-10s".format(empId, name, "$hours:$rem"))
        }
            return result
    }

    // returns checked in without checked out
    fun getCurrentlyCheckedIn(): List<String> {
        return this.filter { it.checkOutDateTime == null }
            .map { it.employeeId }
            .distinct()
    }

    // prints in a format
    override fun toString(): String {
        if (this.isEmpty()) return "No attendance records found."
        return this.joinToString(separator = "\n") { it.toString() }
    }
}