package com.Z.project.utils;

import java.text.DecimalFormat;

public  class SendMsg2Byte
{
    public  static String  Nor2Sci (String string )
    {
        DecimalFormat dFormat =new DecimalFormat("0.0E0");
        return dFormat.format(Double.parseDouble(string));
    }
    public static  String sendMsg (String str)
    {
        String  strings []={"A5","A1","","2E","","","","A1","E5"};
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

}
