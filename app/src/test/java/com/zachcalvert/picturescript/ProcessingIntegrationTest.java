package com.zachcalvert.picturescript;

import com.zachcalvert.picturescript.export.ExportRequest;
import com.zachcalvert.picturescript.export.service.ExportService;
import com.zachcalvert.picturescript.model.FolderBase;
import com.zachcalvert.picturescript.repository.FileRepository;
import com.zachcalvert.picturescript.repository.FolderBaseRepository;
import com.zachcalvert.picturescript.repository.service.FolderBaseService;
import com.zachcalvert.picturescript.service.ingest.DirectoryIngestionService;
import com.zachcalvert.picturescript.service.util.FileExtensionExtractorService;
import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
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

    ClassPathResource in1Folder = new ClassPathResource("in1");
    assertTrue(in1Folder.getFile().isDirectory());
    assertTrue(in1Folder.exists());
    directoryIngestionService.processDirectory(true, in1Folder.getFile().getAbsolutePath(), Arrays.asList(new String[]{}));
    assertEquals(3, fileRepository.count());
    FolderBase fbOut1 = folderBaseService.createOrFindFolderBase(out1.getAbsolutePath());
    ExportRequest request = new ExportRequest(Paths.get(fbOut1.getPath()), fileRepository.findRequiredOutputShaSums(Arrays.asList("jpg")), Arrays.asList("${base}/photos/${yyyy}/${MM}/${dd}/${filename}"));
    exportService.processExport(request);
    assertTrue("File should exist", Paths.get(fbOut1.getPath(), "/photos/2017/10/20/a.jpg").toFile().exists());
    assertEquals(6, fileRepository.count());
    assertTrue("File should exist", Paths.get(fbOut1.getPath(), "/photos/2017/10/20/a.jpg").toFile().exists());
    exportService.processExport(request);
    assertEquals(6, fileRepository.count());
    request = new ExportRequest(Paths.get(fbOut1.getPath()), fileRepository.findRequiredOutputShaSums(Arrays.asList("jpg")), Arrays.asList("${base}/photos-b/${yyyy}/${MM}/${dd}/${filename}"));
    exportService.processExport(request);
    assertEquals(6, fileRepository.count());
    assertTrue("File should exist", Paths.get(fbOut1.getPath(), "/photos-b/2017/10/20/a.jpg").toFile().exists());


    FolderBase fbOut2 = folderBaseService.createOrFindFolderBase(out2.getAbsolutePath());
    request = new ExportRequest(Paths.get(fbOut2.getPath()), fileRepository.findRequiredOutputShaSums(Arrays.asList("jpg")), Arrays.asList("${base}/photos-o2/${yyyy}/${MM}/${dd}/${filename}"));
    exportService.processExport(request);
    assertEquals(9, fileRepository.count());
    assertTrue("File should exist", Paths.get(fbOut2.getPath(), "/photos-o2/2017/10/20/a.jpg").toFile().exists());
  }

  /**
   * load, export, clear, re-import
   */
  @Test
  public void exportReloadTest() throws Exception {

    File out1 = temporaryFolder.newFolder("out1");
    File out2 = temporaryFolder.newFolder("out2");

    ClassPathResource in1Folder = new ClassPathResource("in1");
    assertTrue(in1Folder.getFile().isDirectory());
    assertTrue(in1Folder.exists());
    directoryIngestionService.processDirectory(true, in1Folder.getFile().getAbsolutePath(), Arrays.asList(new String[]{}));
    assertEquals(3, fileRepository.count());
    FolderBase fbOut1 = folderBaseService.createOrFindFolderBase(out1.getAbsolutePath());
    ExportRequest request = new ExportRequest(Paths.get(fbOut1.getPath()), fileRepository.findRequiredOutputShaSums(Arrays.asList("jpg")), Arrays.asList("${base}/photos/${yyyy}/${MM}/${dd}/${filename}"));
    exportService.processExport(request);

    clear();
  }
}
