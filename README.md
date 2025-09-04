# Employee Attendance Management System

A comprehensive employee attendance management system built with Dropwizard + Kotlin backend and PostgreSQL database.

## Project Structure

```
├── backend/                    # Backend application
│   ├── src/main/kotlin/
│   │   ├── com/attendance/
│   │   │   ├── config/         # Configuration classes
│   │   │   ├── dao/           # Data Access Objects
│   │   │   ├── dto/           # Data Transfer Objects
│   │   │   ├── model/         # Entity models
│   │   │   ├── resources/     # REST API endpoints
│   │   │   └── service/       # Business logic
│   │   └── AttendanceApplication.kt
│   ├── src/main/resources/
│   │   └── migrations/        # Database migrations
│   ├── build.gradle.kts       # Build configuration
│   └── config.yml             # Application configuration
├── frontend/                   # Frontend application (React + Vite + Tailwind)
└── README.md
```

## Technologies Used

### Backend
- **Dropwizard 4.0.4** - Java framework for RESTful web services
- **Kotlin 1.9.10** - Programming language
- **JDBI3** - SQL convenience library
- **PostgreSQL** - Database
- **Flyway** - Database migration tool
- **Jackson** - JSON serialization/deserialization

### Database
- **PostgreSQL** - Primary database
- **Flyway** - Database schema management

## Prerequisites

- Java 17 or higher
- PostgreSQL 12 or higher
- Gradle 7.0 or higher

## Setup Instructions

### 1. Database Setup

1. Create a PostgreSQL database named `Attendance_Management2`
2. Update `config.yml` with your database credentials if different from defaults

### 2. Build and Run

```bash
# Build the project
./gradlew build

# Run the application
./gradlew run

# Or run the JAR file
java -jar build/libs/attendance-management-1.0.0.jar server config.yml
```

The application will start on:
- **Application**: http://localhost:8085
- **Admin**: http://localhost:8086

### 3. Database Migrations

The application will automatically run database migrations on startup using Flyway.

## API Endpoints

### Employee Management

#### Create Employee
```http
POST /api/employees
Content-Type: application/json

{
  "first_name": "John",
  "last_name": "Doe",
  "email": "john.doe@company.com",
  "password": "password123",
  "role_name": "Developer",
  "dept_name": "Engineering",
  "reporting_to": null
}
```

**Note:** Use `role_name` and `dept_name` instead of IDs. The system will automatically look up the corresponding IDs.

#### Get All Employees
```http
GET /api/employees
GET /api/employees?dept_id=1
GET /api/employees?role_id=2
GET /api/employees?search=john
```

#### Get Employee by ID
```http
GET /api/employees/{id}
GET /api/employees/uuid/{empId}
```

#### Update Employee
```http
PUT /api/employees/{empId}
Content-Type: application/json

{
  "first_name": "John",
  "last_name": "Smith",
  "email": "john.smith@company.com",
  "role_id": 1,
  "dept_id": 1,
  "reporting_to": null
}
```

#### Delete Employee
```http
DELETE /api/employees/{empId}
```

#### Get Roles and Departments
```http
GET /api/employees/roles
GET /api/employees/departments
```

### Attendance Management

#### Check-in
```http
POST /api/attendance/checkin
Content-Type: application/json

{
  "emp_id": "550e8400-e29b-41d4-a716-446655440001",
  "checkin_datetime": "2024-01-15T09:00:00"
}
```

#### Check-out
```http
POST /api/attendance/checkout
Content-Type: application/json

{
  "emp_id": "550e8400-e29b-41d4-a716-446655440001",
  "checkout_datetime": "2024-01-15T17:00:00"
}
```

#### Get Attendance Records
```http
GET /api/attendance/employee/{empId}
GET /api/attendance/employee/{empId}/date/{date}
GET /api/attendance/date/{date}
GET /api/attendance/date-range?start_date=2024-01-01&end_date=2024-01-31
```

#### Get Attendance Summary
```http
GET /api/attendance/summary/employee/{empId}?start_date=2024-01-01&end_date=2024-01-31
GET /api/attendance/summary/date/{date}
GET /api/attendance/summary/today
```

#### Today's Attendance
```http
GET /api/attendance/today
```

## Data Models

### Employee
- `id`: Auto-generated integer ID
- `emp_id`: UUID identifier
- `first_name`: Employee's first name
- `last_name`: Employee's last name
- `email`: Unique email address
- `role_id`: Role identifier (1-5)
- `dept_id`: Department identifier (1-7)
- `reporting_to`: UUID of reporting manager

### Attendance
- `id`: Auto-generated integer ID
- `emp_id`: Employee UUID reference
- `checkin_datetime`: Check-in timestamp
- `checkout_datetime`: Check-out timestamp (nullable)

### Roles
1. **Employee** - Regular employee
2. **Supervisor** - Team supervisor
3. **Manager** - Department manager
4. **Director** - Executive director
5. **Administrator** - System administrator

### Departments
1. **IT** - Information Technology
2. **HR** - Human Resources
3. **Finance** - Finance and Accounting
4. **Marketing** - Marketing and Sales
5. **Operations** - Business Operations
6. **Engineering** - Product Engineering
7. **Support** - Customer Service and Support

## Business Rules

### Check-in Validation
- Employee can only check-in once per day
- Check-in time must be between 6:00 AM and 10:00 PM
- Employee must exist in the system

### Check-out Validation
- Employee must have an open attendance record for the day
- Check-out time must be after check-in time
- Check-out time must be between 6:00 AM and 10:00 PM

### Employee Validation
- Email must be unique across all employees
- Role and department IDs must be valid
- Reporting manager must exist in the system

## Testing with Bruno

You can test the API endpoints using Bruno or any other API testing tool. The application includes sample data that will be automatically inserted when you run the migrations.

## Next Steps

1. **Frontend Development**: Build React frontend with Vite and Tailwind CSS
2. **Authentication**: Add JWT-based authentication and authorization
3. **Reporting**: Implement advanced reporting and analytics
4. **Notifications**: Add email/SMS notifications for late check-ins
5. **Mobile App**: Develop mobile application for check-in/check-out

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## License

This project is licensed under the MIT License.
