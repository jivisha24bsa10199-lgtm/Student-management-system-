# Student Management System

A comprehensive Java-based Student Management System built with PostgreSQL database integration through Supabase. This system provides complete functionality for managing students, courses, enrollments, and attendance tracking.

## Overview

The Student Management System is designed to help educational institutions efficiently manage their student data, course offerings, enrollment processes, and attendance tracking. The application features a clean console-based interface with robust data validation and error handling.

## Features

### Student Management
- Add, view, update, and delete student records
- Track student status (Active, Inactive, Graduated)
- Comprehensive student profile with personal information
- Search functionality by student ID
- View all students or filter by status

### Course Management
- Create and manage course offerings
- Track course details including instructor, credits, and capacity
- Semester-based course organization
- Monitor course enrollment capacity
- Update or remove courses from the system

### Enrollment Management
- Enroll students in courses
- Track enrollment status (Enrolled, Completed, Dropped)
- Assign and update grades
- View student enrollments and course rosters
- Automatic capacity validation
- Complete enrollments with final grades

### Attendance Management
- Mark daily attendance (Present, Absent, Late)
- View attendance history by student and course
- Calculate attendance percentages
- Update attendance records with remarks
- Date-based attendance tracking

### Reports & Analytics
- Student performance reports with grades and attendance
- Course enrollment statistics
- Attendance summary reports
- Active students listing
- Comprehensive data visualization

## Technologies Used

- **Java 11** - Core programming language
- **PostgreSQL** - Relational database management
- **Supabase** - Cloud database platform
- **Maven** - Dependency management and build tool
- **JDBC** - Database connectivity
- **SLF4J** - Logging framework
- **Dotenv Java** - Environment variable management
- **JUnit 5** - Testing framework

## System Architecture

The application follows a layered architecture pattern:

```
┌─────────────────────────┐
│   Presentation Layer    │  (Console UI)
├─────────────────────────┤
│   Controller Layer      │  (ConsoleController)
├─────────────────────────┤
│   Service Layer         │  (Business Logic)
├─────────────────────────┤
│   DAO Layer             │  (Data Access)
├─────────────────────────┤
│   Model Layer           │  (Entities)
├─────────────────────────┤
│   Database Layer        │  (PostgreSQL/Supabase)
└─────────────────────────┘
```

## Installation & Setup

### Prerequisites
- Java 11 or higher
- Maven 3.6 or higher
- Supabase account (free tier available)
- Git (for version control)

### Steps to Install & Run

1. **Clone the repository**
   ```bash
   git clone <your-repository-url>
   cd student-management-system
   ```

2. **Configure Database**
   - Create a Supabase project at https://supabase.com
   - Copy your Supabase URL and Anon Key
   - Update the `.env` file with your credentials:
     ```
     SUPABASE_URL=your_supabase_url
     SUPABASE_ANON_KEY=your_supabase_anon_key
     ```

3. **Build the project**
   ```bash
   mvn clean compile
   ```

4. **Run the application**
   ```bash
   mvn exec:java -Dexec.mainClass="com.sms.Main"
   ```

## Database Schema

The system uses four main tables:

### students
- Stores student personal information
- Tracks enrollment status
- Maintains contact details

### courses
- Contains course information
- Manages instructor assignments
- Tracks course capacity

### enrollments
- Links students to courses
- Stores grades and enrollment status
- Tracks enrollment dates

### attendance
- Records daily attendance
- Associates with enrollments
- Supports status tracking and remarks

## Testing

Run the test suite:
```bash
mvn test
```

The project includes unit tests for:
- Data validation
- DAO operations
- Service layer business logic
- Error handling scenarios

## Project Structure

```
student-management-system/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── sms/
│   │   │           ├── controllers/     # UI controllers
│   │   │           ├── services/        # Business logic
│   │   │           ├── dao/             # Data access layer
│   │   │           ├── models/          # Entity classes
│   │   │           ├── utils/           # Utilities
│   │   │           ├── exceptions/      # Custom exceptions
│   │   │           └── Main.java        # Application entry point
│   │   └── resources/                   # Configuration files
│   └── test/
│       └── java/                        # Test classes
├── .env                                 # Environment variables
├── pom.xml                              # Maven configuration
├── README.md                            # This file
└── statement.md                         # Project statement
```

## Usage Examples

### Adding a Student
1. Select "Student Management" from the main menu
2. Choose "Add New Student"
3. Enter student details (ID, name, email, etc.)
4. System validates input and creates the record

### Enrolling a Student
1. Navigate to "Enrollment Management"
2. Select "Enroll Student in Course"
3. Enter student ID and course code
4. System validates capacity and student status

### Marking Attendance
1. Go to "Attendance Management"
2. Choose "Mark Attendance"
3. Select student and course
4. Mark status (Present/Absent/Late)

## Non-Functional Requirements

### Performance
- Database queries optimized with proper indexing
- Connection pooling for efficient resource usage
- Response time under 2 seconds for most operations

### Security
- Input validation to prevent SQL injection
- Email and phone number format validation
- Row Level Security (RLS) enabled on all tables
- Secure database connection with SSL

### Usability
- Clean, intuitive console interface
- Clear error messages and feedback
- Consistent navigation patterns
- Helpful prompts and instructions

### Reliability
- Comprehensive error handling
- Transaction support for data consistency
- Automatic rollback on failures
- Data integrity constraints at database level

### Maintainability
- Modular architecture with clear separation of concerns
- Well-documented code
- Consistent naming conventions
- SOLID principles applied throughout

### Scalability
- Support for unlimited students and courses
- Efficient query patterns for large datasets
- Indexed foreign keys for fast lookups
- Prepared statements for query optimization

## Error Handling

The system implements robust error handling:
- **ValidationException** - Input validation errors
- **DatabaseException** - Database operation failures
- **RecordNotFoundException** - Missing records
- Detailed error logging for troubleshooting

## Logging

Application logs are written to:
- Console (for real-time monitoring)
- `application.log` file (for persistence)

Log levels include INFO, ERROR, WARN, and DEBUG.

## Future Enhancements

Potential improvements for future versions:
- Web-based interface using Spring Boot
- Email notifications for important events
- Advanced analytics and reporting dashboard
- Integration with learning management systems
- Mobile application support
- Batch operations for bulk data import/export
- Advanced search and filtering capabilities
- Document generation (transcripts, reports, etc.)

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is developed as an academic project for educational purposes.

## Support

For issues, questions, or contributions, please create an issue in the GitHub repository.

## Acknowledgments

- VITyarthi for project guidelines
- Supabase for cloud database platform
- Java and Maven communities for excellent tools and documentation
