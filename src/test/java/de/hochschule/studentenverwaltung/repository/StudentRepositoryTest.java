package de.hochschule.studentenverwaltung.repository;

import de.hochschule.studentenverwaltung.entity.Student;
import org.junit.jupiter.api.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testklasse für StudentRepository.
 * Nutzt eine H2-In-Memory-Datenbank für isolierte Tests.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StudentRepositoryTest {

    private StudentRepository repository;

    @BeforeEach
    void setUp() {
        // Jeder Test bekommt eine neue, frische Repository-Instanz
        repository = new StudentRepository();
    }

    @Test
    @Order(1)
    @DisplayName("Sollte eine leere Liste zurückgeben, wenn keine Studenten vorhanden sind")
    void findAll_shouldReturnEmptyList_whenNoStudents() {
        // Arrange & Act
        List<Student> students = repository.findAll();

        // Assert
        assertNotNull(students);
        assertTrue(students.isEmpty());
        assertEquals(0, students.size());
    }

    @Test
    @Order(2)
    @DisplayName("Sollte Student speichern und ID generieren")
    void save_shouldPersistStudent_andGenerateId() {
        // Arrange
        Student student = new Student();
        student.setFirstName("Max");
        student.setLastName("Mustermann");
        student.setEmail("max@example.com");

        // Act
        repository.save(student);

        // Assert
        assertNotNull(student.getId());
        assertTrue(student.getId() > 0);
    }

    @Test
    @Order(3)
    @DisplayName("Sollte gespeicherten Student über findAll() finden")
    void findAll_shouldReturnSavedStudent() {
        // Arrange
        Student student = new Student();
        student.setFirstName("Erika");
        student.setLastName("Muster");
        student.setEmail("erika@example.com");

        repository.save(student);

        // Act
        List<Student> students = repository.findAll();

        // Assert
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
        // Arrange
        Student student = new Student();
        student.setFirstName("Ali");
        student.setLastName("Baddah");
        student.setEmail("ali@example.com");
        repository.save(student);

        // Act
        Student found = repository.findById(student.getId());

        // Assert
        assertNotNull(found);
        assertEquals(student.getId(), found.getId());
        assertEquals("Ali", found.getFirstName());
    }

    @Test
    @Order(5)
    @DisplayName("Sollte null zurückgeben, wenn Student nicht existiert")
    void findById_shouldReturnNull_whenNotExists() {
        // Act
        Student found = repository.findById(999L);

        // Assert
        assertNull(found);
    }

    @Test
    @Order(6)
    @DisplayName("Sollte Student erfolgreich löschen")
    void deleteById_shouldRemoveStudent() {
        // Arrange
        Student student = new Student();
        student.setFirstName("Lösch");
        student.setLastName("Test");
        student.setEmail("loesch@example.com");
        repository.save(student);

        // Act
        boolean deleted = repository.deleteById(student.getId());
        List<Student> remaining = repository.findAll();

        // Assert
        assertTrue(deleted);
        assertEquals(0, remaining.size());
    }
}
