package com.launchdarkly.client;

import com.google.gson.JsonElement;

abstract class EventFactory {
  public static final EventFactory DEFAULT = new DefaultEventFactory();
  
  protected abstract long getTimestamp();
  
  public Event.FeatureRequest newFeatureRequestEvent(FeatureFlag flag, LDUser user, FeatureFlag.VariationAndValue result, JsonElement defaultVal) {
    return new Event.FeatureRequest(getTimestamp(), flag.getKey(), user, flag.getVersion(),
        result == null ? null : result.getVariation(), result == null ? null : result.getValue(),
        defaultVal, null, flag.isTrackEvents(), flag.getDebugEventsUntilDate(), false);
  }
  
  public Event.FeatureRequest newDefaultFeatureRequestEvent(FeatureFlag flag, LDUser user, JsonElement defaultValue) {
    return new Event.FeatureRequest(getTimestamp(), flag.getKey(), user, flag.getVersion(),
        null, defaultValue, defaultValue, null, flag.isTrackEvents(), flag.getDebugEventsUntilDate(), false);
  }
  
  public Event.FeatureRequest newUnknownFeatureRequestEvent(String key, LDUser user, JsonElement defaultValue) {
    return new Event.FeatureRequest(getTimestamp(), key, user, null, null, defaultValue, defaultValue, null, false, null, false);
  }
  
  public Event.FeatureRequest newPrerequisiteFeatureRequestEvent(FeatureFlag prereqFlag, LDUser user, FeatureFlag.VariationAndValue result,
      FeatureFlag prereqOf) {
    return new Event.FeatureRequest(getTimestamp(), prereqFlag.getKey(), user, prereqFlag.getVersion(),
        result == null ? null : result.getVariation(), result == null ? null : result.getValue(),
        null, prereqOf.getKey(), prereqFlag.isTrackEvents(), prereqFlag.getDebugEventsUntilDate(), false);
  }

  public Event.FeatureRequest newDebugEvent(Event.FeatureRequest from) {
    return new Event.FeatureRequest(from.creationDate, from.key, from.user, from.version, from.variation, from.value,
        from.defaultVal, from.prereqOf, from.trackEvents, from.debugEventsUntilDate, true);
  }
  
  public Event.Custom newCustomEvent(String key, LDUser user, JsonElement data) {
    return new Event.Custom(getTimestamp(), key, user, data);
  }
  
  public Event.Identify newIdentifyEvent(LDUser user) {
    return new Event.Identify(getTimestamp(), user);
  }
  
  public static class DefaultEventFactory extends EventFactory {
    @Override
    protected long getTimestamp() {
      return System.currentTimeMillis();
    }
  }
}
