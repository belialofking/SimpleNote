package simplenote.util;

import java.io.IOException;
import java.util.Calendar;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class IDUtil {
    private IDUtil(){}
    public static String generateId(){
        return UUID.randomUUID().toString().replace("-","");
    }
    private static final String MONTHNAME = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    
    private static String getDtString(){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);
        
        String str_year = Integer.toString((year - 2000),36);
        String str_month = String.valueOf(MONTHNAME.charAt(month));
        String str_day = Integer.toString(day, 32);
        String str_hour = String.valueOf(MONTHNAME.charAt(hour));
        String str_second = Integer.toString(minute * 60 + second, 36);
        if(str_second.length() == 1){
            str_second = "00" + str_second;
        }else if(str_second.length() == 2){
            str_second = "0" + str_second;
        }
        return  str_year + str_month + str_day + str_hour + str_second;
    }
    private static String lastDt = "";
    private static final AtomicInteger atom = new AtomicInteger(1);
    private static synchronized String getDtId(String postfix){
        String dt = getDtString();
        int num = 1;
        if(dt.equals(lastDt)){
            num = atom.getAndIncrement();
        }else{
            lastDt = dt;
            atom.set(num + 1);
        }
        String strNum = Integer.toString(num, 36);
        if(strNum.length() == 1){
            strNum = "00" + strNum;
        }else if(strNum.length() == 2){
            strNum = "0" + strNum;
        }
        return (dt + strNum).toUpperCase();
        
    }
    public static void main(String[] args){
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < 100; j++) {
                        System.out.println(getDtId(""));
                    }
                }
            });
            thread.start();
            
        }
        try {
            System.in.read();
        } catch (IOException ex) {
            Logger.getLogger(IDUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
