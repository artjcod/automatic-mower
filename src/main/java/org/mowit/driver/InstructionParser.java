package org.mowit.driver;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Scanner;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.mowit.exception.MowerException;
import org.mowit.messages.MessageGetter;
import org.mowit.model.AbstarctMower;
import org.mowit.model.Direction;
import org.mowit.model.HasValidator;
import org.mowit.model.Instruction;
import org.mowit.model.Lawn;
import org.mowit.model.MowerPosition;
import org.mowit.model.MowerStatus;
import org.mowit.monitoring.impl.MowerStatusChangedEmailAlert;
import org.mowit.monitoring.impl.MowerStatusChangedXMLReport;
import org.mowit.sequencer.SequencerFactory;
import org.mowit.service.contracts.ILawnService;
import org.mowit.service.contracts.IMowerService;
import org.mowit.service.factory.LawnFactory;
import org.mowit.service.factory.MowerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import static org.mowit.exception.MowerConst.INST_EXECUTED;
import static org.mowit.exception.MowerConst.SEPARATOR;
import static org.mowit.exception.MowerConst.CONTINUE_MESSAGE;
import static org.mowit.exception.MowerConst.LAWN_COORDINATES_MSG;

import static org.mowit.exception.MowerConst.LAWN_CREATED_SUCCESSFULLY_MSG;
import static org.mowit.exception.MowerConst.X_POSITION_MSG;
import static org.mowit.exception.MowerConst.Y_POSITION_MSG;
import static org.mowit.exception.MowerConst.DIRECTION_MSG;
import static org.mowit.exception.MowerConst.CARDINAL_PATTERN;
import static org.mowit.exception.MowerConst.WRONG_CARDINAL;
import static org.mowit.exception.MowerConst.MOWER_CREATION_POSTION_MSG;
import static org.mowit.exception.MowerConst.ASK_FOR_INSTRUCTION;
import static org.mowit.exception.MowerConst.INSTRUCTION_PATTERN;
import static org.mowit.exception.MowerConst.WRONG_INSTRUCTION;

/**
 * This Class aims to parse an instruction and execute it.
 * 
 * @author snaceur
 * @version V1.0
 */

@Service("instructionParser")
@ComponentScan({ "org.mowit.service.contracts", "org.mowit.service", "org.mowit.view" })
public class InstructionParser {

	private static Logger LOG = LoggerFactory.getLogger(InstructionParser.class);

	@Autowired
	@Qualifier("mowerService")
	private IMowerService mowerService;

	@Autowired
	@Qualifier("lawnService")
	private ILawnService lawnService;

	/**
	 * Parse the input stream and read the instruction.
	 * 
	 * @param in
	 */
	public void parse(InputStream in) {
		Scanner scanner = new Scanner(in);
		Lawn lawn = scanLawn(scanner, (in instanceof FileInputStream));
		while (true) {
			AbstarctMower mower = buildMower(scanner, lawn);
			parseExecute(scanner, mower);
			System.out.println(MessageGetter.getMessage(INST_EXECUTED));
			System.out.println(SEPARATOR);
			mower.setState(MowerStatus.FINISHED);
			mowerService.merge(mower);
			if (!mower.isGenerated()) {
				System.out.println(MessageGetter.getMessage(CONTINUE_MESSAGE));
				String hasNextMower = scanner.next();
				if (!hasNextMower.toLowerCase().equals("y"))
					break;
			} else {
				if (!scanner.hasNext()) {
					break;
				}
			}
		}

		scanner.close();
	}

