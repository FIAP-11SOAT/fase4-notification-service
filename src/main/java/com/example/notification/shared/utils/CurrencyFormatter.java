package com.example.notification.shared.utils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyFormatter {

    public static String convertToCurrencyString(BigDecimal valor){
        NumberFormat formats = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        return formats.format(valor);
    }
}
