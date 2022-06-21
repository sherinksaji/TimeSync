package com.example.fbase;

public class Time {
    String time;

    //
    //time takes in strings in the form HH:MM like 07:00
    public Time(String time) {
        this.time= time;

    }



    public String getTime () {
        return this.time;
    }

    public void setTime (String time){
        this.time = time;
    }

    public int getHour() {
        char c1= this.time.charAt(0);
        String h1=Character.toString(c1);
        if (h1.equals("0")){           //if hour is like HH=07
            char c2=this.time.charAt(1);
            String h2=Character.toString(c2);
            return Integer.valueOf(h2);//then return single digit int like 7
        }
        else{
            String [] breakDownTime=this.time.split(":");//if hour is like HH=17
            String hour= breakDownTime[0];
            return Integer.valueOf(hour);//then return double digit integer like 17

        }
    }

    public int getMinute() {
        String [] hourMinSeparate=this.time.split(":");
        String m=hourMinSeparate[1];
        String [] m01=m.split("");
        String m0=m01[0];
        String m1=m01[1];
        if (m0.equals("0")){ //if minute is like MM=07
            return Integer.valueOf(m1);
        }
        else{return Integer.valueOf(m);} //if minute is like MM=17
    }

    public void setHour(int hour){
        String hourStr="";
        if (hour>=10){
            hourStr=Integer.toString(hour);
        }
        else{
            hourStr="0"+Integer.toString(hour);//if single digit int hour, need to format as HH=07
        }
        String [] hourMinSeparate = this.time.split(":");
        String str= hourStr +":"+ hourMinSeparate[1];
        this.setTime(str);

    }

    public void setMinute(int minute){
        String minuteStr="";
        if (minute>=10){
            minuteStr=Integer.toString(minute);
        }
        else{
            minuteStr="0"+Integer.toString(minute);//if single digit int minute, need to format as MM=07
        }
        String [] hourMinSeparate = this.time.split(":");
        String str2 =hourMinSeparate[0]+":"+minuteStr;
        this.setTime(str2);

    }

    public int getIntTime() {
        String timeString = this.time;
        timeString = timeString.replace(":", "");
        int startTimeInt = Integer.valueOf(timeString);
        return startTimeInt;
    }






}
