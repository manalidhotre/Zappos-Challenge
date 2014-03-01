package edu.northeastern.zappos.manali.workenv;

import java.util.ArrayList;

//class to compute output
public class ComputeOutput {
	/*
	 * Output: Adds the Result Products to finalResult list
	 * Logic : 
	 * 1.) Start with the empty List and total price
	 * 2.) Keep adding in the list for all combinations and reduce the added price
	 * 		from the total price
	 * 3.) Check if the array size is equal to the no of gifts required
	 * 4.) For equal array keep adding in the output list
	 */
	public static void getPossibleCombinations(double totalPrice, ArrayList<Product> giftsArray,int index, int noOfGifts){
		if(totalPrice < 0 || noOfGifts <= 0){
			return;
		}
		if( giftsArray.size() == noOfGifts)
			ZapposChallenge.finalresultAdd(giftsArray);

		for(int i=index;i < ZapposChallenge.getinputArray().size();i++){
			Product visit =  ZapposChallenge.getinputArray().get(i);
			double price = visit.getPrice();
			ArrayList<Product> newVisiters = new ArrayList<Product>(giftsArray);
			if(giftsArray.size() < noOfGifts){
				newVisiters.add(visit);
			}
			getPossibleCombinations(totalPrice-price,newVisiters, ++index, noOfGifts);
		}
	}
}
