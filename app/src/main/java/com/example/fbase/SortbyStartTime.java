package com.example.fbase;

import java.util.Comparator;


public class SortbyStartTime implements Comparator<TimePeriod> {
    @Override
    public int compare(TimePeriod t1, TimePeriod t2) {
        int t1startTime=t1.startTime.getIntTime();
        int t2startTime=t2.startTime.getIntTime();
        return t1startTime-t2startTime;
    }


}
