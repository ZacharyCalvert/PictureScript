package com.zachcalvert.picturescript;

import com.zachcalvert.picturescript.repository.FileRepository;
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
public class PictureScriptApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Autowired
	FileRepository fileRepository;

	@Test
	public void testTotalCount() {
		// total of 5 input files processed, 3 written to output
		Assert.assertEquals("Unexpected count of total files to be processed as part of application completion", 8, fileRepository.count());
		Assert.assertEquals(3, fileRepository.findRequiredOutputShaSums(Arrays.asList("png")).size());
	}
}
