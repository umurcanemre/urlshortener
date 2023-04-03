package com.ue.urlshortener.application.hasher;

import com.ue.urlshortener.domain.UrlPointerRepository;
import java.security.SecureRandom;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RandomUrlHashingService implements UrlHasher {
    private static final int HASH_LENGTH = 5; // ideally would be dynamic, or at least a config
    private static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final SecureRandom RND = new SecureRandom();
    private UrlPointerRepository urlPointerRepository;

    @Override
    public String hash(String url) {
        boolean candidateTaken;
        String candidate;
        do {
            //This is a suboptimal approach that'll work as MVP for a while depending on how much traffic the product gets.
            //As there is about 916 million combinations for current setup (5 alpha-numeric characters).

            //This ideally will be replaced with a decent hashing approach or better as
            // deterioration of the performance with current approach would worsen exponentially.

            //Incremental approach is not considered as it'd allow for people being able to guess what "hashes" are
            // in use and go through them which would give our system a higher than necessary load
            candidate = getRandomString();
            candidateTaken = urlPointerRepository.findByTargetIdentifier(candidate).isPresent();
            //Considering every loop an "attempt", a monitor is needed on many "attempt per hash" is needed.
            //This value will go higher and higher as the hash capacity of 916 million is being reached
        } while (candidateTaken);
        return candidate;
    }

    private String getRandomString() {
        var sb = new StringBuilder(HASH_LENGTH);
        for (int i = 0; i < HASH_LENGTH; i++) {
            sb.append(AB.charAt(RND.nextInt(AB.length())));
        }
        return sb.toString();
    }
}
