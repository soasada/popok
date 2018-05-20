package com.popokis.popok.util.validator;

public abstract class ValidatorDecorator<T> implements Validator<T> {

  private final Validator<T> validator;

  public ValidatorDecorator(Validator<T> validator) {
    this.validator = validator;
  }

  @Override
  public void validate(T model) {
    validator.validate(model);
  }
}
