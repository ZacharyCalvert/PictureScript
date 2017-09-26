package com.zachcalvert.picturescript.out.step;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class MoveStepTest {

  @Test
  public void testCopyBehavior() throws Exception {
    File temp = FileUtils.getTempDirectory();
    Path from = Paths.get(temp.getAbsolutePath(), "from.jpg");
    Path to = Paths.get(temp.getAbsolutePath(), "to.jpg");
    Path collision = Paths.get(temp.getAbsolutePath(), "to_1.jpg");
    FileUtils.writeStringToFile(from.toFile(), "testdata");
    new CopyStep(from, to).execute(false);
    new MoveStep(from, to).execute(false);
    String copiedData = FileUtils.readFileToString(to.toFile());
    assertEquals(copiedData, "testdata");

    assertFalse(from.toFile().exists());

    String collisionData = FileUtils.readFileToString(collision.toFile());
    assertEquals(collisionData, "testdata");
    to.toFile().delete();
    collision.toFile().delete();
  }
}
