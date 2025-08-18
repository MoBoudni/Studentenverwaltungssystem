package de.hochschule.studentenverwaltung.mapper;

/**
 * Mapper-Klasse für die Konvertierung zwischen Student-Entity und StudentDto.
 * 
 * Diese Utility-Klasse stellt statische Methoden zur Verfügung, um zwischen
 * der Entity-Klasse (Student) und dem Data Transfer Object (StudentDto)
 * zu konvertieren. Dies folgt dem Mapper-Pattern und hilft dabei, eine
 * klare Trennung zwischen den verschiedenen Darstellungen der Daten
 * aufrechtzuerhalten.
 * 
 * @author Team
 * @version 2.0
 */


import de.hochschule.studentenverwaltung.entity.Student;
import de.hochschule.studentenverwaltung.dto.StudentDto;

public class StudentMapper {
    
    /**
     * Konvertiert ein Student-Entity-Objekt in ein StudentDto-Objekt.
     * 
     * Diese Methode nimmt eine Student-Entity und erstellt daraus ein
     * entsprechendes DTO-Objekt für den Transfer zwischen den Schichten.
     * 
     * @param student das zu konvertierende Student-Entity-Objekt
     * @return das entsprechende StudentDto-Objekt
     * @throws NullPointerException wenn der student-Parameter null ist
     */
    public static StudentDto mapToStudentDto(Student student) {
        return new StudentDto(
                student.getId(),
                student.getFirstName(),
                student.getLastName(),
                student.getEmail()
        );
    }

    /**
     * Konvertiert ein StudentDto-Objekt in ein Student-Entity-Objekt.
     * 
     * Diese Methode nimmt ein DTO und erstellt daraus eine entsprechende
     * Entity für die Persistierung in der Datenbank.
     * 
     * @param studentDto das zu konvertierende StudentDto-Objekt
     * @return das entsprechende Student-Entity-Objekt
     * @throws NullPointerException wenn der studentDto-Parameter null ist
     */
    public static Student mapToStudent(StudentDto studentDto) {
        return new Student(
                studentDto.getId(),
                studentDto.getFirstName(),
                studentDto.getLastName(),
                studentDto.getEmail()
        );
    }
}
