/*
 * ECE 480 Spring 2011
 * Team 2 Design Project
 * Matt Gottshall
 * Jake D'Onofrio
 * Gordie Stein
 * Andrew Kling
 */
package com.iDocent;

/**
 * This is a thread runnable class for downloading AP locations from the server
 *
 */
//A class to start the AP list downloading process within a separate thread
public class APDownloader implements Runnable {
	ScanResultReceiver SRR;
	WeightedScanFactory wsFactory;
	
	public APDownloader(ScanResultReceiver scanResultReceiver, WeightedScanFactory wsFactory) {
		SRR = scanResultReceiver;
		this.wsFactory = wsFactory;
	}

	/**
	 * This is run when the thread is executed.  It downloads the list of APs
	 * and then notifies the scanResultReceiver.
	 * 
	 * See also: {@link ScanResultReceiver}
	 */
	@Override
	public void run() {
		wsFactory.StartScanLoop();
		SRR.APsReady();
	}

}
