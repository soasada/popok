package com.popokis.popok.template;

import com.popokis.popok.http.response.Response;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TemplatingTest {
  @Test
  void htmlPageTemplateTest() {
    Map<String, Object> data = new HashMap<>();
    data.put("title", "Test Application");
    data.put("user", "New User");
    data.put("response", Response.ok("123"));

    String stringTemplate = Templating.getInstance().render("test.ftlh", data);

    String expected = "<html>\n" +
        "<head>\n" +
        "    <title>Test Application</title>\n" +
        "</head>\n" +
        "<body>\n" +
        "<h1>Welcome New User!</h1>\n" +
        "<p>Our latest response:\n" +
        "    <a href=\"OK\">123</a>!\n" +
        "</body>\n" +
        "</html>";

    assertEquals(expected, stringTemplate);
  }
}