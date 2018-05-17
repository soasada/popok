package com.popokis.popok.serialization.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public final class CustomObjectMapper {

  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
  private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_TIME;
  private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

  private final ObjectMapper mapper;

  private CustomObjectMapper() {
    mapper = new ObjectMapper();
    Jackson8Module jackson8Module = new Jackson8Module();

    jackson8Module.addStringSerializer(Timestamp.class, (val) -> val.toLocalDateTime().toString());
    jackson8Module.addStringSerializer(LocalDate.class, val -> val.format(DATE_FORMATTER));
    jackson8Module.addStringSerializer(LocalTime.class, val -> val.format(TIME_FORMATTER));
    jackson8Module.addStringSerializer(LocalDateTime.class, val -> val.format(DATETIME_FORMATTER));

    mapper.findAndRegisterModules();
    mapper.registerModule(jackson8Module);
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    mapper.enable(DeserializationFeature.USE_LONG_FOR_INTS);
  }

  private static class Holder {
    private static final CustomObjectMapper INSTANCE = new CustomObjectMapper();
  }

  public static CustomObjectMapper getInstance() {
    return Holder.INSTANCE;
  }

  public ObjectMapper mapper() {
    return mapper;
  }

  public String toJson(Object o) {
    try {
      return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(o);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e.getMessage());
    }
  }
}
