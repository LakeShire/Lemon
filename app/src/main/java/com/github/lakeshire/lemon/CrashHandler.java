package com.github.lakeshire.lemon;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CrashHandler implements UncaughtExceptionHandler {

	/** 获取CrashHandler实例 ,单例模式 */
	public static CrashHandler getInstance() {

		if (crashHandler == null) {
			synchronized (CrashHandler.class) {
				crashHandler = new CrashHandler();
			}
		}
		return crashHandler;

	}

	private UncaughtExceptionHandler mDefaultHandler;
	private static CrashHandler crashHandler;
	private Context mContext;

	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");

	private CrashHandler() {

	}

	public File getLogFilePath(Context ctx) {
		String sdStatus = Environment.getExternalStorageState();
		if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
			return null;
		}

		String dirStr = Environment.getExternalStorageDirectory().getPath() + "/Android/data/" + ctx.getPackageName() + "/files/error/";
		File dir = new File(dirStr);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		
		String pathStr = Environment.getExternalStorageDirectory().getPath() + "/Android/data/" + ctx.getPackageName() + "/files/error/" + simpleDateFormat.format(new Date()) + ".log";

		File path = new File(pathStr);
		try {
			path.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return path;
	}

	/**
	 * 初始化
	 * 
	 * @param context
	 */
	public void init(Context context) {
		mContext = context;
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	/**
	 * 当UncaughtException发生时会转入该重写的方法来处理
	 */
	public void uncaughtException(Thread thread, Throwable ex) {
		if (thread == null || ex == null || mDefaultHandler == null) {
			System.exit(0);
			return;
		}

		File savePathFile = getLogFilePath(mContext);

		if (savePathFile == null) {
			mDefaultHandler.uncaughtException(thread, ex);
			return;
		}

		String logMessage;
		PrintWriter printWriter = null;

		try {
			printWriter = new PrintWriter(new FileWriter(savePathFile, true));
			logMessage = String.format("%s\r\n\r\nThread: %d\r\n\r\nMessage:\r\n\r\n%s\r\n\r\nStack Trace:\r\n\r\n%s", new Date(), thread.getId(), ex.getMessage(), Log.getStackTraceString(ex));

			printWriter.print(logMessage);
			printWriter.print("\n\n---------------------------------------------------------------------------\n\n");
		}
		catch (Throwable tr2) {

		}
		finally {
			if (printWriter != null) {
				printWriter.close();
			}
		}
		mDefaultHandler.uncaughtException(thread, ex);
	}
}
