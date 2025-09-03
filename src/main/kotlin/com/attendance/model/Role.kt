package com.attendance.model

enum class Role(val id: Int, val roleName: String, val description: String) {
    EMPLOYEE(1, "Employee", "Regular employee"),
    SUPERVISOR(2, "Supervisor", "Team supervisor"),
    MANAGER(3, "Manager", "Department manager"),
    DIRECTOR(4, "Director", "Executive director"),
    ADMIN(5, "Administrator", "System administrator");
    
    companion object {
        fun fromId(id: Int): Role? = values().find { it.id == id }
    }
}
