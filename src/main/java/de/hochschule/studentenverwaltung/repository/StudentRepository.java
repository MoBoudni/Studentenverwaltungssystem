package de.hochschule.studentenverwaltung.repository;

/**
 * Repository-Klasse für Student-Datenzugriff.
 * 
 * Diese Klasse implementiert das Repository-Pattern und ist verantwortlich
 * für alle datenbankbezogenen Operationen mit Student-Entitäten.
 * Sie verwendet eine In-Memory H2-Datenbank für die Datenpersistierung
 * und stellt CRUD-Operationen (Create, Read, Update, Delete) bereit.
 * 
 * @author Studentenverwaltungssystem Team
 * @version 1.0
 */

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import de.hochschule.studentenverwaltung.entity.Student;

public class StudentRepository {
    /** JDBC-URL für die H2 In-Memory-Datenbank */
    private final String jdbcUrl = "jdbc:h2:mem:testdb";
    /** Datenbankbenutzername */
    private final String username = "sa";
    /** Datenbankpasswort */
    private final String password = "";

    /**
     * Konstruktor für StudentRepository.
     * Initialisiert das Repository und erstellt die Datenbanktabelle falls nötig.
     */
    public StudentRepository() {
        createTableIfNotExists();
    }

    /**
     * Erstellt die Students-Tabelle falls sie noch nicht existiert.
     * 
     * Diese Methode wird beim Initialisieren des Repository aufgerufen
     * und stellt sicher, dass die erforderliche Datenbankstruktur vorhanden ist.
     */
    private void createTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS students (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                "first_name VARCHAR(100) NOT NULL, " +
                "last_name VARCHAR(100) NOT NULL, " +
                "email VARCHAR(100) NOT NULL UNIQUE" +
                ")";

        try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Ruft alle Studenten aus der Datenbank ab.
     * 
     * @return eine Liste aller Student-Objekte in der Datenbank
     */
    public List<Student> findAll() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students";

        try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Student student = new Student(
                        rs.getLong("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email")
                );
                students.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    /**
     * Speichert einen Studenten in der Datenbank.
     * 
     * Wenn die Student-ID null ist, wird ein neuer Datensatz eingefügt.
     * Andernfalls wird der bestehende Datensatz aktualisiert.
     * 
     * @param student das zu speichernde Student-Objekt
     */
    public void save(Student student) {
        String sql;
        if (student.getId() == null) {
            sql = "INSERT INTO students (first_name, last_name, email) VALUES (?, ?, ?)";
        } else {
            sql = "UPDATE students SET first_name = ?, last_name = ?, email = ? WHERE id = ?";
        }

        try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, student.getFirstName());
            pstmt.setString(2, student.getLastName());
            pstmt.setString(3, student.getEmail());

            if (student.getId() != null) {
                // Update bestehender Datensatz
                pstmt.setLong(4, student.getId());
                pstmt.executeUpdate();
            } else {
                // Neuen Datensatz einfügen und generierte ID setzen
                pstmt.executeUpdate();
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        student.setId(generatedKeys.getLong(1));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sucht einen Studenten anhand seiner ID.
     * 
     * @param id die ID des gesuchten Studenten
     * @return das Student-Objekt oder null, wenn nicht gefunden
     */
    public Student findById(Long id) {
        String sql = "SELECT * FROM students WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Student(
                            rs.getLong("id"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("email")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Löscht einen Studenten anhand seiner ID.
     * 
     * @param id die ID des zu löschenden Studenten
     */
    public void deleteById(Long id) {
        String sql = "DELETE FROM students WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
