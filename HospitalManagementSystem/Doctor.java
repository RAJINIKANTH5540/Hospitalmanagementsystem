package HospitalManagementSystem;

import java.sql.*;
import java.util.Scanner;

public class Doctor {
    private final Connection connection;
    private final Scanner scanner;

    public Doctor(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public void viewDoctor() {
        String query = "SELECT * FROM doctors";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            System.out.println("\nDoctors:");
            System.out.println("+-----------+----------------------+---------------------+");
            System.out.println("| Doctor ID | Name                 | Specialization      |");
            System.out.println("+-----------+----------------------+---------------------+");

            boolean found = false;
            while (resultSet.next()) {
                found = true;
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String specialization = resultSet.getString("specialization");
                System.out.printf("| %-9d | %-20s | %-19s |\n", id, name, specialization);
            }

            if (!found) {
                System.out.println("| No doctors found in the records.                        |");
            }

            System.out.println("+-----------+----------------------+---------------------+");

        } catch (SQLException e) {
            System.err.println("❌ Database error while fetching doctors: " + e.getMessage());
        }
    }

    public boolean getDoctorById(int id) {
        String query = "SELECT id FROM doctors WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            System.err.println("❌ Database error checking doctor existence: " + e.getMessage());
        }
        return false;
    }
}