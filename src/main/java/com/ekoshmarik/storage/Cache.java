package com.ekoshmarik.storage;

import java.util.List;

/**
 * A photo gallery storage interface.
 */
public interface Cache {
  /**
   * The addPhotoToCache() method pushes photo to a cache.
   * @param photo is an image file represented by byte array.
   */
  void addDataToCache(byte[] photo);

  /**
   * The clear() method clears a cache of stored data.
   */
  void clear();

  /**
   * The getData() method return a photo a with a specified id from a cache.
   * @param id is a symbol which uniquely identifies an image.
   * @return a photo represented by byte array.
   */
  byte[] getData(int id);

  /**
   * The getData() method returns all photos from a cache.
   * @return photos represented by byte arrays.
   */
  List<byte[]> getData();

  /**
   * The getSize() method returns current amount of photos in a cache.
   * @return amount of photos in a cache.
   */
  int getSize();
}
