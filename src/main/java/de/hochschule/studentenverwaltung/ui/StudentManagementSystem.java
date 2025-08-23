package de.hochschule.studentenverwaltung.ui;

/**
 * Haupt-Anwendungsklasse für das Studentenverwaltungssystem.
 * 
 * Diese Klasse stellt eine konsolenbasierte Benutzeroberfläche für die 
 * Verwaltung von Studenten bereit. Sie bietet Funktionen zum Erstellen, 
 * Lesen, Aktualisieren und Löschen von Studentendatensätzen.
 * Die Anwendung folgt einem geschichteten Architekturmuster mit klarer
 * Trennung der Verantwortlichkeiten.
 * 
 * @author Team
 * @version 2.0
 */

import de.hochschule.studentenverwaltung.dto.StudentDto;
import de.hochschule.studentenverwaltung.repository.StudentRepository;
import de.hochschule.studentenverwaltung.service.StudentService;

import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StudentManagementSystem {
    
    private static final Logger logger = LoggerFactory.getLogger(StudentManagementSystem.class);
    
    /**
     * Haupt-Einstiegspunkt der Anwendung.
     * Initialisiert das System und startet die Benutzeroberflächen-Schleife.
     * 
     * @param args Kommandozeilenargumente (werden nicht verwendet)
     */
    public static void main(String[] args) {
        logger.info("🚀 Studentenverwaltungssystem wird gestartet...");
        
        Scanner scanner = new Scanner(System.in);
        StudentService studentService = new StudentService(new StudentRepository());

        logger.info("StudentService und Repository wurden initialisiert.");

        while (true) {
            displayMainMenu();
            
            int choice;
            try {
                choice = scanner.nextInt();
            } catch (Exception e) {
                logger.warn("Ungültige Eingabe: keine Zahl eingegeben.");
                System.out.println("Bitte eine Zahl eingeben.");
                scanner.nextLine(); // Puffer leeren
                continue;
            }
            scanner.nextLine(); // Zeilenumbruchzeichen konsumieren

            logger.info("Benutzer wählt Option: {}", choice);

            switch (choice) {
                case 1:
                    listStudents(studentService);
                    break;
                case 2:
                    addStudent(scanner, studentService);
                    break;
                case 3:
                    editStudent(scanner, studentService);
                    break;
                case 4:
                    deleteStudent(scanner, studentService);
                    break;
                case 5:
                    viewStudent(scanner, studentService);
                    break;
                case 6:
                    logger.info("Benutzer beendet das Programm.");
                    System.out.println("Auf Wiedersehen!");
                    return;
                default:
                    logger.warn("Ungültige Option eingegeben: {}", choice);
                    System.out.println("Ungültige Option. Versuchen Sie es erneut.");
            }
        }
    }

    /**
     * Zeigt die Hauptmenü-Optionen dem Benutzer an.
     */
    private static void displayMainMenu() {
        System.out.println("\n=== Studentenverwaltungssystem ===");
        System.out.println("1. Alle Studenten auflisten");
        System.out.println("2. Neuen Studenten hinzufügen");
        System.out.println("3. Student bearbeiten");
        System.out.println("4. Student löschen");
        System.out.println("5. Student anzeigen");
        System.out.println("6. Beenden");
        System.out.print("Wählen Sie eine Option: ");
    }

    /**
     * Listet alle Studenten im System auf.
     * Zeigt eine formatierte Liste aller Studenten mit ihren wichtigsten Informationen.
     * 
     * @param service der StudentService für den Datenzugriff
     */
    private static void listStudents(StudentService service) {
        logger.info("Benutzer listet alle Studenten auf.");
        List<StudentDto> students = service.getAllStudents();
        System.out.println("\n--- Studenten ---");
        if (students.isEmpty()) {
            System.out.println("Keine Studenten gefunden.");
        } else {
            students.forEach(s -> System.out.println(s.getId() + ". " + s.getFirstName() + " " + s.getLastName() + " (" + s.getEmail() + ")"));
            logger.info("{} Studenten angezeigt.", students.size());
        }
    }

    /**
     * Fügt einen neuen Studenten zum System hinzu.
     * Führt den Benutzer durch die Eingabe der erforderlichen Studenteninformationen.
     * 
     * @param scanner der Scanner für Benutzereingaben
     * @param service der StudentService für Datenoperationen
     */
    private static void addStudent(Scanner scanner, StudentService service) {
        System.out.print("Vorname: ");
        String firstName = scanner.nextLine();
        System.out.print("Nachname: ");
        String lastName = scanner.nextLine();
        System.out.print("E-Mail: ");
        String email = scanner.nextLine();

        StudentDto student = new StudentDto(null, firstName, lastName, email);
        service.createStudent(student);
        logger.info("Neuer Student hinzugefügt: {} {}, E-Mail: {}", firstName, lastName, email);
        System.out.println("Student erfolgreich hinzugefügt.");
    }

    /**
     * Bearbeitet die Informationen eines bestehenden Studenten.
     * Erlaubt dem Benutzer, einzelne Felder zu ändern oder leer zu lassen,
     * um die vorhandenen Werte zu behalten.
     * 
     * @param scanner der Scanner für Benutzereingaben
     * @param service der StudentService für Datenoperationen
     */
    private static void editStudent(Scanner scanner, StudentService service) {
        System.out.print("Geben Sie die ID des zu bearbeitenden Studenten ein: ");
        Long id;
        try {
            id = scanner.nextLong();
        } catch (Exception e) {
            logger.warn("Ungültige ID-Eingabe beim Bearbeiten.");
            System.out.println("Bitte eine gültige Zahl eingeben.");
            scanner.nextLine();
            return;
        }
        scanner.nextLine();

        logger.info("Bearbeitung angefordert für Student mit ID: {}", id);
        StudentDto student = service.getStudentById(id);
        if (student == null) {
            logger.warn("Bearbeitungsversuch für nicht existierenden Student mit ID: {}", id);
            System.out.println("Student nicht gefunden.");
            return;
        }

        System.out.print("Neuer Vorname (" + student.getFirstName() + "): ");
        String firstName = scanner.nextLine();
        if (firstName.isEmpty()) firstName = student.getFirstName();

        System.out.print("Neuer Nachname (" + student.getLastName() + "): ");
        String lastName = scanner.nextLine();
        if (lastName.isEmpty()) lastName = student.getLastName();

        System.out.print("Neue E-Mail (" + student.getEmail() + "): ");
        String email = scanner.nextLine();
        if (email.isEmpty()) email = student.getEmail();

        student.setFirstName(firstName);
        student.setLastName(lastName);
        student.setEmail(email);

        service.updateStudent(student);
        logger.info("Student mit ID {} aktualisiert: {} {}, E-Mail: {}", id, firstName, lastName, email);
        System.out.println("Student aktualisiert.");
    }

    /**
     * Löscht einen Studenten aus dem System.
     * Fordert den Benutzer zur Eingabe der Student-ID auf und entfernt den Datensatz.
     * 
     * @param scanner der Scanner für Benutzereingaben
     * @param service der StudentService für Datenoperationen
     */
    private static void deleteStudent(Scanner scanner, StudentService service) {
        System.out.print("Geben Sie die ID des zu löschenden Studenten ein: ");
        Long id;
        try {
            id = scanner.nextLong();
        } catch (Exception e) {
            logger.warn("Ungültige ID-Eingabe beim Löschen.");
            System.out.println("Bitte eine gültige Zahl eingeben.");
            scanner.nextLine();
            return;
        }
        scanner.nextLine();

        logger.info("Löschvorgang gestartet für Student mit ID: {}", id);
        boolean deleted = service.deleteStudent(id);
        if (deleted) {
            logger.info("Student mit ID {} erfolgreich gelöscht.", id);
            System.out.println("Student gelöscht.");
        } else {
            logger.warn("Versuch, nicht existierenden Student mit ID {} zu löschen.", id);
            System.out.println("Student nicht gefunden.");
        }
    }

    /**
     * Zeigt detaillierte Informationen über einen bestimmten Studenten an.
     * Sucht nach der Student-ID und zeigt alle verfügbaren Informationen an.
     * 
     * @param scanner der Scanner für Benutzereingaben
     * @param service der StudentService für Datenoperationen
     */
    private static void viewStudent(Scanner scanner, StudentService service) {
        System.out.print("Geben Sie die ID des anzuzeigenden Studenten ein: ");
        Long id;
        try {
            id = scanner.nextLong();
        } catch (Exception e) {
            logger.warn("Ungültige ID-Eingabe beim Anzeigen.");
            System.out.println("Bitte eine gültige Zahl eingeben.");
            scanner.nextLine();
            return;
        }
        scanner.nextLine();

        logger.info("Details angefordert für Student mit ID: {}", id);
        StudentDto student = service.getStudentById(id);
        if (student == null) {
            logger.warn("Anzeigeanfrage für nicht existierenden Student mit ID: {}", id);
            System.out.println("Student nicht gefunden.");
        } else {
            System.out.println("\n--- Studentendetails ---");
            System.out.println("ID: " + student.getId());
            System.out.println("Name: " + student.getFirstName() + " " + student.getLastName());
            System.out.println("E-Mail: " + student.getEmail());
            logger.info("Details für Student ID {} angezeigt.", id);
        }
    }
}

