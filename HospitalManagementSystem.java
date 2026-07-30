package HospitalManagementSystem;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

import HospitalManagementSystem.Doctor;
import HospitalManagementSystem.Patient;

public class HospitalManagementSystem {
    // Ideally, load these from environment variables or a properties file
    private static final String url = "jdbc:mysql://localhost:3306/hospital";
    private static final String username = "root";
    private static final String password = "rajinikanth"; // Use the password you set for MySQL    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ MySQL JDBC Driver not found.");
            return;
        }

        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             Scanner scanner = new Scanner(System.in)) {

            Patient patient = new Patient(connection, scanner);
            Doctor doctor = new Doctor(connection, scanner);

            boolean running = true;
            while (running) {
                System.out.println("\n==== HOSPITAL MANAGEMENT SYSTEM ====");
                System.out.println("1. Add Patient");
                System.out.println("2. View Patients");
                System.out.println("3. View Doctors");
                System.out.println("4. Book Appointment");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");

                if (!scanner.hasNextInt()) {
                    System.out.println("❌ Invalid input! Please enter a number.");
                    scanner.next(); // Consume invalid token
                    continue;
                }

                int choice = scanner.nextInt();

                switch (choice) {
                    case 1 -> patient.addPatient();
                    case 2 -> patient.viewPatient();
                    case 3 -> doctor.viewDoctor();
                    case 4 -> bookAppointment(patient, doctor, connection, scanner);
                    case 5 -> {
                        System.out.println("Exiting system. Goodbye!");
                        running = false;
                    }
                    default -> System.out.println("❌ Enter a valid choice (1-5)!");
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Database connection failed: " + e.getMessage());
        }
    }

    public static void bookAppointment(Patient patient, Doctor doctor, Connection connection, Scanner scanner) {
        try {
            System.out.print("Enter Patient ID: ");
            int patientId = scanner.nextInt();

            System.out.print("Enter Doctor ID: ");
            int doctorId = scanner.nextInt();

            System.out.print("Enter Appointment Date (YYYY-MM-DD): ");
            String dateInput = scanner.next();

            // Client-side Date Validation
            LocalDate appointmentDate;
            try {
                appointmentDate = LocalDate.parse(dateInput);
            } catch (DateTimeParseException e) {
                System.out.println("❌ Invalid date format. Please use YYYY-MM-DD.");
                return;
            }

            if (!patient.getPatientById(patientId) || !doctor.getDoctorById(doctorId)) {
                System.out.println("❌ Either patient or doctor does not exist.");
                return;
            }

            if (checkDoctorAvailability(doctorId, appointmentDate, connection)) {
                System.out.println("❌ Doctor is not available on that date. Please choose a different date.");
                return;
            }

            String appointmentQuery = "INSERT INTO appointments (patient_id, doctor_id, appointment_date) VALUES (?, ?, ?)";

            try (PreparedStatement ps = connection.prepareStatement(appointmentQuery)) {
                ps.setInt(1, patientId);
                ps.setInt(2, doctorId);
                ps.setDate(3, Date.valueOf(appointmentDate));

                int rowsAffected = ps.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("✅ Appointment booked successfully!");
                } else {
                    System.out.println("❌ Failed to book appointment.");
                }
            }

        } catch (SQLException e) {
            System.out.println("❌ Database error while booking appointment.");
            e.printStackTrace();
        }
    }

    public static boolean checkDoctorAvailability(int doctorId, LocalDate appointmentDate, Connection connection) {
        String query = "SELECT COUNT(*) FROM appointments WHERE doctor_id = ? AND appointment_date = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, doctorId);
            ps.setDate(2, Date.valueOf(appointmentDate));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // True if already booked
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Error checking doctor availability.");
            e.printStackTrace();
        }

        return false;
    }
}