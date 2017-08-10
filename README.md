[![npm][npm-badge]][npm]
[![react-native][rn-badge]][rn]
[![MIT][license-badge]][license]
[![bitHound Score][bithound-badge]][bithound]
[![Downloads](https://img.shields.io/npm/dm/rnkit-bqs-device-finger-printing.svg)](https://www.npmjs.com/package/rnkit-bqs-device-finger-printing)

BqsDeviceFingerPrinting for react-native for [React Native][rn].

[**Support me with a Follow**](https://github.com/simman/followers)

[npm-badge]: https://img.shields.io/npm/v/rnkit-bqs-device-finger-printing.svg
[npm]: https://www.npmjs.com/package/rnkit-bqs-device-finger-printing
[rn-badge]: https://img.shields.io/badge/react--native-v0.40-05A5D1.svg
[rn]: https://facebook.github.io/react-native
[license-badge]: https://img.shields.io/dub/l/vibe-d.svg
[license]: https://raw.githubusercontent.com/rnkit/rnkit-bqs-device-finger-printing/master/LICENSE
[bithound-badge]: https://www.bithound.io/github/rnkit/rnkit-bqs-device-finger-printing/badges/score.svg
[bithound]: https://www.bithound.io/github/rnkit/rnkit-bqs-device-finger-printing

## Getting Started

First, `cd` to your RN project directory, and install RNMK through [rnpm](https://github.com/rnpm/rnpm) . If you don't have rnpm, you can install RNMK from npm with the command `npm i -S rnkit-bqs-device-finger-printing` and link it manually (see below).

### Android

* #### React Native < 0.29 (Using rnpm)

  `rnpm install rnkit-bqs-device-finger-printing`

* #### React Native >= 0.29
  `$npm install -S rnkit-bqs-device-finger-printing`

  `$react-native link rnkit-bqs-device-finger-printing`

#### Manually
1. JDK 7+ is required
1. Add the following snippet to your `android/settings.gradle`:

  ```gradle
include ':rnkit-bqs-device-finger-printing'
project(':rnkit-bqs-device-finger-printing').projectDir = new File(rootProject.projectDir, '../node_modules/rnkit-bqs-device-finger-printing/android/app')
  ```
  
1. Declare the dependency in your `android/app/build.gradle`
  
  ```gradle
  dependencies {
      ...
      compile project(':rnkit-bqs-device-finger-printing')
  }
  ```
  
1. Import `import io.rnkit.bqs.devicefingerprinting;` and register it in your `MainActivity` (or equivalent, RN >= 0.32 MainApplication.java):

  ```java
  @Override
  protected List<ReactPackage> getPackages() {
      return Arrays.asList(
              new MainReactPackage(),
              new BqsDeviceFingerPrintingPackage()
      );
  }
  ```

Finally, you're good to go, feel free to require `rnkit-bqs-device-finger-printing` in your JS files.

Have fun! :metal:

## Basic Usage

Import library

```
import BqsDeviceFingerPrinting from 'rnkit-bqs-device-finger-printing'
```

# Authentication

## iOS

```
Privacy - Location When In Use Usage Description: 我们需要通过您的地理位置信息获取您周边的相关数据
Privacy - Contacts Usage Description: 是否允许此App访问你的通讯录？
```

## Android

```
<!--必要权限-->
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.READ_PHONE_STATE" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
<uses-permission android:name="android.permission.BLUETOOTH" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
<!--采集传感器信息必要权限-->
<uses-feature android:required="true" android:name="android.hardware.sensor.accelerometer"/>
<!--可选权限 如果需要获取 GPS 定位，需要添加以下两个可选权限-->
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
<!--获取通讯录-->
<uses-permission android:name="android.permission.READ_CONTACTS" />
<!--获取通话记录-->
<uses-permission android:name="android.permission.READ_CALL_LOG"/>
```

# Api

```
/**
 * 初始化方法
 * @param {Object} options 参数
 * ------- options ---------
 * @param {String} [required] partnerId 合作方编码
 * @param {Boolen} isGatherGps 是否采集gps，默认YES:采集 NO:不采集
 * @param {Boolen} isGatherContacts 是否采集通讯录，默认NO：不采集， YES:采集
 * @param {Boolen} isTestingEnv 是否对接白骑士测试环境，默认NO：生产环境, YES:测试环境
 * @param {Boolen} isGatherSensorInfo 是否采集传感器信息,默认YES:采集 NO:不采集
 */
try {
  const token = await BqsDeviceFingerPrinting.init({
    partnerId: 'partnerId',
    isGatherGps: true,
    isGatherContacts: false,
    isTestingEnv: true,
    isGatherSensorInfo: true,
  });
  console.log(`deviceToken: ${token}`);
} catch (err) {
  console.log(err);
}

/**
 * 获取 tokenKey
 * @return {String} token
 */
[async] getTokenKey();

/**
 * 提交通讯录
 * @return {String} token
 */
try {
  const token = await commitContacts();
  console.log(`deviceToken: ${token}, 提交通讯录成功!`)
} catch (err) {
  console.log(err)
}

/**
 * 提交定位 (可能不准)
 * @return {String} token
 */
try {
  const token = await commitLocaiton();
  console.log(`deviceToken: ${token}, 提交定位成功!`)
} catch (err) {
  console.log(err)
}

/**
 * 设置并提交定位
 * @param {double} [required] longitude 经度
 * @param {double} [required] latitude 纬度
 * @return {String} token
 */
try {
  const token = await commitLocaitonWithLongitude(longitude, latitude);
  console.log(`deviceToken: ${token}, 设置并提交定位成功!`)
} catch (err) {
  console.log(err)
}

```

## Contribution

- [@simamn](mailto:liwei0990@gmail.com)

## Questions

Feel free to [contact me](mailto:liwei0990@gmail.com) or [create an issue](https://github.com/rnkit/rnkit-bqs-device-finger-printing/issues/new)

> made with ♥