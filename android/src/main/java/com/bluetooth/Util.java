package com.bluetooth;

import android.bluetooth.BluetoothDevice;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;

public class Util {
    public static WritableMap deviceToWritableMap(BluetoothDevice device) {
        WritableMap params = Arguments.createMap();

        params.putString("name", device.getName());
        params.putString("address", device.getAddress());
        params.putString("id", device.getAddress());

        if (device.getBluetoothClass() != null) {
            params.putInt("class", device.getBluetoothClass().getDeviceClass());
        }

        return params;
    }
}
