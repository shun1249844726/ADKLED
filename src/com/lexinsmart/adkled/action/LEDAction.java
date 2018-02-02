package com.lexinsmart.adkled.action;

import java.util.ArrayList;
//import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.lexinsmart.adkled.model.ApcValues;
import com.lexinsmart.adkled.service.APCValuesService;

//import com.lexinsmart.adkled.model.ApcValues;
//import com.lexinsmart.adkled.service.APCValuesService;

public class LEDAction {
	static Logger logger = Logger.getLogger(LEDAction.class);
	static Timer timer = new Timer();

	public static void main(String Args[]) {
		logger.info("main");
	}

	/** startservice
	 * qidong
	 * @param args
	 */
	public static void StartService(String[] args) {
		timer.schedule(new UpdateDBTask(), 0, 1000 * 60 * 5);
		logger.info("timer start");

	}

	/**
	 * stopservie
	 * @param args
	 */
	public static void StopService(String[] args) {
		timer.cancel();
		logger.info("timer cancle");
	}
}

class UpdateDBTask extends TimerTask {
	static Logger logger = Logger.getLogger(UpdateDBTask.class);

	@Override
	public void run() {
		String fileString = ReadText.fileToString();
		if (fileString != null && !fileString.equals("")) {
			ArrayList<ApcValues> tankList = ReadText.getTankList(fileString);
			long nowTime = System.currentTimeMillis();
			APCValuesService apcValuesService = new APCValuesService();
			apcValuesService.setAPCValues(tankList);
			logger.info("tanklistsize: " + tankList.size() + "  usetime: " + (System.currentTimeMillis() - nowTime)
					+ " ms");
		}
	}
}