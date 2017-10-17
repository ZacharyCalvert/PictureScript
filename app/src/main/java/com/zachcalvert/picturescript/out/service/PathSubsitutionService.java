package com.zachcalvert.picturescript.out.service;

import com.zachcalvert.picturescript.out.conf.OutputOrder;
import com.zachcalvert.picturescript.out.substitution.SubstitutionProvider;
import java.nio.file.Path;
import java.nio.file.Paths;
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

  public Path constructTargetOutputPath(String shaSum, Path from, OutputOrder outputOrder) {
    HashMap<String, String> replacements = new HashMap<>();
    for (SubstitutionProvider provider:substitutionProviders) {
      replacements.putAll(provider.prepareSubstitutions(shaSum, from, outputOrder));
    }
    StrSubstitutor strSubstitutor = new StrSubstitutor(replacements);
    String pathFormat = outputOrder.getYmlOutputTemplate().getFormat();
    return Paths.get(strSubstitutor.replace(pathFormat));
  }
}
