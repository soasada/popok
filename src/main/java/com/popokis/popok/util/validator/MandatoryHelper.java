package com.popokis.popok.util.validator;

import java.util.Objects;

public final class MandatoryHelper {

  private MandatoryHelper() {
  }

  public static boolean nullStringRequiredMessage(String value, String fieldName) {
    return MandatoryHelper.nullString(value, ValidationMessage.getString(ValidationMessage.A_FIELD_IS_REQUIRED, fieldName));
  }

  private static boolean nullString(String value, String message) {
    if (Objects.isNull(value)) {
      throw new RuntimeException(message);
    }

    return true;
  }

  public static boolean emptyStringCannotBeEmptyMessage(String value, String fieldName) {
    return MandatoryHelper.emptyString(value, ValidationMessage.getString(ValidationMessage.FIELD_CANNOT_BE_EMPTY, fieldName));
  }

  private static boolean emptyString(String value, String message) {
    if (value.isEmpty()) {
      throw new RuntimeException(message);
    }

    return true;
  }
}

