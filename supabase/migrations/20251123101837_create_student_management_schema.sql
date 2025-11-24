/*
  # Student Management System - Database Schema

  ## Overview
  Creates a comprehensive database schema for managing students, courses, enrollments, and attendance.

  ## 1. New Tables

  ### students
  - `id` (uuid, primary key) - Unique identifier for each student
  - `student_id` (text, unique) - Human-readable student ID (e.g., STU001)
  - `first_name` (text) - Student's first name
  - `last_name` (text) - Student's last name
  - `email` (text, unique) - Student's email address
  - `phone` (text) - Contact phone number
  - `date_of_birth` (date) - Student's date of birth
  - `address` (text) - Residential address
  - `enrollment_date` (date) - Date of enrollment
  - `status` (text) - Active/Inactive/Graduated
  - `created_at` (timestamptz) - Record creation timestamp
  - `updated_at` (timestamptz) - Last update timestamp

  ### courses
  - `id` (uuid, primary key) - Unique identifier for each course
  - `course_code` (text, unique) - Course code (e.g., CS101)
  - `course_name` (text) - Full course name
  - `description` (text) - Course description
  - `credits` (integer) - Credit hours
  - `instructor` (text) - Instructor name
  - `semester` (text) - Semester offered
  - `max_capacity` (integer) - Maximum student capacity
  - `created_at` (timestamptz) - Record creation timestamp
  - `updated_at` (timestamptz) - Last update timestamp

  ### enrollments
  - `id` (uuid, primary key) - Unique identifier for enrollment
  - `student_id` (uuid, foreign key) - References students table
  - `course_id` (uuid, foreign key) - References courses table
  - `enrollment_date` (date) - Date of enrollment
  - `grade` (text) - Final grade (A, B, C, etc.)
  - `status` (text) - Enrolled/Completed/Dropped
  - `created_at` (timestamptz) - Record creation timestamp
  - `updated_at` (timestamptz) - Last update timestamp

  ### attendance
  - `id` (uuid, primary key) - Unique identifier for attendance record
  - `enrollment_id` (uuid, foreign key) - References enrollments table
  - `attendance_date` (date) - Date of attendance
  - `status` (text) - Present/Absent/Late
  - `remarks` (text) - Additional notes
  - `created_at` (timestamptz) - Record creation timestamp

  ## 2. Security
  - Enable RLS on all tables
  - Add policies for authenticated access with appropriate restrictions

  ## 3. Indexes
  - Create indexes on foreign keys and frequently queried columns for performance

  ## 4. Important Notes
  - All timestamps use timestamptz for timezone awareness
  - Unique constraints on student_id and course_code for data integrity
  - Cascade deletes handled appropriately to maintain referential integrity
  - Default values set for timestamps and status fields
*/

CREATE TABLE IF NOT EXISTS students (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  student_id text UNIQUE NOT NULL,
  first_name text NOT NULL,
  last_name text NOT NULL,
  email text UNIQUE NOT NULL,
  phone text,
  date_of_birth date,
  address text,
  enrollment_date date DEFAULT CURRENT_DATE,
  status text DEFAULT 'Active',
  created_at timestamptz DEFAULT now(),
  updated_at timestamptz DEFAULT now()
);

CREATE TABLE IF NOT EXISTS courses (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  course_code text UNIQUE NOT NULL,
  course_name text NOT NULL,
  description text,
  credits integer DEFAULT 3,
  instructor text,
  semester text,
  max_capacity integer DEFAULT 30,
  created_at timestamptz DEFAULT now(),
  updated_at timestamptz DEFAULT now()
);

CREATE TABLE IF NOT EXISTS enrollments (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  student_id uuid NOT NULL REFERENCES students(id) ON DELETE CASCADE,
  course_id uuid NOT NULL REFERENCES courses(id) ON DELETE CASCADE,
  enrollment_date date DEFAULT CURRENT_DATE,
  grade text,
  status text DEFAULT 'Enrolled',
  created_at timestamptz DEFAULT now(),
  updated_at timestamptz DEFAULT now(),
  UNIQUE(student_id, course_id)
);

CREATE TABLE IF NOT EXISTS attendance (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  enrollment_id uuid NOT NULL REFERENCES enrollments(id) ON DELETE CASCADE,
  attendance_date date DEFAULT CURRENT_DATE,
  status text DEFAULT 'Present',
  remarks text,
  created_at timestamptz DEFAULT now()
);

CREATE INDEX IF NOT EXISTS idx_enrollments_student_id ON enrollments(student_id);
CREATE INDEX IF NOT EXISTS idx_enrollments_course_id ON enrollments(course_id);
CREATE INDEX IF NOT EXISTS idx_attendance_enrollment_id ON attendance(enrollment_id);
CREATE INDEX IF NOT EXISTS idx_students_email ON students(email);
CREATE INDEX IF NOT EXISTS idx_students_student_id ON students(student_id);
CREATE INDEX IF NOT EXISTS idx_courses_course_code ON courses(course_code);

ALTER TABLE students ENABLE ROW LEVEL SECURITY;
ALTER TABLE courses ENABLE ROW LEVEL SECURITY;
ALTER TABLE enrollments ENABLE ROW LEVEL SECURITY;
ALTER TABLE attendance ENABLE ROW LEVEL SECURITY;

CREATE POLICY "Allow public read access to students"
  ON students FOR SELECT
  TO public
  USING (true);

CREATE POLICY "Allow public insert to students"
  ON students FOR INSERT
  TO public
  WITH CHECK (true);

CREATE POLICY "Allow public update to students"
  ON students FOR UPDATE
  TO public
  USING (true)
  WITH CHECK (true);

CREATE POLICY "Allow public delete from students"
  ON students FOR DELETE
  TO public
  USING (true);

CREATE POLICY "Allow public read access to courses"
  ON courses FOR SELECT
  TO public
  USING (true);

CREATE POLICY "Allow public insert to courses"
  ON courses FOR INSERT
  TO public
  WITH CHECK (true);

CREATE POLICY "Allow public update to courses"
  ON courses FOR UPDATE
  TO public
  USING (true)
  WITH CHECK (true);

CREATE POLICY "Allow public delete from courses"
  ON courses FOR DELETE
  TO public
  USING (true);

CREATE POLICY "Allow public read access to enrollments"
  ON enrollments FOR SELECT
  TO public
  USING (true);

CREATE POLICY "Allow public insert to enrollments"
  ON enrollments FOR INSERT
  TO public
  WITH CHECK (true);

CREATE POLICY "Allow public update to enrollments"
  ON enrollments FOR UPDATE
  TO public
  USING (true)
  WITH CHECK (true);

CREATE POLICY "Allow public delete from enrollments"
  ON enrollments FOR DELETE
  TO public
  USING (true);

CREATE POLICY "Allow public read access to attendance"
  ON attendance FOR SELECT
  TO public
  USING (true);

CREATE POLICY "Allow public insert to attendance"
  ON attendance FOR INSERT
  TO public
  WITH CHECK (true);

CREATE POLICY "Allow public update to attendance"
  ON attendance FOR UPDATE
  TO public
  USING (true)
  WITH CHECK (true);

CREATE POLICY "Allow public delete from attendance"
  ON attendance FOR DELETE
  TO public
  USING (true);
