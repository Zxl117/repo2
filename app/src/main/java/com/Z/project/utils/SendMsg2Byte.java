package com.Z.project.utils;

import com.clj.fastble.utils.HexUtil;
import com.mob.tools.utils.Strings;

import java.text.DecimalFormat;

public  class SendMsg2Byte
{
    public  static String  Nor2Sci (String string )
    {
        DecimalFormat dFormat =new DecimalFormat("0.0E0");
        return dFormat.format(Double.parseDouble(string));
    }


    /**
     * 发送指令到剂量仪
     * @param str
     * @param type   类型 A1 剂量率 A2 累计剂量
     * @return
     */
    public static  String sendMsg (String str,String type )
    {
        String  strings []={"A5",type,"","2E","","","",type,"E5"};
        String strRst=Nor2Sci(str);
        String result ="";
        if (strRst.contains("-"))
        {
            strings[5]="46";
        }
        else
        {
            strings[5]="45";
        }
        strings[2]="0"+String.valueOf(strRst.charAt(0));
        strings[4]="0"+String.valueOf(strRst.charAt(2));
        strings[6]="0"+String.valueOf(strRst.charAt(strRst.length()-1));
        for(String string:strings)
        {
            result+=string;
        }
        return result;
    }
    public static String  sendMsgT(String hour,String minute,String second,String type )
    {
        hour=String.format("%02X",Integer.valueOf(hour));
        minute=String.format("%02X",Integer.valueOf(minute));
        second=String.format("%02X",Integer.valueOf(second));
        String  strings []={"A5",type, hour, minute, second,"00","00",type,"E5"};
        String result ="";
        for ( String str: strings)
        {
            result+=str;
        }
        return  result;
     }

}
