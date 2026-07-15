package com.param.privilege.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;

import com.param.privilege.constants.Constants;

import com.param.global.common.ServiceException;
public class Logger {
	final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Logger.class);


	public static void log(String log) {
		BufferedWriter bw = null;
		FileWriter fw = null;
		try {
			long millis = System.currentTimeMillis();
			java.sql.Date date = new java.sql.Date(millis);
			final String FILENAME = Constants.LOG_FILE + date;
			File file = new File(FILENAME);

			if (!file.exists()) {
				file.createNewFile();
			}

			fw = new FileWriter(file.getAbsoluteFile(), true);
			bw = new BufferedWriter(fw);
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			bw.write(timestamp + " : " + log);
			bw.newLine();
			bw.newLine();

		} catch (IOException e) {
		logger.error("log failed", e);
		} finally {
			try {
				if (bw != null)
					bw.close();
				if (fw != null)
					fw.close();
			} catch (IOException ex) {
			logger.error("log failed", ex);
			}
		}
	}
}
