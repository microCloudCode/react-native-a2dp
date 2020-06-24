package com.bluetooth;

import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.a2dp.A2dpModule;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;

import java.util.Set;

/**
 * 蓝牙
 */
public class Bluetooth {
    public BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private Promise mDeviceDiscoveryPromise=null;//设备发现钩子
    private Promise connectPromise=null;//连接钩子
    private ReactApplicationContext mReactContext;
    private BluetoothService bluetoothService;
    private BlueToothtReceiver blueToothtReceiver;

    public Bluetooth(ReactApplicationContext reactContext) {
        mReactContext = reactContext;
        bluetoothService = new BluetoothService(reactContext, bluetoothAdapter);
        this.registerBluetoothDeviceReceiver();
    }

    /**
     * 获取已配对蓝牙列表
     */
    public void list(Promise promise) {
        WritableArray deviceList = Arguments.createArray();
        if (bluetoothAdapter != null) {
            Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();
            for (BluetoothDevice rawDevice : bondedDevices) {
                WritableMap device = Util.deviceToWritableMap(rawDevice);
                deviceList.pushMap(device);
            }
        }
        promise.resolve(deviceList);
    }

    // 发现未配对设备
    public void discoverUnpairedDevices(Promise promise) {
        mDeviceDiscoveryPromise = promise;
        bluetoothAdapter.startDiscovery();
    }

    // 连接A2DP
    public void connectA2dp(String id,Promise promise) {
        bluetoothAdapter.cancelDiscovery();// 取消发现
        connectPromise=promise;
        if (bluetoothAdapter != null) {
            BluetoothDevice device = bluetoothAdapter.getRemoteDevice(id);
            if (device != null) {
                boolean isok=bluetoothService.createBond(device);
                bluetoothService.connectA2dp(device,promise);
            }else {
                promise.reject("找不到设备");
            }
        }
    }
    // 断开A2DP
    public void disconnectA2dp() {
        bluetoothService.disConnectA2dp();
    }

    // 注册广播
    private void registerBluetoothDeviceReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        intentFilter.addAction(BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        intentFilter.setPriority(Integer.MAX_VALUE);//设置优先级
        blueToothtReceiver = new BlueToothtReceiver();//注册
        mReactContext.registerReceiver(blueToothtReceiver, intentFilter);
    }

    public class  BlueToothtReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {//发现新设备
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                WritableMap d = Util.deviceToWritableMap(device);
                A2dpModule.sendEvent("device", d);
            }
            if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {//搜索结束
                mDeviceDiscoveryPromise.resolve("搜索结束");
            }

            if(BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED.equals(action)){//蓝牙连接成功
                int currentState= intent.getIntExtra(BluetoothA2dp.EXTRA_STATE, -1);//当前状态
                if(currentState == BluetoothA2dp.STATE_CONNECTED) {//连接成功
                    connectPromise.resolve("A2dp连接成功");
                    A2dpModule.sendEvent("connectSucceeded", "");
                }
                if(currentState == BluetoothA2dp.STATE_DISCONNECTED){//断开连接
                    A2dpModule.sendEvent("connectDisconnect", "");
                    connectPromise.reject("连接失败");
                }
            }

            if(BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)){//配对状态
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(device.getBondState()==BluetoothDevice.BOND_NONE){//取消配对/未配对
                    connectPromise.reject("配对失败");
                }
            }
        }
    }
}
