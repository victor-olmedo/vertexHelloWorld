package com.example.starter.idValidator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class idValidatorTest {

  @Test
  void validate() {
  }

  @Test
  void testValidate() {
    String input = "1";
    boolean output = idValidator.validate(input);
    assertTrue(output);
  }
  @Test
  void testValidateFalse() {
    String input = "a";
    boolean output = idValidator.validate(input);
    assertFalse(output);
  }
}
