// Emanuele Contini, matricola 756441
// Emanuele Gobessi, matricola 757599
// Diego Guidi, matricola 758420
// Nicola Curchi, matricola 757786
// Mirko Gurzau, matricola 757925

package com.lab_lib.frontend.Services;

import javafx.beans.property.SimpleStringProperty;

/**
 * Classe Book rappresenta un libro con proprietà osservabili per titolo e autore.
 * Utilizza SimpleStringProperty per permettere il binding con interfacce JavaFX.
 */
public class Book {

    // Proprietà osservabili per titolo e autore del libro
    private final SimpleStringProperty titolo;
    private final SimpleStringProperty autore;

    /**
     * Costruttore della classe Book.
     * Inizializza le proprietà titolo e autore.
     * 
     * @param titolo Il titolo del libro
     * @param autore L'autore del libro
     */
    public Book(String titolo, String autore) {
        this.titolo = new SimpleStringProperty(titolo);
        this.autore = new SimpleStringProperty(autore);
    }

    // Getter e Setter per la proprietà titolo

    /**
     * Ottiene il valore corrente del titolo.
     * 
     * @return titolo del libro
     */
    public String getTitolo() {
        return titolo.get();
    }

    /**
     * Imposta un nuovo valore per il titolo.
     * 
     * @param value nuovo titolo del libro
     */
    public void setTitolo(String value) {
        titolo.set(value);
    }

    // Getter e Setter per la proprietà autore

    /**
     * Ottiene il valore corrente dell'autore.
     * 
     * @return autore del libro
     */
    public String getAutore() {
        return autore.get();
    }

    /**
     * Imposta un nuovo valore per l'autore.
     * 
     * @param value nuovo autore del libro
     */
    public void setAutore(String value) {
        autore.set(value);
    }

    // Metodi per ottenere le proprietà osservabili (utile per binding in JavaFX)

    /**
     * Restituisce la proprietà osservabile del titolo.
     * 
     * @return proprietà titolo
     */
    public SimpleStringProperty titoloProperty() {
        return titolo;
    }

    /**
     * Restituisce la proprietà osservabile dell'autore.
     * 
     * @return proprietà autore
     */
    public SimpleStringProperty autoreProperty() {
        return autore;
    }

}
