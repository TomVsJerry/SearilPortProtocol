package com.roadrover.tboxservice.tools;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 16����ֵ��String/Byte֮���ת��
 */
public class CHexConver {
    private final static char[] mChars = "0123456789ABCDEF".toCharArray();
    private final static String mHexStr = "0123456789ABCDEF";

    /**
     * ���16�����ַ����Ƿ���Ч
     *
     * @param sHex String 16�����ַ���
     * @return boolean
     */
    public static boolean checkHexStr(String sHex) {
        String sTmp = sHex.toString().trim().replace(" ", "").toUpperCase(Locale.US);
        int iLen = sTmp.length();

        if (iLen > 1 && iLen % 2 == 0) {
            for (int i = 0; i < iLen; i++)
                if (!mHexStr.contains(sTmp.substring(i, i + 1)))
                    return false;
            return true;
        } else
            return false;
    }

    /**
     * �ַ���ת����ʮ�������ַ���
     *
     * @param str String ��ת����ASCII�ַ���
     * @return String ÿ��Byte֮��ո�ָ�����: [61 6C 6B]
     */
    public static String str2HexStr(String str) {
        StringBuilder sb = new StringBuilder();
        byte[] bs = str.getBytes();

        for (int i = 0; i < bs.length; i++) {
            sb.append(mChars[(bs[i] & 0xFF) >> 4]);
            sb.append(mChars[bs[i] & 0x0F]);
            sb.append(' ');
        }
        return sb.toString().trim();
    }

    /**
     * ʮ�������ַ���ת���� ASCII�ַ���
     *
     * @param hexStr String Byte�ַ���
     * @return String ��Ӧ���ַ���
     */
    public static String hexStr2Str(String hexStr) {
        hexStr = hexStr.toString().trim().replace(" ", "").toUpperCase(Locale.US);
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];
        int iTmp = 0x00;
        ;

        for (int i = 0; i < bytes.length; i++) {
            iTmp = mHexStr.indexOf(hexs[2 * i]) << 4;
            iTmp |= mHexStr.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (iTmp & 0xFF);
        }
        return new String(bytes);
    }

    /**
     * bytesת����ʮ�������ַ���
     *
     * @param b    byte[] byte����
     * @param iLen int ȡǰNλ���� N=iLen
     * @return String ÿ��Byteֵ֮��ո�ָ�
     */
    public static String byte2HexStr(byte[] b, int iLen) {
        StringBuilder sb = new StringBuilder();
        for (int n = 0; n < iLen; n++) {
            sb.append(mChars[(b[n] & 0xFF) >> 4]);
            sb.append(mChars[b[n] & 0x0F]);
            sb.append(' ');
        }
        return sb.toString().trim().toUpperCase(Locale.US);
    }

    /**
     * bytes�ַ���ת��ΪByteֵ
     *
     * @param src String Byte�ַ�����ÿ��Byte֮��û�зָ���(�ַ���Χ:0-9 A-F)
     * @return byte[]
     */
    public static byte[] hexStr2Bytes(String src) {
        /*������ֵ���й淶������*/
        src = src.trim().replace(" ", "").toUpperCase(Locale.US);
        //����ֵ��ʼ��
        int m = 0, n = 0;
        int iLen = src.length() / 2; //���㳤��
        byte[] ret = new byte[iLen]; //����洢�ռ�

        for (int i = 0; i < iLen; i++) {
            m = i * 2 + 1;
            n = m + 1;
            ret[i] = (byte) (Integer.decode("0x" + src.substring(i * 2, m) + src.substring(m, n)) & 0xFF);
        }
        return ret;
    }

    /**
     * String���ַ���ת����unicode��String
     *
     * @param strText String ȫ���ַ���
     * @return String ÿ��unicode֮���޷ָ���
     * @throws Exception
     */
    public static String strToUnicode(String strText)
            throws Exception {
        char c;
        StringBuilder str = new StringBuilder();
        int intAsc;
        String strHex;
        for (int i = 0; i < strText.length(); i++) {
            c = strText.charAt(i);
            intAsc = (int) c;
            strHex = Integer.toHexString(intAsc);
            if (intAsc > 128)
                str.append("\\u");
            else // ��λ��ǰ�油00
                str.append("\\u00");
            str.append(strHex);
        }
        return str.toString();
    }

    /**
     * unicode��Stringת����String���ַ���
     *
     * @param hex String 16����ֵ�ַ��� ��һ��unicodeΪ2byte��
     * @return String ȫ���ַ���
     * @see \\CHexConver.unicodeToString("\\u0068\\u0065\\u006c\\u006c\\u006f")
     */
    public static String unicodeToString(String hex) {
        int t = hex.length() / 6;
        int iTmp = 0;
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < t; i++) {
            String s = hex.substring(i * 6, (i + 1) * 6);
            // ��16���Ƶ�stringתΪint
            iTmp = (Integer.valueOf(s.substring(2, 4), 16) << 8) | Integer.valueOf(s.substring(4), 16);
            // ��intת��Ϊ�ַ�
            str.append(new String(Character.toChars(iTmp)));
        }
        return str.toString();
    }

    //��λ��ǰ����λ�ں�
    public static byte[] int2bytes(int num) {
        byte[] result = new byte[4];
        result[0] = (byte) ((num >>> 24) & 0xff);//˵��һ
        result[1] = (byte) ((num >>> 16) & 0xff);
        result[2] = (byte) ((num >>> 8) & 0xff);
        result[3] = (byte) ((num >>> 0) & 0xff);
        return result;
    }

    //��λ��ǰ����λ�ں�
    public static int bytes2int(byte[] bytes) {
        int result = 0;
        if (bytes.length == 4) {
            int a = (bytes[0] & 0xff) << 24;//˵����
            int b = (bytes[1] & 0xff) << 16;
            int c = (bytes[2] & 0xff) << 8;
            int d = (bytes[3] & 0xff);
            result = a | b | c | d;
        }
        return result;
    }


    //��λ��ǰ����λ�ں�
    public static byte[] int2bytesExtend(int num) {
        byte[] result = new byte[2];
        result[0] = (byte) ((num >>> 8) & 0xff);
        result[1] = (byte) ((num >>> 0) & 0xff);
        return result;
    }

    //��λ��ǰ����λ�ں�
    public static int bytes2intExtend(byte[] bytes) {
        int result = 0;
        if (bytes.length == 2) {
            int a = (bytes[0] & 0xff) << 8;
            int b = (bytes[1] & 0xff);
            result = a | b;
        }
        return result;
    }


    /**
     * �ж��ַ����Ƿ�������
     *
     * @param strName �ַ���
     * @return �Ƿ�������
     */
    public static boolean isMessyCode(String strName) {
        try {
            Pattern p = Pattern.compile("\\s*|\t*|\r*|\n*");
            Matcher m = p.matcher(strName);
            String after = m.replaceAll("");
            String temp = after.replaceAll("\\p{P}", "");
            char[] ch = temp.trim().toCharArray();
            int length = (ch != null) ? ch.length : 0;
            for (int i = 0; i < length; i++) {
                char c = ch[i];
                if (!Character.isLetterOrDigit(c)) {
                    String str = "" + ch[i];
                    if (!str.matches("[\u4e00-\u9fa5]+")) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
