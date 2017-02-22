package com.airbnb.lottie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class AnimatableScaleValue extends BaseAnimatableValue<ScaleXY, ScaleXY> {
  AnimatableScaleValue(LottieComposition composition) {
    super(composition);
    initialValue = new ScaleXY();
  }

  AnimatableScaleValue(JSONObject scaleValues, int frameRate, LottieComposition composition,
      boolean isDp) {
    super(scaleValues, frameRate, composition, isDp);
  }

  @Override protected ScaleXY valueFromObject(Object object, float scale) throws JSONException {
    JSONArray array = (JSONArray) object;
    try {
      if (array.length() >= 2) {
        return new ScaleXY().scale((float) array.getDouble(0) / 100f * scale,
            (float) array.getDouble(1) / 100f * scale);
      }
    } catch (JSONException e) {
      // Do nothing.
    }

    return new ScaleXY();
  }

  @Override public KeyframeAnimation<ScaleXY> createAnimation() {
    if (!hasAnimation()) {
      return new StaticKeyframeAnimation<>(initialValue);
    }

    ScaleKeyframeAnimation animation =
        new ScaleKeyframeAnimation(duration, composition, keyTimes, keyValues, interpolators);
    animation.setStartDelay(delay);
    return animation;
  }
}
