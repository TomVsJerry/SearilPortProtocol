package com.roadrover.tboxservice.aidl;

interface ITBoxServiceListener{
	int onTBoxServiceParamChange(int id,in byte[] value);
}