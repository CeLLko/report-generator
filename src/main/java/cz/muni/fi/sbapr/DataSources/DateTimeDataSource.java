/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.sbapr.DataSources;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextShape;
import org.w3c.dom.Element;

/**
 *
 * @author Adam
 */
public class DateTimeDataSource extends DataSource<String> {

    private final ZoneId zoneId;
    private final DateTimeFormatter dateTimeFormatter;
    private final String timeString;
    private ZonedDateTime timeStamp;
    private LocalDate date;
    private LocalTime time;

    public DateTimeDataSource(Element element) {
        super(element);
        timeString = getAttribute("string");
        //2007-12-03T10:15:30.00Z
        this.zoneId = ZoneId.of(getAttribute("timeZone"));
        this.dateTimeFormatter = DateTimeFormatter.ofPattern(getAttribute("format")).withZone(this.zoneId);
    }


    @Override
    public String getData() {
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
        return dateTimeFormatter.format(timeStamp);
    }

    @Override
    public XSLFShape updateShape(XSLFSlide slide, XSLFShape shape) {
        ((XSLFTextShape) shape).clearText();
        ((XSLFTextShape) shape).appendText(getData(), false);
        return shape;
    }

}
