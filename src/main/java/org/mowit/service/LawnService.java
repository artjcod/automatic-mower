package org.mowit.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.mowit.exception.MowerException;
import org.mowit.exception.NotFoundException;
import org.mowit.messages.MessageGetter;
import org.mowit.model.HasValidator;
import org.mowit.model.Lawn;
import org.mowit.service.contracts.ILawnService;
import org.mowit.service.factory.LawnFactory;
import org.mowit.setting.SettingInterface;
import org.mowit.setting.SettingLoaderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.mowit.exception.MowerConst.BACKSLASH;
import static org.mowit.exception.MowerConst.DEFAULT;
import static org.mowit.exception.MowerConst.LAWN_CREATED_SUCCESSFULLY_MSG;
import static org.mowit.exception.MowerConst.LAWN_NOT_FOUND;
import static org.mowit.exception.MowerConst.WRONG_TYPE;;

@Component("lawnService")
public class LawnService implements ILawnService {
	private static Logger LOG = LoggerFactory.getLogger(MowerService.class);

	@Autowired
	ObjectSerializer ObjectSerializer;

	@Override
	public void merge(Object obj) {
		if (obj instanceof Lawn) {
			Lawn lawn = (Lawn) obj;
			FileOutputStream stream = null;
			SettingInterface settingLoader = SettingLoaderFactory.getSettingLoader(DEFAULT);
			String serFolder = settingLoader.getSerFolder();
			File savefolder = new File(serFolder);
			if (!savefolder.exists()) {
				savefolder.mkdir();
			}
			StringBuilder br = new StringBuilder();
			br.append(savefolder).append(BACKSLASH).append(settingLoader.getLawnFolderName()).append(BACKSLASH)
					.append(lawn.getId());
			try {
				stream = new FileOutputStream(br.toString());
				new ObjectSerializer(stream).writeValue(lawn);
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
		if (object instanceof Lawn) {
			Lawn lawn = (Lawn) object;
			SettingInterface settingLoader = SettingLoaderFactory.getSettingLoader(DEFAULT);
			String serFolder = settingLoader.getSerFolder();
			StringBuilder br = new StringBuilder();
			br.append(serFolder).append(lawn.getId());
			File savefolder = new File(br.toString());
			if (savefolder.exists()) {
				savefolder.delete();
			}
		}
	}

	@Override
	public Collection<Lawn> findAll() {
		if (LawnFactory.getLanws().isEmpty()) {
			new ObjectSerializer().loadLawns();
		}
		return LawnFactory.getLanws();
	}

	@Override
	public Object findById(Object id) {
		try {
			Lawn lawn = LawnFactory.getLawn((Integer) id);
			if (lawn != null) {
				return lawn;
			} else {
				ObjectSerializer.loadLawns();
				lawn = LawnFactory.getLawn((Integer) id);
				if (lawn != null) {
					return lawn;
				} else {
					throw new NotFoundException(LAWN_NOT_FOUND, new String[] { id.toString() });
				}
			}
		} catch (Exception e) {
			throw new MowerException(e);
		}

	}

	@Override
	public Lawn createLawn(int marginX, int marginY) {
		Lawn lawn = new Lawn(marginX, marginY);
		Set<ConstraintViolation<HasValidator>> violations = lawn.validate();
		if (!violations.isEmpty()) {
			for (ConstraintViolation<HasValidator> constraint : violations) {
				String message = constraint.getMessage();
				LOG.error(message);
			}
		} else {
			this.merge(lawn);
			LawnFactory.registerLawn(lawn);
			String message = MessageGetter.getMessage(LAWN_CREATED_SUCCESSFULLY_MSG);
			LOG.info(message);
			System.out.println(message);
		}

		return lawn;
	}
}
