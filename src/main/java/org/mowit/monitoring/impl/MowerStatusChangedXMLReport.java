package org.mowit.monitoring.impl;

import java.io.File;
import java.util.Date;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.mowit.exception.MowerException;
import org.mowit.model.AbstarctMower;
import org.mowit.monitoring.Observable;
import org.mowit.monitoring.Observer;
import org.mowit.setting.SettingInterface;
import org.mowit.setting.SettingLoaderFactory;

import static org.mowit.exception.MowerConst.BACKSLASH;
import static org.mowit.exception.MowerConst.DEFAULT;
import static org.mowit.exception.MowerConst.XML_EXTENTION;

/**
 * This Class send an xml report about this mower.
 * 
 * @author snaceur
 * @version V1.0
 */
public class MowerStatusChangedXMLReport implements Observer {

	@Override
	public void sendAlert(Observable observable) {
		if (observable instanceof AbstarctMower) {
			try {
				AbstarctMower mower = (AbstarctMower) observable;
				SettingInterface settingLoader = SettingLoaderFactory.getSettingLoader(DEFAULT);
				StringBuilder br = new StringBuilder(settingLoader.getSerFolder());
				br.append(BACKSLASH).append(settingLoader.getReportingFolder()).append(BACKSLASH)
						.append(mower.getIdentifier()).append(new Date().getTime()).append(XML_EXTENTION);
				File file = new File(br.toString());
				JAXBContext jaxbContext = JAXBContext.newInstance(AbstarctMower.class);
				Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
				jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				jaxbMarshaller.marshal(mower, file);
			} catch (Exception e) {
				throw new MowerException(e);
			}
		}
	}
}
