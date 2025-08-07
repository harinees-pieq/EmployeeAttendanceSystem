class EmployeeList : ArrayList<Employee>() {

    // adds employee
    fun addEmployee(emp: Employee): String? {
        super.add(emp)
        return null
    }

    // deletes employee
    fun deleteEmployee(empId: String): Boolean {
        return this.removeIf { it.employeeId == empId }
    }

    // find employee
    fun findById(id: String): Employee? {
        return this.find { it.employeeId == id }
    }

    // returns list of employee
    fun listAll(): List<Employee> {
        return this.toList()
    }

    // print in a format
    override fun toString(): String {
        if (this.isEmpty()) return "No employees found."
        return this.joinToString(separator = "\n") { it.toString() }
    }
}
