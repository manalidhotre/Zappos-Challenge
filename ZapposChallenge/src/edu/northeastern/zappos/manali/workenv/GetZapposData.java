package edu.northeastern.zappos.manali.workenv;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetZapposData {
	
	public String getZapposResponse(String url){
		StringBuffer response = new StringBuffer();
		try{
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			int responseCode = con.getResponseCode();


			switch(responseCode){
			case 200:
				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String inputLine;
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				break;

			default:
				response.append("Bad response");
				break;
			}
		}
		catch(Exception ex){
			ex.printStackTrace();
			//System.out.println("get response error" + ex.getMessage());
			response.append("Bad response");
		}
		return response.toString();
	}

}
