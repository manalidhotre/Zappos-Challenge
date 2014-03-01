package edu.northeastern.zappos.manali.workenv;

import java.util.Map.Entry;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class GetProductData {
	
	public void convertMapToList(){	
		for(Entry<Integer, Product> entry: ZapposChallenge.getInput().entrySet()){
			Product visit = entry.getValue();
			ZapposChallenge.inputArrayAdd(visit); 
		}
	}

	public boolean checkNextResponseForInRange(String response, double start_price, double end_price, int no_of_gifts){
		boolean is_next_response_invalid = false;
		try{
			if(response != "Bad response"){
				JSONParser parser = new JSONParser();
				Object obj = parser.parse(response);
				JSONObject response_object = (JSONObject) obj;
				JSONArray result_array = (JSONArray) response_object.get("results");
				double price = 0.0;
				int product_id = 0,style_id=0;
				int no_of_objects = result_array.size();

				JSONObject result_json;
				Object r1 = result_array.get(no_of_objects-1);
				JSONObject result_json_r1 = (JSONObject) r1;

				Double last_price = Double.parseDouble(result_json_r1.get("price").toString().substring(1));
				if (last_price >= start_price){
					for(Object result: result_array){
						result_json = (JSONObject) result;
						price = Double.parseDouble(result_json.get("price").toString().substring(1));
						product_id = Integer.parseInt(result_json.get("productId").toString());
						style_id = Integer.parseInt(result_json.get("styleId").toString());
						if(price >= start_price){
							Product newProduct = new Product();
							newProduct.setPrice(price);
							newProduct.setProductId(product_id);
							newProduct.setStyleId(style_id);
							int count = ZapposChallenge.getuniquePriceCounter().containsKey(price) ? 
									ZapposChallenge.getuniquePriceCounter().get(price) : 0;
							ZapposChallenge.getuniquePriceCounter().put(price, count + 1);
							if(ZapposChallenge.getuniquePriceCounter().get(price) <= no_of_gifts)
							ZapposChallenge.inputAdd(product_id, newProduct);						
						}
					}
				}
				if(price > end_price)
					is_next_response_invalid = true;
			}
			else 
				return false;
		}
		catch(Exception ex){
		}
		return is_next_response_invalid;
	}


}
