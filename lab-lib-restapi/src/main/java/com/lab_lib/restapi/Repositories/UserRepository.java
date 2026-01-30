// Emanuele Contini, matricola 756441
// Emanuele Gobessi, matricola 757599
// Diego Guidi, matricola 758420
// Nicola Curchi, matricola 757786
// Mirko Gurzau, matricola 757925

package com.lab_lib.restapi.Repositories;

import com.lab_lib.restapi.Models.AppUser;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository JPA per l'entità {@link com.lab_lib.restapi.Models.AppUser}.
 *
 * <p>Fornisce metodi per verificare l'esistenza di utenti tramite email,
 * nickname o token, e metodi di lookup per recuperare utenti tramite token,
 * email o nickname.
 */
@Repository
public interface UserRepository extends JpaRepository<AppUser, Long>{
    /**
     * Verifica se esiste un utente con la email fornita.
     *
     * @param email indirizzo email da verificare
     * @return {@code true} se esiste un utente con quella email, {@code false} altrimenti
     */
    boolean existsByEmail(String email);

    /**
     * Verifica se esiste un utente con il nickname fornito.
     *
     * @param nickname nickname da verificare
     * @return {@code true} se il nickname è già in uso, {@code false} altrimenti
     */
    boolean existsByNickname(String nickname);

    /**
     * Verifica se esiste un utente associato al token specificato.
     *
     * @param token token UUID dell'utente
     * @return {@code true} se esiste un utente con quel token, {@code false} altrimenti
     */
    boolean existsByToken(UUID token);

    /**
     * Recupera l'utente associato al token fornito.
     *
     * @param token token UUID dell'utente
     * @return istanza di {@link AppUser} corrispondente al token, o {@code null} se non trovata
     */
    AppUser findByToken(UUID token);

    /**
     * Recupera l'utente associato all'email fornita.
     *
     * @param email indirizzo email
     * @return istanza di {@link AppUser} o {@code null} se non trovata
     */
    AppUser findByEmail(String email);

    /**
     * Recupera l'utente associato al nickname fornito.
     *
     * @param nickname nickname dell'utente
     * @return istanza di {@link AppUser} o {@code null} se non trovata
     */
    AppUser findByNickname(String nickname);
}
