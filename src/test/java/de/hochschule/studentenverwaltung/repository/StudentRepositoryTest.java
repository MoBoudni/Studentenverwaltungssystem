package de.hochschule.studentenverwaltung.repository;

import de.hochschule.studentenverwaltung.entity.Student;
import org.junit.jupiter.api.*;
import java.sql.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

// Logging
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Testklasse für StudentRepository.
 * Nutzt eine H2-In-Memory-Datenbank für isolierte Tests.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StudentRepositoryTest {

    private static final Logger logger = LoggerFactory.getLogger(StudentRepositoryTest.class);

    private StudentRepository repository;

    @BeforeEach
    void setUp() {
        repository = new StudentRepository();
    }

    @AfterEach
    void tearDown() {
        String jdbcUrl = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";
        try (Connection conn = DriverManager.getConnection(jdbcUrl, "sa", "");
             Statement stmt = conn.createStatement()) {

            stmt.execute("DELETE FROM students");
            stmt.execute("ALTER TABLE students ALTER COLUMN id RESTART WITH 1");
            logger.info("🧹 Tabelle 'students' geleert und ID-Zähler zurückgesetzt.");

        } catch (SQLException e) {
            logger.error("❌ Fehler beim Leeren der Tabelle: {}", e.getMessage(), e);
        }
    }

    @Test
    @Order(1)
    @DisplayName("Sollte eine leere Liste zurückgeben, wenn keine Studenten vorhanden sind")
    void findAll_shouldReturnEmptyList_whenNoStudents() {
        List<Student> students = repository.findAll();

        assertNotNull(students);
        assertTrue(students.isEmpty());
        assertEquals(0, students.size());
    }

    @Test
    @Order(2)
    @DisplayName("Sollte Student speichern und ID generieren")
    void save_shouldPersistStudent_andGenerateId() {
        Student student = new Student();
        student.setFirstName("Max");
        student.setLastName("Mustermann");
        student.setEmail("max@example.com");

        repository.save(student);

        assertNotNull(student.getId());
        assertTrue(student.getId() > 0);
    }

    @Test
    @Order(3)
    @DisplayName("Sollte gespeicherten Student über findAll() finden")
    void findAll_shouldReturnSavedStudent() {
        Student student = new Student();
        student.setFirstName("Erika");
        student.setLastName("Muster");
        student.setEmail("erika@example.com");

        repository.save(student);

        List<Student> students = repository.findAll();

        assertFalse(students.isEmpty());
        assertEquals(1, students.size());
        Student found = students.get(0);
        assertEquals("Erika", found.getFirstName());
        assertEquals("Muster", found.getLastName());
        assertEquals("erika@example.com", found.getEmail());
    }

    @Test
    @Order(4)
    @DisplayName("Sollte Student über ID finden")
    void findById_shouldReturnStudent_whenExists() {
        Student student = new Student();
        student.setFirstName("Ali");
        student.setLastName("Baddah");
        student.setEmail("ali@example.com");
        repository.save(student);

        Student found = repository.findById(student.getId());

        assertNotNull(found);
        assertEquals(student.getId(), found.getId());
        assertEquals("Ali", found.getFirstName());
    }

    @Test
    @Order(5)
    @DisplayName("Sollte null zurückgeben, wenn Student nicht existiert")
    void findById_shouldReturnNull_whenNotExists() {
        Student found = repository.findById(999L);
        assertNull(found);
    }

    @Test
    @Order(6)
    @DisplayName("Sollte Student erfolgreich löschen")
    void deleteById_shouldRemoveStudent() {
        Student student = new Student();
        student.setFirstName("Lösch");
        student.setLastName("Test");
        student.setEmail("loesch@example.com");
        repository.save(student);

        boolean deleted = repository.deleteById(student.getId());
        List<Student> remaining = repository.findAll();

        assertTrue(deleted);
        assertEquals(0, remaining.size());
    }
}