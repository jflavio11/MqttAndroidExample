# MqttAndroidExample
An example Android app using MQTT protocol

#### What is MQTT?
MQTT is a publish-subscribe-based messaging protocol, this means that clients must subscribe to a specific topic where messages are sent. The MQTT broker (or server) is in charge of managing of sending message to a specific (or specifics) topics and all clients subscribed to it will be receiving the data.
![MQTT architecture. Image from HiveMQ](https://miro.medium.com/max/1400/1*niJrX0DR9yzxUSb9TgQxgA.png)

#### In Android?
The implementation of this protocol was developed thanks to Eclipse Paho project. You can check an article I wrote for The Android Pub about this project here: [About the MQTT protocol for IoT on Android](https://android.jlelse.eu/about-the-mqtt-protocol-for-iot-on-android-efb4973577b)
