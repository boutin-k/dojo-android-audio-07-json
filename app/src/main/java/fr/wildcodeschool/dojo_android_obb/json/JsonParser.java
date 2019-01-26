package fr.wildcodeschool.dojo_android_obb.json;

import android.util.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class JsonParser {
  private int mNumberOfSongs = 0;
  private List<Song> mSongs = new ArrayList<>();

  // Storage class
  public class Song {
    public String artist = "";
    public String title  = "";
    public String path   = "";
    public String cover  = "";
  }

  //Singleton
  private static final JsonParser mInstance = new JsonParser();
  public static JsonParser getInstance() {
    return mInstance;
  }
  private JsonParser() {}

  // Accessors / Getters
  public int getNumberOfSongs() { return mNumberOfSongs; }
  public Song getSong(int index) { return mSongs.get(index); }

  /**
   *
   * @param inputStream
   * @throws IOException
   */
  public void readJsonStream(InputStream inputStream) throws IOException {
    // try with statement works here because JsonReader implement Closeable interface
    try (JsonReader reader = new JsonReader(new InputStreamReader(inputStream))) {
      reader.beginObject();
      while (reader.hasNext()) {
        String lKey = reader.nextName();
        if ("numberOfSongs".equals(lKey)) {
          mNumberOfSongs = reader.nextInt();
          continue;
        }
        if ("songs".equals(lKey)) {
          readJsonArray(reader);
          continue;
        }
        reader.skipValue();
      }
      reader.endObject();
    }
  }

  /**
   *
   * @param reader
   * @throws IOException
   */
  private void readJsonArray(JsonReader reader) throws IOException {
    reader.beginArray();
    while (reader.hasNext()) {
      Song lSong = new Song();
      // JsonArray contains JsonObjects,
      // so we must parse the content of JsonObjects here
      reader.beginObject();
      while (reader.hasNext()) {
        String lKey = reader.nextName();
        if ("artist".equals(lKey)) {
          lSong.artist = reader.nextString();
          continue;
        }
        if ("title".equals(lKey)) {
          lSong.title = reader.nextString();
          continue;
        }
        if ("path".equals(lKey)) {
          lSong.path = reader.nextString();
          continue;
        }
        if ("cover".equals(lKey)) {
          lSong.cover = reader.nextString();
          continue;
        }
        reader.skipValue();
      }
      reader.endObject();
      // Store song data in the list
      mSongs.add(lSong);
    }
    reader.endArray();
  }
}