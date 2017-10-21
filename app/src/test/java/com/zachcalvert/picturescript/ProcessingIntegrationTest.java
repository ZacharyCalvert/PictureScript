package com.zachcalvert.picturescript;

import com.zachcalvert.picturescript.export.ExportRequest;
import com.zachcalvert.picturescript.export.service.ExportService;
import com.zachcalvert.picturescript.loader.BaseDirLoader;
import com.zachcalvert.picturescript.model.FolderBase;
import com.zachcalvert.picturescript.out.service.OrderDeliveryService;
import com.zachcalvert.picturescript.repository.FileRepository;
import com.zachcalvert.picturescript.repository.FolderBaseRepository;
import com.zachcalvert.picturescript.repository.service.FolderBaseService;
import com.zachcalvert.picturescript.service.ingest.DirectoryIngestionService;
import com.zachcalvert.picturescript.service.ingest.FileProcessorService;
import com.zachcalvert.picturescript.service.util.FileExtensionExtractorService;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import mockit.Expectations;
import mockit.Mocked;
import mockit.Tested;
import mockit.Verifications;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {PictureScriptApplication.class})
@ActiveProfiles("test")
public class ProcessingIntegrationTest {

  @Autowired
  FileRepository fileRepository;

  @Autowired
  FolderBaseRepository folderBaseRepository;

  @Autowired
  DirectoryIngestionService directoryIngestionService;

  @Autowired
  ExportService exportService;

  @Autowired
  FolderBaseService folderBaseService;

  @Autowired
  FileProcessorService fileProcessorService;

  @Rule
  public TemporaryFolder temporaryFolder = new TemporaryFolder();

  @Before
  @After
  public void clear() {
    fileRepository.deleteAll();
    folderBaseRepository.deleteAll();
  }

  /**
   * load, export, export, move
   */
  @Test
  public void exportMoveTest() throws Exception {

    File out1 = temporaryFolder.newFolder("out1");
    File out2 = temporaryFolder.newFolder("out2");

    File file = ResourceUtils.getFile(this.getClass().getResource("/in1"));

    assertEquals(0, fileRepository.count());
    assertTrue(file.isDirectory());
    assertTrue(file.exists());
    directoryIngestionService.processDirectory(true, file.getAbsolutePath(), Arrays.asList(new String[]{}));
    assertEquals(3, fileRepository.count());
    FolderBase fbOut1 = folderBaseService.createOrFindFolderBase(out1.getAbsolutePath());
    assertFalse("File should NOT exist: " + Paths.get(fbOut1.getPath(), "/photos/a.jpg").toString(), Paths.get(fbOut1.getPath(), "/photos/a.jpg").toFile().exists());
    ExportRequest request = new ExportRequest(Paths.get(fbOut1.getPath()), fileRepository.findRequiredOutputShaSums(Arrays.asList("jpg")), Arrays.asList("${base}/photos/${filename}"));
    exportService.processExport(request);
    assertTrue("File should exist: " + Paths.get(fbOut1.getPath(), "/photos/a.jpg").toString(), Paths.get(fbOut1.getPath(), "/photos/a.jpg").toFile().exists());
    assertEquals(6, fileRepository.count());
    assertTrue("File should exist", Paths.get(fbOut1.getPath(), "/photos/a.jpg").toFile().exists());
    exportService.processExport(request);
    assertEquals(6, fileRepository.count());
    request = new ExportRequest(Paths.get(fbOut1.getPath()), fileRepository.findRequiredOutputShaSums(Arrays.asList("jpg")), Arrays.asList("${base}/photos-b/${filename}"));
    exportService.processExport(request);
    assertEquals(6, fileRepository.count());
    assertTrue("File should exist", Paths.get(fbOut1.getPath(), "/photos-b/a.jpg").toFile().exists());


    FolderBase fbOut2 = folderBaseService.createOrFindFolderBase(out2.getAbsolutePath());
    request = new ExportRequest(Paths.get(fbOut2.getPath()), fileRepository.findRequiredOutputShaSums(Arrays.asList("jpg")), Arrays.asList("${base}/photos-o2/${filename}"));
    exportService.processExport(request);
    assertEquals(9, fileRepository.count());
    assertTrue("File should exist", Paths.get(fbOut2.getPath(), "/photos-o2/a.jpg").toFile().exists());
  }

  /**
   * load, export, clear, re-import
   */
  @Test
  public void exportReloadTest() throws Exception {

    File out1 = temporaryFolder.newFolder("out1");
    File out2 = temporaryFolder.newFolder("out2");

    new Expectations(fileProcessorService) {};

    ClassPathResource in1Folder = new ClassPathResource("in1");
    assertTrue(in1Folder.getFile().isDirectory());
    assertTrue(in1Folder.exists());
    directoryIngestionService.processDirectory(true, in1Folder.getFile().getAbsolutePath(), Arrays.asList(new String[]{}));
    assertEquals(3, fileRepository.count());
    FolderBase fbOut1 = folderBaseService.createOrFindFolderBase(out1.getAbsolutePath());
    ExportRequest request = new ExportRequest(Paths.get(fbOut1.getPath()), fileRepository.findRequiredOutputShaSums(Arrays.asList("jpg")), Arrays.asList("${base}/photos/${yyyy}/${MM}/${dd}/${filename}"));
    exportService.processExport(request);

    new Verifications() {
      {
        fileProcessorService.processFile((Path)any, (FolderBase)any); times = 3;
      }
    };

    clear();

    assertEquals(0, fileRepository.count());
    directoryIngestionService.processDirectory(true, fbOut1.getPath(), Arrays.asList(new String[]{}));
    new Verifications() {
      {
        fileProcessorService.processFile((Path)any, (FolderBase)any); times = 3;
      }
    };

    assertEquals(3, fileRepository.count());
  }
}
