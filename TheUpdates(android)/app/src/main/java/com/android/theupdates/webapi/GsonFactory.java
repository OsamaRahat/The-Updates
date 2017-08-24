package com.android.theupdates.webapi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GsonFactory {

	public static final String DATE_FROMAT = "dd-MM-yyyy";
	public static final String TIME_FROMAT = "HH:mm:ss";
	// 2013-06-11 00:00:00
	//2013-07-04 10:54:30
	public static final String DATE_TIME_FROMAT = "yyyy-MM-dd HH:mm:ss";

	private static Gson configuredGson;

	public static Gson getConfiguredGson() {

		if (configuredGson == null) {
			GsonBuilder builder = new GsonBuilder();
			// Date
			// java.util.Date
			builder.registerTypeAdapter(Date.class, new DateSerializer(
					DATE_TIME_FROMAT));
			configuredGson = builder.create();
		}

		return configuredGson;
	}

	public static class DateSerializer implements JsonSerializer<Date>,
			JsonDeserializer<Date> {

		SimpleDateFormat sf;

		public DateSerializer(String format) {
			sf = new SimpleDateFormat(format);
		}

		@Override
		public Date deserialize(JsonElement json, Type arg1,
								JsonDeserializationContext arg2) throws JsonParseException {
			Date time = null;
			try {
				time = sf.parse(json.getAsJsonPrimitive().getAsString());
			} catch (ParseException e) {
				// TODO : Add proper logs.
				return null;
			}
			return time;
		}

		@Override
		public JsonElement serialize(Date src, Type arg1,
									 JsonSerializationContext arg2) {
			return new JsonPrimitive(sf.format(src));
		}

	}

}