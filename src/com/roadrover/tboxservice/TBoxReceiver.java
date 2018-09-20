package com.roadrover.tboxservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.sax.StartElementListener;
import android.util.Log;

public class TBoxReceiver extends BroadcastReceiver {
	
	private static final String TAG = "TBoxReceiver";
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		Log.d(TAG, "onReceive() action = " + action);
		if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){
			Intent service = new Intent();
			service.setClass(context, TBoxService.class);
			context.startService(service);
		}
	}
}
