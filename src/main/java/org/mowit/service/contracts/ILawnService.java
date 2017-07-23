package org.mowit.service.contracts;

import org.mowit.model.Lawn;
import org.springframework.stereotype.Component;

@Component("lawnService")
public interface ILawnService extends GenericService {
	public Lawn createLawn(int marginX,int marginY);

}
