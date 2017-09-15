
package io.rnkit.bqs.devicefingerprinting;

import android.support.annotation.Nullable;

import com.bqs.risk.df.android.BqsDF;
import com.bqs.risk.df.android.BqsParams;
import com.bqs.risk.df.android.OnBqsDFCallRecordListener;
import com.bqs.risk.df.android.OnBqsDFContactsListener;
import com.bqs.risk.df.android.OnBqsDFListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.UiThreadUtil;

public class BqsDeviceFingerPrintingModule extends ReactContextBaseJavaModule implements OnBqsDFListener, OnBqsDFContactsListener, OnBqsDFCallRecordListener {

    private ReactApplicationContext reactContext;
    private Promise promise;

    public BqsDeviceFingerPrintingModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "RNKitBqsDeviceFingerPrinting";
    }

    @ReactMethod
    public void init(ReadableMap args, @Nullable final Promise promise) throws Exception {
        this.promise = promise;
        //1、添加回调
        BqsDF.setOnBqsDFListener(this);
        BqsDF.setOnBqsDFContactsListener(this);
        BqsDF.setOnBqsDFCallRecordListener(this);

        final BqsParams bqsParams = new BqsParams();
        if (args.hasKey("partnerId")) {
            bqsParams.setPartnerId(args.getString("partnerId"));
        } else {
            throw new Exception("partnerId is null!");
        }
        if (args.hasKey("isGatherGps")) {
            bqsParams.setGatherGps(args.getBoolean("isGatherGps"));
        }
        if (args.hasKey("isGatherContacts")) {
            bqsParams.setGatherContact(args.getBoolean("isGatherContacts"));
        }
        if (args.hasKey("isTestingEnv")) {
            bqsParams.setTestingEnv(args.getBoolean("isTestingEnv"));
        }
        if (args.hasKey("isGatherSensorInfo")) {
            bqsParams.setGatherSensorInfo(args.getBoolean("isGatherSensorInfo"));
        }

        UiThreadUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                BqsDF.initialize(reactContext.getCurrentActivity(), bqsParams);
            }
        });
    }

    @ReactMethod
    public void getTokenKey(@Nullable final Promise promise) {
        String token = BqsDF.getTokenKey();
        promise.resolve(token);
    }

    @ReactMethod
    public void commitContacts(@Nullable final Promise promise) {
        this.promise = promise;
        BqsDF.setOnBqsDFContactsListener(this);
        BqsDF.setOnBqsDFCallRecordListener(this);
        BqsDF.commitContactsAndCallRecords(true, false);
    }

    @ReactMethod
    public void commitLocaitonWithLongitude(final double latitude, final double longitude) {
        BqsDF.commitLocation(latitude, longitude);
    }

    @ReactMethod
    public void commitLocaiton() {
        BqsDF.commitLocation();
    }

    @Override
    public void onSuccess(String tokenKey) {
        if (this.promise != null) this.promise.resolve(tokenKey);
        this.clearUp();
    }

    @Override
    public void onFailure(String resultCode, String resultDesc) {
        if (this.promise != null) this.promise.reject(resultCode, resultDesc);
        this.clearUp();
    }

    @Override
    public void a(boolean b) {}

    private void clearUp() {
        BqsDF.setOnBqsDFListener(null);
        BqsDF.setOnBqsDFContactsListener(null);
        BqsDF.setOnBqsDFCallRecordListener(null);
        this.promise = null;
    }
}