package com.sms.dao;

import com.sms.exceptions.DatabaseException;
import com.sms.exceptions.RecordNotFoundException;
import com.sms.models.Attendance;
import com.sms.utils.DatabaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AttendanceDAO {
    private static final Logger logger = LoggerFactory.getLogger(AttendanceDAO.class);

    public void create(Attendance attendance) throws DatabaseException {
        String sql = "INSERT INTO attendance (enrollment_id, attendance_date, status, remarks) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setObject(1, attendance.getEnrollmentId());
            stmt.setDate(2, Date.valueOf(attendance.getAttendanceDate()));
            stmt.setString(3, attendance.getStatus());
            stmt.setString(4, attendance.getRemarks());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DatabaseException("Creating attendance failed, no rows affected");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    attendance.setId((UUID) generatedKeys.getObject(1));
                }
            }

            logger.info("Attendance record created successfully");
        } catch (SQLException e) {
            logger.error("Error creating attendance: " + e.getMessage());
            throw new DatabaseException("Failed to create attendance", e);
        }
    }

    public Attendance findById(UUID id) throws DatabaseException, RecordNotFoundException {
        String sql = "SELECT * FROM attendance WHERE id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToAttendance(rs);
            } else {
                throw new RecordNotFoundException("Attendance not found with id: " + id);
            }
        } catch (SQLException e) {
            logger.error("Error finding attendance by id: " + e.getMessage());
            throw new DatabaseException("Failed to find attendance", e);
        }
    }

    public List<Attendance> findByEnrollmentId(UUID enrollmentId) throws DatabaseException {
        String sql = "SELECT * FROM attendance WHERE enrollment_id = ? ORDER BY attendance_date DESC";
        List<Attendance> attendanceList = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, enrollmentId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                attendanceList.add(mapResultSetToAttendance(rs));
            }

            return attendanceList;
        } catch (SQLException e) {
            logger.error("Error finding attendance by enrollment: " + e.getMessage());
            throw new DatabaseException("Failed to find attendance by enrollment", e);
        }
    }

    public List<Attendance> findByDate(LocalDate date) throws DatabaseException {
        String sql = "SELECT * FROM attendance WHERE attendance_date = ?";
        List<Attendance> attendanceList = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(date));
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                attendanceList.add(mapResultSetToAttendance(rs));
            }

            return attendanceList;
        } catch (SQLException e) {
            logger.error("Error finding attendance by date: " + e.getMessage());
            throw new DatabaseException("Failed to find attendance by date", e);
        }
    }

    public void update(Attendance attendance) throws DatabaseException {
        String sql = "UPDATE attendance SET status = ?, remarks = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, attendance.getStatus());
            stmt.setString(2, attendance.getRemarks());
            stmt.setObject(3, attendance.getId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DatabaseException("Updating attendance failed, no rows affected");
            }

            logger.info("Attendance updated successfully");
        } catch (SQLException e) {
            logger.error("Error updating attendance: " + e.getMessage());
            throw new DatabaseException("Failed to update attendance", e);
        }
    }

    public void delete(UUID id) throws DatabaseException {
        String sql = "DELETE FROM attendance WHERE id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new DatabaseException("Deleting attendance failed, no rows affected");
            }

            logger.info("Attendance deleted successfully with id: " + id);
        } catch (SQLException e) {
            logger.error("Error deleting attendance: " + e.getMessage());
            throw new DatabaseException("Failed to delete attendance", e);
        }
    }

    public double getAttendancePercentage(UUID enrollmentId) throws DatabaseException {
        String sql = "SELECT " +
                    "COUNT(*) as total, " +
                    "SUM(CASE WHEN status = 'Present' THEN 1 ELSE 0 END) as present " +
                    "FROM attendance WHERE enrollment_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, enrollmentId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int total = rs.getInt("total");
                int present = rs.getInt("present");
                if (total == 0) return 0.0;
                return (present * 100.0) / total;
            }
            return 0.0;
        } catch (SQLException e) {
            logger.error("Error calculating attendance percentage: " + e.getMessage());
            throw new DatabaseException("Failed to calculate attendance percentage", e);
        }
    }

    private Attendance mapResultSetToAttendance(ResultSet rs) throws SQLException {
        Attendance attendance = new Attendance();
        attendance.setId((UUID) rs.getObject("id"));
        attendance.setEnrollmentId((UUID) rs.getObject("enrollment_id"));
        attendance.setAttendanceDate(rs.getDate("attendance_date").toLocalDate());
        attendance.setStatus(rs.getString("status"));
        attendance.setRemarks(rs.getString("remarks"));
        return attendance;
    }
}
