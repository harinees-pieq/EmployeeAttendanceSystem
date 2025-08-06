import java.lang.IllegalArgumentException

class Employee(
    firstNameInput: String,
    lastNameInput: String,
    roleInput: String,
    departmentInput: String,
    reportingToInput: String
) {
    var firstName: String
    var lastName: String
    var role: Role
    var department: Department
    var reportingTo: String
    var employeeId: String

    private companion object {
        var idCounter = 0
    }

    init {
        validateFirstName(firstNameInput)?.let { throw IllegalArgumentException(it) }
        validateLastName(lastNameInput)?.let { throw IllegalArgumentException(it) }
        validateRole(roleInput)?.let { throw IllegalArgumentException(it) }
        validateDepartment(departmentInput)?.let { throw IllegalArgumentException(it) }
        validateReportingTo(reportingToInput)?.let { throw IllegalArgumentException(it) }

        this.firstName = firstNameInput
        this.lastName = lastNameInput
        this.role = Role.valueOf(roleInput.uppercase())
        this.department = Department.valueOf(departmentInput.uppercase())
        this.reportingTo = reportingToInput

        idCounter++
        this.employeeId = "PIEQ%04d".format(idCounter)
    }

    private fun validateRole(roleStr: String): String? {
        return try {
            Role.valueOf(roleStr.trim().uppercase())
            null
        } catch (e: Exception) {
            "Invalid role. Must be one of: ${Role.entries.joinToString()}"
        }
    }

    private fun validateDepartment(deptStr: String): String? {
        return try {
            Department.valueOf(deptStr.trim().uppercase())
            null
        } catch (e: Exception) {
            "Invalid department. Must be one of: ${Department.entries.joinToString()}"
        }
    }

    private fun validateFirstName(name: String): String? =
        if (name.isBlank()) "First name cannot be blank." else null

    private fun validateLastName(name: String): String? =
        if (name.isBlank()) "Last name cannot be blank." else null

    private fun validateReportingTo(value: String): String? =
        if (value.isBlank()) "Reporting manager ID cannot be blank." else null

    override fun toString(): String {
        return "ID: $employeeId | Name: $firstName $lastName | Role: $role | Department: $department | Reports to: $reportingTo"
    }
}
