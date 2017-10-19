package com.zachcalvert.picturescript.export.substitution;

import com.zachcalvert.picturescript.export.ExportRequest;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class TargetDirSubstitutionProvider implements SubstitutionProvider {

  @Override
  public Map<String, String> prepareSubstitutions(String shaSum, Path from, ExportRequest export) {
    HashMap<String, String> result = new HashMap<>();
    result.put("base", export.getBaseOutputPath().toString());
    return result;
  }
}
