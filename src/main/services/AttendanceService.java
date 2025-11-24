package com.sms.services;

import com.sms.dao.AttendanceDAO;
import com.sms.dao.EnrollmentDAO;
import com.sms.exceptions.DatabaseException;
import com.sms.exceptions.RecordNotFoundException;
import com.sms.exceptions.ValidationException;
import com.sms.models.Attendance;
import com.sms.models.Enrollment;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class AttendanceService {
    private final AttendanceDAO attendanceDAO;
    private final EnrollmentDAO enrollmentDAO;

    public AttendanceService() {
        this.attendanceDAO = new AttendanceDAO();
        this.enrollmentDAO = new EnrollmentDAO();
    }

    public void markAttendance(UUID enrollmentId, LocalDate date, String status) throws DatabaseException, RecordNotFoundException, ValidationException {
        Enrollment enrollment = enrollmentDAO.findById(enrollmentId);
        if (!enrollment.getStatus().equals("Enrolled")) {
            throw new ValidationException("Can only mark attendance for enrolled students");
        }

        if (!status.equals("Present") && !status.equals("Absent") && !status.equals("Late")) {
            throw new ValidationException("Status must be Present, Absent, or Late");
        }

        Attendance attendance = new Attendance(enrollmentId, date, status);
        attendanceDAO.create(attendance);
    }

    public Attendance getAttendance(UUID id) throws DatabaseException, RecordNotFoundException {
        return attendanceDAO.findById(id);
    }

    public List<Attendance> getEnrollmentAttendance(UUID enrollmentId) throws DatabaseException {
        return attendanceDAO.findByEnrollmentId(enrollmentId);
    }

    public List<Attendance> getAttendanceByDate(LocalDate date) throws DatabaseException {
        return attendanceDAO.findByDate(date);
    }

    public void updateAttendance(UUID attendanceId, String status, String remarks) throws DatabaseException, RecordNotFoundException, ValidationException {
        if (!status.equals("Present") && !status.equals("Absent") && !status.equals("Late")) {
            throw new ValidationException("Status must be Present, Absent, or Late");
        }

        Attendance attendance = attendanceDAO.findById(attendanceId);
        attendance.setStatus(status);
        attendance.setRemarks(remarks);
        attendanceDAO.update(attendance);
    }

    public double getAttendancePercentage(UUID enrollmentId) throws DatabaseException {
        return attendanceDAO.getAttendancePercentage(enrollmentId);
    }

    public void deleteAttendance(UUID id) throws DatabaseException {
        attendanceDAO.delete(id);
    }
}
