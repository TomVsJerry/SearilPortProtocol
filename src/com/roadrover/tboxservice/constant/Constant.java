package com.roadrover.tboxservice.constant;

public class Constant {
	
	public static class COMMAND_ID{
		public static final int DOWNLOAD_PACKAGE_REQUEST_CMD = 0x41;// TBOX ֪ͨ HU ���³�����Ҫ����
		public static final int DOWNLOAD_CONFRIM_CMD_ = 0x44;// HU �� TBOX ����������Ȩ״̬
		public static final int DOWNLOAD_PROGRESS_CMD = 0x42;// TBOX �� HU ���� OTA �����������ؽ���
		public static final int DOWNLOAD_RESULT_CMD = 0x43;// TBOX �� HU ���� OTA �����������ؽ��
		public static final int DOWNLOAD_AGAIN_CMD_ = 0x45;// HU �� TBOX �����������س���
		
		public static final int UPGRADE_REQUEST_CMD = 0x46;//Tbox ��������(����������������� ���ʹ˱���)
		public static final int UPGRADE_CONFIRM_CMD_ = 0x47;//HU ֪ͨ Tbox ����
		public static final int UPGRADE_STATE_CMD = 0x48;//Tbox ����״̬����
		public static final int UPGRADE_USER_CONFRIM_CMD_ = 0x49;//�û�ѡ������,HU ���������� TBOX
		public static final int UPGRADE_PROGRESS_CMD = 0x4A;//TBOX �� HU ���� OTA ��������
		public static final int UPGRADE_RESULT_CMD = 0x4B;//TBOX �� HU �ṩ��ǰ�������
		
		public static final int UPGRADE_REVERT_CMD_ = 0x4C;//HU����ʧ�ܺ�ع�
		public static final int UPGRADE_REVERT_RESULT = 0x4D;//TBOX �� HU �����ع����
	}
}
