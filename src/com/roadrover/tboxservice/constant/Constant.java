package com.roadrover.tboxservice.constant;

public class Constant {
	
	public static class COMMAND_ID{
		public static final int DOWNLOAD_PACKAGE_REQUEST_CMD = 0x41;// TBOX 通知 HU 有新程序需要下载
		public static final int DOWNLOAD_CONFRIM_CMD_ = 0x44;// HU 向 TBOX 返回下载授权状态
		public static final int DOWNLOAD_PROGRESS_CMD = 0x42;// TBOX 向 HU 反馈 OTA 升级程序下载进度
		public static final int DOWNLOAD_RESULT_CMD = 0x43;// TBOX 向 HU 反馈 OTA 升级程序下载结果
		public static final int DOWNLOAD_AGAIN_CMD_ = 0x45;// HU 向 TBOX 请求重新下载程序
		
		public static final int UPGRADE_REQUEST_CMD = 0x46;//Tbox 请求升级(有升级包已下载完成 发送此报文)
		public static final int UPGRADE_CONFIRM_CMD_ = 0x47;//HU 通知 Tbox 升级
		public static final int UPGRADE_STATE_CMD = 0x48;//Tbox 升级状态反馈
		public static final int UPGRADE_USER_CONFRIM_CMD_ = 0x49;//用户选择升级,HU 发升级请求到 TBOX
		public static final int UPGRADE_PROGRESS_CMD = 0x4A;//TBOX 向 HU 反馈 OTA 升级进度
		public static final int UPGRADE_RESULT_CMD = 0x4B;//TBOX 向 HU 提供当前升级结果
		
		public static final int UPGRADE_REVERT_CMD_ = 0x4C;//HU升级失败后回滚
		public static final int UPGRADE_REVERT_RESULT = 0x4D;//TBOX 向 HU 反馈回滚结果
	}
}
