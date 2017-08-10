/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { Component } from 'react';
import {
  AppRegistry,
  StyleSheet,
  Text,
  View,
  Button
} from 'react-native';

import BqsDeviceFingerPrinting from 'rnkit-bqs-device-finger-printing';

export default class Example extends Component {

  constructor(props) {
    super(props);
  }

  async initSdk() {
    try {
      const token = await BqsDeviceFingerPrinting.init({
        partnerId: 'xxxxxxx',
        isGatherGps: true,
        isGatherContacts: true,
        isTestingEnv: true,
        isGatherSensorInfo: true,
      });
      alert(`初始化成功 -- token: ${token}`)
    } catch (err) {
      alert(`初始化失败 -- token: ${err.message}`)
    }
  }

  async getTokenKey() {
    try {
      const token = await BqsDeviceFingerPrinting.getTokenKey();
      alert('token: ' + token + ' -- 获取 tokenKey成功!!');
    } catch (err) {
      alert(`获取 tokenKey失败 -- token: ${err.message}`)
    }
  }

  async commitContacts() {
    try {
      const token = await BqsDeviceFingerPrinting.commitContacts();
      alert(`deviceToken: ${token}, 提交通讯录成功!`)
    } catch (err) {
      alert(`提交通讯录失败 -- token: ${err.message}`)
    }
  }
  
  async commitLocaiton() {
    try {
      BqsDeviceFingerPrinting.commitLocaiton();
      alert(`提交定位成功!`)
    } catch (err) {
      alert(`提交通讯录失败 -- token: ${err.message}`)
    }
  }
  
  async commitLocaitonWithLongitude() {
    try {
      BqsDeviceFingerPrinting.commitLocaitonWithLongitude(1234.123, 21.3123213);
      alert(`设置并提交定位成功!`)
    } catch (err) {
      alert(`设置并提交定位失败 -- token: ${err.message}`)
    }
  }

  render() {
    return (
      <View style={styles.container}>
        <Button title="initSdk" onPress={() => {this.initSdk()}}/>
        <Button title="getTokenKey" onPress={() => {this.getTokenKey()}}/>
        <Button title="commitContacts" onPress={() => {this.commitContacts()}}/>
        <Button title="commitLocaiton" onPress={() => {this.commitLocaiton()}}/>
        <Button title="commitLocaitonWithLongitude" onPress={() => {this.commitLocaitonWithLongitude()}}/>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
  instructions: {
    textAlign: 'center',
    color: '#333333',
    marginBottom: 5,
  },
});
