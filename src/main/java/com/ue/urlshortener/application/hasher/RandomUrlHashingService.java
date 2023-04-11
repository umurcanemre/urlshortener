package com.ue.urlshortener.application.hasher;

import com.ue.urlshortener.domain.UrlPointerRepository;
import java.security.SecureRandom;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class RandomUrlHashingService implements UrlHasher {
    private static final SecureRandom RND = new SecureRandom();
    private UrlPointerRepository urlPointerRepository;
    private HasherConfigurations config;

    @Override
    public String hash(String url) {
        boolean candidateTaken;
        String candidate;
        var attemptCount = 0;
        do {
            //"As of January 2023, there are 202,900,724 active websites" according to Google
            // there is about 916 million combinations for current setup (5 alpha-numeric characters)
            // because of this, depending on product needs, this approach can serve fine in a Prod environment for some time.

            //This ideally will be replaced with a decent hashing approach or better as
            // deterioration of the performance with current approach would worsen exponentially.

            //Incremental approach is not considered as it'd allow for people being able to guess what "hashes" are
            // in use and go through them which would give our system a higher than necessary load
            candidate = getRandomString();
            candidateTaken = urlPointerRepository.findByTargetIdentifier(candidate).isPresent();
            //Considering every loop an "attempt", a monitor is needed on many "attempt per hash" is needed.
            //This value will go higher and higher as the hash capacity of 916 million is being reached
            attemptCount++;
        } while (candidateTaken);
        logHashingAttempts(attemptCount);
        return candidate;
    }

    private String getRandomString() {
        var length = config.getLength();
        var poolSize = config.getPool().length();
        var sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(config.getPool().charAt(RND.nextInt(poolSize)));
        }
        return sb.toString();
    }

    // For MVP this amount of monitoring can be enough but proper metric handling would be high priority post-MVP as it's important to track historical track of attempt counts
    private void logHashingAttempts(int attemptCount) {
        if (attemptCount == 1) {
            log.info("Random hash generated in 1 attempt");
        } else {
            log.warn("Random hash generated in {} attempt", attemptCount);
        }
    }
}
