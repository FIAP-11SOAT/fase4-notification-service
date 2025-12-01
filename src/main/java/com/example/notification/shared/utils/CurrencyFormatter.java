package com.example.notification.shared.utils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyFormatter {

    private CurrencyFormatter() {
        // Construtor privado para evitar instanciação
    }

    public static String convertToCurrencyString(BigDecimal valor) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.of("pt", "BR"));
        return formatter.format(valor);
    }
}