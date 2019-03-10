package com.content_load_sb.utils;

import java.util.concurrent.TimeUnit;

public class Utils {

    public String stopwatch(long start, String whatFor) {
        System.out.println("STOPWATCH FOR [" + whatFor + "]");

        long end = System.nanoTime();
        long duration = end - start;
        long timePassedMilis = duration / 1000000;
        System.out.println("start ms : " + start / 1000000);
        System.out.println("end ms : " + end / 1000000);
        System.out.println("nanoSeconds passed : " + timePassedMilis);
        String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(timePassedMilis),
                TimeUnit.MILLISECONDS.toMinutes(timePassedMilis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(timePassedMilis) % TimeUnit.MINUTES.toSeconds(1));
        System.out.println("Total Time : " + hms);
        System.out.println("--------------------------------------------------------");
        return hms;
    }

}