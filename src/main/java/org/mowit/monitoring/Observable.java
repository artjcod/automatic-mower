package org.mowit.monitoring;

/**
 * Design-Pattern :used to ensure monitoring when the status of the observable
 * is changed.
 * 
 * @author snaceur
 * V 1.0
 */

public interface Observable {

	public void addObserver(Observer observer);

	public void removeObserver(Observer observer);

	public void notifyObservers();

}
