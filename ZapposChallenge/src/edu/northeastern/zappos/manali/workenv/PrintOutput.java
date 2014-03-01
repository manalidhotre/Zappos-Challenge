package edu.northeastern.zappos.manali.workenv;

import java.util.ArrayList;

//class to print the output result
public class PrintOutput {
	// loop through the final result list and get the product id cost and 
	// 
	public static void getOutputProducts(){
		for(int i=0;i < ZapposChallenge.getfinalresult().size();i++){
			ArrayList<Product> resultProduct = ZapposChallenge.getfinalresult().get(i);
			System.out.println("Individual Array of Products");
			for(int j=0 ; j < resultProduct.size();j++){
				System.out.println("ProductId: "+ resultProduct.get(j).getProductId());
				System.out.println("Price: "+ resultProduct.get(j).getPrice());
				System.out.println("StyleId: "+ resultProduct.get(j).getStyleId());
				System.out.println();
			}
		}
	}
}
