package com.zachcalvert.picturescript.out.substitution;

import com.zachcalvert.picturescript.model.File;
import com.zachcalvert.picturescript.out.conf.OutputOrder;
import java.nio.file.Path;
import java.util.Map;

public interface SubstitutionProvider {

  Map<String, String> prepareSubstitutions(String shaSum, Path from, OutputOrder order);
}
