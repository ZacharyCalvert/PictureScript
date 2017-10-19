package com.zachcalvert.picturescript.export.substitution;

import com.zachcalvert.picturescript.export.ExportRequest;
import com.zachcalvert.picturescript.repository.FileRepository;
import java.nio.file.Path;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DateSubstitutionProvider implements SubstitutionProvider {

  private final FileRepository fileRepository;

  @Autowired
  public DateSubstitutionProvider(
      FileRepository fileRepository) {
    this.fileRepository = fileRepository;
  }

  @Override
  public Map<String, String> prepareSubstitutions(String shaSum, Path from, ExportRequest exportRequest) {
    HashMap<String, String> result = new HashMap<>();
    Instant earliestInstant = fileRepository.findTopBySha256OrderByEarliestKnownDateDesc(shaSum).getEarliestKnownDate();
    result.put("yyyy", DateTimeFormatter.ofPattern("yyyy").withZone(ZoneId.systemDefault()).format(earliestInstant));
    result.put("MM", DateTimeFormatter.ofPattern("MM").withZone(ZoneId.systemDefault()).format(earliestInstant));
    result.put("dd", DateTimeFormatter.ofPattern("dd").withZone(ZoneId.systemDefault()).format(earliestInstant));
    return result;
  }
}
