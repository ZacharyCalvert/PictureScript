package com.zachcalvert.picturescript.out.step;

import com.zachcalvert.picturescript.export.step.CopyStep;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class CopyStepTest {

  @Test
  public void testCopyBehavior() throws Exception {
    File temp = FileUtils.getTempDirectory();
    Path from = Paths.get(temp.getAbsolutePath(), "from.jpg");
    Path to = Paths.get(temp.getAbsolutePath(), "to.jpg");
    Path collision = Paths.get(temp.getAbsolutePath(), "to_1.jpg");
    FileUtils.writeStringToFile(from.toFile(), "testdata");
    new CopyStep(from, to).execute(false);
    new CopyStep(from, to).execute(false);
    String copiedData = FileUtils.readFileToString(to.toFile());
    assertEquals(copiedData, "testdata");
    String originalData = FileUtils.readFileToString(from.toFile());
    assertEquals(originalData, "testdata");
    String collisionData = FileUtils.readFileToString(collision.toFile());
    assertEquals(collisionData, "testdata");
    from.toFile().delete();
    to.toFile().delete();
    collision.toFile().delete();
  }
}
