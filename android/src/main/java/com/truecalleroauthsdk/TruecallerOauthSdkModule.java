package com.truecalleroauthsdk;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.module.annotations.ReactModule;

@ReactModule(name = TruecallerOauthSdkModule.NAME)
public class TruecallerOauthSdkModule extends ReactContextBaseJavaModule {
  public static final String NAME = "TruecallerOauthSdk";

  public TruecallerOauthSdkModule(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @Override
  @NonNull
  public String getName() {
    return NAME;
  }


  // Example method
  // See https://reactnative.dev/docs/native-modules-android
  @ReactMethod
  public void multiply(double a, double b, Promise promise) {
    promise.resolve(a * b);
  }
  @ReactMethod
  public void toast(String s, Promise promise) {
    Toast.makeText(getReactApplicationContext(),s,Toast.LENGTH_SHORT);
    promise.resolve(null);
  }
}
