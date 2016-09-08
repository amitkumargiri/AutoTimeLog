package resource;

import java.net.URL;

public class GetResource {

  public static URL getResource(String name) {
    return GetResource.class.getResource(name);
  }

  public static String getPath() {
    URL url = GetResource.class.getResource("GetResource.class");
    String path = url.toString();
    return path.substring(5, path.lastIndexOf("/")).replace("/", "\\");
  }
}
