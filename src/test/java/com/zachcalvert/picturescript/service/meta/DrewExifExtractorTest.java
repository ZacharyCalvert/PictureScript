package com.zachcalvert.picturescript.service.meta;

import org.junit.Test;

import java.time.Instant;
import java.util.Optional;

import static org.junit.Assert.*;

public class DrewExifExtractorTest {

    @Test
    public void testProperNullOnEarliestDateFind() {
        Optional<Instant> result = DrewExifExtractor.getEarliestInstant(null, Instant.now(), Instant.ofEpochMilli(1));
        assertNotNull(result.orElse(null));
        assertTrue(Instant.ofEpochMilli(1L).equals(result.get()));
    }
}
