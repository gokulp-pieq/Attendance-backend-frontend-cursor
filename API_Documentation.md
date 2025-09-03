# Attendance Management API Documentation

## Base URL
```
http://localhost:8085
```

## API Overview
This API provides endpoints for managing employees and tracking attendance in an organization.

**Total Endpoints: 20**
- **Employee Management: 10 endpoints**
- **Attendance Management: 10 endpoints**

---

## Employee Management

### 1. Get All Employees
**Endpoint:** `GET /api/employees`

**Description:** Retrieve all employees with optional filtering capabilities.

**Query Parameters:**
- `dept_id` (optional): Filter by department ID
- `role_id` (optional): Filter by role ID  
- `search` (optional): Search by employee name

**Example Request:**
```bash
GET /api/employees?dept_id=1&role_id=1&search=john
```

**Response Structure:**
```json
[
  {
    "id": 1,
    "empId": "550e8400-e29b-41d4-a716-446655440000",
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "roleId": 1,
    "deptId": 1,
    "reportingTo": null
  }
]
```

---

### 2. Get Employee by UUID
**Endpoint:** `GET /api/employees/uuid/{empId}`

**Description:** Get employee details by their UUID.

**Path Parameters:**
- `empId`: Employee's UUID

**Example Request:**
```bash
GET /api/employees/uuid/550e8400-e29b-41d4-a716-446655440000
```

**Response Structure:**
```json
{
  "id": 1,
  "empId": "550e8400-e29b-41d4-a716-446655440000",
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "roleId": 1,
  "deptId": 1,
  "reportingTo": null
}
```

---

### 3. Get Employee by Email
**Endpoint:** `GET /api/employees/email/{email}`

**Description:** Get employee details by their email address.

**Path Parameters:**
- `email`: Employee's email address

**Example Request:**
```bash
GET /api/employees/email/john.doe@example.com
```

**Response Structure:** Same as Get Employee by UUID

---

### 4. Create Employee
**Endpoint:** `POST /api/employees`

**Description:** Create a new employee.

