package com.ls.design_pattern.observer;

public class Program {

	public static void main(String[] args) {
		WeatherData weatherData = new WeatherData(); //主题实例
		CurrentConditionDisplay c = new CurrentConditionDisplay(weatherData); //观察者实例，绑定主题
		weatherData.setMeasurements(80, 60, 30); //主题变化，把信息发送给观察者
	}
}
