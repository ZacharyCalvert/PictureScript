package com.zachcalvert.picturescript.export.substitution;

import com.zachcalvert.picturescript.export.ExportRequest;
import java.nio.file.Path;
import java.util.Map;

public interface SubstitutionProvider {

  Map<String, String> prepareSubstitutions(String shaSum, Path from, ExportRequest exportRequest);
}
