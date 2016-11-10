package com.ekoshmarik.controller;

import com.ekoshmarik.service.PhotoGalleryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.io.ByteArrayInputStream;

/**
 * The PhotoGalleryController class implements managing requests and creating response.
 */
@Controller
public class PhotoGalleryController {

  private static final Logger LOGGER = LoggerFactory.getLogger(PhotoGalleryController.class);

  private static final int DEFAULT_ROWS = 4;

  private static final int DEFAULT_ASPECT_RATIO = 200;

  @Autowired
  private PhotoGalleryService service;

  /**
   * The index() method shows main page of a photo gallery with a placeholder to type
   * a local directory.
   * @return a view of a photo gallery with a placeholder.
   */
  @RequestMapping(value = "/photo", method = RequestMethod.GET)
  public final ModelAndView index() {
    LOGGER.info("Returned main view.");
    return new ModelAndView("index", "index", true);
  }

  /**
   * The postPhotos() method scans directory for images, uploads .png images from the client
   * to server and shows a view of a photo gallery with default parameters
   * @param pathname is path to a local directory.
   * @return a view of a photo gallery with default parameters.
   */
  @RequestMapping(value = "/photo", method = RequestMethod.POST)
  public final ModelAndView postPhotos(@RequestParam final String pathname) {
    service.uploadPhotos(pathname);
    ModelAndView mav = getModelAndView();
    setDefaultAspectRatio(mav);
    LOGGER.info("Returned a view of a photo gallery.");
    return mav;
  }

  /**
   * The setBlackBackground() method sets a black background of a view of a photo gallery.
   * @return a view of a photo gallery on black background.
   */
  @RequestMapping(value = "/photo/blackbackground", method = RequestMethod.GET)
  public final ModelAndView setBlackBackground() {
    ModelAndView mav = getModelAndView();
    mav.addObject("blackbackground", true);
    setDefaultAspectRatio(mav);
    LOGGER.info("Returned a view of a photo gallery on black background.");
    return mav;
  }

  /**
   * The setOriginalAspectRatio() method sets an original aspect ratio of images
   * of a view of a photo gallery.
   * @return a view of a photo gallery with original aspect ratio of images.
   */
  @RequestMapping(value = "/photo/original", method = RequestMethod.GET)
  public final ModelAndView setOriginalAspectRatio() {
    ModelAndView mav = getModelAndView();
    mav.addObject("original", true);
    LOGGER.info("Returned a view of a photo gallery with original image aspect ratio.");
    return mav;
  }

  /**
   * The setCustomRow() method sets a custom quantity of images in a row
   * of a view of a photo gallery.
   * @param row is a custom quantity of images in a row.
   * @return a view of a photo gallery with custom quantity of images in a row.
   */
  @RequestMapping(value = "/photo/row/{row}", method = RequestMethod.GET)
  public final ModelAndView setCustomRow(@PathVariable final int row) {
    ModelAndView mav = getModelAndView();
    mav.addObject("row", row);
    setDefaultAspectRatio(mav);
    LOGGER.info("Returned a view of a photo gallery with {} images in a row.", row);
    return mav;
  }

  /**
   * The setCustomAspectRatio() method returns a view of a photo gallery
   * with custom aspect ratio of images.
   * @param wh means width and height of the custom value in form of ZZZxYYY.
   * @return a view of a photo gallery with photos of the custom aspect ratio.
   */
  @RequestMapping("/photo/wh/{wh:\\d{3}x\\d{3}}")
  public final ModelAndView setCustomAspectRatio(@PathVariable final String wh) {
    ModelAndView mav = getModelAndView();
    String[] aspectRatio = wh.split("x");
    mav.addObject("width", aspectRatio[0]);
    mav.addObject("height", aspectRatio[1]);
    LOGGER.info("Returned a view of a photo gallery with {} aspect ratio of images.", wh);
    return mav;
  }

  /**
   * The getPhoto() method returns specified photo from a server storage.
   * @param id is id of the image to retrieve.
   * @return a png image represented by {@code InputStreamResource}.
   */
  @RequestMapping(value = "/photo/{id:\\d++}", method = RequestMethod.GET)
  public final ResponseEntity<InputStreamResource> getPhoto(@PathVariable final int id) {
    byte[] photo = service.getPhoto(id);
    LOGGER.debug("Returned image represented by InputStreamResource.");
    return ResponseEntity.ok()
        .contentLength(photo.length)
        .contentType(MediaType.IMAGE_PNG)
        .body(new InputStreamResource(new ByteArrayInputStream(photo)));
  }

  /**
   * The getModelAndView() method sets default parameters of a view of a photo gallery.
   * @return a view of a photo gallery with default parameters.
   */
  private ModelAndView getModelAndView() {
    ModelAndView mav = new ModelAndView("index");
    mav.addObject("gallery", true);
    mav.addObject("amount", service.getAmountOfPhotos());
    mav.addObject("photos", service.getPhotos());
    mav.addObject("row", DEFAULT_ROWS);
    LOGGER.debug("Returned a view of a photo gallery with default parameters.");
    return mav;
  }

  /**
   * The setDefaultAspectRatio() method sets a default aspect ratio of an image.
   * @param mav is a view to set a parameters to.
   */
  private void setDefaultAspectRatio(ModelAndView mav) {
    mav.addObject("width", DEFAULT_ASPECT_RATIO);
    mav.addObject("height", DEFAULT_ASPECT_RATIO);
    LOGGER.debug("Set default aspect ratio of am image/images.");
  }
}