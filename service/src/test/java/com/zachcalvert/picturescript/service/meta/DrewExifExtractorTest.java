package com.zachcalvert.picturescript.service.meta;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.Instant;
import java.util.Optional;
import org.junit.Test;

public class DrewExifExtractorTest {

  @Test
  public void testProperNullOnEarliestDateFind() {
    Optional<Instant> result = DrewExifExtractor.getEarliestInstant(null, Instant.now(), Instant.ofEpochMilli(1));
    assertNotNull(result.orElse(null));
    assertTrue(Instant.ofEpochMilli(1L).equals(result.get()));
  }
}
