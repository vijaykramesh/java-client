package com.launchdarkly.client;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class VariationOrRolloutTest {
  @Test
  public void canBucketByIntAttributeSameAsString() {
    LDUser user = new LDUser.Builder("key")
        .custom("stringattr", "33333")
        .custom("intattr", 33333)
        .build();
    float resultForString = VariationOrRollout.bucketUser(user, "key", "stringattr", "salt");
    float resultForInt = VariationOrRollout.bucketUser(user, "key", "intattr", "salt");
    assertEquals(resultForString, resultForInt, Float.MIN_VALUE);
  }

  @Test
  public void cannotBucketByFloatAttribute() {
    LDUser user = new LDUser.Builder("key")
        .custom("floatattr", 33.5f)
        .build();
    float result = VariationOrRollout.bucketUser(user, "key", "floatattr", "salt");
    assertEquals(0f, result, Float.MIN_VALUE);
  }

  @Test
  public void cannotBucketByBooleanAttribute() {
    LDUser user = new LDUser.Builder("key")
        .custom("boolattr", true)
        .build();
    float result = VariationOrRollout.bucketUser(user, "key", "boolattr", "salt");
    assertEquals(0f, result, Float.MIN_VALUE);
  }
}
