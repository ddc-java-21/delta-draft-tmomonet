package edu.cnm.deepdive.deltadraft.service;

import androidx.core.content.FileProvider;
import edu.cnm.deepdive.deltadraft.R;

public class ImageFileProvider extends FileProvider {

  public ImageFileProvider() {
    super(R.xml.provider_paths);
  }
}
