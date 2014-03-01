// Author : Manali Dhotre
// email address : dhotre.m@husky.neu.edu

/* ******************************************************************
 * OVERVIEW:
 * The following program creates a list of Zappos products 
 * whose combined value matches as closely as possible to X products.
 * *******************************************************************
 */
package edu.northeastern.zappos.manali.workenv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/* ***************************************************************************
 * STEPS/APPROACH:
 * 1.) Take the input gifts and total price from the user
 * 2.) Use the search api url with facet pricefacet to get the available range
 * 3.) Find the range valid for given total price and get the count of items 
 *     from the same call
 *     (As the data is vast and spread across hundreds of pages, I have used
 *     a function to get a range where the target data can possibly be. For this,
 * 4.) Loop through every 100th page and check the first result price is in 
 * 		valid range. 
 * 5.) Once we get the valid start page, loop through all the data on each page
 * 		and store them in the input list.
 * 6.) Get all the possible combinations of input list that matches the input 
 * 		gifts count value.
 * 7.) Output all the possible closest combinations.
 * 		
 * *****************************************************************************
 */

/*
 * major PERFORMANCE IMPROVEMENT strategies used:
 * 1.) Reduce the number of Products stored in the local data structure 
 * 		to minimal.
 * 2.) The api returned Products with same ProductId and Price but 
 * 		different StyleId. Used a HashMap with ProductId as key to
 * 		to store only unique product data
 * 3.) The api returned many products of exactly same price and in the valid 
 * 		range. This exact price was causing to many permutation combinations 
 * 		to get the closest combination of required Product. I reduced the
 * 		the count of exact prices to not exceed the number of gifts as it 
 * 		will give the required best combination without too many permutation.
 * 		(This saved considerate run time without hampering the result quality)
 * 4.) Divided the total price to the number of gifts to give the possible
 * 		price of individual products. 
 * 		Initially the program tries to find the products with the exact 
 * 		individual price. If if does not find suitable matches,
 * 		it increases the margin approximation of $0.5 to the price each 
 * 		loop to get the closest possible solution 
 */

// class to to get list of Zappos products
public class ZapposChallenge  {

	// to hold the input in 
	private static HashMap<Integer,Product> input = new HashMap<Integer,Product>();
	private static ArrayList<Product> inputArray = new ArrayList<Product>();
	private static HashMap<Double, Integer> uniquePriceCounter = new HashMap<Double, Integer>();
	private static int count_items_range;
	private static List<ArrayList<Product>> final_result = new ArrayList<ArrayList<Product>>();

	public static void setCountItemsRange(int count){
		count_items_range = count;
	}

	public static int getCountItemsRange(){
		return count_items_range;
	}

	public static void finalresultAdd(ArrayList<Product> listProducts){
		final_result.add(listProducts);
	}

	public static List<ArrayList<Product>> getfinalresult(){
		return final_result;
	}

	public static void inputAdd(int productId, Product p){
		input.put(productId, p);
	}

	public static HashMap<Integer,Product> getInput(){
		return input;
	}

	public static void uniquePriceCounterAdd(double price, int count){
		uniquePriceCounter.put(price, count);
	}

	public static HashMap<Double, Integer> getuniquePriceCounter(){
		return uniquePriceCounter;
	}

	public static void inputArrayAdd(Product p){
		inputArray.add(p);
	}

	public static ArrayList<Product> getinputArray(){
		return inputArray;
	}

	// start main function
	public static void main(String[] args){

		// create a object of type ZapposChallenge to access functions
		GetZapposData zapposData = new GetZapposData();
		GetRangeData zapposRangeData = new GetRangeData();
		GetProductData zapposProductDataObject = new GetProductData();
		// take number of gifts and price as input from the user
		Scanner user_input = new Scanner(System.in);
		System.out.println("Enter the number of items:");
		int no_of_gifts = Integer.parseInt(user_input.next());
		System.out.println("Enter the total price");
		double total_price = Double.parseDouble(user_input.next());
		user_input.close();

		double margin = 0;
		boolean FoundResult = false;
		String price_range_url ="http://api.zappos.com/Search?term=&includes=[\"facets\"]&facets=[\"priceFacet\"]&key=12c3302e49b9b40ab8a222d7cf79a69ad11ffd78&excludes=[\"results\"]";
		do{		
			double equal_cost = total_price/no_of_gifts;
			double start_price = (equal_cost-margin) > 0 ? equal_cost-margin : 0.0;
			double end_price = (equal_cost + margin) <= total_price ? equal_cost + margin : total_price;


			String response_body =  zapposData.getZapposResponse(price_range_url);
			if(!response_body.equals("Bad response")){
				String valid_range = zapposRangeData.getCountOfItemsInPriceRange(response_body, total_price);

				//max 100 items to displayed on a single page
				int limit_items_on_page = 100;
				int total_pages = (int)Math.ceil(count_items_range/limit_items_on_page);		
				int page =0;
				for(page= 0 ; page <= total_pages; page = page + 100){
					String get_products_page = 
							"http://api.zappos.com/Search?term=&filters={\"priceFacet\":[\""+ 
									valid_range + "\"]}&key=12c330	2e49b9b40ab8a222d7cf79a69ad11ffd78&limit=100&page=" 
									+ page +"&sort={%22price%22:%22asc%22}";
					String response =  zapposData.getZapposResponse(get_products_page);
					double first_price= zapposRangeData.getPageRange(response);
					if(first_price > equal_cost)
						break;
				} 
				page = (page == 0) ? 0 : page - 100;

				for(int i=page; i<= total_pages ; i++){
					String get_products_page = "http://api.zappos.com/Search?term=&filters={\"priceFacet\":[\""+ valid_range + "\"]}&key=12c3302e49b9b40ab8a222d7cf79a69ad11ffd78&limit=100&page=" + i +"&sort={%22price%22:%22asc%22}";
					String response =  zapposData.getZapposResponse(get_products_page);
					if(zapposProductDataObject.checkNextResponseForInRange(response, start_price, end_price,no_of_gifts))
						break;
				}
				zapposProductDataObject.convertMapToList();
				ComputeOutput.getPossibleCombinations(total_price, new ArrayList<Product>(),0,no_of_gifts);	
				if(final_result.size() > 0)
					FoundResult = true;
				PrintOutput.getOutputProducts();
			}
			else{
				System.out.println(response_body);
				FoundResult = true;
			}
			margin += 0.5;
		} while (FoundResult == false);
	}// end main function
}// end class
