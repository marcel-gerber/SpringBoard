package de.marcelgerber.springboard.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class BlackListService {

    private final ArrayList<String> blackList = new ArrayList<>();

    /**
     * Adds a JWT to the black list
     *
     * @param token JWT as String
     */
    public void addToken(String token) {
        blackList.add(token);
    }

    /**
     * Returns 'true' if the provided JWT is on the black list
     *
     * @param token JWT as String
     * @return boolean
     */
    public boolean contains(String token) {
        return blackList.contains(token);
    }

}
