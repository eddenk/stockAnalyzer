import java.io.*;
import java.math.*;
import java.util.*;

import yahoofinance.*;

public class myStock {
	//HashMap provides fast retrieval -- used for data retrieval
	//TreeSet allows key-value pairs to be sorted by value -- used for insertation and update & top 10 stock (sorted data structure)
	// This is the nested class provided for you to store the information associated with a stock symbol
	
	private static class stockInfo 
	{
		private String name;
		private BigDecimal price;
		private String symbolStock;
		public stockInfo(String nameIn, BigDecimal priceIn, String symbolStockin) 
		{
			name = nameIn;
			price = priceIn;
			symbolStock = symbolStockin;
		}
		public String toString() 
		{
			StringBuilder stockInfoString = new StringBuilder("");
			stockInfoString.append(name + " " + price.toString());
			return stockInfoString.toString();
		}
	}
	
	//comparing stock price 1 and 2
	//Comparator should be overwritten sort record by price
	class MyComparator implements Comparator<Map.Entry<String, stockInfo>> 
	{
		  public int compare(Map.Entry<String, stockInfo> s1, Map.Entry<String, stockInfo> s2) 
		  {
		    BigDecimal stock1, stock2;

		    stock1 = s1.getValue().price;
		    stock2 = s2.getValue().price;

		    return stock2.compareTo(stock1);
		  }

	}
	HashMap<String, stockInfo> stockData;
	TreeSet<Map.Entry<String, stockInfo>> ts;
	public myStock () 
	{
		//define the constructor to create the database
		stockData = new HashMap<String, stockInfo>();
		ts = new TreeSet<Map.Entry<String, stockInfo> >(new MyComparator());
		//make tree set 
		//compare stock a and stock b 
	}
    
	public void insertOrUpdate(String symbol, stockInfo stock) 
	{
		stockData.put(symbol, stock);
		AbstractMap.SimpleEntry<String, stockInfo> e = new AbstractMap.SimpleEntry<String, stockInfo>(symbol, stock);
		ts.add(e);
		//implement this method used to initialize and update the database -- complexity is O(log(n))   		
	}
	
	public stockInfo get(String symbol) {
		return stockData.get(symbol);
		//implement this method to quickly retrieve record from database
		//time complexity should be O(1) constant time   
		
	}
	
	public List<Map.Entry<String, stockInfo>> top(int k) {
		//return the stock records with top k prices --  O(k) 
		//iterate through tree map
		//while less top k add to the list the key value pair
		List <Map.Entry<String, stockInfo>> topStock = new ArrayList<>();
		//Creating an iterator 
        Iterator<Map.Entry<String, stockInfo>> value = ts.iterator();
        int itr =0;
        while (value.hasNext() && itr<k)
        {
        	topStock.add(value.next());
        	itr++;
        }
        return topStock;
	}
	

    public static void main(String[] args) throws IOException {   	
    	
    	//test the database creation based on the input file
    	myStock techStock = new myStock();
    	BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader("./US-Tech-Symbols.txt"));
			String line = reader.readLine();
			while (line != null) {
				String[] var = line.split(":");
				
				//YahooFinance API is used and make sure the library file is included in the project build path
				Stock stock = YahooFinance.get(var[0]);
				
				// test the insertOrUpdate operation
				techStock.insertOrUpdate(var[0], new stockInfo(var[1], stock.getQuote().getPrice(), var[0])); 
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int i = 1;
		System.out.println("===========Top 10 stocks===========");
		
		// test the top operation
		for (Map.Entry<String, stockInfo> element : techStock.top(10)) {
		    System.out.println("[" + i + "]" +element.getKey() + " " + element.getValue());
		    i++;
		}
		
		// test the get operation
		System.out.println("===========Stock info retrieval===========");
    	System.out.println("VMW" + " " + techStock.get("VMW"));
    	System.out.println("CHL" + " " + techStock.get("CHL"));
    }
}