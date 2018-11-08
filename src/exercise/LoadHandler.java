package exercise;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LoadHandler {

	private static final int MAX_PRICE_UPDATES = 2;
	private final Consumer consumer;
	

	// Last time the message was sent to a consumer
	private long lastMsgToConsumerTimeMillis = 0;

	// At the end of each second - this map ( which contains PriceUpdates in
	// essense) will be sent to the consumer. The String (key) in this map 
	// is the company name and the Double (value) is the price
	private Map<String, Double> mostRecentPrices = new HashMap();

	// This will store data about all companies for which updates
	// passed through the application. We can do this, since even NASDAQ
	// list about 3000 companies, so we won't run out of memory
	private Map<String, Long> lastCompanyUpdateTime = new HashMap();

	public LoadHandler(Consumer consumer) {
		this.consumer = consumer;
	}

	// The trigger which will make the LoadHandler send the message to a consumer
	// will be a receipt of a message from the producer. If enough time has passed
	// since the last message to consumer, then another message will be sent.
	// This can be a problem if the producer can, for example, stop for an hour
	// for some reason before some data which was collected by the load handler
	// was sent. However, this does not seem to be the case with this problem, and
	// an assumption is made that at least 1 message per second will be received from
	// the producer
	public void receive(PriceUpdate priceUpdate) {

		long curTimeMillis = System.currentTimeMillis();

		// We use a map, which stores only the most recent price update for a given stock
		// Therefore if during each one second iteration the load balancer receives updates 
		// about less than MAX_PRICE_UPDATE number of companies everything will work as expected.
		String companyName = priceUpdate.getCompanyName();
		double price = priceUpdate.getPrice();

		// Checking if we've ever sent an update about this company to a consumer
		if (!lastCompanyUpdateTime.containsKey(companyName)) {
			lastCompanyUpdateTime.put(companyName, Long.valueOf(0));
		}

	    mostRecentPrices.put(companyName, price);

        // Time to send data accumulated by the load balancer to the consumer ? 
		if (curTimeMillis - lastMsgToConsumerTimeMillis > 1000) {

			int numToRemove = mostRecentPrices.size() - MAX_PRICE_UPDATES;
			if (numToRemove > 0) {
				
				while( numToRemove > 0 ) {
				  String mostRecentlyUpdatedCompany = getCompanyWithMostRecentUpdateTime();
				  mostRecentPrices.remove(mostRecentlyUpdatedCompany);
				  //System.out.println("Removed" + mostRecentlyUpdatedCompany );
				  numToRemove--;
				}
			}

			List<PriceUpdate> lpu = getPriceUpdateListForConsumer();
			consumer.send(lpu);
			recordCompanyUpdateTimeForConsumer(lpu, curTimeMillis);
			lastMsgToConsumerTimeMillis = System.currentTimeMillis();
			mostRecentPrices.clear();
			
		}

	}

	private List<PriceUpdate> getPriceUpdateListForConsumer() {
		List<PriceUpdate> li = new ArrayList<>();

		Set<String> keys = mostRecentPrices.keySet();
		for (String key : keys) {
			PriceUpdate pu = new PriceUpdate(key, mostRecentPrices.get(key));
			li.add(pu);
		}

		return li;
	}

	private void recordCompanyUpdateTimeForConsumer(List<PriceUpdate> li, long timeMillis) {

		for (PriceUpdate pu : li) {
			String companyName = pu.getCompanyName();
			Long updTime = Long.valueOf(timeMillis);
			this.lastCompanyUpdateTime.put(companyName, updTime);
		}

	}

	private String getCompanyWithMostRecentUpdateTime() {

		// TODO Refactor this ot make it more secure
		long mostRecentUpdateTime = 0;
		String compWithMostRecentUpdateTime = "";
		for (String compName : mostRecentPrices.keySet()) {

			long compUpdateTime = lastCompanyUpdateTime.get(compName);
			if (compUpdateTime >= mostRecentUpdateTime) {
				mostRecentUpdateTime = compUpdateTime;
				compWithMostRecentUpdateTime = compName;
			}

		}

		// System.out.println("Company with most recent update time:" +
		// compWithMostRecentUpdateTime +" "+mostRecentUpdateTime );

		return compWithMostRecentUpdateTime;
	}

}
