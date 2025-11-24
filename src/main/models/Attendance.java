package com.sms.models;

import java.time.LocalDate;
import java.util.UUID;

public class Attendance {
    private UUID id;
    private UUID enrollmentId;
    private LocalDate attendanceDate;
    private String status;
    private String remarks;

    public Attendance() {}

    public Attendance(UUID enrollmentId, LocalDate attendanceDate, String status) {
        this.enrollmentId = enrollmentId;
        this.attendanceDate = attendanceDate;
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(UUID enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public LocalDate getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(LocalDate attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public String toString() {
        return String.format("Attendance[Date=%s, Status=%s, Remarks=%s]",
                attendanceDate, status, remarks != null ? remarks : "None");
    }
}
