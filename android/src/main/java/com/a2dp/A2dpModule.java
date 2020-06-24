package com.a2dp;

import android.content.Context;
import android.media.AudioManager;

import androidx.annotation.Nullable;

import com.bluetooth.Bluetooth;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

public class A2dpModule extends ReactContextBaseJavaModule {
    private Bluetooth bluetooth = null;// 蓝牙实例
    private AudioManager mAudioManager = null;
    public static ReactApplicationContext reactContext;

    public A2dpModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        mAudioManager = (AudioManager) reactContext.getSystemService(Context.AUDIO_SERVICE);
         bluetooth = new Bluetooth(this.reactContext);//初始化
    }

    @Override
    public String getName() {
        return "A2dp";
    }

    /**
     * 已配对蓝牙列表
     */
    @ReactMethod
    public void deviceList(Promise promise) {
        bluetooth.list(promise);
    }

    /**
     * 扫描未配对的设备
     */
    @ReactMethod
    public void scan(Promise promise) {
        bluetooth.discoverUnpairedDevices();
        promise.resolve(true);
    }

    /**
     * 连接A2dp
     */
    @ReactMethod
    public void connectA2dp(String id,Promise promise) {
        if (bluetooth != null) {
            bluetooth.connectA2dp(id,promise);
        }else{
            promise.reject("初始化错误");
        };
    }
    /**
     * 关闭A2dp
     */
    @ReactMethod
    public void disconnectA2dp(Promise promise) {
        if (bluetooth != null) {
            bluetooth.disconnectA2dp();
        };
        promise.resolve(true);
    }
    /**
     * 开启SCO
     * 
     * @param promise
     */
    @ReactMethod
    public void startBluetoothSco(Promise promise) {
        mAudioManager.setBluetoothScoOn(true);
        mAudioManager.startBluetoothSco();
        promise.resolve(true);
    }

    /**
     * 停止SCO
     * 
     * @param promise
     */
    @ReactMethod
    public void stopBluetoothSco(Promise promise) {
        mAudioManager.setBluetoothScoOn(false);
        mAudioManager.stopBluetoothSco();
        promise.resolve(true);
    }

    // 发送事件到js
    public static void sendEvent(String eventName, @Nullable WritableMap params) {
        A2dpModule.reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName, params);
    }

    public static void sendEvent(String eventName, String params) {
        A2dpModule.reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName, params);
    }
}
