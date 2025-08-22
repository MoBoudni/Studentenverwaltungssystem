package de.hochschule.studentenverwaltung.repository;

/**
 * Repository-Klasse für Student-Datenzugriff.
 * 
 * Diese Klasse implementiert das Repository-Pattern und ist verantwortlich
 * für alle datenbankbezogenen Operationen mit Student-Entitäten.
 * Sie verwendet eine In-Memory H2-Datenbank für die Datenpersistierung
 * und stellt CRUD-Operationen (Create, Read, Update, Delete) bereit.
 * 
 * @author Team
 * @version 2.0
 */

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import de.hochschule.studentenverwaltung.entity.Student;

public class StudentRepository {
    /** JDBC-URL für die H2 In-Memory-Datenbank */
    private final String jdbcUrl = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1"; // ; DB_CLOSE_ON_EXIT=FALSE";
    /** Datenbankbenutzername */
    private final String username = "sa";
    /** Datenbankpasswort */
    private final String password = "";

    /**
     * Konstruktor für StudentRepository.
     * Initialisiert das Repository und erstellt die Datenbanktabelle falls nötig.
     */
    public StudentRepository() {
    	System.out.println("Initialisiere StudentRepository und erstelle Tabelle...");
        createTableIfNotExists();
        debugCheckTableExists(); // ← Prüfung hinzufügen
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
            System.out.println("✅ Tabelle 'students' wurde erfolgreich erstellt oder existiert bereits.");
        } catch (SQLException e) {
            System.err.println("❌ FEHLER beim Erstellen der Tabelle 'students': " + e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * Ruft alle Studenten aus der Datenbank ab.
     * 
     * @return eine Liste aller Student-Objekte in der Datenbank
     */
    public List<Student> findAll() {
    	System.out.println("📊 Lese Studenten aus DB...");
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
            System.out.println("🔍 " + students.size() + " Student(en) aus der DB geladen.");
        } catch (SQLException e) {
        	 System.err.println("❌ Fehler beim Lesen aller Studenten: " + e.getMessage());
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
            System.out.println("📥 Student gespeichert: " + student.getFirstName() + " " + student.getLastName());
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
    public boolean deleteById(Long id) {
        String sql = "DELETE FROM students WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0; // true, wenn mindestens eine Zeile gelöscht wurde
        } catch (SQLException e) {
            System.err.println("❌ Fehler beim Löschen des Students mit ID " + id + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }    
    private void debugCheckTableExists() {
        String sql = "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'students'";
        try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                System.out.println("🔍 Tabelle 'students' existiert.");
            } else {
                System.out.println("❌ Tabelle 'students' existiert NICHT!");
            }
        } catch (SQLException e) {
            System.err.println("❌ Fehler bei Tabellenprüfung: " + e.getMessage());
        }
    }
}
