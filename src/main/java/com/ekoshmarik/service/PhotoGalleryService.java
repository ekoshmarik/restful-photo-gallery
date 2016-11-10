package com.ekoshmarik.service;

import com.ekoshmarik.storage.Cache;
import com.ekoshmarik.storage.CacheImpl;
import com.ekoshmarik.util.DirectoryScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * The PhotoGalleryImpl implements a photo gallery service.
 */
@Service
public class PhotoGalleryService {

  private static final Logger LOGGER = LoggerFactory.getLogger(PhotoGalleryService.class);

  private static final int BUFF_SIZE = 4096;

  private Cache cache;

  public PhotoGalleryService() {
    this.cache = new CacheImpl();
  }

  /**
   * The uploadPhotos() method scans directory for a .png image and adds its to a storage.
   * @param dir is a full path to directory to scan.
   */
  public void uploadPhotos(String dir) {
    LOGGER.info("Uploading photos...");
    List<Path> files = DirectoryScanner.getFiles(dir);

    if (files.isEmpty()){
      LOGGER.info("Photos not found.");
      return;
    }

    cache.clear();

    byte[] buffer = new byte[BUFF_SIZE];

    for (Path file : files) {
      try (InputStream photo = Files.newInputStream(file)) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        while (photo.available() > 0) {
          int size = photo.read(buffer);
          stream.write(buffer, 0, size);
        }
        cache.addDataToCache(stream.toByteArray());
      } catch (IOException e) {
        LOGGER.error("Error occurred while reading file: {}.", e.getMessage());
      }
    }
    LOGGER.info("Photos have been uploaded.");
  }

  /**
   * The getPhoto() method returns a photo with a specified id from a storage.
   * @param id is a symbol which uniquely identifies an image.
   * @return a photo represented by byte array.
   */
  public byte[] getPhoto(int id) {
    return cache.getData(id);
  }

  /**
   * The getPhotos() method returns all photos from a storage.
   * @return photos represented by byte arrays.
   */
  public List<byte[]> getPhotos() {
    return cache.getData();
  }

  /**
   * The getAmountOfPhotos() method returns current amount of photos
   * @return current amount of photos store in a storage.
   */
  public int getAmountOfPhotos() {
    return cache.getSize();
  }
}
