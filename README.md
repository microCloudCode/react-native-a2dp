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
