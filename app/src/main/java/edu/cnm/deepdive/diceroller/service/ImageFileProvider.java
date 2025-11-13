package edu.cnm.deepdive.diceroller.service;

import androidx.core.content.FileProvider;
import edu.cnm.deepdive.diceroller.R;

public class ImageFileProvider extends FileProvider {

  public ImageFileProvider() {
    super(R.xml.provider_paths);
  }
}
