package utils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StringFormatter {
    public static String msToHumanTime(long millis) {
        long h = millis / (1000L * 60L * 60L);
        long m = millis / (1000L * 60L) - h * 60L;
        long s = millis / 1000L - h * 60L * 60L - m * 60L;
        long ms = millis - h * 1000L * 60L * 60L - m * 1000L * 60L - s * 1000L;
        String hS = h > 0 ? h + " h " : "";
        String mS = (h > 0) || (m > 0) ? m + " m " : "";
        String sS = ((h > 0) || (m > 0)) || (s > 0) ? s + " s " : "";
        String msS = (((h > 0) || (m > 0)) || (s > 0)) || (ms > 0) ? ms + " ms " : "";
        return hS + mS + sS + msS;
    }

    static String simpleDateForDirName(Date date) {
        return new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(date);
    }

    public static String toGbMbKbString(long bytes) {
        long l = 1024L;
        long gB = bytes / (l * l * l);
        long mB = bytes / (l * l) - gB * l;
        long kB = bytes / (l) - gB * l * l - mB * l;
        long b = bytes % (l);
        String gbS = gB > 0 ? gB + " Gb " : "";
        String mbS = (gB > 0) || (mB > 0) ? mB + " Mb " : "";
        String kbS = ((gB > 0) || (mB > 0)) || (kB > 0) ? kB + " Kb " : "";
        String bS = (((gB > 0) || (mB > 0)) || (kB > 0)) || (b > 0) ? b + " b " : "";
        return gbS + mbS + kbS + bS;
    }

    public static String elapsed(Date startDate) {
        return msToHumanTime(new Date().getTime() - startDate.getTime());
    }

    public static Long msToEnd(Date startDate, Long index, Long size) {
        long elapsedMS = new Date().getTime() - startDate.getTime();
        if (index == 0) index++;
        return size * elapsedMS / index - elapsedMS;
    }

    public static double p(long index, long size) {
        return Double.parseDouble(new DecimalFormat("#.###").format(((double) index / (double) size) * 100));
    }

    public static String getTimeToEnd(Date totalStartTime, int i, int size) {
        return msToHumanTime(msToEnd(totalStartTime, (long) i, (long) size));
    }

    public static SimpleDateFormat sdfDate = new SimpleDateFormat("YYY-MM-dd");
    public static SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");

    public static String makeJSONDateString(Date date) {
        return sdfDate.format(date) + "T" + sdfTime.format(date);
    }

    public static String makeProgressLogString(String methodName, Date startDate, int index, int size) {
        return "[" + methodName + "]\t" +
                "[" + (index + 1) + "/" + size + "]\t" +
                "[" + p(index+1, size) + " %]\t" +
                String.format("%-40s", "[time ot end : " + getTimeToEnd(startDate, index, size) + "]\t") +
                String.format("%-40s", "[elapsed : " + elapsed(startDate) + "]");
    }
}
