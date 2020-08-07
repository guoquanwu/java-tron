package org.tron.tool.litefullnode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class Util {

  /**
   * copy src to dest, if dest is a directory and already exists, throw Exception.
   *
   * <p>note: this method is not rigorous, because all the dirs that its FileName
   * is contained in List(subDirs) will be filtered, this may result in unpredictable result.
   *
   * @param src
   *        Path or File
   * @param dest
   *        Path or File
   * @param subDirs
   *        only the subDirs in {@code src} will be copied
   *
   * @throws IOException
   *         IOException
   */
  public static void copyFolder(Path src, Path dest, List<String> subDirs)
          throws IOException {
    // todo: rename method
    Files.walk(src)
            .parallel()
            .filter(path ->
              subDirs.contains(path.getParent().getFileName().toString())
                      | subDirs.contains(path.getFileName().toString())
            )
            .forEach(source -> copy(source, dest.resolve(src.relativize(source))));
  }

  private static void copy(Path source, Path dest) {
    try {
      Files.copy(source, dest, StandardCopyOption.REPLACE_EXISTING);
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }
}
