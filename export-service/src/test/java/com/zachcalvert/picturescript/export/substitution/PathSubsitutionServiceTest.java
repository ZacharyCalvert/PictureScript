package com.zachcalvert.picturescript.export.substitution;

import com.zachcalvert.picturescript.export.ExportRequest;
import com.zachcalvert.picturescript.export.substitution.providers.DateSubstitutionProvider;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import mockit.Mocked;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PathSubsitutionServiceTest {

  @Autowired
  PathSubsitutionService subsitutionService;

  @Autowired
  FakeSubstitionService fakeSubstitionService;

  @Test
  public void testHappyPathReplacements(@Mocked DateSubstitutionProvider doNothingMock) {
    Path result = subsitutionService.constructTargetOutputPath("abc", Paths.get("/not/real/path"), new ExportRequest(Paths.get("/some/fake/base"),
        Arrays.asList("a1"), Arrays.asList("/${1}/b", "/a/b/c")));
    assertEquals("/a/b/c", result.toString());
    HashMap<String, String> subs = new HashMap<>();
    subs.put("1", "replace");
    fakeSubstitionService.setSubs(subs);

    result = subsitutionService.constructTargetOutputPath("abc", Paths.get("/not/real/path"), new ExportRequest(Paths.get("/some/fake/base"),
        Arrays.asList("a1"), Arrays.asList("/${1}/b", "/a/b/c")));
    assertEquals("/replace/b", result.toString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidConfiguration(@Mocked DateSubstitutionProvider doNothingMock) {
    subsitutionService.constructTargetOutputPath("abc", Paths.get("/not/real/path"), new ExportRequest(Paths.get("/some/fake/base"),
        Arrays.asList("a1"), Arrays.asList("/${notfound}/b")));
  }
}
