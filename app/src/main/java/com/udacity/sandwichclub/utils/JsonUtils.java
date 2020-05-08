package com.udacity.sandwichclub.utils;

import android.util.Log;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class JsonUtils {
	private static final String TAG_NAME = "mainName";
	private static final String TAG_KNOWN_AS = "alsoKnownAs";
	private static final String TAG_INGREDIENTS = "ingredients";
	private static final String TAG_IMAGE = "image";
	private static final String TAG_DESCRIPTION = "description";
	private static final String TAG_ORIGIN = "placeOfOrigin";


	public static Sandwich parseSandwichJson(String json) {
        Sandwich sandwich = new Sandwich();
		try {
			JSONObject sandwichData = new JSONObject(json);
			JSONObject names = sandwichData.getJSONObject("name");

			sandwich.setMainName(names.getString(TAG_NAME));
			sandwich.setAlsoKnownAs(convertJsonArrayToList(names.getJSONArray(TAG_KNOWN_AS)));
			sandwich.setIngredients(convertJsonArrayToList(sandwichData.getJSONArray(TAG_INGREDIENTS)));
			sandwich.setImage(sandwichData.getString(TAG_IMAGE));
			sandwich.setDescription(sandwichData.getString(TAG_DESCRIPTION));
			sandwich.setPlaceOfOrigin(sandwichData.getString(TAG_ORIGIN));
		} catch (JSONException e) {
			Log.e("JSON Parser", "Error parsing data " + e.toString());
		}
        return sandwich;
    }

    private static List<String> convertJsonArrayToList(JSONArray jsonArray) {
		List<String> list = new ArrayList<String>();
		if (jsonArray != null) {
			try {
				for (int i = 0; i < jsonArray.length(); i++) {
					list.add(jsonArray.getString(i));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
}
