package de.hochschule.studentenverwaltung.service;

/**
 * Service-Klasse für die Geschäftslogik der Studentenverwaltung.
 * 
 * Diese Klasse implementiert das Service-Pattern und fungiert als
 * Vermittler zwischen der Präsentationsschicht und der Datenzugriffsschicht.
 * Sie enthält die Geschäftslogik der Anwendung und koordiniert
 * die Operationen zwischen DTOs und Entities.
 * 
 * @author Team
 * @version 2.0
 */
import de.hochschule.studentenverwaltung.entity.Student;
import de.hochschule.studentenverwaltung.dto.StudentDto;
import de.hochschule.studentenverwaltung.mapper.StudentMapper;
import de.hochschule.studentenverwaltung.repository.StudentRepository;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StudentService {
	
	private static final Logger logger = LoggerFactory.getLogger(StudentService.class);
	
    /** Das Repository für den Datenzugriff */
    private final StudentRepository studentRepository;

    /**
     * Konstruktor für StudentService.
     * 
     * @param studentRepository das Repository für den Datenzugriff
     */
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    /**
     * Ruft alle Studenten ab und konvertiert sie zu DTOs.
     * 
     * @return eine Liste aller StudentDto-Objekte
     */
    public List<StudentDto> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(StudentMapper::mapToStudentDto)
                .collect(Collectors.toList());
    }

    /**
     * Erstellt einen neuen Studenten.
     * 
     * Konvertiert das DTO zu einer Entity und speichert es über das Repository.
     * 
     * @param studentDto das StudentDto mit den Daten des neuen Studenten
     */
    public void createStudent(StudentDto studentDto) {
        Student student = StudentMapper.mapToStudent(studentDto);
        studentRepository.save(student);
        // Setze die ID im DTO zurück
        studentDto.setId(student.getId()); //  Wichtig!
        logger.info("✅ Neuer Student erstellt mit ID: {}", student.getId());
    }

    /**
     * Ruft einen Studenten anhand seiner ID ab.
     * 
     * @param studentId die ID des gesuchten Studenten
     * @return das StudentDto-Objekt oder null, wenn nicht gefunden
     */
    public StudentDto getStudentById(Long studentId) {
        Student student = studentRepository.findById(studentId);
        return student != null ? StudentMapper.mapToStudentDto(student) : null;
    }

    /**
     * Aktualisiert die Daten eines bestehenden Studenten.
     * 
     * Konvertiert das DTO zu einer Entity und speichert die Änderungen
     * über das Repository.
     * 
     * @param studentDto das StudentDto mit den aktualisierten Daten
     */
    public void updateStudent(StudentDto studentDto) {
        Student student = StudentMapper.mapToStudent(studentDto);
        studentRepository.save(student);
    }

    /**
     * Löscht einen Studenten anhand seiner ID.
     * 
     * @param studentId die ID des zu löschenden Studenten
     */
    public boolean deleteStudent(Long studentId) {
        return studentRepository.deleteById(studentId);
    }
}
