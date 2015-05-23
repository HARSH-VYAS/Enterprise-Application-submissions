package edu.sjsu.cmpe.cache.client;

import java.util.HashMap;
import java.util.Map;

public class CRDTClient {
    public static void readOnRepair(CacheServiceInterface param1, CacheServiceInterface param2, 
    		CacheServiceInterface param3) throws Exception {

		CacheServiceInterface cache1  = param1;
    	CacheServiceInterface cache2  = param2;
    	CacheServiceInterface cache3  = param3;
    	
        long K = 1;
        String v = "a";
        
        cache1.put(K, v);
        cache2.put(K, v);
        cache3.put(K, v);
        
        System.out.println("Setting value a step");
        Thread.sleep(30000);
        
        cache1.get(1);
	    cache2.get(1);
	    cache3.get(1);
	        
	    System.out.println("Getting value a step");
	    Thread.sleep(1000);
	    
	    System.out.println("Result from Server A is: " + cache1.getValue());
	    System.out.println("Result from Server B is: " + cache2.getValue());
	    System.out.println("Result from Server C is: " + cache3.getValue());
        
        v = "b";
        cache1.put(K, v);
        cache2.put(K, v);
        cache3.put(K, v);
        
        System.out.println("Setting value b step");
        Thread.sleep(30000);
	        
	    cache1.get(1);
	    cache2.get(1);
	    cache3.get(1);
	        
	    System.out.println("Getting value b step");
	    Thread.sleep(1000);
	    
	    System.out.println("Result from Server A is: " + cache1.getValue());
	    System.out.println("Result from Server B is: " + cache2.getValue());
	    System.out.println("Result from Server C is: " + cache3.getValue());
	        
	    String[] values = {cache1.getValue(), cache2.getValue(), cache3.getValue()};
	    
	    Map<String, Integer> map = new HashMap<String, Integer>();
	    String majority = null;
	    for (String val : values) {
	        Integer count = map.get(val);	        
	        map.put(val, count != null ? count+1 : 1);
	        if (map.get(val) > values.length / 2) {
	        	majority = val;
	        	break;
	        }	
	    }
	    
	    cache1.put(K, majority);
        cache2.put(K, majority);
        cache3.put(K, majority);
        
        System.out.println("Correcting value b step");
	    Thread.sleep(1000);
	    
	    cache1.get(K);
        cache2.get(K);
        cache3.get(K);
        
        System.out.println("Getting again value b step");
	    Thread.sleep(1000);
	    
	    System.out.println("Result from Server A is: " + cache1.getValue());
	    System.out.println("Result from Server B is: " + cache2.getValue());
	    System.out.println("Result from Server C is: " + cache3.getValue());
    }
}
