package HospitalManagementSystem;

import java.sql.*;
import java.util.Scanner;

public class Patient {
    private final Connection connection;
    private final Scanner scanner;

    public Patient(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public void addPatient() {
        scanner.nextLine(); // consume leftover newline from previous inputs
        System.out.print("Enter patient name: ");
        String name = scanner.nextLine();

        System.out.print("Enter patient age: ");
        while (!scanner.hasNextInt()) { // Input validation for non-numeric input
            System.out.print("❌ Invalid age. Please enter a valid number: ");
            scanner.next();
        }
        int age = scanner.nextInt();
        scanner.nextLine(); // consume newline after integer input

        System.out.print("Enter patient gender: ");
        String gender = scanner.nextLine();

        // Pluralized table name 'patients' to standard JDBC practices
        String query = "INSERT INTO patients (name, age, gender) VALUES (?, ?, ?)";

        // Try-with-resources auto-closes PreparedStatement
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, age);
            preparedStatement.setString(3, gender);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("✅ Patient added successfully!");
            } else {
                System.out.println("❌ Failed to add patient.");
            }
        } catch (SQLException e) {
            System.err.println("❌ Database error while adding patient: " + e.getMessage());
        }
    }

    public void viewPatient() {
        String query = "SELECT * FROM patients";

        // Try-with-resources auto-closes PreparedStatement and ResultSet
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            System.out.println("\nPatients:");
            System.out.println("+-----------+----------------------+-----+--------+");
            System.out.println("| PatientID | Name                 | Age | Gender |");
            System.out.println("+-----------+----------------------+-----+--------+");

            boolean found = false;
            while (resultSet.next()) {
                found = true;
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String gender = resultSet.getString("gender");
                System.out.printf("| %-9d | %-20s | %-3d | %-6s |\n", id, name, age, gender);
            }

            if (!found) {
                System.out.println("| No patients found.                                 |");
            }

            System.out.println("+-----------+----------------------+-----+--------+");

        } catch (SQLException e) {
            System.err.println("❌ Database error while fetching patients: " + e.getMessage());
        }
    }

    public boolean getPatientById(int id) {
        String query = "SELECT id FROM patients WHERE id = ?";

        // Try-with-resources auto-closes PreparedStatement and ResultSet
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next(); // Returns true if record exists
            }
        } catch (SQLException e) {
            System.err.println("❌ Database error checking patient existence: " + e.getMessage());
        }
        return false;
    }
}