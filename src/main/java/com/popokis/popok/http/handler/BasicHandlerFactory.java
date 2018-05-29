package com.popokis.popok.http.handler;

import com.popokis.popok.data.access.Database;
import com.popokis.popok.data.mapper.Mapper;
import com.popokis.popok.data.query.BasicRepository;
import com.popokis.popok.http.extractor.IdExtractor;
import com.popokis.popok.http.extractor.PostExtractor;
import com.popokis.popok.http.manipulator.BasicManipulator;
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
    return new SyncHandler<>(
        new PostExtractor(),
        loggerName,
        new JacksonDeserializator<>(requestType),
        createValidator,
        new BasicManipulator<>(),
        new InsertDBService<>(db, repository.saveQuery()),
        object -> "",
        new BasicManipulator<>());
  }

  public HttpHandler update() {
    return new SyncHandler<>(
        new PostExtractor(),
        loggerName,
        new JacksonDeserializator<>(requestType),
        updateValidator,
        new BasicManipulator<>(),
        new UpdateDBService<>(db, repository.modifyQuery()),
        object -> "",
        new BasicManipulator<>());
  }

  public HttpHandler remove() {
    return new SyncHandler<>(
        new IdExtractor(),
        loggerName,
        Long::parseLong,
        new IdValidator(new BasicValidator<>()),
        new BasicManipulator<>(),
        new RemoveDBService<>(db, repository.removeQuery()),
        object -> "",
        new BasicManipulator<>());
  }

  public HttpHandler search() {
    return new SyncHandler<>(
        new IdExtractor(),
        loggerName,
        Long::parseLong,
        new IdValidator(new BasicValidator<>()),
        new BasicManipulator<>(),
        new SearchDBService<>(db, repository.findQuery(), mapper),
        object -> "",
        new BasicManipulator<>());
  }

  public HttpHandler all() {
    return new SyncHandler<>(
        e -> "",
        loggerName,
        s -> null,
        new BasicValidator<>(),
        new BasicManipulator<>(),
        new DefaultAllService<>(db, repository.allQuery(), mapper),
        object -> "",
        new BasicManipulator<>());
  }
}
