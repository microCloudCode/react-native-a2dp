import { NativeModules,NativeEventEmitter } from 'react-native';

const { A2dp } = NativeModules;

const eventEmitter = new NativeEventEmitter(A2dp);

A2dp.on = (eventName, handler) => {
    eventEmitter.addListener(eventName, handler)
}

A2dp.removeListener = (eventName, handler) => {
    eventEmitter.removeListener(eventName, handler)
}

export default A2dp;
