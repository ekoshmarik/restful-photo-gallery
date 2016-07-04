package com.ekoshmarik.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * The CacheImpl class implements a photo gallery storage.
 */
@Repository
public class CacheImpl implements Cache {

  private static final Logger LOGGER = LoggerFactory.getLogger(CacheImpl.class);

  private List<byte[]> photos = new ArrayList<>();

  /**
   * {@inheritDoc}
   */
  @Override
  public void addDataToCache(byte[] photo) {
    LOGGER.debug("Adding a new photo to the cache...");
    photos.add(photo);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void clear() {
    LOGGER.debug("Clearing the cache...");
    photos.clear();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<byte[]> getData() {
    LOGGER.debug("Returning all photos store in the cache.. ");
    return photos;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public byte[] getData(int id) {
    LOGGER.debug("Returning the photo with id {}...", id);
    return photos.get(id);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getSize() {
    LOGGER.debug("Returning the cache size...");
    return photos.size();
  }
}
