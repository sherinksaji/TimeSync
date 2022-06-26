package com.example.fbase;
/**
 DirtyTimeSet class takes in a TimePeriod object.
 DirtyTimeSet has method void roundStartTime () to round’s  startTimes like 08:17 and 08:45 to
 earlier perfect half an hours like 08:00 and 08:30.Another method is void roundEndTime() to
 round endTimes like 09:17 and 09:45 to later perfect half an hours like 09:30 and 10:00.
 int DurationInHalfHours() returns the number of halfAnHours between a startTime and endTime.
 Method String matchStart() returns a DirtyTimeSet object’s first perfect half-hour TimePeriod based
 on its already perfect startTime. Method String matchEnd() returns a DirtyTimeSet object’s last
 perfect half-hour TimePeriod based on its already perfect endTime.
 For example, if DirtyTimeSet is 08:00 - 12:00, then matchStart() returns “08:00 - 08:30” and
 matchEnd() returns “11:30 - 12:00”.
 */


public class DirtyTimeSet {
    private TimePeriod dirtyTimeSet;
    int startHour;
    int startMin;
    int endHour;
    int endMin;



    DirtyTimeSet(TimePeriod timeSet){
        this.dirtyTimeSet=timeSet;
        this.startHour=startHour;
        this.startMin=startMin;
        this.endHour=endHour;
        this.endMin=endMin;
    }

    public void  roundStartTime(){
        startHour=this.dirtyTimeSet.startTime.getHour();
        startMin=this.dirtyTimeSet.startTime.getMinute();
        //round to 0-->if got class at 7.15, u pretend class is at 07:00
        //0 will be rounded to 0
        if (startMin<30){
            this.dirtyTimeSet.startTime.setMinute(0);
        }
        //round up 30-->if got class at 7.45, u pretend class is at 07:30
        //30 will be rounded to 30
        else{
            this.dirtyTimeSet.startTime.setMinute(30);
        }

    }

    public void roundEndTime(){
        this.endHour=this.dirtyTimeSet.endTime.getHour();
        this.endMin=this.dirtyTimeSet.endTime.getMinute();

        //0 will be rounded to 0
        if (endMin==0){
            this.dirtyTimeSet.endTime.setMinute(0);
        }

        //round to 30 --> if class ends at 7.15, u pretend class ends at 07:30
        //30 will be rounded to 30

        if (endMin>0&&endMin<=30){
            this.dirtyTimeSet.endTime.setMinute(30);
        }
        //round minute to 0 and plus 1 hour--> if class ends at 7.45, you pretend class ends at 08:00
        if (endMin>30) {
            if (endHour!=23) {//23:45 needs to be rounded to 00:00
                this.dirtyTimeSet.endTime.setHour(endHour + 1);
            }
            else{this.dirtyTimeSet.endTime.setHour(0);}
            this.dirtyTimeSet.endTime.setMinute(0);

        }

    }

    //the below method returns difference in halfHours between dirtyTimeSet.startTime and dirtyTimeSet.endTime
    //the durations between dirtyTimeSet.startTimes and dirtyTimeSet.endTimes will always be in perfect halfAnHours
    // because of the round startTime and round endTime methods
    public int durationInHalfHours(){
        int diffInHalfHours;
        int [] duration=this.dirtyTimeSet.Duration();
        if (duration[1]==30){
            diffInHalfHours=(duration[0])*2+1;
        }
        else{
            diffInHalfHours=(duration[0])*2;
        }
        return diffInHalfHours;

    }

    //given a startTime, matchStart() gives a string "startTime - halfAnHourAfterStartTime"
    //e.g. if dirtyTimeSet.startTime is 07:00, then matchStart returns "07:00 - 07:30"
    public String matchStart(){
        String [] breakDownStart=dirtyTimeSet.startTime.getTime().split(":");
        String startHourString= breakDownStart[0];
        String startMinuteString= breakDownStart[1];
        String halfHourTiming;
        String startHourString2;
        //special case: start time with 23:30 must give string 23:30 - 00:00
        if (dirtyTimeSet.startTime.getTime().equals("23:30")){
            halfHourTiming= "23:30 - 00:00";
        }
        else {
            if (dirtyTimeSet.startTime.getMinute()==0) { //"07:00 - 07:30"
                halfHourTiming = dirtyTimeSet.startTime.getTime() + " - " + startHourString + ":30";
            } else {  //"07:30 - 08:00"

                if (dirtyTimeSet.startTime.getHour()<9) {//allowing for 09:30-->10:00 instead of 010:30
                    int startHourInt2 = dirtyTimeSet.startTime.getHour()+1;
                    startHourString2 = "0" + Integer.toString(startHourInt2);
                } else {
                    int startHourInt2 = dirtyTimeSet.startTime.getHour() + 1;
                    startHourString2 = Integer.toString(startHourInt2);
                }

                halfHourTiming = dirtyTimeSet.startTime.getTime() + " - " + startHourString2 + ":00";
            }
        }

        return halfHourTiming;

    }

    //given a endTime,matchEnd() gives a string "halfAnHourBeforeEndTime - endTime"
    //e.g. if dirtyTimeSet.endTime is "08:00", then matchEnd() returns "07:30 - 08:00"
    public String matchEnd(){
        String [] breakDownEnd=dirtyTimeSet.endTime.getTime().split(":");
        String endHourString= breakDownEnd[0];
        String endMinuteString=breakDownEnd[1];
        String endHalfHourTiming;
        String endHourString2;
        //special case
        if (dirtyTimeSet.endTime.getTime().equals("00:00")){
            endHalfHourTiming= "23:30 - 00:00";
        }
        else {
            if (dirtyTimeSet.endTime.getMinute()==30) { //"09:00 - 09:30"
                endHalfHourTiming = endHourString + ":00"+" - "+dirtyTimeSet.endTime.getTime();
            } else {  //"09:30 - 10:00"
                if (dirtyTimeSet.endTime.getHour()<=10) {//10-1=9
                    int endHourInt2 = dirtyTimeSet.endTime.getHour() - 1;
                    endHourString2 = "0" + Integer.toString(endHourInt2);
                } else {
                    int endHourInt2 =  dirtyTimeSet.endTime.getHour() - 1;
                    endHourString2 = Integer.toString(endHourInt2);
                }

                endHalfHourTiming = endHourString2+":30" + " - " + dirtyTimeSet.endTime.getTime();
            }
        }

        return endHalfHourTiming;
    }

    public String toString (){
        return this.dirtyTimeSet.toString();
    }




}

