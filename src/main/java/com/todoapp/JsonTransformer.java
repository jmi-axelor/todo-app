package com.todoapp;
 
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import spark.Response;
import spark.ResponseTransformer;
 
public class JsonTransformer implements ResponseTransformer {
 
	public String render(Object model) {
		if (model instanceof Response) {
			Gson gson = new GsonBuilder().registerTypeAdapter(Response.class, new MyTypeAdapter<Response>()).create();
			return gson.toJson(model);
		} 
		else {
			Gson gsonNormal = new Gson();
			return gsonNormal.toJson(model);
		}
	}

	class MyTypeAdapter<T> extends TypeAdapter<T> {
		public T read(JsonReader reader) throws IOException {
			return null;
		}

		public void write(JsonWriter writer, T obj) throws IOException {
			if (obj == null) {
				writer.nullValue();
				return;
			}
			writer.value(obj.toString());
		}
	}
 
}