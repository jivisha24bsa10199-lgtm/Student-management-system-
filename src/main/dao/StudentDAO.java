package com.sms.dao;

import com.sms.exceptions.DatabaseException;
import com.sms.exceptions.RecordNotFoundException;
import com.sms.models.Student;
import com.sms.utils.DatabaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StudentDAO {
    private static final Logger logger = LoggerFactory.getLogger(StudentDAO.class);

    public void create(Student student) throws DatabaseException {
        String sql = "INSERT INTO students (student_id, first_name, last_name, email, phone, " +
                    "date_of_birth, address, enrollment_date, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, student.getStudentId());
            stmt.setString(2, student.getFirstName());
            stmt.setString(3, student.getLastName());
            stmt.setString(4, student.getEmail());
            stmt.setString(5, student.getPhone());
            stmt.setDate(6, Date.valueOf(student.getDateOfBirth()));
            stmt.setString(7, student.getAddress());
            stmt.setDate(8, Date.valueOf(student.getEnrollmentDate()));
            stmt.setString(9, student.getStatus());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DatabaseException("Creating student failed, no rows affected");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    student.setId((UUID) generatedKeys.getObject(1));
                }
            }

            logger.info("Student created successfully: " + student.getStudentId());
        } catch (SQLException e) {
            logger.error("Error creating student: " + e.getMessage());
            throw new DatabaseException("Failed to create student", e);
        }
    }

    public Student findById(UUID id) throws DatabaseException, RecordNotFoundException {
        String sql = "SELECT * FROM students WHERE id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToStudent(rs);
            } else {
                throw new RecordNotFoundException("Student not found with id: " + id);
            }
        } catch (SQLException e) {
            logger.error("Error finding student by id: " + e.getMessage());
            throw new DatabaseException("Failed to find student", e);
        }
    }

    public Student findByStudentId(String studentId) throws DatabaseException, RecordNotFoundException {
        String sql = "SELECT * FROM students WHERE student_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, studentId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToStudent(rs);
            } else {
                throw new RecordNotFoundException("Student not found with student_id: " + studentId);
            }
        } catch (SQLException e) {
            logger.error("Error finding student by student_id: " + e.getMessage());
            throw new DatabaseException("Failed to find student", e);
        }
    }

    public List<Student> findAll() throws DatabaseException {
        String sql = "SELECT * FROM students ORDER BY enrollment_date DESC";
        List<Student> students = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                students.add(mapResultSetToStudent(rs));
            }

            logger.info("Retrieved " + students.size() + " students");
            return students;
        } catch (SQLException e) {
            logger.error("Error retrieving all students: " + e.getMessage());
            throw new DatabaseException("Failed to retrieve students", e);
        }
    }

    public void update(Student student) throws DatabaseException {
        String sql = "UPDATE students SET first_name = ?, last_name = ?, email = ?, phone = ?, " +
                    "date_of_birth = ?, address = ?, status = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, student.getFirstName());
            stmt.setString(2, student.getLastName());
            stmt.setString(3, student.getEmail());
            stmt.setString(4, student.getPhone());
            stmt.setDate(5, Date.valueOf(student.getDateOfBirth()));
            stmt.setString(6, student.getAddress());
            stmt.setString(7, student.getStatus());
            stmt.setObject(8, student.getId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DatabaseException("Updating student failed, no rows affected");
            }

            logger.info("Student updated successfully: " + student.getStudentId());
        } catch (SQLException e) {
            logger.error("Error updating student: " + e.getMessage());
            throw new DatabaseException("Failed to update student", e);
        }
    }

    public void delete(UUID id) throws DatabaseException {
        String sql = "DELETE FROM students WHERE id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new DatabaseException("Deleting student failed, no rows affected");
            }

            logger.info("Student deleted successfully with id: " + id);
        } catch (SQLException e) {
            logger.error("Error deleting student: " + e.getMessage());
            throw new DatabaseException("Failed to delete student", e);
        }
    }

    public List<Student> findByStatus(String status) throws DatabaseException {
        String sql = "SELECT * FROM students WHERE status = ? ORDER BY enrollment_date DESC";
        List<Student> students = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                students.add(mapResultSetToStudent(rs));
            }

            return students;
        } catch (SQLException e) {
            logger.error("Error finding students by status: " + e.getMessage());
            throw new DatabaseException("Failed to find students by status", e);
        }
    }

    private Student mapResultSetToStudent(ResultSet rs) throws SQLException {
        Student student = new Student();
        student.setId((UUID) rs.getObject("id"));
        student.setStudentId(rs.getString("student_id"));
        student.setFirstName(rs.getString("first_name"));
        student.setLastName(rs.getString("last_name"));
        student.setEmail(rs.getString("email"));
        student.setPhone(rs.getString("phone"));
        student.setDateOfBirth(rs.getDate("date_of_birth").toLocalDate());
        student.setAddress(rs.getString("address"));
        student.setEnrollmentDate(rs.getDate("enrollment_date").toLocalDate());
        student.setStatus(rs.getString("status"));
        return student;
    }
}
