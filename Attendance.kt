import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Attendance(
    val employeeId: String,
    var checkInDateTime: LocalDateTime,
    var checkOutDateTime: LocalDateTime? = null,
    var workingHours: Double? = null
) {
    // checkout - update
    fun checkOut(checkOutDateTime: LocalDateTime? = null): String? {
        if (this.checkOutDateTime != null) {
            return "Already checked out at ${this.checkOutDateTime?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))}"
        }

        val now = LocalDateTime.now()
        val actualCheckOutTime = checkOutDateTime ?: now

        if (actualCheckOutTime.isAfter(now)) {
            return "Cannot check out with a future date/time."
        }

        if (!actualCheckOutTime.isAfter(checkInDateTime)) {
            return "Check-out time must be after check-in time (${checkInDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))})."
        }

        val checkInDate = checkInDateTime.toLocalDate()
        val checkOutDate = actualCheckOutTime.toLocalDate()
        if (checkInDate != checkOutDate) {
            return "Check-out must be on the same date as check-in ($checkInDate)."
        }

        this.checkOutDateTime = actualCheckOutTime

        val duration = Duration.between(checkInDateTime, this.checkOutDateTime)
        this.workingHours = duration.toMinutes() / 60.0

        return null
    }

    // prints in a format
    override fun toString(): String {
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
        val checkInStr = checkInDateTime.format(formatter)
        val checkOutStr = checkOutDateTime?.format(formatter) ?: "Still working"
        val workingHoursStr = workingHours?.let {
            val hours = it.toInt()
            val minutes = ((it - hours) * 60).toInt()
            String.format("%02d:%02d", hours, minutes)
        } ?: ""

        return buildString {
            append("EmployeeID: $employeeId | Check-In: $checkInStr | Check-Out: $checkOutStr")
            if (workingHoursStr.isNotEmpty()) {
                append(" | Working Hours: $workingHoursStr")
            }
        }
    }

    // checks within the date
    fun isWithin(dateFrom: LocalDateTime, dateTo: LocalDateTime): Boolean {
        return !checkInDateTime.isBefore(dateFrom) && !checkInDateTime.isAfter(dateTo)
    }
}