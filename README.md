# react-native-a2dp

## 支持平台
+ Android

## 安装

`$ npm install react-native-a2dp --save`

## 例子
```javascript
import A2dp from 'react-native-a2dp';

let deviceList = await A2dp.deviceList()//获取已匹配列表
await A2dp.connectA2dp(deviceList[0].id)//连接A2DP
A2dp.startBluetoothSco();//开启SCO

```
## 方法

### deviceList()
获取已配对蓝牙列表。返回`Promise`

### scan()
开始扫描设备。返回`Promise`

### connectA2dp(id)
以A2DP方式连接蓝牙。返回`Promise` resolve=连接成功

**参数**
- `id` - `String` - 蓝牙设备ID

### disconnectA2dp()
断开连接。该方法会立即返回`Promise`，不可用于已断开判断

### startBluetoothSco()
开启SCO通道。返回一个`Promise` 开启之后即可通过蓝牙设备采集音频数据

### stopBluetoothSco()
关闭SCO通道。返回一个`Promise`

## 事件

### device
发现新设备

**参数**

- `name` - `String` - 设备名称
- `id` - `Number` - 硬件地址

### connectSucceeded
连接成功

**参数**

- `none`

### connectDisconnect
连接断开

**参数**

- `none`

### scanStop
扫描完毕

**参数**

- `none`