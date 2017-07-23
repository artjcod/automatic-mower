package org.mowit.service;

import org.mowit.model.AbstarctMower;
import org.mowit.model.Direction;
import org.mowit.model.Instruction;
import org.mowit.model.Lawn;
import org.mowit.model.MowerPosition;
import org.mowit.monitoring.impl.MowerStatusChangedEmailAlert;
import org.mowit.monitoring.impl.MowerStatusChangedXMLReport;
import org.mowit.service.contracts.ILawnService;
import org.mowit.service.contracts.IMowerService;
import org.mowit.service.factory.MowerFactory;
import org.mowit.setting.SettingInterface;
import org.mowit.setting.SettingLoaderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import static org.mowit.model.Instruction.*;
import static org.mowit.exception.MowerConst.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Scanner;

import static org.mowit.model.Direction.*;

import org.mowit.driver.InstructionParser;
import org.mowit.exception.MowerException;
import org.mowit.exception.NotFoundException;
import org.mowit.messages.MessageGetter;

@Component("mowerService")
public class MowerService implements IMowerService {
	@Autowired
	ObjectSerializer ObjectSerializer;

	@Autowired
	@Qualifier("instructionParser")
	InstructionParser instructionParser;

	@Autowired
	@Qualifier("lawnService")
	private ILawnService lawnService;

	@Override
	public MowerPosition execute(AbstarctMower mower, Instruction inst) {
		MowerPosition nextPosition = getNextPosition(mower.getPosition(), inst);
		if (nextPosition.validate().isEmpty() && isValid(nextPosition)) {
			mower.setPosition(nextPosition);
			mower.addPositionHistory(nextPosition);
			System.out.println(MessageGetter.getMessage(MOVED_MSG, new String[] { nextPosition.toString() }));
		} else {
			System.out.println(MessageGetter.getMessage(INSTRUCTION_IGNORED, new String[] { inst.toString() }));
		}
		return nextPosition;
	}

	private boolean isValid(MowerPosition position) {
		if (position == null) {
			return true;
		} else {
			Integer posX = position.getX();
			Integer posY = position.getY();
			Integer lawnX = position.getLawn().getMarginX();
			Integer lawnY = position.getLawn().getMarginY();

			if (posX < 0 || posX > lawnX) {
				return false;
			}
			if (posY < 0 || posY > lawnY) {
				return false;
			}
			return true;
		}
	}

	public MowerPosition getNextPosition(MowerPosition current, Instruction inst) {
		try {
			MowerPosition newInstance = (MowerPosition) current.clone();
			Direction currentDirection = newInstance.getDirection();
			if (inst == FORWARD) {
				moveForward(newInstance, currentDirection);
			} else if (inst == LEFT) {
				moveLeft(newInstance, currentDirection);
			} else if (inst == RIGHT) {
				moveRight(newInstance, currentDirection);
			} else {
				throw new MowerException(UNKNOWN_COMMAND, new String[] { inst.toString() });
			}
			return newInstance;
		} catch (CloneNotSupportedException e) {
			throw new MowerException(FATAL_ERROR, new String[] {});
		}
	}

	protected void moveRight(MowerPosition newInstance, Direction currentDirection) {
		if (currentDirection == NORTH) {
			newInstance.setDirection(EAST);
		} else if (currentDirection == SOUTH) {
			newInstance.setDirection(WEST);
		} else if (currentDirection == EAST) {
			newInstance.setDirection(SOUTH);
		} else if (currentDirection == WEST) {
			newInstance.setDirection(NORTH);
		}
	}

	protected void moveLeft(MowerPosition newInstance, Direction currentDirection) {
		if (currentDirection == NORTH) {
			newInstance.setDirection(WEST);
		} else if (currentDirection == SOUTH) {
			newInstance.setDirection(EAST);
		} else if (currentDirection == EAST) {
			newInstance.setDirection(NORTH);
		} else if (currentDirection == WEST) {
			newInstance.setDirection(SOUTH);
		}
	}

	protected void moveForward(MowerPosition newInstance, Direction currentDirection) {
		if (currentDirection == Direction.NORTH) {
			newInstance.setY(newInstance.getY() + Direction.NORTH.getMove());
		} else if (currentDirection == Direction.SOUTH) {
			newInstance.setY(newInstance.getY() + Direction.SOUTH.getMove());
		}

		else if (currentDirection == Direction.EAST) {
			newInstance.setX(newInstance.getX() + Direction.EAST.getMove());
		} else if (currentDirection == Direction.WEST) {
			newInstance.setX(newInstance.getX() + Direction.WEST.getMove());
		}
	}

