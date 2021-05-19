package com.example.starter.handler.idValidator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class idValidatorTest {

  @Test
  void validate() {
  }

  @Test
  void testValidate() {
    String input = "1";
    boolean output = IdValidatorHandler.validateHelper(input);
    assertTrue(output);
  }
  @Test
  void testValidateFalse() {
    String input = "a";
    boolean output = IdValidatorHandler.validateHelper(input);
    assertFalse(output);
  }
}
