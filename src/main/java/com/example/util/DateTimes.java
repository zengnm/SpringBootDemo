package com.example.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;
import java.util.function.Function;

/**
 * 日期工具类
 *
 * @author zengnianmei
 */
public final class DateTimes {
    private final static DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private LocalDateTime localDateTime;

    private DateTimes(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public static DateTimes now() {
        return new DateTimes(LocalDateTime.now());
    }

    public static DateTimes of(long epochMilli) {
        return new DateTimes(LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMilli), ZoneOffset.systemDefault()));
    }

    public static DateTimes of(Date date) {
        return new DateTimes(date.toInstant().atZone(ZoneOffset.systemDefault()).toLocalDateTime());
    }

    public static DateTimes of(LocalDateTime localDateTime) {
        return new DateTimes(localDateTime);
    }

    public static DateTimes of(String text) {
        return new DateTimes(LocalDateTime.parse(text, DEFAULT_FORMATTER));
    }

    public static DateTimes of(String text, DateTimeFormatter formatter) {
        return new DateTimes(LocalDateTime.parse(text, formatter));
    }

    public DateTimes then(Function<LocalDateTime, LocalDateTime> then) {
        this.localDateTime = then.apply(this.localDateTime);
        return this;
    }

    public long toMills() {
        return localDateTime.atZone(ZoneOffset.systemDefault()).toInstant().toEpochMilli();
    }

    public String format() {
        return localDateTime.format(DEFAULT_FORMATTER);
    }

    public String format(DateTimeFormatter formatter) {
        return localDateTime.format(formatter);
    }

    public LocalDateTime toLocalDateTime() {
        return localDateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DateTimes dateTimes = (DateTimes) o;
        return Objects.equals(localDateTime, dateTimes.localDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(localDateTime);
    }

    @Override
    public String toString() {
        return format();
    }

    public static void main(String[] args) {
        Date date = new Date();
        System.out.println(DateTimes.of(date).equals(DateTimes.of(date.getTime())));
        System.out.println(DateTimes.now().then(e -> e.plusDays(1)).format());
    }
}
