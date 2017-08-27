package com.zachcalvert.picturescript.service;

import org.apache.juli.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

@Service
public class ShaSumCalculator {

    private static final Logger logger = LoggerFactory.getLogger(ShaSumCalculator.class);

    // Reference: http://www.javacreed.com/how-to-generate-sha1-hash-value-of-file/
    public String sha256(final File file) throws NoSuchAlgorithmException, IOException {

        logger.debug("Processing sha sum for " + file.getAbsolutePath());

        final MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");

        try (InputStream is = new BufferedInputStream(new FileInputStream(file))) {
            final byte[] buffer = new byte[1024];
            for (int read = 0; (read = is.read(buffer)) != -1;) {
                messageDigest.update(buffer, 0, read);
            }
        }

        // Convert the byte to hex format
        try (Formatter formatter = new Formatter()) {
            for (final byte b : messageDigest.digest()) {
                formatter.format("%02x", b);
            }
            String shaSum =  formatter.toString();
            logger.info("SHA SUM for {} is {} ", file.getAbsolutePath(), shaSum);
            return shaSum;
        }
    }
}
