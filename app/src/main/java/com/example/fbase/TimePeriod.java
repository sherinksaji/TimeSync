package com.example.fbase;

public class TimePeriod {
    Time startTime;
    Time endTime;

    //TimePeriod takes in String like "07:00 - 08:00"
    public TimePeriod(String timeSet){
        String [] startEndSeparate=timeSet.split(" - ");
        this.startTime=new Time(startEndSeparate[0]);
        this.endTime=new Time(startEndSeparate[1]);

    }

    public void setTimePeriod(String timeSet){
        String [] startEndSeparate=timeSet.split(" - ");
        this.startTime.setTime(startEndSeparate[0]);
        this.endTime.setTime(startEndSeparate[1]);
    }

    //returns an array [diffInHours,diffInMins]
    public int [] Duration(){
        int diffInHours;

        if (this.endTime.getHour()!=0) {
            diffInHours = this.endTime.getHour() - this.startTime.getHour();
        }
        else{diffInHours=24-this.startTime.getHour();}
        int diffInMins=this.endTime.getMinute()-this.startTime.getMinute();
        if (diffInMins<0){//borrow 60 minutes from diffInHours
            diffInHours-=1;
            diffInMins+=60;
        }
        int [] duration=new int[2];
        duration[0]=diffInHours;
        duration[1]=diffInMins;

        return duration;
    }

    @Override
    public String toString(){
        return this.startTime.getTime()+" - "+this.endTime.getTime();
    }


}
