package com.accounted4.commons.finance;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;



/**
 * Utilities for accessing the AlphaVantage stock quote service.
 * 
 * @author glenn
 */
public class AlphaVantageService {

    // Requires certificate to be registered with jvm.  Example registration:
    // sudo ~/sw/java/jdk1.8.0_74/bin/keytool -importcert -file ./wwwalphavantageco.crt -alias alphavantage -keystore ~/sw/java/jdk1.8.0_74/jre/lib/security/cacerts -storepass changeit

    private static final String ALPHAVANTAGE_API_KEY = System.getenv("ALPHAVANTAGE_API_KEY");

    private static final String ALPHAVANTAGE__GLOBAL_QUOTE_QUERY_TEMPLATE =
            "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=%s&apikey=%s";


    /**
     * 
     * @param tmxSymbol Ticker symbol as used on the TMX. Will need to be converted.
     * @return
     * @throws IOException 
     */
    public AlphaVantageQuoteDao getQuote(final String tmxSymbol) throws IOException {

        String alphaVantageSymbol = convertTmxSymbolToAlphaVantageSymbol(tmxSymbol);

        String url = String.format(
                ALPHAVANTAGE__GLOBAL_QUOTE_QUERY_TEMPLATE,
                alphaVantageSymbol,
                ALPHAVANTAGE_API_KEY
        );
        
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        String content;
        
        try (CloseableHttpResponse response = httpclient.execute(httpGet)) {
            HttpEntity entity = response.getEntity();
            content = EntityUtils.toString(entity);
            EntityUtils.consume(entity);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        AlphaVantageQuoteDao quote = objectMapper.readValue(content, AlphaVantageQuoteDao.class);

        return quote;
        
    }


    /*
     * First "." is replaced by a "-"
     * Subsequent "." are removed.
     *
     * Ex.:   ARE.DB.B  => ARE-DBB.TO
     *
     */
    private String convertTmxSymbolToAlphaVantageSymbol(final String symbol) {
        String newSymbol = symbol.replaceFirst("\\.", "-").replace(".", "") + ".TO";
        return newSymbol;
    }

}