	@Override
	public void merge(Object object) {
		if (object instanceof AbstarctMower) {
			AbstarctMower mower = (AbstarctMower) object;
			FileOutputStream stream = null;
			SettingInterface settingLoader = SettingLoaderFactory.getSettingLoader(DEFAULT);
			String serFolder = settingLoader.getSerFolder();
			File savefolder = new File(serFolder);
			if (!savefolder.exists()) {
				savefolder.mkdir();
			}
			StringBuilder br = new StringBuilder();
			br.append(savefolder).append(BACKSLASH).append(settingLoader.getMowerFolderName()).append(BACKSLASH)
					.append(mower.getIdentifier());
			try {
				stream = new FileOutputStream(br.toString());
				mower.setUpdateDate(new Date());
				new ObjectSerializer(stream).writeValue(mower);
			} catch (Exception e) {
				throw new RuntimeException(e);
			} finally {
				if (stream != null) {
					try {
						stream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		} else {
			throw new MowerException(WRONG_TYPE, new String[] {});
		}
	}

	@Override
	public void delete(Object object) {
		if (object instanceof AbstarctMower) {
			AbstarctMower mower = (AbstarctMower) object;
			SettingInterface settingLoader = SettingLoaderFactory.getSettingLoader(DEFAULT);
			String serFolder = settingLoader.getSerFolder();
			StringBuilder br = new StringBuilder();
			br.append(serFolder).append(mower.getIdentifier());
			File savefolder = new File(br.toString());
			if (savefolder.exists()) {
				savefolder.delete();
			}
		}
	}

	@Override
	public Collection<AbstarctMower> findAll() {
		if (MowerFactory.getMowers().isEmpty()) {
			ObjectSerializer.loadMowers();
		}
		return MowerFactory.getMowers();
	}

	@Override
	public Object findById(Object id) {
		try {
			AbstarctMower mower = MowerFactory.getMower((String) id);
			if (mower != null) {
				return mower;
			} else {
				ObjectSerializer.loadMowers();
				mower = MowerFactory.getMower((String) id);
				if (mower != null) {
					return mower;
				} else {
					throw new NotFoundException(MOWER_NOT_FOUND, new String[] { id.toString() });
				}
			}
		} catch (Exception e) {
			throw new MowerException(e);
		}

	}

	/**
	 * Reuse an existing mower.
	 * 
	 * @param scanner
	 */
	public AbstarctMower reuseMower(Scanner scanner) {
		System.out.println(MessageGetter.getMessage(ASK_FOR_MOWER_IDENTIFIER));
		String identifier = scanner.next();
		AbstarctMower mower = (AbstarctMower) this.findById(identifier);
		MowerPosition position = mower.getPosition();
		Lawn lawn = position.getLawn();

		StringBuilder lawnMsg = new StringBuilder();
		lawnMsg.append(OP_LEFT).append(lawn.getMarginX()).append(COMMA).append(lawn.getMarginY()).append(OP_RIGHT);

		System.out.println(MessageGetter.getMessage(MOWER_FOUND,
				new String[] { position.toString(), String.valueOf(lawn.getId()), lawnMsg.toString() }));

		System.out.println(MessageGetter.getMessage(ASK_RESET_POSITION));

		String reset = scanner.next("[YN|yn]");
		if (reset.toUpperCase().equals("Y")) {
			System.out.println(
					MessageGetter.getMessage(X_POSITION_MSG, new String[] { String.valueOf(lawn.getMarginX()) }));
			int newX = scanner.nextInt();
			System.out.println(
					MessageGetter.getMessage(Y_POSITION_MSG, new String[] { String.valueOf(lawn.getMarginY()) }));
			int newY = scanner.nextInt();
			System.out.println(MessageGetter.getMessage(DIRECTION_MSG));
			Direction newDirection = instructionParser
					.getDirection(scanner.next(MessageGetter.getMessage(CARDINAL_PATTERN)));
			System.out.println(MessageGetter.getMessage(ASK_TO_ASSIGN_NEW_LAWN));
			String resetLawn = scanner.next("[RS|rs]");
			if (resetLawn.toUpperCase().equals("R")) {
				System.out.println(MessageGetter.getMessage(LAWN_COORDINATES_MSG));
				int newLawnX = scanner.nextInt();
				int newLawnY = scanner.nextInt();
				lawn = lawnService.createLawn(newLawnX, newLawnY);
			} else {
				System.out.println(MessageGetter.getMessage(ASK_FOR_LAWN_IDENTIFIER));
				int lawnId = scanner.nextInt();
				lawn = (Lawn) lawnService.findById(lawnId);
				System.out.println(MessageGetter.getMessage(LAWN_FOUND,
						new String[] { String.valueOf(lawn.getMarginX()), String.valueOf(lawn.getMarginY()) }));
			}
			MowerPosition newPosition = new MowerPosition(newX, newY, newDirection, lawn);
			mower.setPosition(newPosition);
			mower.addPositionHistory(newPosition);
			mower.addObserver(new MowerStatusChangedEmailAlert());
			mower.addObserver(new MowerStatusChangedXMLReport());
			mower.setGenerated(false);
			this.merge(mower);
		}
		return mower;
	}
}
