package edu.northeastern.zappos.manali.workenv;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class GetRangeData {
	
	public String getCountOfItemsInPriceRange(String response_body, Double total_price){
		String valid_range = null;
		try{
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(response_body);
			JSONObject response_object = (JSONObject) obj;
			JSONArray facets_array = (JSONArray) response_object.get("facets");

			JSONObject values_obj, value_obj;
			JSONArray values_array = null;
			for (Object facets : facets_array){
				values_obj = (JSONObject) facets;
				values_array = (JSONArray) values_obj.get("values");			
			}
			if(total_price >= 200.0){
				valid_range = "$200.00 and Over";
			}
			if(total_price > 100.0 && total_price <= 200.0){
				valid_range = "$200.00 and Under";
			}
			if(total_price > 50.0 && total_price <= 100.0){
				valid_range = "$100.00 and Under";
			}
			if(total_price <= 50.0){
				valid_range = "$50.00 and Under";
			}
			for(Object v : values_array){
				value_obj = (JSONObject) v;
				if (value_obj.get("name").toString().equals(valid_range)){
					ZapposChallenge.setCountItemsRange(Integer.parseInt(value_obj.get("count").toString()));
				}
			}
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		return valid_range;
	}
	
	public double getPageRange(String response){
		double price = 0.0;
		try{
			if(response != "Bad response"){
				JSONParser parser = new JSONParser();
				Object obj = parser.parse(response);
				JSONObject response_object = (JSONObject) obj;
				JSONArray result_array = (JSONArray) response_object.get("results");
				Object first_result = result_array.get(0);
				JSONObject result_json = (JSONObject) first_result;
				price = Double.parseDouble(result_json.get("price").toString().substring(1));
			}
		}
		catch(Exception e){
		}
		return price;
	}

}
