package com.zachcalvert.picturescript;

import com.zachcalvert.picturescript.repository.FileRepository;
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
		Assert.assertEquals("Expectd 5 total files to be found as part of application startup", 6, fileRepository.count());
	}
}
