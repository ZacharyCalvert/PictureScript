package com.zachcalvert.picturescript;

import com.zachcalvert.picturescript.out.conf.OutputConfiguration;
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
public class OutputConfigurationTest {

  @Autowired
  private OutputConfiguration outputConfiguration;

  @Test
  public void evaluateOutputExtraction() {
    Assert.assertEquals(2, outputConfiguration.getTemplates().size());
    Assert.assertEquals(1, outputConfiguration.getTargets().size());


  }
}
