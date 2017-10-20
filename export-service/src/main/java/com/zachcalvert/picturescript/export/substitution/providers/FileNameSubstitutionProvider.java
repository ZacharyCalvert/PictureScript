package com.zachcalvert.picturescript.export.substitution.providers;

import com.zachcalvert.picturescript.export.ExportRequest;
import com.zachcalvert.picturescript.export.substitution.SubstitutionProvider;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class FileNameSubstitutionProvider implements SubstitutionProvider {

  public static final String SUB_FNAME = "filename";

  @Override
  public Map<String, String> prepareSubstitutions(String shaSum, Path from, ExportRequest exportRequest) {
    HashMap<String, String> result = new HashMap<>();
    result.put(SUB_FNAME, from.getFileName().toString());
    return result;
  }
}
