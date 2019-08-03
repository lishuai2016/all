package com.ls.design_pattern.observer;

public interface Subject {

	void registerObserver(Observer o);
	
	void removeObserver(Observer o);
	
	void notifyObservers();
}
