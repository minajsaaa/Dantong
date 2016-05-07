package com.goodmorningrainbow.common;

import java.util.ArrayList;

import com.goodmorningrainbow.model.PriceListDT;

public class Const {
    public Const() {}
    
    public final static boolean VERBOSE = false;
    
    public final static int SELECT_MANUF_RESULT_CODE = 10;
    public final static int SELECT_PHONE_LIST_RESULT_CODE = 20;
    public final static int SELECT_PRICE_LIST_RESULT_CODE = 30;
    public final static int CLUB_SELECT_PRICE_RESULT_CODE = 40;
    public final static int SELECT_MONTHLY_LIST_RESULT_CODE = 50;
    public final static int SELECT_ADVICE_RESULT_CODE = 60;
    
    public static int SELECT_AGENCY = 0;
    public static String SELECT_MANUF = "1";
    
    //public static ArrayList<PhoneListDT> PHONE_LIST_ARRAY = null;
    public static ArrayList<PriceListDT> PRICE_LIST_ARRAY = null;
    
    public static int PHONE_POSITION = 0;
    public static String PHONE_ID = "";
    public static int PRICE_POSITION = 0;
    
    public final static String SK_BASE_PRICE_ID = "60";
    public final static String KT_BASE_PRICE_ID = "55";
    public final static String LG_BASE_PRICE_ID = "36";
    public final static String SE_BASE_PRICE_ID = "4";
    public final static String CJ_BASE_PRICE_ID = "4";
    public final static String UM_BASE_PRICE_ID = "2";
    
    public static boolean isClub = false;
    
    public final static String BASE_URL = "http://web0.goodmorningrainbow.com";
    public final static String PRICE_LIST_URL = BASE_URL + "/m_rainbow/mobile/dan_price_list.php";
    public final static String PHONE_LIST_URL = BASE_URL + "/m_rainbow/mobile/dan_phone_list.php";
    public final static String COST_LIST_URL = BASE_URL + "/m_rainbow/mobile/dan_cost_list.php";
    public final static String PHONE_UPDATE_URL = BASE_URL + "/m_rainbow/mobile/dan_phone_update.php";
    public final static String COMMENT_LIST_URL = BASE_URL + "/m_rainbow/mobile/dan_comment_list.php";
    public final static String COMMENT_UPDATE_URL = BASE_URL + "/m_rainbow/mobile/dan_comment_update.php";
    
    public final static int TELECOM_SK = 0;
    public final static int TELECOM_KT = 1;
    public final static int TELECOM_LG = 2;
    public final static int TELECOM_CJ = 3;
    public final static int TELECOM_SV = 4;    
    public final static int TELECOM_UM = 5;
    
    public final static String POPUP_TYPE = "POPUP_TYPE";
    public final static String SHOW_COST = "SHOW_COST";
    public final static String SHOW_MONTHLY = "SHOW_MONTHLY";
    
    public final static int PAGE_LEFT = 0;
    public final static int PAGE_RIGHT = 1;
    public final static int PAGE_ADVICE = 2;
    public final static int PAGE_ADVICE_DETAIL = 3;
    
    public final static int H_LEFT_IMG = 0;
    public final static int H_RIGHT_IMG = 1;    
    public final static int LEFT_ANI = 2;
    public final static int RIGHT_ANI = 3;
    
    public final static int COMMENT_POPUP_RESULT = 10000;
}
