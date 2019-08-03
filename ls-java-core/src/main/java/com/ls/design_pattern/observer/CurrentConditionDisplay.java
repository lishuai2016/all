package com.ls.design_pattern.observer;

public class CurrentConditionDisplay implements Observer {
	private float temperature;
	private float humidity;
	public Subject subject;
	//在构造函数中传递一个要观察的主题，并把该观察者注册到主题中去
	public CurrentConditionDisplay(Subject subject) {
		this.subject = subject;
		subject.registerObserver(this);
	}
	@Override
	public void update(float temperature, float humidity, float pressure) {
		this.temperature = temperature;
		this.humidity = humidity;
		System.out.println("CurrentCondition :" + temperature + "   " + humidity);
	}

}
