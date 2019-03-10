package com.content_load_sb.helper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by asd on 8.11.2017.
 */
public class CustomerDateAndTimeDeserialize extends JsonDeserializer<Date> {

    SimpleDateFormat oldFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss aaa");
    SimpleDateFormat newFormat = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    public Date deserialize(JsonParser paramJsonParser,
                            DeserializationContext paramDeserializationContext)
            throws IOException {
        Date date = null;
        String str = paramJsonParser.getText().trim();
        try {
            date = oldFormat.parse(str);
            //return date;
            oldFormat.applyPattern("dd/MM/yyyy");
            return newFormat.parse(oldFormat.format(date));
        } catch (ParseException e) {
            oldFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss aaa");
            try {
                oldFormat.parse(str);
                return oldFormat.parse(str);
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
        return paramDeserializationContext.parseDate(str);
    }
}