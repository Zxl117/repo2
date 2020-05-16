package com.Z.project.utils;

public class Rec_Msg
{

    public static float showData (byte [] buf)
    {
       return getTureValue(buf);
    }


   private static  float getTureValue(byte [] buf )
   {
      float result;
       if(buf[0]==(byte)0xA5&&buf[1]==(byte)0xB3&&buf[buf.length-2]==(byte)0xB3)
       {
           if ( buf[5]==0x45)
           {
               result=(float)(((buf[2]-48)+(double)(buf[4]-48)/10)*(Math.pow(10,(buf[6]-48))));
           }
           else
           {
               result=(float)(((buf[2]-48)+(double)(buf[4]-48)/10)*(Math.pow(10,-(buf[6]-48))));
           }
           return result;
       }
      return 0;

   }








}
