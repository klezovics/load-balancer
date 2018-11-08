package exercise;

import java.util.concurrent.TimeUnit;

public class Producer extends Thread {

	private LoadHandler loadHandler;

	public Producer(LoadHandler loadHandler) {
		this.loadHandler = loadHandler;
	}

	@Override
	public void run() {
		generateUpdates();
	}

	public void generateUpdates() {
		while (true) {
			for (int i = 1; i < 100; i++) {
				loadHandler.receive(new PriceUpdate("STOCK7", 11.63));
				loadHandler.receive(new PriceUpdate("STOCK1", 92.85));
				loadHandler.receive(new PriceUpdate("STOCK6", 13.13));
				loadHandler.receive(new PriceUpdate("STOCK2", 140.71));
				loadHandler.receive(new PriceUpdate("STOCK3", 91.56));
				loadHandler.receive(new PriceUpdate("STOCK2", 166.73));
				loadHandler.receive(new PriceUpdate("STOCK3", 71.71));
				loadHandler.receive(new PriceUpdate("STOCK2", 860.76));
				loadHandler.receive(new PriceUpdate("STOCK1", 9.85));
				loadHandler.receive(new PriceUpdate("STOCK2", 110.71));
				loadHandler.receive(new PriceUpdate("STOCK3", 91.23));
				loadHandler.receive(new PriceUpdate("STOCK4", 13.63));
				loadHandler.receive(new PriceUpdate("STOCK5", 14.63));
				loadHandler.receive(new PriceUpdate("STOCK4", 150.63));
				loadHandler.receive(new PriceUpdate("STOCK6", 17.63));
				
			}
			
			try {
				TimeUnit.MILLISECONDS.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
