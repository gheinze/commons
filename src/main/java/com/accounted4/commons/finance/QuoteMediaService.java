package com.accounted4.commons.finance;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import java.io.IOException;
import java.time.LocalDate;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * Utilities for accessing the QuoteMedia stock quote service.
 *
 * https://app.quotemedia.com/quotetools/getHistoryDownload.csv?&webmasterId=501&startDay=2&startMonth=1&startYear=2019&endDay=2&endMonth=1&endYear=2019&isRanged=true&symbol=bmo.pr.d:ca
 * 
 * @author glenn
 */
public class QuoteMediaService {

    public static final LocalDate NOW = LocalDate.now();
    public static final int START_DAY   = NOW.getDayOfMonth();
    public static final int START_MONTH = NOW.getMonthValue();
    public static final int START_YEAR  = NOW.getYear();

    private static final String QUOTEMEDIAL_HISTORY_QUERY_TEMPLATE =
            "https://app.quotemedia.com/quotetools/getHistoryDownload.csv?&webmasterId=501&startDay=%s&startMonth=%s&startYear=%s&endDay=%s&endMonth=%s&endYear=%s&isRanged=true&symbol=%s";

    
    public QuoteMediaQuoteDao getQuote(final String tmxSymbol) throws IOException {

        String quoteMediaSymbol = convertTmxSymbolToQuoteMedia(tmxSymbol);

        String url = String.format(QUOTEMEDIAL_HISTORY_QUERY_TEMPLATE,
                START_DAY,
                START_MONTH,
                START_YEAR,
                START_DAY,   // endDay
                START_MONTH, // endMonth
                START_YEAR,  // endYear
                quoteMediaSymbol
        );

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        String content;
        
        try (CloseableHttpResponse response = httpclient.execute(httpGet)) {
            HttpEntity entity = response.getEntity();
            content = EntityUtils.toString(entity);
            EntityUtils.consume(entity);
        }

        QuoteMediaQuoteDao quote = createQuote(tmxSymbol, content);
        
        return quote;
    }

    
    
    
    /*
     * Ex.:   BMO.PR.D => BMO.PR.D:CA
     */
    private String convertTmxSymbolToQuoteMedia(final String symbol) {
        String newSymbol = symbol + ":CA";
        return newSymbol;
    }
    
    

    /*
     * csv input format:
     *   date,open,high,low,close,volume,changed,changep,adjclose,tradeval,tradevol
     *   2019-01-26,23.09,23.11,22.93,23.10,4235,0.09,0.39%,23.10,97561.29,15    
    */    
    private QuoteMediaQuoteDao createQuote(final String symbol, final String csv) throws IOException {

        CsvSchema bootstrapSchema = CsvSchema.emptySchema().withHeader();
        ObjectMapper mapper = new CsvMapper();
        QuoteMediaQuoteDao quote = mapper.readerFor(QuoteMediaQuoteDao.class).with(bootstrapSchema).readValue(csv);
        quote.setSymbol(symbol);

        return quote;

    }    
    
}
