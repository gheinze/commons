package com.accounted4.commons.finance;

import java.math.BigDecimal;
import java.time.LocalDate;


/**
 *
 * @author glenn
 */
public interface Quote {

    BigDecimal getClosingPrice();
    
    LocalDate getLocalDate();
    
    default boolean isGoodQuote() {
        return getClosingPrice() != null && getLocalDate() != null;
    }
    
    
}
