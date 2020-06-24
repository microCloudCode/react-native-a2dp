package com.bluetooth;

import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;

import java.lang.reflect.Method;

class BluetoothService extends Thread {
    private BluetoothAdapter bluetoothAdapter;// 本地的蓝牙适配器
    private BluetoothA2dp mA2dp;
    BluetoothDevice mmDevice = null;

    public BluetoothService(ReactApplicationContext reactContext, BluetoothAdapter bluetoothAdapter) {
        this.bluetoothAdapter = bluetoothAdapter;
        // 获取配置代理对象
        bluetoothAdapter.getProfileProxy(reactContext, new BluetoothProfile.ServiceListener() {
            @Override
            public void onServiceConnected(int profile, BluetoothProfile proxy) {
                if (profile == BluetoothProfile.A2DP) {
                    mA2dp = (BluetoothA2dp) proxy;
                }
            }

            @Override
            public void onServiceDisconnected(int profile) {
                if (profile == BluetoothProfile.A2DP) {
                    mA2dp = null;
                }
            }
        }, BluetoothProfile.A2DP);
    }

    // 连接至A2dp
    public void connectA2dp(BluetoothDevice device, Promise promise) {
        mmDevice = device;
        try {
            // 通过反射获取BluetoothA2dp中connect方法（hide的），进行连接。
            Method connectMethod = BluetoothA2dp.class.getMethod("connect", BluetoothDevice.class);
            connectMethod.invoke(mA2dp, mmDevice);
        } catch (Exception e) {
            e.printStackTrace();
            promise.reject("连接错误");
        }
    }

    // 断开A2dp连接
    public void disConnectA2dp() {
        try {
            // 通过反射获取BluetoothA2dp中connect方法（hide的），断开连接。
            Method connectMethod = BluetoothA2dp.class.getMethod("disconnect", BluetoothDevice.class);
            connectMethod.invoke(mA2dp, mmDevice);
            mmDevice = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//    配对
    public boolean createBond(BluetoothDevice device) {
        try {
            Method boned = device.getClass().getMethod("createBond");
            return (boolean) boned.invoke(device);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
