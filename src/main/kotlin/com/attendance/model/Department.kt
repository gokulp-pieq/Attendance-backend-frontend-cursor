package com.attendance.model

enum class Department(val id: Int, val deptName: String, val description: String) {
    IT(1, "Information Technology", "IT and Software Development"),
    HR(2, "Human Resources", "Human Resources and Recruitment"),
    FINANCE(3, "Finance", "Finance and Accounting"),
    MARKETING(4, "Marketing", "Marketing and Sales"),
    OPERATIONS(5, "Operations", "Business Operations"),
    ENGINEERING(6, "Engineering", "Product Engineering"),
    SUPPORT(7, "Customer Support", "Customer Service and Support");
    
    companion object {
        fun fromId(id: Int): Department? = values().find { it.id == id }
    }
}
