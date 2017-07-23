package org.mowit.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class JaxbStringAdapter extends XmlAdapter<String, Boolean> {

	@Override
	public String marshal(Boolean v) throws Exception {
		if (v)
			return "File";
		else
			return "Manual";
	}

	@Override
	public Boolean unmarshal(String v) throws Exception {
		if (v.equals("File"))
			return true;
		else
			return false;
	}

}
