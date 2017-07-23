package org.mowit.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import javax.validation.constraints.Future;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.mowit.adapter.JaxbStringAdapter;
import org.mowit.exception.MowerException;
import org.mowit.monitoring.Observable;
import org.mowit.monitoring.Observer;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.annotation.Id;

/**
 * This class is the model of a Mower.
 * 
 * @author snaceur
 * @version V1.0
 */
@Cacheable("mowers")
@XmlRootElement(name="Mower")
@XmlType(propOrder = { "identifier", "creationDate", "updateDate", "state", "generated", "history"})
public class AbstarctMower implements Serializable, Observable {


	private static final long serialVersionUID = 1L;

	@Id
	protected String identifier;

	protected MowerPosition position;

	private MowerStatus state;

	@XmlElement(name = "PositionHistory")
	protected ArrayList<MowerPosition> history = new ArrayList<>();

	transient ArrayList<Observer> observers = new ArrayList<>();

	protected boolean isGenerated;

	private Date creationDate;

	@Future
	private Date updateDate;

	public AbstarctMower(String identifier, MowerPosition mowerPosition) {
		this.setIdentifier(identifier);
		this.setState(MowerStatus.IDLE);
		this.setPosition(mowerPosition);
		this.setCreationDate(new Date());
		this.setUpdateDate(new Date());
	}

	public AbstarctMower() {

	}

	@XmlTransient
	public MowerPosition getPosition() {
		return position;
	}

	public String getIdentifier() {
		return identifier;
	}

	@XmlAttribute
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public MowerStatus getState() {
		return state;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	@XmlElement
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public void setPosition(MowerPosition position) {
		this.position = position;
	}

	@XmlElement
	public void setState(MowerStatus state) {
		this.state = state;
		if (state == MowerStatus.FINISHED) {
			notifyObservers();
		}
	}

	@XmlElement
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	@XmlElement(name = "origin")
	@XmlJavaTypeAdapter(JaxbStringAdapter.class)
	public Boolean isGenerated() {
		return isGenerated;
	}

	public void setGenerated(boolean isGenerated) {
		this.isGenerated = isGenerated;
	}

	@Override
	public void addObserver(Observer observer) {
		if (observers == null) {
			observers = new ArrayList<Observer>();
		}
		observers.add(observer);
	}

	@Override
	public void removeObserver(Observer observer) {
		observers.remove(observer);
	}

	@Override
	public void notifyObservers() {
		for (Observer observer : observers) {
			try {
				observer.sendAlert(this);
			} catch (Exception e) {
				throw new MowerException(e);
			}
		}
	}

	public void addPositionHistory(MowerPosition position) {
		history.add(position);
	}

	public ArrayList<MowerPosition> getPositionHistory() {
		return history;
	}
}
