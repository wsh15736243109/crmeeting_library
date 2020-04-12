package com.cr.pn.Utils.Json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTypeAdapter implements JsonSerializer<Date>, JsonDeserializer<Date>{

	private final DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		if (!(json instanceof JsonPrimitive)) {
			throw new JsonParseException("The date should be ApiObserverProgress string value");
		}

		try {
			Date date = format.parse(json.getAsString());
			return date;
		} catch (ParseException e) {
			throw new JsonParseException(e);
		}
	}
	@Override
	public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
		String dfString = format.format(src);
		return new JsonPrimitive(dfString);
	}
}   
