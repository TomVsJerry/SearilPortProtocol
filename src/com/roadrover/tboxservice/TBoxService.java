package com.roadrover.tboxservice;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.security.InvalidParameterException;

import com.roadrover.tboxservice.aidl.ITBoxService;
import com.roadrover.tboxservice.aidl.ITBoxServiceListener;
import com.roadrover.tboxservice.protocal.TBoxParser;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

public class TBoxService extends Service {

	private static String TAG = "TBoxService";
	private TBoxApplication mApp;
	private OutputStream mOutputStream;
	private InputStream mInputStream;
	private TBoxParser mParser;
	private Thread mReadThread;
	private HandlerThread mParserHandlerThread;
	private ParserHandler mParserHandler;
	private Binder mBinder = new TBoxBinder(this);
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {

			default:
				break;
			}
		};
	};
	

	class ParserHandler extends Handler {
		public ParserHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			byte[] value = (byte[]) msg.obj;
			int len = msg.arg1;
			try {
				mParser.receiveValue(value, len);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		Log.d(TAG, "onBind()");
		return mBinder;
	}

	@Override
	public void onCreate() {
		Log.d(TAG, "onCreate()");
		super.onCreate();
		mApp = TBoxApplication.getInstance();
		initSerial();
		mParserHandlerThread = new HandlerThread("handler_thread");
		mParserHandlerThread.start();
        mParserHandler = new ParserHandler(mParserHandlerThread.getLooper());
	}

	private int initSerial() {
		Log.e(TAG, "initSerial");
		try {
			mApp.getSerialPort();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidParameterException e) {
			e.printStackTrace();
		}
		if (mApp.mSerialPort == null)
			return -1;

		mOutputStream = mApp.mSerialPort.getOutputStream();
		mInputStream = mApp.mSerialPort.getInputStream();
		Log.d(TAG, "mOutputStream = " + mOutputStream);
		Log.d(TAG, "mInputStream = " + mInputStream);
		if (mOutputStream == null || mInputStream == null)
			return -2;
		mParser = new TBoxParser(mApp, mHandler);
		mReadThread = new ReadThread();
		mReadThread.start();
		mParser.init(mOutputStream);
		mApp.mOutputStream = mOutputStream;
		return 0;
	}

	private class ReadThread extends Thread {
		@Override
		public void run() {
			super.run();
			Log.d(TAG, "start to read data");
			byte[] value = new byte[1024];
			while (!isInterrupted()) {
				try {
					if (mInputStream == null)
						return;
					int readlen = mInputStream.read(value, 0, value.length);
					if (readlen > 0) {
						Message msg = Message.obtain();
						msg.obj = value;
						msg.arg1 = readlen;
						mParserHandler.sendMessage(msg);
					} else if (readlen == -1) {
						// mParser.init(mOutputStream);
					}
					try {
						sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} catch (IOException e) {
					Log.e(TAG, e.getMessage());
					mOutputStream = null;
					mInputStream = null;
					return;
				}
			}
		}
	}
	
	class TBoxBinder extends ITBoxService.Stub{
		private WeakReference<TBoxService> mService;

        public TBoxBinder(TBoxService service) {
            this.mService = new WeakReference<TBoxService>(service);
        }
		@Override
		public void registerTBoxServiceListener(ITBoxServiceListener listener)
				throws RemoteException {
			mService.get().registerTBoxServiceListener(listener);
		}

		@Override
		public void unregisteTBoxServiceListener(ITBoxServiceListener listener)
				throws RemoteException {
			mService.get().unregisterTBoxServiceListener(listener);
		}

		@Override
		public int setTBoxParam(int id, byte[] value) throws RemoteException {
			mService.get().setTBoxParam(id,value);
			return 0;
		}
	}

	public void registerTBoxServiceListener(ITBoxServiceListener listener) {
		mApp.registerTBoxServiceListener(listener);
		
	}
	
	public void unregisterTBoxServiceListener(ITBoxServiceListener listener) {
		mApp.unregisterTBoxServiceListener(listener);
	}

	public void setTBoxParam(int id, byte[] value) {
		mParser.setTBoxParam(id,value);
	}

}
