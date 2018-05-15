package com.egs.account.utils.convertor;

import java.sql.Date;
import java.time.LocalDate;

public class DateConvertor {

    public static java.util.Date convertLocalDateToDate(LocalDate dateToConvert) {
        return Date.valueOf(dateToConvert);
    }
}
