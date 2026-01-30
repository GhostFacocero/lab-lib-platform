// Emanuele Contini, matricola 756441
// Emanuele Gobessi, matricola 757599
// Diego Guidi, matricola 758420
// Nicola Curchi, matricola 757786
// Mirko Gurzau, matricola 757925

package com.lab_lib.restapi.Services;

import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.lab_lib.restapi.DTO.AppUser.*;
import com.lab_lib.restapi.Models.AppUser;
import com.lab_lib.restapi.Repositories.UserRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servizio che espone le operazioni legate alla gestione degli utenti.
 *
 * <p>Include funzionalità di registrazione, autenticazione e utilità
 * per risolvere l'ID utente a partire dal token. Utilizza {@link UserRepository}
 * per le operazioni di persistenza e un {@link EntityManager} per forzare
 * refresh oggetti quando necessario.
 *
 * <p>Tutti i metodi pubblici che modificano lo stato del database sono
 * annotati con {@code @Transactional} per garantire rollback automatico in
 * caso di eccezioni non gestite.
 */
@Service
public class UserService {

    /** Entity manager usato per operazioni stateful e refresh espliciti. */
    @PersistenceContext
    private EntityManager entityManager;

    /** Repository per accesso e persistenza degli utenti. */
    private final UserRepository userRepository;

    /**
     * Costruttore di servizio.
     *
     * @param userRepository repository per l'accesso agli utenti
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Registra un nuovo utente nel sistema.
     *
     * <p>Esegue controlli sui dati (es. lunghezza minima della password,
     * unicità di email e nickname), cifra la password con un algoritmo
     * semplice e salva l'utente nel repository. Ritorna il token UUID
     * generato per l'utente appena creato.
     *
     * @param newUser DTO con i dati di registrazione
     * @return il token univoco (UUID) dell'utente creato
     * @throws IllegalArgumentException se i dati non sono validi
     * @throws IllegalStateException se email già in uso
     */
    @Transactional //abbellitore che rollbacka automaticamente in caso di eccezioni
    public UUID registerUser(RegisterRequest newUser) {

        // Basic password length check
        if (newUser.getPassword() == null || newUser.getPassword().length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters.");
        }

        if (userRepository.existsByEmail(newUser.getEmail())) {
            throw new IllegalStateException("Email already in use.");
        }

        if (userRepository.existsByNickname(newUser.getNickname())) {
            throw new IllegalArgumentException("Nickname already in use.");
        }

        AppUser user = new AppUser();
        user.setNickname(newUser.getNickname());
        user.setName(newUser.getName());
        user.setSurname(newUser.getSurname());
        user.setCf(newUser.getCf());
        user.setEmail(newUser.getEmail());
        user.setPassword(cypher(newUser.getPassword()));


        AppUser saved = userRepository.save(user);
        entityManager.refresh(saved); // forza un SELECT sul database per aggiornare i campi

        return saved.getToken();
    }

    /**
     * Effettua il login di un utente e restituisce il token UUID associato.
     *
     * @param User DTO con nickname e password
     * @return UUID token dell'utente autenticato
     * @throws IllegalArgumentException se le credenziali sono invalide o la password non rispetta i requisiti
     */
    @Transactional
    public UUID loginUser(LoginRequest User) {
        // Always check user input
        if(User.getPassword() == null || User.getPassword().length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters.");
        }  
        AppUser existingUser = userRepository.findByNickname(User.getNickname());
        if(existingUser == null || !decypher(existingUser.getPassword()).equals(User.getPassword())) {
            throw new IllegalArgumentException("Invalid nickname or password.");
        } else {
            return existingUser.getToken();
        }
    }

    /**
     * Risolve l'ID utente a partire da un token UUID.
     *
     * @param token token UUID dell'utente
     * @return id numerico dell'utente
     * @throws NoSuchElementException se il token non è valido
     */
    public Long getUserIdByToken(UUID token) {
        if(!userRepository.existsByToken(token))
            throw new NoSuchElementException("Authentication failed: token does not exist");
        AppUser user = userRepository.findByToken(token);
        return user.getId();
    }

    /**
     * Verifica l'esistenza di un utente associato al token fornito.
     *
     * @param token token UUID
     * @return {@code true} se esiste un utente con quel token, {@code false} altrimenti
     */
    public boolean existsByToken(UUID token) {
        return userRepository.existsByToken(token);
    }

    /**
     * Verifica se esiste un utente con l'id specificato.
     *
     * @param id identificatore dell'utente
     * @return {@code true} se l'utente esiste, {@code false} altrimenti
     */
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    /**
     * Recupera l'utente per id.
     *
     * @param id identificatore dell'utente
     * @return istanza di {@link AppUser}
     * @throws NoSuchElementException se l'utente non esiste
     */
    public AppUser findUserById(Long id) {
        return userRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("User not found"));
    }

    /**
     * Cifra la stringa fornita usando una semplice rotazione dei punti codice.
     *
     * <p>Questa implementazione non è pensata per la produzione: serve a
     * offrire una leggera offuscatura della password prima della persistenza
     * nel DB all'interno di questo esercizio. Per la produzione usare algoritmi
     * sicuri come BCrypt/PBKDF2/Argon2.
     *
     * @param s stringa da cifrare
     * @return stringa cifrata
     */
    private String cypher(String s) {
        String cypher = "";
        for(int i = 0; i < s.length(); i++) {
            int code = s.codePointAt(i);
            int codeCypher = code + 3;
            if(codeCypher > 127) {
                int diff = codeCypher - 127;
                codeCypher = 32 + diff;
            }
            cypher += (char)codeCypher;
        }
        return cypher;
    }

    /**
     * Decifra la stringa precedentemente cifrata da {@link #cypher(String)}.
     *
     * @param s stringa cifrata
     * @return stringa originale decifrata
     */
    private String decypher(String s) {
        String decypher = "";
        for(int i = 0; i < s.length(); i++) {
            int code = s.codePointAt(i);
            int codeDecypher = code - 3;
            if(codeDecypher < 32) {
                int diff = 32 - codeDecypher;
                codeDecypher = 127 - diff;
            }
            decypher += (char)codeDecypher;
        }
        return decypher;
    }

}
