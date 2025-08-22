package de.hochschule.studentenverwaltung.service;

import de.hochschule.studentenverwaltung.dto.StudentDto;
import de.hochschule.studentenverwaltung.repository.StudentRepository;
import org.junit.jupiter.api.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testklasse für StudentService.
 * Testet die Business-Logik, die auf StudentRepository aufbaut.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StudentServiceTest {

    private StudentService studentService;
    private StudentRepository repository;

    @BeforeEach
    void setUp() {
        // Neue Repository-Instanz für jeden Test (mit H2-In-Memory-DB)
        repository = new StudentRepository();
        // Service erhält das frische Repository
        studentService = new StudentService(repository);
    }

    @Test
    @Order(1)
    @DisplayName("Sollte leere Liste zurückgeben, wenn keine Studenten vorhanden sind")
    void getAllStudents_shouldReturnEmptyList_whenNoStudents() {
        // Act
        List<StudentDto> students = studentService.getAllStudents();

        // Assert
        assertNotNull(students);
        assertTrue(students.isEmpty());
    }

    @Test
    @Order(2)
    @DisplayName("Sollte neuen Studenten anlegen und in der Liste finden")
    void createStudent_shouldPersistAndBeVisibleInList() {
        // Arrange
        StudentDto studentDto = new StudentDto(null, "Max", "Mustermann", "max@example.com");

        // Act
        studentService.createStudent(studentDto);
        List<StudentDto> students = studentService.getAllStudents();

        // Assert
        assertFalse(students.isEmpty());
        assertEquals(1, students.size());
        StudentDto found = students.get(0);
        assertNotNull(found.getId());
        assertEquals("Max", found.getFirstName());
        assertEquals("Mustermann", found.getLastName());
        assertEquals("max@example.com", found.getEmail());
    }

    @Test
    @Order(3)
    @DisplayName("Sollte Student über ID finden")
    void getStudentById_shouldReturnStudent_whenExists() {
        // Arrange
        StudentDto studentDto = new StudentDto(null, "Erika", "Muster", "erika@example.com");
        studentService.createStudent(studentDto);

        // Act
        StudentDto found = studentService.getStudentById(studentDto.getId());

        // Assert
        assertNotNull(found);
        assertEquals(studentDto.getId(), found.getId());
        assertEquals("Erika", found.getFirstName());
    }

    @Test
    @Order(4)
    @DisplayName("Sollte null zurückgeben, wenn Student nicht existiert")
    void getStudentById_shouldReturnNull_whenNotFound() {
        // Act
        StudentDto found = studentService.getStudentById(999L);

        // Assert
        assertNull(found);
    }

    @Test
    @Order(5)
    @DisplayName("Sollte Student aktualisieren")
    void updateStudent_shouldUpdateExistingStudent() {
        // Arrange
        StudentDto studentDto = new StudentDto(null, "Ali", "Baddah", "ali@example.com");
        studentService.createStudent(studentDto);

        // Ändere Daten
        studentDto.setFirstName("Ali (geändert)");
        studentDto.setEmail("ali.geaendert@example.com");

        // Act
        studentService.updateStudent(studentDto);
        StudentDto updated = studentService.getStudentById(studentDto.getId());

        // Assert
        assertNotNull(updated);
        assertEquals("Ali (geändert)", updated.getFirstName());
        assertEquals("ali.geaendert@example.com", updated.getEmail());
    }

    @Test
    @Order(6)
    @DisplayName("Sollte Student löschen")
    void deleteStudent_shouldRemoveStudent() {
        // Arrange
        StudentDto studentDto = new StudentDto(null, "Lösch", "Test", "loesch@example.com");
        studentService.createStudent(studentDto);

        // Act
        studentService.deleteStudent(studentDto.getId());
        List<StudentDto> remaining = studentService.getAllStudents();

        // Assert
        assertEquals(0, remaining.size());
        assertNull(studentService.getStudentById(studentDto.getId()));
    }
}