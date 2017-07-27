package org.mowit.view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;
import java.util.Scanner;

import javax.annotation.PostConstruct;

import org.mowit.driver.InstructionParser;
import org.mowit.exception.MowerException;
import org.mowit.exception.NotFoundException;
import org.mowit.init.GlobalInitialization;
import org.mowit.messages.MessageGetter;
import org.mowit.model.AbstarctMower;
import org.mowit.model.MowerStatus;
import org.mowit.service.ObjectSerializer;
import org.mowit.service.contracts.ILawnService;
import org.mowit.service.contracts.IMowerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import static org.mowit.exception.MowerConst.*;

@SpringBootApplication
@ComponentScan({ "org.mowit.service.contracts", "org.mowit.service", "org.mowit.driver", "org.mowit.model",
		"org.mowit.setting","org.mowit.init" })
@EnableCaching
public class CommandLineRunnerView  {

	private static Logger LOG = LoggerFactory.getLogger(CommandLineRunnerView.class);

	@Autowired
	@Qualifier("objectSerializer")
	ObjectSerializer objectSerializer;

	@Autowired
	@Qualifier("instructionParser")
	InstructionParser instructionParser;

	@Autowired
	@Qualifier("mowerService")
	private IMowerService mowerService;

	@Autowired
	@Qualifier("lawnService")
	private ILawnService lawnService;
	
	
	@Autowired
	GlobalInitialization globalInitialization;
	
	
	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {
			Scanner scanner = null;
			try {
				scanner = new Scanner(System.in);
				System.out.println(MessageGetter.getMessage(CHOOSE_AN_OPTION));
				System.out.println(MessageGetter.getMessage(INST_FROM_FILE));
				System.out.println(MessageGetter.getMessage(INST_FROM_CONSOLE));
				System.out.println(MessageGetter.getMessage(USE_EXISTING_MOWER));

				int option = scanner.nextInt();
				if (option == 1) {
					instructionsFromFile(scanner);
				} else if (option == 3) {
					AbstarctMower reuseMower = mowerService.reuseMower(scanner);
					instructionParser.parseExecute(scanner, reuseMower);
					System.out.println(MessageGetter.getMessage(INST_EXECUTED));
					System.out.println(SEPARATOR);
					reuseMower.setState(MowerStatus.FINISHED);
					mowerService.merge(reuseMower);
				} else {
					instructionParser.parse(System.in);
				}
			} finally {
				if (scanner != null) {
					scanner.close();
				}
			}
		};
	}
	
	public static void main(String[] args) throws NotFoundException {
		try {
			SpringApplication.run(CommandLineRunnerView.class, args);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			throw new MowerException(e);
		}
	}

	@PostConstruct
	public void load() {
		String startTime = new Date().toString();
		LOG.info(MessageGetter.getMessage(LOADING_OBJECTS_START, new String[] { startTime }));
		objectSerializer.loadLawns();
		LOG.info(MessageGetter.getMessage(LAWN_LOADED_SUCCESSFULLY));
		objectSerializer.loadMowers();
		LOG.info(MessageGetter.getMessage(MOWER_LOADED_SUCCESSFULLY));
		String endTime = new Date().toString();
		LOG.info(MessageGetter.getMessage(LOADING_OBJECTS_FINISHED, new String[] { endTime }));
	}


	/**
	 * Read instruction from a file
	 * 
	 * @param scanner
	 * @throws FileNotFoundException
	 */
	protected void instructionsFromFile(Scanner scanner) throws FileNotFoundException {
		System.out.println(MessageGetter.getMessage(INPUT_FILE_PATH));
		String path = scanner.next();
		InputStream stream = new FileInputStream(path);
		instructionParser.parse(stream);
	}
}
