import { NativeModules, Platform } from 'react-native';

const LINKING_ERROR =
  `The package 'react-native-truecaller-oauth-sdk' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

const TruecallerOauthSdk = NativeModules.TruecallerOauthSdk
  ? NativeModules.TruecallerOauthSdk
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

export function multiply(a: number, b: number): Promise<number> {
  return TruecallerOauthSdk.multiply(a, b);
}

export function toast(s: string): void {
  TruecallerOauthSdk.toast(s);
}

export function isUsable(): Promise<void> {
  return TruecallerOauthSdk.isUsable();
}

export function authenticate(): Promise<void> {
  return TruecallerOauthSdk.authenticate();
}
