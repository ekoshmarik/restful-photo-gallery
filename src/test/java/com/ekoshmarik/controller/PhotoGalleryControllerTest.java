package com.ekoshmarik.controller;

import com.ekoshmarik.RestfulPhotoGalleryApplication;
import com.ekoshmarik.service.PhotoGalleryService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RestfulPhotoGalleryApplication.class)
@WebAppConfiguration
public class PhotoGalleryControllerTest {

  private static String pathToImage = System.getProperty("user.dir")
      + "/src/test/resources/images";

  private static String URL = "/photo";

  @Autowired
  private WebApplicationContext applicationContext;

  @Autowired
  private PhotoGalleryService photoGalleryService;

  private MockMvc mockMvc;

  @Before
  public void setup() {
    mockMvc = MockMvcBuilders
              .webAppContextSetup(applicationContext)
              .build();
  }

  @Test
  public void testIndex() throws Exception {
    mockMvc.perform(get(URL))
        .andExpect(status().isOk())
        .andExpect(view().name("index"))
        .andExpect(model().attribute("index", true));
  }

  @Test
  public void testPostPhotos() throws Exception {
    mockMvc.perform(post(URL).param("pathname", pathToImage))
        .andExpect(status().isOk())
        .andExpect(view().name("index"))
        .andExpect(model().attribute("amount", 1));
    assertEquals(photoGalleryService.getAmountOfPhotos(), 1);
  }

  @Test
  public void testSetBlackBackground() throws Exception {
    mockMvc.perform(get(URL + "/blackbackground"))
        .andExpect(status().isOk())
        .andExpect(view().name("index"))
        .andExpect(model().attribute("blackbackground", true));
  }

  @Test
  public void testSetOriginalAspectRatio() throws Exception {
    mockMvc.perform(get(URL + "/original"))
        .andExpect(status().isOk())
        .andExpect(view().name("index"))
        .andExpect(model().attribute("original", true));
  }

  @Test
  public void testSetCustomRow() throws Exception {
    mockMvc.perform(get(URL + "/row/5"))
        .andExpect(status().isOk())
        .andExpect(view().name("index"))
        .andExpect(model().attribute("row", 5));
  }

  @Test
  public void testSetCustomAspectRatio() throws Exception {
    mockMvc.perform(get(URL + "/wh/250x250"))
        .andExpect(status().isOk())
        .andExpect(view().name("index"))
        .andExpect(model().attribute("height", "250"))
        .andExpect(model().attribute("width", "250"));
  }

  @Test
  public void testGetPhoto() throws Exception {
    photoGalleryService.uploadPhotos(pathToImage);
    mockMvc.perform(get(URL + "/0"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.IMAGE_PNG));
  }
}