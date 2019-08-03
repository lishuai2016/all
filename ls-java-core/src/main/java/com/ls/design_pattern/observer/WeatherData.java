package com.ls.design_pattern.observer;

import java.util.ArrayList;
import java.util.List;

public class WeatherData implements Subject {
	//观察者都注册到这里
	public List<Observer> observers;
	
	private float temperature;
	private float humidity;
	private float pressure;
	//状态改变的标志位
	boolean changed = false;
	
	public WeatherData() {
		this.observers = new ArrayList<Observer>();
	}
	
	@Override
	public void registerObserver(Observer o) {
		observers.add(o);
	}

	@Override
	public void removeObserver(Observer o) {
		int i = observers.indexOf(o);
		if (i >= 0) {
			observers.remove(o);
		}
	}

	@Override
	public void notifyObservers() {
		if (changed) {
			for (Observer o : observers) {
				o.update(temperature, humidity, pressure);
			}
		}
	}
	void setChanged() {
		this.changed = true;
	}
	//状态改变
	public void setMeasurements(float temperature, float humidity, float pressure) {
		this.temperature = temperature;
		this.humidity = humidity;
		this.pressure = pressure;
		measurementsChanged();
	}
	public void measurementsChanged() {
		setChanged();
		notifyObservers();
	}
}
