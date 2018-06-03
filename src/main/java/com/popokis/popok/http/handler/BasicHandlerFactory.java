package com.popokis.popok.http.handler;

import com.popokis.popok.data.access.Database;
import com.popokis.popok.data.mapper.Mapper;
import com.popokis.popok.data.query.BasicRepository;
import com.popokis.popok.http.extractor.IdExtractor;
import com.popokis.popok.http.extractor.PostExtractor;
import com.popokis.popok.serialization.json.JacksonDeserializator;
import com.popokis.popok.service.db.DefaultAllService;
import com.popokis.popok.service.db.InsertDBService;
import com.popokis.popok.service.db.RemoveDBService;
import com.popokis.popok.service.db.SearchDBService;
import com.popokis.popok.service.db.UpdateDBService;
import com.popokis.popok.util.validator.BasicValidator;
import com.popokis.popok.util.validator.IdValidator;
import com.popokis.popok.util.validator.Validator;
import io.undertow.server.HttpHandler;

public final class BasicHandlerFactory<T> {

  private final Validator<T> createValidator;
  private final Validator<T> updateValidator;
  private final String loggerName;
  private final Class<T> requestType;
  private final BasicRepository<T> repository;
  private final Mapper<T> mapper;
  private final Database db;

  public BasicHandlerFactory(Validator<T> createValidator,
                             Validator<T> updateValidator,
                             String loggerName,
                             Class<T> requestType,
                             BasicRepository<T> repository,
                             Mapper<T> mapper,
                             Database db) {
    this.createValidator = createValidator;
    this.updateValidator = updateValidator;
    this.loggerName = loggerName;
    this.requestType = requestType;
    this.repository = repository;
    this.mapper = mapper;
    this.db = db;
  }

  public HttpHandler create() {
    return new DataBaseHandler<>(
        new PostExtractor(),
        loggerName,
        new JacksonDeserializator<>(requestType),
        createValidator,
        new InsertDBService<>(db, repository.saveQuery()));
  }

  public HttpHandler update() {
    return new DataBaseHandler<>(
        new PostExtractor(),
        loggerName,
        new JacksonDeserializator<>(requestType),
        updateValidator,
        new UpdateDBService<>(db, repository.modifyQuery()));
  }

  public HttpHandler remove() {
    return new DataBaseHandler<>(
        new IdExtractor(),
        loggerName,
        Long::parseLong,
        new IdValidator(new BasicValidator<>()),
        new RemoveDBService<>(db, repository.removeQuery()));
  }

  public HttpHandler search() {
    return new DataBaseHandler<>(
        new IdExtractor(),
        loggerName,
        Long::parseLong,
        new IdValidator(new BasicValidator<>()),
        new SearchDBService<>(db, repository.findQuery(), mapper));
  }

  public HttpHandler all() {
    return new DataBaseHandler<>(
        e -> "",
        loggerName,
        s -> null,
        new BasicValidator<>(),
        new DefaultAllService<>(db, repository.allQuery(), mapper));
  }
}
