/*
 * ECE 480 Spring 2011
 * Team 2 Design Project
 * Matt Gottshall
 * Jake D'Onofrio
 * Gordie Stein
 * Andrew Kling
 */
package com.iDocent;

//A class to start the AP list downloading process within a separate thread
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
