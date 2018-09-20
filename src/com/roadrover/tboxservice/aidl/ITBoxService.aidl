package com.roadrover.tboxservice.aidl;
import com.roadrover.tboxservice.aidl.ITBoxServiceListener;

interface ITBoxService{
	void registerTBoxServiceListener(in ITBoxServiceListener listener);
    void unregisteTBoxServiceListener(in ITBoxServiceListener listener);
    int setTBoxParam(int id,in byte[] value);
}