package org.mowit.init;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.mowit.exception.MowerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.mowit.exception.MowerConst.INITILIAZE;
import static org.mowit.exception.MowerConst.INIT_FAILED;

/**
 * This class will initialize all the implementers of Initializable.
 * 
 * @author snaceur
 * @version V1.0
 */

@Component
@Scope(value = "singleton")
public class GlobalInitialization {

	private static GlobalInitialization instance = new GlobalInitialization();
	Logger LOG = LoggerFactory.getLogger(GlobalInitialization.class);

	@Autowired
	private ApplicationContext ctx;

	public static GlobalInitialization getInstance() {
		return instance;
	}

	private GlobalInitialization() {
	}

	@PostConstruct
	public void init() {
		try {
			LOG.info("/////////////////Class initiation started at" + new Date() + "///////////////////////////");
			boolean initiliazed = true;
			Map<String, Initializable> beans = ctx.getBeansOfType(Initializable.class);
			for (Initializable classZ : beans.values()) {
				Method method = classZ.getClass().getMethod(INITILIAZE, (Class[]) null);
				initiliazed &= (boolean) method.invoke(null, new Object[0]);
			}
			if (!initiliazed) {
				throw new MowerException(INIT_FAILED, new String[] {});
			}
			LOG.info("/////////////////Class initiation finished at" + new Date() + "///////////////////////////");

		} catch (Exception e) {
			throw new MowerException(e);
		}
	}
}
