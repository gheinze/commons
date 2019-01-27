package com.accounted4.commons.finance;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;
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
public class QuoteMediaQuoteDao {

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


}
