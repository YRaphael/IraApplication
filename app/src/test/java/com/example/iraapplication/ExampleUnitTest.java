package com.example.iraapplication;

import com.example.iraapplication.domain.ValutaItem;
import com.example.iraapplication.pojo.Record;
import com.example.iraapplication.utils.ConverterUtils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void convert_isCorrect() {
        assertEquals(ConverterUtils.pureConvert(Record.RUB_RECORD, Record.RUB_RECORD), 1.0, 0.1);
    }

    @Test
    public void lengthOfCurrencies_equalsFour() {

        ValutaItem item = ValutaItem.RUB;
        Object[] possibleValues = item.getDeclaringClass().getEnumConstants();

        assertEquals(possibleValues.length, 4);
    }

    @Test
    public void RUB_isPresented() {
        ValutaItem item = ValutaItem.RUB;
        Object[] possibleValues = item.getDeclaringClass().getEnumConstants();

        boolean result = false;
        for (Object obj :
                possibleValues) {
            result |= "RUB".equals(((ValutaItem) obj).name());
        }
        assertTrue(result);
    }

    @Test
    public void EUR_isPresented() {
        ValutaItem item = ValutaItem.RUB;
        Object[] possibleValues = item.getDeclaringClass().getEnumConstants();

        boolean result = false;
        for (Object obj :
                possibleValues) {
            result |= "EUR".equals(((ValutaItem) obj).name());
        }
        assertTrue(result);
    }

    @Test
    public void US_isPresented() {
        ValutaItem item = ValutaItem.RUB;
        Object[] possibleValues = item.getDeclaringClass().getEnumConstants();

        boolean result = false;
        for (Object obj :
                possibleValues) {
            result |= "US".equals(((ValutaItem) obj).name());
        }
        assertTrue(result);
    }

    @Test
    public void IEN_isPresented() {
        ValutaItem item = ValutaItem.RUB;
        Object[] possibleValues = item.getDeclaringClass().getEnumConstants();

        boolean result = false;
        for (Object obj :
                possibleValues) {
            result |= "IEN".equals(((ValutaItem) obj).name());
        }
        assertTrue(result);
    }

}