	/**
	 * Create Lawn from the 1st line.
	 * 
	 * @param scanner
	 * @return {@link Lawn}
	 */
	private Lawn scanLawn(Scanner scanner, boolean isStp) {
		if (!isStp)
			System.out.println(MessageGetter.getMessage(LAWN_COORDINATES_MSG));
		int width = scanner.nextInt();
		int heigth = scanner.nextInt();
		Lawn lawn = new Lawn(width, heigth);
		lawn.setGenerated(isStp);
		Set<ConstraintViolation<HasValidator>> violations = lawn.validate();
		if (!violations.isEmpty()) {
			for (ConstraintViolation<HasValidator> constraint : violations) {
				String message = constraint.getMessage();
				LOG.error(message);
		           throw new MowerException(message);

			}
			return null;
		} else {
			lawnService.merge(lawn);
			LawnFactory.registerLawn(lawn);
			String message = MessageGetter.getMessage(LAWN_CREATED_SUCCESSFULLY_MSG);
			LOG.info(message);
			System.out.println(message);
			return lawn;
		}
	}

	/**
	 * Create a mower from the input information.
	 * 
	 * @param scanner
	 * @param lawn
	 * @return AbstractMower
	 */
	private AbstarctMower buildMower(Scanner scanner, Lawn lawn) {
		boolean isStp = lawn.isGenerated();
		if (!isStp) {
			System.out.println(
					MessageGetter.getMessage(X_POSITION_MSG, new String[] { String.valueOf(lawn.getMarginX()) }));
		}
		int x = scanner.nextInt();
		if (!isStp) {
			System.out.println(
					MessageGetter.getMessage(Y_POSITION_MSG, new String[] { String.valueOf(lawn.getMarginY()) }));
		}
		int y = scanner.nextInt();
		if (!isStp) {
			System.out.println(MessageGetter.getMessage(DIRECTION_MSG));
		}
		String cardinal = scanner.next(MessageGetter.getMessage(CARDINAL_PATTERN));

		Direction dir = getDirection(cardinal);
		MowerPosition position = new MowerPosition(x, y, dir, lawn);
		int sequence = SequencerFactory.getSequence(AbstarctMower.class);
		AbstarctMower mower = new AbstarctMower("MW-" + sequence, position);

		// Set position History
		mower.addPositionHistory(position);
		mower.setGenerated(isStp);
		mower.addObserver(new MowerStatusChangedEmailAlert());
		mower.addObserver(new MowerStatusChangedXMLReport());

		mowerService.merge(mower);
		MowerFactory.registerMower(mower);
		System.out.println(MessageGetter.getMessage(MOWER_CREATION_POSTION_MSG, new String[] { position.toString() }));
		LOG.info(MessageGetter.getMessage(MOWER_CREATION_POSTION_MSG, new String[] { position.toString() }));
		return mower;
	}

	/**
	 * Decode direction
	 * 
	 * @param cardinal
	 * @return Direction
	 */
	public Direction getDirection(String cardinal) {
		if ("N".equals(cardinal))
			return Direction.NORTH;
		if ("S".equals(cardinal))
			return Direction.SOUTH;
		if ("E".equals(cardinal))
			return Direction.EAST;
		if ("W".equals(cardinal))
			return Direction.WEST;
		throw new MowerException(WRONG_CARDINAL, new String[] {});
	}

	/**
	 * Execute instruction
	 * 
	 * @param scanner
	 * @param mower
	 */
	public void parseExecute(Scanner scanner, AbstarctMower mower) {
		if (!mower.isGenerated()) {
			System.out.println(MessageGetter.getMessage(ASK_FOR_INSTRUCTION));
		}
		String instructions = scanner.next(INSTRUCTION_PATTERN);
		for (char inst : instructions.toCharArray()) {
			mower.setState(MowerStatus.WORKING);
			Instruction instruction = decodeInstruction(inst);
			mowerService.execute(mower, instruction);
		}
	}

	/**
	 * Decode Instruction
	 * 
	 * @param inst
	 * @return
	 */
	private Instruction decodeInstruction(char inst) {
		if (inst == 'R') {
			return Instruction.RIGHT;
		} else if (inst == 'L') {
			return Instruction.LEFT;
		} else if (inst == 'F') {
			return Instruction.FORWARD;
		} else {
			throw new IllegalArgumentException(WRONG_INSTRUCTION);
		}
	}
}
