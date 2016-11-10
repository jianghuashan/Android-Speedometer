## Android-Speedometer

Android自定义控件项目，汽车速度计控件，支持加速减速，指定速度值，更新总里程和获取当前速度相关。

## Preview

ScreenShot文件夹为效果演示图（与项目代码无关）

![](https://raw.githubusercontent.com/smartbetter/Android-Speedometer/master/ScreenShot/screenshot1.gif)
![](https://raw.githubusercontent.com/smartbetter/Android-Speedometer/master/ScreenShot/screenshot2.gif)
![](https://raw.githubusercontent.com/smartbetter/Android-Speedometer/master/ScreenShot/screenshot3.gif)
![](https://raw.githubusercontent.com/smartbetter/Android-Speedometer/master/ScreenShot/screenshot4.gif)

## Explain

SpeedView 是 Android-Speedometer自定义View，SpeedData是SpeedView绘图逻辑部分。

SpeedView 对外提供了以下方法：
	
	gotoRotate()方法：开始模拟加速减速动画
	setSpeedValue(double value)方法：设置速度值，并滑动到指定位置
	getSpeedValue()方法：获取当前速度
	setTotalMileage(double totalMileage)方法：更新总里程

## Simple Example

```java
private SpeedView speedView;

speedView.gotoRotate(); //加速减速
speedView.setSpeedValue(30.0); //设置当前速度值
speedView.setSpeedValue(0); //急刹车
speedView.setTotalMileage(1000.0); //更新总里程
double speedValue = speedView.getSpeedValue(); //获取当前速度值

```

## XML Usage

```xml
<com.gc.speedometer.view.SpeedView
	android:id="@+id/speed_view"
	android:layout_width="match_parent"
	android:layout_height="wrap_content"/>
```

## License

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

	   http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
