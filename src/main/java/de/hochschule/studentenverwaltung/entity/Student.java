package de.hochschule.studentenverwaltung.entity;

/**
 * Student Entity Klasse
 * 
 * Repräsentiert einen Studenten im Studentenverwaltungssystem.
 * Dies ist ein einfaches POJO (Plain Old Java Object), das
 * Studentendaten mit ordnungsgemäßer Kapselung durch Getter und Setter kapselt.
 * 
 * @author Studentenverwaltungssystem Team
 * @version 1.0
 */
public class Student {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;

    /**
     * Standard-Konstruktor für Student.
     * Erstellt ein leeres Student-Objekt.
     */
    public Student() {}

    /**
     * Parametrisierter Konstruktor für Student.
     * 
     * @param id die eindeutige Kennung des Studenten
     * @param firstName der Vorname des Studenten
     * @param lastName der Nachname des Studenten
     * @param email die E-Mail-Adresse des Studenten
     */
    public Student(Long id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    /**
     * Gibt die eindeutige Kennung des Studenten zurück.
     * 
     * @return die ID des Studenten
     */
    public Long getId() {
        return id;
    }

    /**
     * Setzt die eindeutige Kennung des Studenten.
     * 
     * @param id die zu setzende ID des Studenten
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gibt den Vornamen des Studenten zurück.
     * 
     * @return der Vorname des Studenten
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Setzt den Vornamen des Studenten.
     * 
     * @param firstName der zu setzende Vorname
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gibt den Nachnamen des Studenten zurück.
     * 
     * @return der Nachname des Studenten
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Setzt den Nachnamen des Studenten.
     * 
     * @param lastName der zu setzende Nachname
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gibt die E-Mail-Adresse des Studenten zurück.
     * 
     * @return die E-Mail-Adresse des Studenten
     */
    public String getEmail() {
        return email;
    }

    /**
     * Setzt die E-Mail-Adresse des Studenten.
     * 
     * @param email die zu setzende E-Mail-Adresse
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gibt eine String-Darstellung des Student-Objekts zurück.
     * 
     * @return ein String mit allen Studenteninformationen
     */
    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}