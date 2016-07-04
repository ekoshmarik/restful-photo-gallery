package com.ekoshmarik.service;

import com.ekoshmarik.storage.Cache;
import com.ekoshmarik.util.DirectoryScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class PhotoGalleryServiceImpl implements PhotoGalleryService {

  private static final Logger LOGGER = LoggerFactory.getLogger(PhotoGalleryServiceImpl.class);

  private static final int BUFF_SIZE = 4096;

  @Autowired
  private Cache cache;

  /**
   * {@inheritDoc}
   */
  @Override
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
   * {@inheritDoc}
   */
  @Override
  public byte[] getPhoto(int id) {
    return cache.getData(id);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<byte[]> getPhotos() {
    return cache.getData();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getAmountOfPhotos() {
    return cache.getSize();
  }
}
