package com.roadrover.tboxservice;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Iterator;

import com.roadrover.tboxservice.aidl.ITBoxServiceListener;
import com.roadrover.tboxservice.seriaport.SerialPort;

import android.app.Application;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class TBoxApplication extends Application {

	private static final String TAG = "TBoxApplication";
	public SerialPort mSerialPort = null;
	public OutputStream mOutputStream;
	private static TBoxApplication instance;
	private ArrayList<DevListener> mListeners = new ArrayList<DevListener>();

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
	}

	public static TBoxApplication getInstance() {
		return instance;
	}

	public SerialPort getSerialPort() throws SecurityException, IOException,
			InvalidParameterException {
		if (mSerialPort == null) {
			String path = "/dev/ttyMT3"; //
			int baudrate = 115200;
			if ((path.length() == 0) || (baudrate == -1)) {
				throw new InvalidParameterException();
			}

			File device = new File(path);
			if (!device.canRead() || !device.canWrite()) {
				return null;
			}
			/* Open the serial port */
			mSerialPort = new SerialPort(device, baudrate, 0);
		}
		return mSerialPort;
	}

	class DevListener implements IBinder.DeathRecipient {
		public ITBoxServiceListener listener;

		public DevListener(ITBoxServiceListener l) {
			listener = l;
		}

		public void binderDied() {
			synchronized (mListeners) {
				mListeners.remove(this);
			}
		}
	}

	public void registerTBoxServiceListener(ITBoxServiceListener l) {
		DevListener obj = null;
		IBinder lbinder = l.asBinder();
		synchronized (mListeners) {
			Iterator<DevListener> it = mListeners.iterator();
			while (it.hasNext()) {
				obj = it.next();
				if (lbinder.equals(obj.listener.asBinder()))
					return;
			}
			obj = new DevListener(l);
			try {
				lbinder.linkToDeath(obj, 0);
			} catch (RemoteException e) {
				return;
			}
			mListeners.add(obj);
		}
	}

	public void unregisterTBoxServiceListener(ITBoxServiceListener listener) {
		DevListener obj = null;
		synchronized (mListeners) {
			Iterator<DevListener> it = mListeners.iterator();
			IBinder lbinder = listener.asBinder();
			while (it.hasNext()) {
				obj = it.next();
				if (lbinder.equals(obj.listener.asBinder()))
					break;
				obj = null;
			}
			if (obj != null) {
				int index = mListeners.indexOf(obj);
				if (index >= 0)
					mListeners.remove(index);
				lbinder.unlinkToDeath(obj, 0);
			}
		}
	}

	public int notifyTBoxParamChange(int id, byte[] value) {
		synchronized (mListeners) {
			if (mListeners.size() == 0)
				return -1;
			for (DevListener listener : mListeners) {
				try {
					listener.listener.onTBoxServiceParamChange(id, value);
				} catch (Exception e) {
					e.printStackTrace();
					Log.e(TAG, " notifyDeviceInfo " + e.getMessage());
				}
			}
			return 0;
		}
	}

}
