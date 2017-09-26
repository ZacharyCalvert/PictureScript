package com.zachcalvert.picturescript.out.substitution;

import com.zachcalvert.picturescript.out.conf.OutputOrder;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class TargetDirSubstitutionProvider implements SubstitutionProvider {

  @Override
  public Map<String, String> prepareSubstitutions(String shaSum, Path from, OutputOrder order) {
    HashMap<String, String> result = new HashMap<>();
    result.put("base", order.getBaseOutputPath().toString());
    return result;
  }
}