**Request Body:**
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "roleId": 1,
  "deptId": 1,
  "reportingTo": null
}
```

**Required Fields:**
- `firstName`: Employee's first name
- `lastName`: Employee's last name
- `email`: Employee's email address
- `roleId`: Role ID (1 = Developer, 2 = Manager, 3 = Analyst, 4 = Admin)
- `deptId`: Department ID (1 = Engineering, 2 = Marketing, 3 = HR, 4 = Finance)

**Optional Fields:**
- `reportingTo`: UUID of the employee this person reports to

**Response Structure:**
```json
{
  "id": 1,
  "empId": "550e8400-e29b-41d4-a716-446655440000",
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "roleId": 1,
  "deptId": 1,
  "reportingTo": null
}
```

**Status Codes:**
- `201 Created`: Employee created successfully
- `400 Bad Request`: Validation error in request body

---

### 5. Update Employee by UUID
**Endpoint:** `PUT /api/employees/uuid/{empId}`

**Description:** Update an existing employee by UUID.

**Path Parameters:**
- `empId`: Employee's UUID

**Request Body:** Same structure as Create Employee

**Example Request:**
```bash
PUT /api/employees/uuid/550e8400-e29b-41d4-a716-446655440000
```

**Response Structure:** Same as Create Employee

**Status Codes:**
- `200 OK`: Employee updated successfully
- `400 Bad Request`: Invalid UUID format or validation error
- `404 Not Found`: Employee not found

---

### 6. Update Employee by Email
**Endpoint:** `PUT /api/employees/email/{email}`

**Description:** Update an existing employee by email address.

**Path Parameters:**
- `email`: Employee's email address

**Request Body:** Same structure as Create Employee

**Example Request:**
```bash
PUT /api/employees/email/john.doe@example.com
```

**Response Structure:** Same as Create Employee

**Status Codes:**
- `200 OK`: Employee updated successfully
- `400 Bad Request`: Validation error in request body
- `404 Not Found`: Employee not found

---

### 7. Delete Employee by UUID
**Endpoint:** `DELETE /api/employees/uuid/{empId}`

**Description:** Delete an employee by UUID.

**Path Parameters:**
- `empId`: Employee's UUID

**Example Request:**
```bash
DELETE /api/employees/uuid/550e8400-e29b-41d4-a716-446655440000
```

**Response Structure:**
- `204 No Content`: Employee deleted successfully
- `400 Bad Request`: Invalid UUID format
- `500 Internal Server Error`: Failed to delete employee

---

### 8. Delete Employee by Email
**Endpoint:** `DELETE /api/employees/email/{email}`

**Description:** Delete an employee by email address.

**Path Parameters:**
- `email`: Employee's email address

**Example Request:**
```bash
DELETE /api/employees/email/john.doe@example.com
```

**Response Structure:**
- `204 No Content`: Employee deleted successfully
- `404 Not Found`: Employee not found
- `500 Internal Server Error`: Failed to delete employee

---

### 9. Get Roles
**Endpoint:** `GET /api/employees/roles`

**Description:** Get all available employee roles.

**Example Request:**
```bash
GET /api/employees/roles
```

**Response Structure:**
```json
[
  {
    "id": 1,
    "name": "Developer",
    "description": "Software developer role"
  },
  {
    "id": 2,
    "name": "Manager",
    "description": "Team management role"
  },
  {
    "id": 3,
    "name": "Analyst",
    "description": "Business analysis role"
  },
  {
    "id": 4,
    "name": "Admin",
    "description": "Administrative role"
  }
]
```

---

### 10. Get Departments
**Endpoint:** `GET /api/employees/departments`

**Description:** Get all available departments.

**Example Request:**
```bash
GET /api/employees/departments
```

**Response Structure:**
```json
[
  {
    "id": 1,
    "name": "Engineering",
    "description": "Software development department"
  },
  {
    "id": 2,
    "name": "Marketing",
    "description": "Marketing and sales department"
  },
  {
    "id": 3,
    "name": "HR",
    "description": "Human resources department"
  },
  {
    "id": 4,
    "name": "Finance",
    "description": "Financial operations department"
  }
]
```

---

## Attendance Management

### 11. Employee Check-in
**Endpoint:** `POST /api/attendance/checkin`

**Description:** Record employee check-in.

**Request Body:**
```json
{
  "emp_id": "550e8400-e29b-41d4-a716-446655440000",
  "checkin_datetime": "2024-01-15T09:00:00"
}
```

**Required Fields:**
- `emp_id`: Employee's UUID
- `checkin_datetime`: Check-in timestamp (ISO 8601 format, defaults to current time if not provided)

**Response Structure:**
```json
{
  "id": 1,
  "employeeId": "550e8400-e29b-41d4-a716-446655440000",
  "checkinTime": "2024-01-15T09:00:00",
  "checkoutTime": null,
  "location": "Office Building A",
  "notes": "On time arrival",
  "createdAt": "2024-01-15T09:00:00",
  "updatedAt": "2024-01-15T09:00:00"
}
```

**Status Codes:**
- `201 Created`: Check-in recorded successfully
- `400 Bad Request`: Validation error or invalid employee ID

---

### 12. Employee Check-out
**Endpoint:** `POST /api/attendance/checkout`

**Description:** Record employee check-out.

**Request Body:**
```json
{
  "emp_id": "550e8400-e29b-41d4-a716-446655440000",
  "checkout_datetime": "2024-01-15T17:00:00"
}
```

**Required Fields:**
- `emp_id`: Employee's UUID
- `checkout_datetime`: Check-out timestamp (ISO 8601 format, defaults to current time if not provided)

**Response Structure:** Same as Check-in response, but with `checkoutTime` populated

**Status Codes:**
- `200 OK`: Check-out recorded successfully
- `400 Bad Request`: Validation error or invalid employee ID

---

### 13. Get Attendance by Employee
**Endpoint:** `GET /api/attendance/employee/{empId}`

**Description:** Get all attendance records for a specific employee.

**Path Parameters:**
- `empId`: Employee's UUID

**Example Request:**
```bash
GET /api/attendance/employee/550e8400-e29b-41d4-a716-446655440000
```

**Response Structure:**
```json
[
  {
    "id": 1,
    "employeeId": "550e8400-e29b-41d4-a716-446655440000",
    "checkinTime": "2024-01-15T09:00:00",
    "checkoutTime": "2024-01-15T17:00:00",
    "location": "Office Building A",
    "notes": "Regular work day",
    "createdAt": "2024-01-15T09:00:00",
    "updatedAt": "2024-01-15T17:00:00"
  }
]
```

---

### 14. Get Attendance by Employee and Date
**Endpoint:** `GET /api/attendance/employee/{empId}/date/{date}`

**Description:** Get attendance record for a specific employee on a specific date.

**Path Parameters:**
- `empId`: Employee's UUID
- `date`: Date in YYYY-MM-DD format

**Example Request:**
```bash
GET /api/attendance/employee/550e8400-e29b-41d4-a716-446655440000/date/2024-01-15
```

**Response Structure:** Single attendance object or null if no record found

**Status Codes:**
- `200 OK`: Attendance record found
- `404 Not Found`: No attendance record for the specified date

---

### 15. Get Attendance by Date
**Endpoint:** `GET /api/attendance/date/{date}`

**Description:** Get all attendance records for a specific date.

**Path Parameters:**
- `date`: Date in YYYY-MM-DD format

**Example Request:**
```bash
GET /api/attendance/date/2024-01-15
```

**Response Structure:** Array of attendance objects

---

### 16. Get Attendance by Date Range
**Endpoint:** `GET /api/attendance/date-range`

**Description:** Get all attendance records within a date range.

**Query Parameters:**
- `start_date`: Start date in YYYY-MM-DD format
- `end_date`: End date in YYYY-MM-DD format

**Example Request:**
```bash
GET /api/attendance/date-range?start_date=2024-01-01&end_date=2024-01-31
```

**Response Structure:** Array of attendance objects

---

### 17. Get Attendance Summary by Employee
**Endpoint:** `GET /api/attendance/summary/employee/{empId}`

**Description:** Get attendance summary for a specific employee within a date range.

**Path Parameters:**
- `empId`: Employee's UUID

**Query Parameters:**
- `start_date`: Start date in YYYY-MM-DD format
- `end_date`: End date in YYYY-MM-DD format

**Example Request:**
```bash
GET /api/attendance/summary/employee/550e8400-e29b-41d4-a716-446655440000?start_date=2024-01-01&end_date=2024-01-31
```

**Response Structure:**
```json
{
  "employeeId": "550e8400-e29b-41d4-a716-446655440000",
  "totalDays": 22,
  "presentDays": 20,
  "absentDays": 2,
  "averageCheckinTime": "09:15:00",
  "averageCheckoutTime": "17:30:00",
  "totalWorkHours": 176.5
}
```

---

### 18. Get Attendance Summary by Date
**Endpoint:** `GET /api/attendance/summary/date/{date}`

**Description:** Get attendance summary for a specific date.

**Path Parameters:**
- `date`: Date in YYYY-MM-DD format

**Example Request:**
```bash
GET /api/attendance/summary/date/2024-01-15
```

**Response Structure:**
```json
{
  "date": "2024-01-15",
  "totalEmployees": 50,
  "presentEmployees": 45,
  "absentEmployees": 5,
  "lateArrivals": 3,
  "earlyDepartures": 2
}
```

---

### 19. Get Today's Attendance
**Endpoint:** `GET /api/attendance/today`

**Description:** Get all attendance records for today.

**Example Request:**
```bash
GET /api/attendance/today
```

**Response Structure:** Array of attendance objects for today

---

### 20. Get Today's Summary
**Endpoint:** `GET /api/attendance/summary/today`

**Description:** Get attendance summary for today.

**Example Request:**
```bash
GET /api/attendance/summary/today
```

**Response Structure:** Same as Get Attendance Summary by Date

---

## Data Models

### Employee Model
```json
{
  "id": "number",
  "empId": "string (UUID)",
  "firstName": "string",
  "lastName": "string",
  "email": "string",
  "roleId": "number",
  "deptId": "number",
  "reportingTo": "string (UUID) | null"
}
```

### Attendance Model
```json
{
  "id": "number",
  "empId": "string (UUID)",
  "checkinDatetime": "string (ISO 8601)",
  "checkoutDatetime": "string (ISO 8601) | null"
}
```

### Department Enum
```json
{
  "1": "Engineering",
  "2": "Marketing", 
  "3": "HR",
  "4": "Finance"
}
```

### Role Enum
```json
{
  "1": "Developer",
  "2": "Manager",
  "3": "Analyst", 
  "4": "Admin"
}
```

---

## Error Responses

### Standard Error Format
```json
{
  "error": "Error message description"
}
```

### Common HTTP Status Codes
- `200 OK`: Request successful
- `201 Created`: Resource created successfully
- `204 No Content`: Request successful, no content to return
- `400 Bad Request`: Invalid request data or parameters
- `404 Not Found`: Resource not found
- `500 Internal Server Error`: Server error

---

## Authentication & Security
Currently, the API does not implement authentication. All endpoints are publicly accessible.

---

## Rate Limiting
Currently, the API does not implement rate limiting.

---

## Notes for Frontend Developers

1. **Date Format**: Always use ISO 8601 format (YYYY-MM-DD) for dates
2. **UUID Format**: Employee IDs use UUID format (8-4-4-4-12 characters)
3. **Time Zones**: All timestamps are in UTC
4. **Field Names**: Use snake_case for request fields (emp_id, checkin_datetime, checkout_datetime)
5. **Validation**: The API validates all input data and returns appropriate error messages
6. **CORS**: Ensure your frontend handles CORS if calling from a different domain
7. **Error Handling**: Always check HTTP status codes and handle error responses appropriately

---

## Testing
Use the provided Bruno collection in the `bruno/` folder to test all endpoints before integration.
