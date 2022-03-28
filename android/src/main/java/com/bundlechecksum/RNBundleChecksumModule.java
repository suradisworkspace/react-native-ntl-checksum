
package com.bundlechecksum;

import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.AssetManager;
import android.util.Log;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;

import java.io.InputStream;
import java.io.BufferedReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class RNBundleChecksumModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;


    public RNBundleChecksumModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "RNBundleChecksum";
    }

    @ReactMethod
    public void getChecksum(Promise promise) {
        byte[] hash = null;
        StringBuilder sb = new StringBuilder();
        try {
            AssetManager assetManager = getReactApplicationContext().getAssets();
            InputStream stream = assetManager.open("index.android.bundle");
            if (stream == null) {
                promise.resolve("");
                return;
            }
            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            hash = MessageDigest.getInstance("SHA-256").digest(buffer);
            if (hash == null) {
                promise.resolve("");
                return;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            promise.resolve("");
            return;
        }

        MessageDigest md = null;
        try {
            for (int i = 0; i < hash.length; i++) {
                sb.append(Integer.toString((hash[i] & 0xff) + 0x100, 16).substring(1));
            }

            promise.resolve(sb.toString());
            return;
        } catch (Exception e) {
            e.printStackTrace();
            promise.resolve("");
            return;
        }
    }

    @ReactMethod
    public void getChecksumCert(String certName, Promise promise) {
        byte[] hash = null;
        StringBuilder sb = new StringBuilder();
        try {
            AssetManager assetManager = getReactApplicationContext().getAssets();
            InputStream stream = assetManager.open(certName.toString() + ".cer");
            if (stream == null) {
                promise.resolve("");
                return;
            }

            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            hash = MessageDigest.getInstance("SHA-256").digest(buffer);
            if (hash == null) {
                promise.resolve("");
                return;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            promise.resolve("");
            return;
        }

        try {
            for (int i = 0; i < hash.length; i++) {
                sb.append(Integer.toString((hash[i] & 0xff) + 0x100, 16).substring(1));
            }

            promise.resolve(sb.toString());
            return;
        } catch (Exception e) {
            e.printStackTrace();
            promise.resolve("");
            return;
        }
    }

    @ReactMethod
    public void getSumMETA(Promise promise) {
        promise.resolve("");
        return;
//        try {
//            Signature sigs = reactContext.getPackageManager().getPackageInfo(reactContext.getPackageName(), PackageManager.GET_SIGNATURES).signatures[0];
//
//            promise.resolve(sigs.hashCode());
//        } catch (Exception e) {
//            promise.resolve("");
//        }
//
//        return;
    }


}
