package exercise;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Please do not change the Consumer.
 */
public class Consumer {

    public void send(List<PriceUpdate> priceUpdates) {
    	
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    	
    	System.out.println("=== Consumer received message ("+ sdf.format(new Date()) + ") ===" );
        priceUpdates.forEach(System.out::println);
        System.out.println("=== End of message === ");
        System.out.println();
    }

}
