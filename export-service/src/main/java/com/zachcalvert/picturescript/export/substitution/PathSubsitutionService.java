package com.zachcalvert.picturescript.export.substitution;

import com.zachcalvert.picturescript.export.ExportRequest;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.text.StrSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PathSubsitutionService {

  private final List<SubstitutionProvider> substitutionProviders;

  @Autowired
  public PathSubsitutionService(
      List<SubstitutionProvider> substitutionProviders) {
    this.substitutionProviders = substitutionProviders;
  }

  public Path constructTargetOutputPath(String shaSum, Path from, ExportRequest exportRequest) {
    HashMap<String, String> replacements = new HashMap<>();
    for (SubstitutionProvider provider:substitutionProviders) {
      replacements.putAll(provider.prepareSubstitutions(shaSum, from, exportRequest));
    }
    StrSubstitutor strSubstitutor = new StrSubstitutor(replacements);
    String result = null;
    for (String path:exportRequest.getPathFormats()) {
      String tempResult = strSubstitutor.replace(path);
      if (!tempResult.contains("$")) {
        result = tempResult;
        break;
      }
    }
    if (result == null) {
      throw new IllegalArgumentException("Variable replacements not fully enabled for list of strings: " + Arrays
          .toString(exportRequest.getPathFormats().toArray(new String[] {})));
    }
    return Paths.get(result);
  }
}
