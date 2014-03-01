package edu.northeastern.zappos.manali.workenv;

public class Product {	 
		private double price;
		private int product_id;
		private int style_id;

		public Product(){
			this.price = 0;
			this.product_id = 0;		
			this.style_id = 0;
		}

		public void setPrice(double price){
			this.price = price;
		}
		public double getPrice(){
			return price;
		}

		public void setProductId(int product_id){
			this.product_id = product_id;
		}

		public int getProductId(){
			return product_id;
		}

		public void setStyleId(int style_id){
			this.style_id = style_id;
		}

		public int getStyleId(){
			return style_id;
		}
}
