package com.zachcalvert.picturescript;

import com.zachcalvert.picturescript.out.conf.YmlOutputConfiguration;
import com.zachcalvert.picturescript.out.conf.OutputOrder;
import java.util.List;
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
public class YmlOutputConfigurationTest {

  @Autowired
  private YmlOutputConfiguration ymlOutputConfiguration;

  @Autowired
  private List<OutputOrder> outputOrders;

  @Test
  public void evaluateOutputExtraction() {
    Assert.assertEquals(2, ymlOutputConfiguration.getTemplates().size());
    Assert.assertEquals(1, ymlOutputConfiguration.getTargets().size());
  }

  @Test
  public void evaluateOutputOrders() {
    Assert.assertEquals(1, outputOrders.size());
  }
}
