package com.ekoshmarik.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The DirectoryScanner class provides for functionality
 * of searching files in the specified  local directory.
 */

@Component
public class DirectoryScanner {

  private static final Logger LOGGER = LoggerFactory.getLogger(DirectoryScanner.class);

  /**
   * The getFiles() method searches for specified image file format in the specified directory
   * and all of its subdirectories.
   * @param directory is a full path to directory to scan.
   * @return a list to full paths to the files matching the input criteria.
   */
  public static List<Path> getFiles(String directory) {
    LOGGER.info("Searching for .png files in '{}'", directory);
    if (directory == null) {
      LOGGER.warn("String with the scanned directory is null!");
      return Collections.emptyList();
    }

    Path dir = Paths.get(directory);

    if (!Files.exists(dir) || !Files.isDirectory(dir)) {
      LOGGER.warn("File does not exist or is not a directory!");
      return Collections.emptyList();
    }

    List<Path> foundPaths = new ArrayList<>();

    try {
      Files.walkFileTree(dir, new SimpleFileVisitor<Path>() {
        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attr) throws IOException {
          if (isFileSuitable(file)) {
            foundPaths.add(file);
          }
          return FileVisitResult.CONTINUE;
        }
      });
    } catch (IOException e) {
      LOGGER.error("Error while reading directory: {}.\n Stopped scan directory", e.getMessage());
    }

    LOGGER.info("{} files have been found in '{}'", foundPaths.size(), dir);

    return foundPaths;
  }

  /**
   * The isFileSuitable() method checks file for compliance with the criteria.
   * @param file is the file being checked for compliance with the criteria.
   * @return true if file's extension is ".png".
   */
  private static boolean isFileSuitable(Path file) {
    return file.toString().endsWith(".png");
  }
}
