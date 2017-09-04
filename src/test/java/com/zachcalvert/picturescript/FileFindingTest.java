package com.zachcalvert.picturescript;

import com.zachcalvert.picturescript.repository.FileRepository;
import com.zachcalvert.picturescript.service.FileExtensionExtractorService;
import java.util.Arrays;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {PictureScriptApplication.class})
@ActiveProfiles("test")
public class FileFindingTest {

  @Autowired
  private FileRepository fileRepository;

  @Autowired
  private FileExtensionExtractorService fileExtensionExtractorService;

  @Test
  public void testExtensionSearch() throws Exception {
    Thread.sleep(1000L);
    Assert.assertEquals(3, fileRepository.findRequiredOutputShaSums(Arrays.asList(fileExtensionExtractorService.standardizeCaseFileExtension("png"))).size());
  }
}
