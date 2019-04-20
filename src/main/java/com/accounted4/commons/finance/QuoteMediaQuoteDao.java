package com.accounted4.commons.finance;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;

/**
 * Data object for storing the CSV response when querying the QuoteMedia
 * stock quote service for a "History" download.
 * i.e. https://app.quotemedia.com/quotetools/getHistoryDownload.csv?&webmasterId=501&startDay=%s&startMonth=%s&startYear=%s&endDay=%s&endMonth=%s&endYear=%s&isRanged=true&symbol=%s
 *
 * @author glenn
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuoteMediaQuoteDao implements Quote {

    @JsonAlias("symbol")
    private String symbol;
    @JsonAlias("date")
    private String date;
    @JsonAlias("open")
    private BigDecimal open;
    @JsonAlias("high")
    private BigDecimal high;
    @JsonAlias("low")
    private BigDecimal low;
    @JsonAlias("close")
    private BigDecimal close;
    @JsonAlias("volume")
    private int volume;


    // Values may have a "N/A", in which case substitue null
    @JsonSetter("open")
    public void setOpen(String in) {
        try {
            open = new BigDecimal(in);
        } catch(NumberFormatException nfe) {
            open = null;
        }
    }
    
    @JsonSetter("high")
    public void setHigh(String in) {
        try {
            high = new BigDecimal(in);
        } catch(NumberFormatException nfe) {
            high = null;
        }
    }
    
    @JsonSetter("low")
    public void setLow(String in) {
        try {
            low = new BigDecimal(in);
        } catch(NumberFormatException nfe) {
            low = null;
        }
    }
 
    @Override
    public LocalDate getLocalDate() {
        return LocalDate.parse(date);
    }

    @Override
    public BigDecimal getClosingPrice() {
        return close;
    }

    
}
