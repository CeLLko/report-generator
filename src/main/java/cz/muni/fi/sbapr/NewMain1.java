/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.sbapr;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Adam
 */
public class NewMain1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ZoneId zoneId = ZoneId.of("Europe/Prague");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy MM dd hh mm ss").withZone(zoneId);

        String timeString = "$today+8d@10h31m50s";
        ZonedDateTime timeStamp = null;
        LocalDate date = null;
        LocalTime time = null;
        boolean useLocal = true;
        if (timeString.contains("$today")) {
            Pattern pattern = Pattern.compile("(\\$today)([\\+\\-]\\d+)?(?:d)?@(\\d+)h(\\d+)m(\\d+)s");
            Matcher matcher = pattern.matcher(timeString);

            if (matcher.matches()) {
                date = LocalDate.now(zoneId).plusDays(Long.parseLong(matcher.group(2)));
                time = LocalTime.of(
                        Integer.parseInt(matcher.group(3)),
                        Integer.parseInt(matcher.group(4)),
                        Integer.parseInt(matcher.group(5)));
            } else {
                throw new IllegalArgumentException();
            }
        } else if (timeString.contains("now")) {
            {
                date = LocalDate.now(zoneId);
                time = LocalTime.now(zoneId);
            }
        } else {
            date = LocalDate.now(zoneId);
            time = LocalTime.now(zoneId);
        }
        timeStamp = Instant.parse(date.toString() + "T" + time.toString()+"Z").atZone(zoneId);
        System.err.println(dateTimeFormatter.format(timeStamp));
    }

}
