package com.zachcalvert.picturescript.export.substitution;

import com.zachcalvert.picturescript.export.ExportRequest;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class FakeSubstitionService implements SubstitutionProvider {

  private HashMap<String, String> subs = new HashMap<>();

  @Override
  public Map<String, String> prepareSubstitutions(String shaSum, Path from,
      ExportRequest exportRequest) {
    return subs;
  }

  public HashMap<String, String> getSubs() {
    return subs;
  }

  public void setSubs(HashMap<String, String> subs) {
    this.subs = subs;
  }
}
