package com.popokis.popok.util.validator;

public enum ValidationMessage {
  A_FIELD_IS_REQUIRED("A $field field is required"),
  FIELD_CANNOT_BE_EMPTY("$field field cannot be empty"),
  A_MODEL_FIELD_WITH_ID_IS_REQUIRED("Model with at least an id attribute needed"),
  THE_FIELD_MUST_BE_LENGTH("The $field field must be size "),
  THE_FIELD_MUST_BE_LESS_THAN_LENGTH("The $field field must be size less than "),
  THE_FIELD_MUST_BE_POSITIVE("The $field field must be positive"),
  THE_FIELD_MUST_BE_ZERO_OR_POSITIVE("The $field field must be zero or positive"),
  THE_NUMBER_CANNOT_BE_THE_SAME("The numbers cannot be the same");

  private final String message;

  ValidationMessage(String message) {
    this.message = message;
  }

  public String message() {
    return message;
  }

  public static String getString(ValidationMessage validationMessage, String field) {
    return validationMessage.message().replace("$field", field);
  }
}
