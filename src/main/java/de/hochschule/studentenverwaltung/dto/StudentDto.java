package de.hochschule.studentenverwaltung.dto;

/**
 * Data Transfer Object (DTO) für Student-Daten.
 * 
 * Diese Klasse dient als Datenobjekt für den Transfer von Studenteninformationen
 * zwischen verschiedenen Schichten der Anwendung. DTOs helfen dabei, die
 * Kopplung zwischen den Schichten zu reduzieren und bieten eine saubere
 * Abstraktion für Datenübertragungen.
 * 
 * @author Studentenverwaltungssystem Team
 * @version 1.0
 */
public class StudentDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;

    /**
     * Standard-Konstruktor für StudentDto.
     * Erstellt ein leeres StudentDto-Objekt.
     */
    public StudentDto() {}

    /**
     * Parametrisierter Konstruktor für StudentDto.
     * 
     * @param id die eindeutige Kennung des Studenten
     * @param firstName der Vorname des Studenten
     * @param lastName der Nachname des Studenten
     * @param email die E-Mail-Adresse des Studenten
     */
    public StudentDto(Long id, String firstName, String lastName, String email) {
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
}