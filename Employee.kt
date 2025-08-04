import java.util.Locale

class Employee(
    firstName: String,
    lastName: String,
    roleInput: String,
    departmentInput: String,
    var reportingTo: String
) {
    var firstName: String
    var lastName: String
    var role: Role
    var department: Department

    // every employee gets a unique ID
    val employeeId: String = generateId()

    // validations
    init {
        require(validateFirstName(firstName) == null) { "Invalid first name" }
        this.firstName = firstName

        require(validateLastName(lastName) == null) { "Invalid last name" }
        this.lastName = lastName

        require(validateRole(roleInput) == null) { "Invalid role provided." }
        this.role = Role.valueOf(roleInput.uppercase(Locale.getDefault()))

        require(validateDepartment(departmentInput) == null) { "Invalid department provided." }
        this.department = Department.valueOf(departmentInput.uppercase(Locale.getDefault()))

        require(validateReportingTo(reportingTo) == null) { "Invalid reporting manager" }
    }

    companion object {
        private var counter = 0

        // generates a new ID
        fun generateId(): String {
            counter++
            return "PIEQ%04d".format(counter)
        }

        fun validateRole(roleStr: String): String? {
            return try {
                Role.valueOf(roleStr.trim().uppercase(Locale.getDefault()))
                null
            } catch (e: IllegalArgumentException) {
                "Invalid role. Must be one of: ${Role.entries.joinToString()}"
            }
        }

        fun validateDepartment(deptStr: String): String? {
            return try {
                Department.valueOf(deptStr.trim().uppercase(Locale.getDefault()))
                null
            } catch (e: IllegalArgumentException) {
                "Invalid department. Must be one of: ${Department.entries.joinToString()}"
            }
        }

        fun validateFirstName(name: String): String? =
            if (name.isBlank()) "First name cannot be blank" else null

        fun validateLastName(name: String): String? =
            if (name.isBlank()) "Last name cannot be blank" else null

        fun validateReportingTo(value: String): String? =
            if (value.isBlank()) "Reporting manager ID cannot be blank" else null

        fun isValidEmployeeId(id: String): Boolean {
            if (id.length != 8) return false
            if (id.substring(0, 4) != "PIEQ") return false
            val numberPart = id.substring(4, 8)
            return numberPart.all { it.isDigit() }
        }
    }

    // updates employee details
    fun updateDetails(
        firstName: String,
        lastName: String,
        roleInput: String,
        departmentInput: String,
        reportingTo: String
    ) {
        require(validateFirstName(firstName) == null) { "Invalid first name" }
        require(validateLastName(lastName) == null) { "Invalid last name" }
        require(validateRole(roleInput) == null) { "Invalid role" }
        require(validateDepartment(departmentInput) == null) { "Invalid department" }
        require(validateReportingTo(reportingTo) == null) { "Invalid reporting manager" }

        this.firstName = firstName
        this.lastName = lastName
        this.role = Role.valueOf(roleInput.uppercase(Locale.getDefault()))
        this.department = Department.valueOf(departmentInput.uppercase(Locale.getDefault()))
        this.reportingTo = reportingTo
    }

    // returns the employee info as a string
    override fun toString(): String {
        return "ID: $employeeId | Name: $firstName $lastName | Role: $role | Department: $department | Reports to: $reportingTo"
    }
}
