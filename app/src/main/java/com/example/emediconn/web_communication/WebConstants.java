package com.example.emediconn.web_communication;

public class WebConstants {

    /* To try the app with Enablex hosted service you need to set the kTry = true */
    public  static  final  boolean kTry = false;

    /*Your webservice host URL, Keep the defined host when kTry = true */

    public static final String kBaseURL = "http://healthcare.blucorsys.in/videocall/api/";

    /*The following information required, Only when kTry = true, When you hosted your own webservice remove these fileds*/

    /*Use enablex portal to create your app and get these following credentials*/
    public static final String kAppId = "5fc4c7bc1a819c56cb4afe23";
    public static final String kAppkey = "yhyaeQeeeRyVyUuzaVyrySysyZaMu8yma4ed ";

    public static final String getRoomId = "create-room/index";
    public static final int getRoomIdCode = 1;
    public static final String validateRoomId = "get-room/index?roomId=";
    public static final int validateRoomIdCode = 2;
    public static final String getTokenURL = "create-token/index";
    public static final int getTokenURLCode = 3;
}
