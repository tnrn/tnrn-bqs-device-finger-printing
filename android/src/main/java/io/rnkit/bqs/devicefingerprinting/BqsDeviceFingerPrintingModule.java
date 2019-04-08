
package io.rnkit.bqs.devicefingerprinting;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

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
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.modules.core.PermissionAwareActivity;
import com.facebook.react.modules.core.PermissionListener;

public class BqsDeviceFingerPrintingModule extends ReactContextBaseJavaModule
        implements OnBqsDFListener, OnBqsDFContactsListener, OnBqsDFCallRecordListener, PermissionListener {

    private ReactApplicationContext reactContext;
    private Promise promise;
    private static final String TAG = "TouNa";

    private final String GRANTED = "granted";
    private final String DENIED = "denied";
    private final String NEVER_ASK_AGAIN = "never_ask_again";

    private BqsParams bqsParams;
    private static final int CODE_PERMISSIONS = 1000;
    private boolean isInitialize = false;

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
        BqsDF.getInstance().setOnBqsDFListener(this);
        BqsDF.getInstance().setOnBqsDFContactsListener(this);
        BqsDF.getInstance().setOnBqsDFCallRecordListener(this);

        bqsParams = new BqsParams();
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
        Log.d(TAG, "BQS requestMultiPermissions");

        //        getPermissionAwareActivity().requestPermissions(requestPermissions, CODE_PERMISSIONS, this);

        initBqsDFSDK();

    }

    private void initBqsDFSDK() {
        UiThreadUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "init BQS ");
                BqsDF.getInstance().initialize(reactContext.getCurrentActivity(), bqsParams);

            }
        });
    }

    @ReactMethod
    public void getTokenKey(@Nullable final Promise promise) {
        if (isInitialize) {
            String token = BqsDF.getInstance().getTokenKey();
            promise.resolve(token);
        } else  {
            promise.reject("10001", "请先进行初始化 -init()");
        }
    }

    @ReactMethod
    public void commitContacts(@Nullable final Promise promise) {
        this.promise = promise;
        BqsDF.getInstance().setOnBqsDFContactsListener(this);
        BqsDF.getInstance().setOnBqsDFCallRecordListener(this);
        BqsDF.getInstance().commitContactsAndCallRecords(true, false);
    }

    @ReactMethod
    public void commitLocaitonWithLongitude(final double latitude, final double longitude) {
        BqsDF.getInstance().commitLocation(latitude, longitude);
    }

    @ReactMethod
    public void commitLocaiton() {
        BqsDF.getInstance().commitLocation();
    }

    @Override
    public void onSuccess(String tokenKey) {
        Log.d(TAG, "init BQS onSuccess");
        isInitialize = true;
        if (this.promise != null) {
            this.promise.resolve(tokenKey);
        }
        this.clearUp();
    }

    @Override
    public void onFailure(String resultCode, String resultDesc) {
        Log.d(TAG, "init BQS onFailure resultCode = " + resultCode + ", resultDesc = " + resultDesc);
        isInitialize = false;
        if (this.promise != null) {
            this.promise.reject(resultCode, resultDesc);
        }
        this.clearUp();
    }

    @Override
    public void onGatherResult(boolean b) {

    }

    private void clearUp() {
        BqsDF.getInstance().setOnBqsDFListener(null);
        BqsDF.getInstance().setOnBqsDFContactsListener(null);
        BqsDF.getInstance().setOnBqsDFCallRecordListener(null);
        this.promise = null;
    }

    @Override
    public boolean onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (CODE_PERMISSIONS == requestCode) {
            final WritableMap grantedPermissions = new WritableNativeMap();
            int grantedCount = 0;
            PermissionAwareActivity activity = getPermissionAwareActivity();
            StringBuilder errResult = new StringBuilder();
            //            errResult.append("[");
            for (int j = 0, len = permissions.length; j < len; j++) {
                String permission = permissions[j];
                if (grantResults[j] == PackageManager.PERMISSION_GRANTED) {
                    //                    grantedPermissions.putString(permission, GRANTED);
                    grantedCount++;
                } else {
                    if (errResult.length() > 0) {
                        // 为了拼接json >_<
                        errResult.append(",");
                    }
                    if (activity.shouldShowRequestPermissionRationale(permission)) {
                        //                        grantedPermissions.putString(permission, DENIED);
                        errResult.append("{\"name\":\"").append(permission).append("\",\"grant\":\"").append(DENIED).append("\"}");
                    } else {
                        errResult.append("{\"name\":\"").append(permission).append("\",\"grant\":\"").append(NEVER_ASK_AGAIN).append("\"}");
                    }
                }
            }

            //            errResult.append("]");

            if (grantedCount == permissions.length) {
                initBqsDFSDK();
            } else {
                promise.reject("10002", "[" + errResult.toString() + "]");
            }
            return true;
        }

        return false;
    }


    private PermissionAwareActivity getPermissionAwareActivity() {
        Activity activity = getCurrentActivity();
        if (activity == null) {
            throw new IllegalStateException("Tried to use permissions API while not attached to an " +
                    "Activity.");
        } else if (!(activity instanceof PermissionAwareActivity)) {
            throw new IllegalStateException("Tried to use permissions API but the host Activity doesn't" +
                    " implement PermissionAwareActivity.");
        }
        return (PermissionAwareActivity) activity;
    }
}