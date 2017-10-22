package com.zachcalvert.picturescript.export.substitution.providers;

import com.zachcalvert.picturescript.export.ExportRequest;
import com.zachcalvert.picturescript.export.substitution.SubstitutionProvider;
import com.zachcalvert.picturescript.model.File;
import com.zachcalvert.picturescript.repository.FileRepository;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParentDirSubstitionProvider implements SubstitutionProvider {

  public static final String PARENT_DIR_KEY = "parent_dir";

  private final FileRepository fileRepository;

  @Autowired
  public ParentDirSubstitionProvider(
      FileRepository fileRepository) {
    this.fileRepository = fileRepository;
  }

  @Override
  public Map<String, String> prepareSubstitutions(String shaSum, Path from, ExportRequest exportRequest) {
    HashMap<String, String> result = new HashMap<>();
    File file = fileRepository.findTopBySha256OrderByEarliestKnownDateDesc(shaSum);
    result.put(PARENT_DIR_KEY, FilenameUtils.getName(Paths.get(file.getOriginalFileName()).getParent().toString()));
    return result;
  }

}
