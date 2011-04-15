package com.iDocent;

public class APDownloader implements Runnable {
	ScanResultReceiver SRR;
	WeightedScanFactory wsFactory;
	
	public APDownloader(ScanResultReceiver scanResultReceiver, WeightedScanFactory wsFactory) {
		SRR = scanResultReceiver;
		this.wsFactory = wsFactory;
	}

	@Override
	public void run() {
		wsFactory.StartScanLoop();
		SRR.APsReady();
	}

}
