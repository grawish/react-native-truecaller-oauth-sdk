package com.truecalleroauthsdk;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.BaseActivityEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.module.annotations.ReactModule;

import com.truecaller.android.sdk.oAuth.CodeVerifierUtil;
import com.truecaller.android.sdk.oAuth.TcOAuthCallback;
import com.truecaller.android.sdk.oAuth.TcOAuthData;
import com.truecaller.android.sdk.oAuth.TcOAuthError;
import com.truecaller.android.sdk.oAuth.TcSdk;
import com.truecaller.android.sdk.oAuth.TcSdkOptions;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Map;

@ReactModule(name = TruecallerOauthSdkModule.NAME)
public class TruecallerOauthSdkModule extends ReactContextBaseJavaModule {
  public static final String NAME = "TruecallerOauthSdk";

  private Promise promise = null;
  private String codeVerifier = "";

  private final TcOAuthCallback tcOAuthCallback = new TcOAuthCallback() {
        @Override
        public void onSuccess(@NonNull TcOAuthData tcOAuthData) {
            Log.w("tcdebug", "Success => " + tcOAuthData);
            promise.resolve("{"+'"'+"codeVerifier"+'"'+": "+'"'+codeVerifier+'"'+", "+'"'+"authToken"+'"'+": "+'"'+ tcOAuthData.getAuthorizationCode() + '"' +", "+'"'+"isSuccess"+'"'+": true }");
        }

        @Override
        public void onFailure(@NonNull TcOAuthError tcOAuthError) {
            Log.w("tcdebug", "Error => " + tcOAuthError);
            promise.reject("Error",tcOAuthError.getErrorMessage());
        }
    };

  public TruecallerOauthSdkModule(ReactApplicationContext reactContext) {
    super(reactContext);
    TcSdkOptions tcSdkOptions = new TcSdkOptions.Builder(reactContext, tcOAuthCallback);
    TcSdk.init(tcSdkOptions);
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
    Toast.makeText(getReactApplicationContext(),s,Toast.LENGTH_LONG).show();
    promise.resolve(null);
  }

   @ReactMethod
      public void isUsable(Promise promise) {
          try {
              if (TcSdk.getInstance() != null) {
                  if (TcSdk.getInstance().isOAuthFlowUsable()) {
                      promise.resolve(true);
                  }
                  promise.resolve(false);
              }
              promise.resolve(false);
          } catch (Exception e) {
              promise.reject(e);
          }
      }

  @ReactMethod
      public void authenticate(Promise promise) {
          try {
              this.promise = promise;
              Log.w("tcdebug", "Shuru Karein!");
              if (TcSdk.getInstance() != null) {
                  Log.w("tcdebug", "TC instance is not Null");
                  if (TcSdk.getInstance().isOAuthFlowUsable()) {
                      Log.w("tcdebug", "TC isOAuth is Usable!");
                      String stateRequested = new BigInteger(130, new SecureRandom()).toString(32); // step 9
                      TcSdk.getInstance().setOAuthState(stateRequested);
                      String[] scopes = new String[]{"profile", "phone", "openid", "offline_access"}; // step 10
                      TcSdk.getInstance().setOAuthScopes(scopes);
                      this.codeVerifier = CodeVerifierUtil.Companion.generateRandomCodeVerifier();
                      String codeChallenge = CodeVerifierUtil.Companion.getCodeChallenge(this.codeVerifier);
                      if (codeChallenge != null) {
                          Log.w("tcdebug", "Code Challenge is Present! " + codeChallenge + " <- codeVerifier -> "+ codeVerifier);
                          TcSdk.getInstance().setCodeChallenge(codeChallenge);
                          TcSdk.getInstance().getAuthorizationCode((FragmentActivity) getCurrentActivity());
                      } else {
                          Log.w("tcdebug", "Code challenge is Null. Can't proceed further");
                      }
                  } else {
                      Log.w("tcdebug", "TC isOAuth not Usable");
                      this.promise.resolve("{\"isSuccess\": false,\"error\": \"TrueCaller is Not Usable\" }");
                  }

              } else {
                  this.promise.reject("Error: ", "Rejected!");
              }
          } catch (Exception e) {
              this.promise.reject(e);
          }
      }

}
