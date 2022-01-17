package com.yumiao.usdttransfer.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * @ClassName DateUtils
 * @Description
 * @Author Administrator
 * @Date 2019/6/12 11:44
 * @Version 1.0
 **/
public class DateUtils {
    static int GRADE_YEAR = 0;
    static long GRADE_YEAR_VALID_TO = 0L;
    public static String DATE_PATTERN = "yyyy-MM-dd";

    public static final String DATE_YMD = "yyyy-MM-dd";

    public static final String DATE_HHMM = "HH:mm";


    public DateUtils() {
    }
    public static void main(String[] args){
        Date endTime=getEndDateTimeOfMonth(new Date());
        System.out.println(formatDateYMDHMS(endTime));
    }

    public static String formatDateYMDHMS(Date dt) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return fmt.format(dt);
    }

    public  static Integer getHour(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int hour= cal.get(Calendar.HOUR_OF_DAY);
        return  hour;
    }

    public static Date getEndDateTimeOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(5, cal.getActualMaximum(5));
        cal.set(11, 23);
        cal.set(12, 59);
        cal.set(13, 59);
        return cal.getTime();
    }

    public static Date getStartDateTimeOfMonth(Date date) {
        Calendar   cal=Calendar.getInstance();//获取当前日期
        cal.add(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一
        cal.set(11, 0);
        cal.set(12, 0);
        cal.set(13, 0);// 天
        return cal.getTime();
    }




    public static String getToday() {
        Calendar cal = Calendar.getInstance();
        Date today = cal.getTime();
        SimpleDateFormat fmt = new SimpleDateFormat(DATE_PATTERN);
        return fmt.format(today);
    }


    public static Date get(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(1, year);
        cal.set(2, month - 1);
        cal.set(5, day);
        cal.set(11, 0);
        cal.set(12, 0);
        cal.set(13, 0);
        cal.set(14, 0);
        return cal.getTime();
    }

    public static Date dayStart(Date dt) {
        if (dt == null) {
            return null;
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(dt);
            cal.set(11, 0);
            cal.set(12, 0);
            cal.set(13, 0);
            cal.set(14, 0);
            return cal.getTime();
        }
    }

    public static Date dayEnd(Date dt) {
        if (dt == null) {
            return null;
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(dt);
            cal.set(11, 23);
            cal.set(12, 59);
            cal.set(13, 59);
            cal.set(14, 999);
            return cal.getTime();
        }
    }

    public static boolean sameDay(Date dt1, Date dt2) {
        if (dt1 == null && dt2 == null) {
            return true;
        } else if (dt1 != null && dt2 != null) {
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(dt1);
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(dt2);
            return cal1.get(1) == cal2.get(1) && cal1.get(2) == cal2.get(2) && cal1.get(5) == cal2.get(5);
        } else {
            return false;
        }
    }

    public static Date addDay(Date dt, int n) {
        if (dt == null) {
            return null;
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(dt);
            cal.add(6, n);
            return cal.getTime();
        }
    }

    public static Date addMonth(Date dt, int n) {
        if (dt == null) {
            return null;
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(dt);
            cal.add(2, n);
            return cal.getTime();
        }
    }

    public static Date prevDate(Date dt) {
        return addDay(dt, -1);
    }

    public static Date nextDate(Date dt) {
        return addDay(dt, 1);
    }

    public static Date toGmt(Date dt, TimeZone tz) {
        if (dt == null) {
            return null;
        } else {
            if (tz == null) {
                tz = TimeZone.getDefault();
            }

            long time = dt.getTime();
            if (tz.inDaylightTime(dt)) {
                time -= (long) tz.getDSTSavings();
            }

            time -= (long) tz.getRawOffset();
            return new Date(time);
        }
    }

    public static Date toGmt(Date dt) {
        return dt == null ? null : toGmt(dt, (TimeZone) null);
    }

    public static int getYear(Date dt) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        return cal.get(1);
    }

    public static int getAge(Date dateOfBirth, Date onDate) {
        Calendar now = Calendar.getInstance();
        Calendar dob = Calendar.getInstance();
        now.setTime(onDate);
        dob.setTime(dateOfBirth);
        if (dob.after(now)) {
            return -1;
        } else {
            int year1 = now.get(1);
            int year2 = dob.get(1);
            int age = year1 - year2;
            int month1 = now.get(2);
            int month2 = dob.get(2);
            if (month2 > month1) {
                --age;
            } else if (month1 == month2) {
                int day1 = now.get(5);
                int day2 = dob.get(5);
                if (day2 > day1) {
                    --age;
                }
            }

            return age;
        }
    }

    public static int getMonth(Date dateOfBirth, Date onDate) {
        Calendar now = Calendar.getInstance();
        Calendar dob = Calendar.getInstance();
        now.setTime(onDate);
        dob.setTime(dateOfBirth);
        if (dob.after(now)) {
            return -1;
        } else {
            int year1 = now.get(1);
            int year2 = dob.get(1);
            int month1 = now.get(2);
            int month2 = dob.get(2);
            int day1 = now.get(5);
            int day2 = dob.get(5);
            int months = (year1 - year2) * 12 + month1 - month2;
            if (day2 > day1) {
                --months;
            }

            return months;
        }
    }

    public static synchronized int getCurrentGradeYear() {
        if (System.currentTimeMillis() > GRADE_YEAR_VALID_TO) {
            Calendar cal = Calendar.getInstance();
            GRADE_YEAR = cal.get(1);
            if (cal.get(2) <= 5) {
                --GRADE_YEAR;
            }

            GRADE_YEAR_VALID_TO = System.currentTimeMillis() + 60000L;
        }

        return GRADE_YEAR;
    }

    public static int getGradeYear(Date date) {
        if (date == null) {
            return getCurrentGradeYear();
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int gradeYear = cal.get(1);
            if (cal.get(2) <= 5) {
                --gradeYear;
            }

            return gradeYear;
        }
    }

    public static int getGrade(int year, int grade, Date onDate) {
        Calendar calOnDate = Calendar.getInstance();
        calOnDate.setTime(onDate);
        int onDateYear = calOnDate.get(1);
        if (calOnDate.get(2) < 8) {
            --onDateYear;
        }

        grade += onDateYear - year;
        if (grade < 0) {
            grade = 0;
        } else if (grade > 13) {
            grade = 13;
        }

        return grade;
    }

    public static Integer getGrade(Integer year, Integer grade, Integer onYear) {
        if (year != null && grade != null && onYear != null) {
            grade = grade + (onYear - year);
            if (grade < 0) {
                grade = 0;
            } else if (grade > 13) {
                grade = 13;
            }

            return grade;
        } else {
            return null;
        }
    }

    public static String getAgeDisplay(Date dateOfBirth) {
        if (dateOfBirth == null) {
            return "";
        } else {
            Date now = new Date();
            int year = getAge(dateOfBirth, now);
            int month = getMonth(dateOfBirth, now) % 12;
            StringBuffer sb = new StringBuffer();
            sb.append(year);
            if (year > 1) {
                sb.append(" yrs ");
            } else {
                sb.append(" yr ");
            }

            if (month == 0) {
                return sb.toString();
            } else {
                if (month > 1) {
                    sb.append(month).append(" mos");
                } else {
                    sb.append(month).append(" mo");
                }

                return sb.toString();
            }
        }
    }

    public static Date getDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 格式化
     *
     * @param date
     * @param formart
     * @return
     */
    public static String formartDate(Date date, String formart){
        SimpleDateFormat formatter = new SimpleDateFormat(formart);
        return formatter.format(date);
    }

    /**
     * 得到两个时间相差多少天，不足一天算一天
     *
     * @param beginDate
     * @param endTime
     * @return
     */
    public static int getDateMargin(Date beginDate, Date endDate) {
        if (endDate == null) {
            endDate = new Date();
        }
        final int mOfDay = 1000 * 60 * 60 * 24;
        final long divtime = (endDate.getTime() - beginDate.getTime());
        final long lday = divtime % mOfDay > 0 ? divtime / mOfDay + 1 : divtime / mOfDay;
        return Long.valueOf(lday).intValue();
    }

    /**
     * 得到当前时间和指定时间的小时差,不足一小时按小时算
     *
     * @param date
     * @return
     */
    public static long getHourMargin(Date beginDate, Date endDate) {
        if (endDate == null) {
            endDate = new Date();
        }
        long hour = endDate.getTime() - beginDate.getTime();
        hour = hour / (60 * 60 * 1000);
        hour = hour % (60 * 60 * 1000) > 0 ? hour + 1 : hour;
        return hour;
    }

    /**
     * 得到两个时间相差多少分钟,不足一分钟按一分钟算
     *
     * @param beginDate
     * @param endDate
     * @return
     */
    public static int getMinuteMargin(Date beginDate, Date endDate) {
        if (endDate == null) {
            endDate = new Date();
        }
        final int mOfMinute = 1000 * 60;
        final long divtime = (endDate.getTime() - beginDate.getTime());
        final long lminute = divtime % mOfMinute > 0 ? divtime / mOfMinute + 1 : divtime / mOfMinute;
        return Long.valueOf(lminute).intValue();
    }

    /**
     * 获取任意小时后的时间
     *
     * @param dt
     * @param month
     * @return
     */
    public static Date getAddMonthDate(Date dt, int month) {
        if (dt == null) {
            dt = new Date(System.currentTimeMillis());
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        cal.add(Calendar.MONTH, month);
        return cal.getTime();
    }

    /**
     * 获取任意小时后的时间
     *
     * @param dt
     * @param hours
     * @return
     */
    public static Date getAddHourDate(Date dt, int hours) {
        if (dt == null) {
            dt = new Date(System.currentTimeMillis());
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY) + hours);
        return cal.getTime();
    }

    /**
     * 获取任意分钟后的时间
     *
     * @param dt
     * @param minute
     * @return
     */
    public static Date getAddMinuteDate(Date dt, int minute) {
        if (dt == null) {
            dt = new Date(System.currentTimeMillis());
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dt);
        calendar.add(Calendar.MINUTE, minute);
        return calendar.getTime();
    }
}
