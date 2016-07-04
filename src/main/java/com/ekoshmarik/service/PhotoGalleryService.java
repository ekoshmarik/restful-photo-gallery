package com.ekoshmarik.service;

import java.util.List;

/**
 * A photo gallery service interface.
 */
public interface PhotoGalleryService {

  /**
   * The uploadPhotos() method scans directory for a .png image and adds its to a storage.
   * @param dir is a full path to directory to scan.
   */
  void uploadPhotos(String dir);

  /**
   * The getPhoto() method returns a photo with a specified id from a storage.
   * @param id is a symbol which uniquely identifies an image.
   * @return a photo represented by byte array.
   */
  byte[] getPhoto(int id);

  /**
   * The getPhotos() method returns all photos from a storage.
   * @return photos represented by byte arrays.
   */
  List<byte[]> getPhotos();

  /**
   * The getAmountOfPhotos() method returns current amount of photos
   * @return current amount of photos store in a storage.
   */
  int getAmountOfPhotos();

}
