# Bruno Collection for Attendance Management API

This Bruno collection contains all the endpoints for testing the Attendance Management API.

## Setup

1. **Install Bruno**: Download and install Bruno from [https://www.usebruno.com/](https://www.usebruno.com/)
2. **Open Collection**: Open Bruno and import this collection folder
3. **Set Environment**: Make sure the `local` environment is selected (or create your own)

## Environment Variables

The collection uses these environment variables:
- `baseUrl`: Base URL for the API (default: `http://localhost:8085`)
- `apiVersion`: API version (default: `v1`)

## API Endpoints

### Employee Management

#### GET Endpoints
- **Get All Employees** - `/api/employees` - Retrieve all employees with optional filtering
- **Get Employee by UUID** - `/api/employees/uuid/{empId}` - Get employee by UUID
- **Get Employee by Email** - `/api/employees/email/{email}` - Get employee by email address
- **Get Roles** - `/api/employees/roles` - Get all available roles
- **Get Departments** - `/api/employees/departments` - Get all available departments

#### POST Endpoints
- **Create Employee** - `/api/employees` - Create a new employee

#### PUT Endpoints
- **Update Employee by UUID** - `/api/employees/uuid/{empId}` - Update an existing employee by UUID
- **Update Employee by Email** - `/api/employees/email/{email}` - Update an existing employee by email

#### DELETE Endpoints
- **Delete Employee by UUID** - `/api/employees/uuid/{empId}` - Delete an employee by UUID
- **Delete Employee by Email** - `/api/employees/email/{email}` - Delete an employee by email

### Attendance Management

#### POST Endpoints
- **Employee Check-in** - `/api/attendance/checkin` - Record employee check-in
- **Employee Check-out** - `/api/attendance/checkout` - Record employee check-out

#### GET Endpoints
- **Get Attendance by Employee** - `/api/attendance/employee/{empId}` - Get all attendance for an employee
- **Get Attendance by Employee and Date** - `/api/attendance/employee/{empId}/date/{date}` - Get attendance for specific date
- **Get Attendance by Date** - `/api/attendance/date/{date}` - Get all attendance for a date
- **Get Attendance by Date Range** - `/api/attendance/date-range` - Get attendance within date range
- **Get Attendance Summary by Employee** - `/api/attendance/summary/employee/{empId}` - Get summary for employee
- **Get Attendance Summary by Date** - `/api/attendance/summary/date/{date}` - Get summary for date
- **Get Today's Attendance** - `/api/attendance/today` - Get today's attendance records
- **Get Today's Summary** - `/api/attendance/summary/today` - Get today's summary

## Usage Notes

1. **UUID Format**: Use valid UUID format for employee IDs (e.g., `550e8400-e29b-41d4-a716-446655440000`)
2. **Date Format**: Use ISO date format (YYYY-MM-DD) for date parameters
3. **Sample Data**: The collection includes sample request bodies for POST/PUT operations
4. **Query Parameters**: Many endpoints support query parameters for filtering and pagination

## Testing Workflow

1. Start with **Get All Employees** to see existing data
2. Create a new employee using **Create Employee**
3. Test employee retrieval using **Get Employee by UUID** or **Get Employee by Email**
4. Test attendance operations with the new employee
5. Use summary endpoints to analyze attendance patterns
6. Clean up by deleting test employees using **Delete Employee by UUID** or **Delete Employee by Email**

## Troubleshooting

- **Connection Issues**: Ensure your server is running on port 8085
- **Validation Errors**: Check request body format and required fields
- **UUID Errors**: Ensure UUID format is correct (8-4-4-4-12 format)
- **Date Errors**: Use ISO date format (YYYY-MM-DD)
