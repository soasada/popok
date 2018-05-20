package com.popokis.popok.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HashTest {

  @Test
  void equalHashesTest() {
    assertNotEquals(Hash.createHash("hi"), Hash.createHash("hi"));
  }

  @Test
  void goodPasswordTest() {
    String hash = Hash.createHash("hi");
    String password = "hi";

    assertTrue(Hash.validatePassword(password, hash));
  }

  @Test
  void wrongPasswordTest() {
    String hash = Hash.createHash("hi");
    String wrongPassword = "hi5";

    assertFalse(Hash.validatePassword(wrongPassword, hash));
  }
}