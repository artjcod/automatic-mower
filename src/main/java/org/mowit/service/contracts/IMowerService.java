package org.mowit.service.contracts;


import java.util.Scanner;

import org.mowit.model.AbstarctMower;
import org.mowit.model.Instruction;
import org.mowit.model.MowerPosition;

public interface IMowerService extends GenericService {
	public MowerPosition execute(AbstarctMower mower, Instruction instruction);
	public AbstarctMower reuseMower(Scanner scanner);

}
