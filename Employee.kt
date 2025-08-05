class Employee (
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

    init {
        this.firstName = firstName
        this.lastName = lastName
        this.role = Role.valueOf(roleInput.uppercase())
        this.department = Department.valueOf(departmentInput.uppercase())
    }

    companion object {
        private var counter = 0

        fun create(
            firstName: String,
            lastName: String,
            roleInput: String,
            departmentInput: String,
            reportingTo: String
        ): Pair<Employee?, String?> {

            val firstNameError = validateFirstName(firstName)
            if (firstNameError != null) return Pair(null, firstNameError)

            val lastNameError = validateLastName(lastName)
            if (lastNameError != null) return Pair(null, lastNameError)

            val roleError = validateRole(roleInput)
            if (roleError != null) return Pair(null, roleError)

            val departmentError = validateDepartment(departmentInput)
            if (departmentError != null) return Pair(null, departmentError)

            val reportingToError = validateReportingTo(reportingTo)
            if (reportingToError != null) return Pair(null, reportingToError)

            val emp = Employee(firstName, lastName, roleInput, departmentInput, reportingTo)
            return Pair(emp, null)
        }


        // generates a new ID
        fun generateId(): String {
            counter++
            return "PIEQ%04d".format(counter)
        }

        fun validateRole(roleStr: String): String? {
            return try {
                Role.valueOf(roleStr.trim().uppercase())
                null
            } catch (e: IllegalArgumentException) {
                "Invalid role. Must be one of: ${Role.entries.joinToString()}"
            }
        }

        fun validateDepartment(deptStr: String): String? {
            return try {
                Department.valueOf(deptStr.trim().uppercase())
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
    ): String? {

        val firstNameError = validateFirstName(firstName)
        if (firstNameError != null) return firstNameError

        val lastNameError = validateLastName(lastName)
        if (lastNameError != null) return lastNameError

        val roleError = validateRole(roleInput)
        if (roleError != null) return roleError

        val departmentError = validateDepartment(departmentInput)
        if (departmentError != null) return departmentError

        val reportingToError = validateReportingTo(reportingTo)
        if (reportingToError != null) return reportingToError

        this.firstName = firstName
        this.lastName = lastName
        this.role = Role.valueOf(roleInput.uppercase())
        this.department = Department.valueOf(departmentInput.uppercase())
        this.reportingTo = reportingTo

        return null // success
    }

    // returns the employee info as a string
    override fun toString(): String {
        return "ID: $employeeId | Name: $firstName $lastName | Role: $role | Department: $department | Reports to: $reportingTo"
    }
}
