package com.roadrover.tboxservice.protocal;

import java.io.IOException;
import java.io.OutputStream;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import com.roadrover.tboxservice.TBoxApplication;
import com.roadrover.tboxservice.constant.Constant;
import com.roadrover.tboxservice.tools.CHexConver;

public class TBoxParser {

	private static final String TAG = "TBoxParser";
	private TBoxApplication mApp;
	private Handler mHandler;
	private OutputStream mOutputStream;
	private HandlerThread mWriteCommandThread;
	private Handler mWriteCommandHandler;

	public TBoxParser(TBoxApplication app, Handler handler) {
		this.mApp = app;
		this.mHandler = handler;
		mWriteCommandThread = new HandlerThread("write_command_thread");
		mWriteCommandThread.start();
		mWriteCommandHandler = new Handler(mWriteCommandThread.getLooper()) {
			@Override
			public void handleMessage(Message msg) {
				byte[] value = (byte[]) msg.obj;
				writeCommandByHandler(value);
			}
		};
	}

	public void init(OutputStream stream) {
		this.mOutputStream = stream;
	}

	public void receiveValue(byte[] value, int len) {
		Log.d(TAG, "receiveValue() value = " + CHexConver.byte2HexStr(value, len));
		if (value.length <= 0) {
			Log.d(TAG, "value length <= 0");
			return;
		}

		if (len == 1) {
			int id = value[0];
			parseCommand(id, new byte[] {});
		} else if (value.length > 1) {
			int id = value[0];
			byte[] data = new byte[len - 1];
			System.arraycopy(value, 1, data, 0, data.length);
			parseCommand(id, data);
		}
	}

	public void parseCommand(int id, byte[] value) {
		mApp.notifyTBoxParamChange(id, value);
		/*
		 * switch (id) { case Constant.COMMAND_ID.DOWNLOAD_PACKAGE_REQUEST_CMD:
		 * 
		 * break; case Constant.COMMAND_ID.DOWNLOAD_PROGRESS_CMD: break; case
		 * Constant.COMMAND_ID.DOWNLOAD_RESULT_CMD: break; case
		 * Constant.COMMAND_ID.UPGRADE_REQUEST_CMD: break; case
		 * Constant.COMMAND_ID.UPGRADE_STATE_CMD: break; case
		 * Constant.COMMAND_ID.UPGRADE_PROGRESS_CMD: break; case
		 * Constant.COMMAND_ID.UPGRADE_RESULT_CMD: break; case
		 * Constant.COMMAND_ID.UPGRADE_REVERT_RESULT: break; default: break; }
		 */
	}

	public void setTBoxParam(int id, byte[] values) {
		byte[] sendValue = null;
		if (values == null || values.length < 0) {
			sendValue = new byte[1];
			sendValue[0] = (byte) id;
		} else {
			sendValue = new byte[values.length + 1];
			sendValue[0] = (byte) id;
			for (int i = 0; i < values.length; i++) {
				sendValue[i + 1] = values[i];
			}
		}
		writeCommand(sendValue);
	}

	public void writeCommand(byte[] values) {
		Message msg = Message.obtain();
		msg.obj = values;
		mWriteCommandHandler.sendMessage(msg);
	}

	public synchronized void writeCommandByHandler(byte[] value) {
		try {
			Log.d(TAG, "write data =" + CHexConver.byte2HexStr(value, value.length));
			mOutputStream.write(value);
			mOutputStream.flush();
			Thread.sleep(100);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
