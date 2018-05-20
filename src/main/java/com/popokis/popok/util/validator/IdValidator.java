package com.popokis.popok.util.validator;

public final class IdValidator extends ValidatorDecorator<Long> {

  public IdValidator(Validator<Long> validator) {
    super(validator);
  }

  @Override
  public void validate(Long id) {
    super.validate(id);

    if (id <= 0) {
      throw new RuntimeException("Id must be positive >= 1.");
    }
  }
}
