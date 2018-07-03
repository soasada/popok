package com.popokis.popok.template;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

public final class Templating {

  private final Configuration cfg;

  private Templating() {
    cfg = new Configuration(Configuration.VERSION_2_3_28);
    cfg.setClassForTemplateLoading(Templating.class, "/");
    cfg.setDefaultEncoding("UTF-8");
    cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
    cfg.setLogTemplateExceptions(false);
    cfg.setWrapUncheckedExceptions(true);
  }

  private static class Holder {
    private static final Templating INSTANCE = new Templating();
  }

  public static Templating getInstance() {
    return Holder.INSTANCE;
  }

  public String render(String path, Map<String, Object> data) {
    try {
      Writer out = new StringWriter();
      Template template = cfg.getTemplate(path);
      template.process(data, out);
      return out.toString();
    } catch (IOException | TemplateException e) {
      throw new RuntimeException(e);
    }
  }
}
