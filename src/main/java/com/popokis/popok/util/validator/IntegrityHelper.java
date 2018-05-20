package com.popokis.popok.util.validator;

import java.time.LocalDate;

public final class IntegrityHelper {

  private IntegrityHelper() {
  }

  public static boolean sameLengthMustBeMessage(int externalLength, int internalLength, String fieldName) {
    return IntegrityHelper.sameLength(
        externalLength,
        internalLength,
        ValidationMessage.getString(ValidationMessage.THE_FIELD_MUST_BE_LENGTH, fieldName) + internalLength
    );
  }

  private static boolean sameLength(int externalLength, int internalLength, String message) {
    if (externalLength != internalLength) {
      throw new RuntimeException(message);
    }

    return true;
  }

  public static boolean lessThanLengthMustBeLessThanMessage(int externalLength, int internalLength, String fieldName) {
    return IntegrityHelper.lessThanLength(
        externalLength,
        internalLength,
        ValidationMessage.getString(ValidationMessage.THE_FIELD_MUST_BE_LESS_THAN_LENGTH, fieldName) + internalLength
    );
  }

  private static boolean lessThanLength(int externalLength, int internalLength, String message) {
    if (externalLength > internalLength) {
      throw new RuntimeException(message);
    }
    return true;
  }

  public static boolean lessThanZeroBePositiveMessage(long value, String fieldName) {
    return IntegrityHelper.lessThanZero(value, ValidationMessage.getString(ValidationMessage.THE_FIELD_MUST_BE_POSITIVE, fieldName));
  }

  private static boolean lessThanZero(long id, String message) {
    if (id <= 0L) {
      throw new RuntimeException(message);
    }
    return true;
  }

  public static boolean lessOrEqualThanZeroBePositiveMessage(long value, String fieldName) {
    return IntegrityHelper.lessOrEqualThanZero(value, ValidationMessage.getString(ValidationMessage.THE_FIELD_MUST_BE_ZERO_OR_POSITIVE, fieldName));
  }

  private static boolean lessOrEqualThanZero(long id, String message) {
    if (id < 0L) {
      throw new RuntimeException(message);
    }
    return true;
  }

  public static boolean notEqualNumberCannotBeSameMessage(long a, long b) {
    return IntegrityHelper.notEqual(a, b, ValidationMessage.getString(ValidationMessage.THE_NUMBER_CANNOT_BE_THE_SAME, ""));
  }

  private static boolean notEqual(long a, long b, String message) {
    if (a == b) {
      throw new RuntimeException(message);
    }
    return true;
  }

  public static boolean isValidDate(LocalDate date) {
    return date.isAfter(LocalDate.now()) || date.isEqual(LocalDate.now());
  }
}

