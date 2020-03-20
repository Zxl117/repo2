package com.Z.project.utils;

public class Rec_Msg
{

    public static double showData (byte [] buf)
    {
//        switch (buf[2])
//        {
//            case (byte) 0xA1:  //剂量率
//
//                break ;
//          case (byte) 0xA2:    //累积剂量
//
//                break ;
//            case (byte) 0xA4:    //剂量增量
//
//                break ;
//            case (byte) 0xA5:    //剂量累加
//
//                break ;
//
//            case (byte) 0xA7:     //刻度系数
//
//                break ;
//
//        }
       return getTureValue(buf);
    }


   private static  double getTureValue(byte [] buf )
   {
       double result ;
       if ( buf[5]==0x45)
       {
           result=(double)(((buf[2])+(double)(buf[4])/10)*(Math.pow(10,(buf[6]))));
       }
       else
       {
           result=(double)(((buf[2])+(double)(buf[4])/10)*(Math.pow(10,-(buf[6]))));

       }
       return result ;

   }








}
