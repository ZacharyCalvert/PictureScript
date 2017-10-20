package com.zachcalvert.picturescript.repository;

import com.zachcalvert.picturescript.model.FolderBase;
import java.time.Instant;
import java.util.Arrays;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class FileRepositoryTest extends RepoTestBase {

  @Before
  public void setUpFiles() {
    constructAndSafeFile("a", true, "in1/a", Instant.MIN, Instant.MIN, "jpg", in1);
    constructAndSafeFile("a", true, "in2/a", Instant.MIN, Instant.MIN, "jpg", in2);
    constructAndSafeFile("b", true, "in1/b", Instant.MIN, Instant.MIN, "jpg", in1);
    constructAndSafeFile("b", false, "out1/b", Instant.MIN, Instant.MIN, "jpg", out1);
    constructAndSafeFile("c", false, "out2/c", Instant.MIN, Instant.MIN, "jpg", out2);
    constructAndSafeFile("d", true, "in1/d", Instant.MIN, Instant.MIN, "png", in1);

  }

  @Test
  public void testShaCounts() {
    Assert.assertEquals(2, fileRepository.findRequiredOutputShaSums(Arrays.asList("jpg")).size());
    Assert.assertEquals(3, fileRepository.findRequiredOutputShaSums(Arrays.asList("jpg", "png")).size());
  }

}
