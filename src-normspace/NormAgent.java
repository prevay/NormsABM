


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import sim.engine.*;
import sim.field.grid.*;
import sim.util.*;
import ec.util.*;

//The class representing agents in the system.

public class NormAgent implements Steppable {
	
	public NormSpace normspace;
	private double vengefulness;
	private double boldness;
	private int payoffs=0;
	private int xloc;
	private int yloc;
	private Bag neighbors;
	private IntBag xPos;
	private IntBag yPos;
	private double seenProb;
	MersenneTwisterFast random;
	
	public NormAgent(int x, int y, int seed, NormSpace normspace) {
		/*
		 * initialize agent with random boldness and vengefulness levels
		 */
		this.random = new MersenneTwisterFast(seed);
		this.vengefulness=random.nextInt(8);
		this.boldness=random.nextInt(8);
		this.xloc=x;
		this.yloc=y;
		this.seenProb=normspace.getSeenProb();//+(1/5)*normspace.getSeenProb()*random.nextDouble(); //get witnessing probability (normally distributed around the mean witnessing probability
		
	}
	
	public int getxLoc() {
		return xloc;
	}
	
	public void setxLoc(int x) {
		this.xloc = x;
	}
	
	
	public double getProb() {
		return seenProb;
	}
	
	public void setProb(double p) {
		this.seenProb = p;
	}
	
	public int getyLoc() {
		return yloc;
	}
	
	
	
	public void setyLoc(int y) {
		this.yloc = y;
	}
	
	
	public double getVengefulness() {
		return vengefulness;
	}
	
	public void setVengefulness(int val) {
		this.vengefulness=val;
	}
	
	public double getBoldness() {
		return boldness;
	}
	
	public void setBoldness(int val) {
		this.boldness=val;
	}
	
	public int getPayoffs() {
		return payoffs;
	}
	
	public void setPayoffs(int val) {
		this.payoffs=val;
	}
	
	public void setNeighbors(Bag neighbors){
		
		this.neighbors = neighbors;
	}
	
	public Bag getNeighbors() {
		return neighbors;
		
	}
	
	public Bag getMooreNbhd(NormSpace normspace) {
		neighbors=normspace.agentGrid.getMooreNeighbors(this.xloc, this.yloc, normspace.nSize, Grid2D.BOUNDED, false,neighbors, this.xPos, this.yPos);
		return neighbors;
	}
	
	public Bag getLittleBox(NormSpace normspace, NormAgent agent) {
		  
		//initialize the agent's little boxes neighborhood:
		 
		 Bag nbhd = new Bag();
		 IntBag xPos = new IntBag();
		 IntBag yPos = new IntBag();
		 Bag neighbors = new Bag();
		 normspace.agentGrid.getMooreNeighbors(agent.getxLoc(), agent.getyLoc(), normspace.getNsize(), Grid2D.TOROIDAL, false,neighbors, xPos, yPos);
	
		 return neighbors;
	}
	
	public Bag getGlocalizedNetwork(NormSpace normspace, NormAgent agent) {

		//initialize the agent's glocalized network neighborhood:
		
		int numCommunities = (int) (5.5 + random.nextGaussian()); //get number of communities
		
		Bag neighbors = new Bag();
		
		//for each community make 3x3 squares randomly placed on the grid
		for (int i=0; i<numCommunities; i++) {
			//make sure the first community is the agent's moore neighborhood
				if (i==0) {
					neighbors = normspace.agentGrid.getMooreNeighbors(agent.getxLoc(), agent.getyLoc(), 1, Grid2D.TOROIDAL, false,neighbors, xPos, yPos);
				}
				else {
					//pick a center for the new community, but make sure it's not somewhere in its neighborhood already.
					NormAgent newPivot;
					do {
					newPivot = (NormAgent) normspace.agentGrid.get(random.nextInt(normspace.getHeight()),random.nextInt(normspace.getWidth()));
					}
					while (neighbors.contains(newPivot));
						
					
					neighbors.add(newPivot);
					neighbors.addAll(normspace.agentGrid.getMooreNeighbors(newPivot.getxLoc(), newPivot.getyLoc(), 1, Grid2D.TOROIDAL, false,new Bag(), xPos, yPos));
				}
			
		}
		
		
		return neighbors;
	}
	
	public Bag getIndividualizedNetwork(NormSpace normspace, NormAgent agent) {
		
		//initialize the agent's networked individualism neighborhood:
		
		Bag neighbors = new Bag();
		IntBag xPos = new IntBag();
		IntBag yPos = new IntBag();
		//first get the agen'ts moore neighborhood:
		neighbors.addAll(normspace.agentGrid.getMooreNeighbors(agent.getxLoc(), agent.getyLoc(), 1, Grid2D.TOROIDAL, false,neighbors, xPos, yPos));
		
		for (int i =0; i<40; i++) {
			
			/*
			 * then pick 40 random locations on the grid and add to the neighborhood
			 */
			int x;
			int y;
			boolean containsNeighbor;
			do {
				
				x=random.nextInt(normspace.getWidth());
				y=random.nextInt(normspace.getHeight());
				containsNeighbor = neighbors.contains(normspace.agentGrid.get(x, y));
				
			}
			while (containsNeighbor); //just make sure every time that the randomly picked location is not yet in the neighborhood
			neighbors.add(normspace.agentGrid.get(x, y));
		}
		
		return neighbors;
	}
	
	public Bag getIndividualizedNetwork2(NormSpace normspace) {
		
		//initialize the agent's networked individualism neighborhood:
		
		Bag neighbors = new Bag();
		IntBag xPos = new IntBag();
		IntBag yPos = new IntBag();
		
		/*
		 * then pick 48 random locations on the grid and add to the neighborhood
		 */
		for (int i =0; i<48; i++) {
			
			int x;
			int y;
			boolean containsNeighbor;
			do {
				
				x=random.nextInt(normspace.getWidth());
				y=random.nextInt(normspace.getHeight());
				containsNeighbor = neighbors.contains(normspace.agentGrid.get(x, y));
				
			}
			while (containsNeighbor); //just make sure every time that the randomly picked location is not yet in the neighborhood
			neighbors.add(normspace.agentGrid.get(x, y));
		}
		
		return neighbors;
	}
		
	
	
	
	
	public void step(SimState state) {
		
		
		//every four steps invoke the selection mechanism:
		if (state.schedule.getTime() % 4 == 0 && state.schedule.getTime()>0 /*or 1?*/) {
			evolve(state);
		}
		normspace = (NormSpace) state;

		//check whether agent defects and if so update it's payoffs and the neighbor's payoffs
		if (isDefected()) {
			calculatePayoffs();
		}
		
		
		
    }
	
	
	
	public boolean isDefected() {
		//check if boldness higher than perceived probability of being seen:
		if ((this.boldness/7) > 1 - Math.pow((1-normspace.getSeenProb()+normspace.noiseLevel*normspace.getSeenProb()*random.nextGaussian()), this.neighbors.size())) {
			normspace.coopGrid.field[this.xloc][this.yloc]=1;
			normspace.setNumDefect(normspace.getNumDefect()+1);
			return true;
		}
		else {
			return false;
		}
		
	}
	
	public void calculatePayoffs() {
		if (!normspace.adjustPayoffs){this.payoffs=this.payoffs+3;}
		else {this.payoffs=this.payoffs+(int)(0.15*this.neighbors.numObjs);}
		for (Object obj : this.neighbors) {
			NormAgent neighbor = (NormAgent) obj;
			neighbor.setPayoffs(neighbor.getPayoffs()-1); //cost of agent's defection on the neighbor
			/*
			 * check if any neighbors see and punish the defection:
			 */
			if (random.nextDouble() < neighbor.getProb()+normspace.noiseLevel*neighbor.getProb()*random.nextGaussian() && random.nextDouble() < (neighbor.getVengefulness()/7)) {
				neighbor.setPayoffs(neighbor.getPayoffs()-3); //enforcement cost for punishing the defection;
				this.payoffs=this.payoffs-9; //cost of punishment;
				
			}
		}
	}
	
	public void evolve(SimState state) { 
		int mean = calcMean();
		double stdev=calcStDev(mean);
		
		//if agent is at least one st. dev. above mean, keep your genetic information:
		if (this.payoffs>mean+stdev) {
			return;
		}
		
		//otherwise randomly select one of your neighbors who is at least one st. dev. above the mean and copy their genetic information
		else {
			Bag candidates = new Bag();
			for (Object obj : this.neighbors) {
				NormAgent neighbor = (NormAgent) obj;
				if (neighbor.getPayoffs()>mean+stdev) {
					candidates.add(neighbor);
				}
			}
			if (candidates.size()!=0) {
				int idx= random.nextInt(candidates.size());
				NormAgent parent = (NormAgent) candidates.get(idx);
				this.boldness=parent.getBoldness();
				this.vengefulness=parent.getVengefulness();
			}
			
		}
		
		//mutate with a small probability:
		mutate(state);
		//update the boldness and vengefulness grids:
		normspace.boldGrid.field[this.xloc][this.yloc]=this.boldness;
		
		normspace.vengefulGrid.field[this.xloc][this.yloc]=this.vengefulness;
		
		this.payoffs=0;
		
	}
	
	public void mutate(SimState state){
		
String binary[] = {"000","001","010","011","100","101","110","111"};
		
		/*
		 * get the offspring's vengefulnes in binary and flip each bit with 1% probability
		 */
		
			String newV = new String();
			int v = (int) this.getVengefulness(); 
			String bin = binary[v];
			for (int i=0; i<3; i++) {
				if (normspace.random.nextDouble() < 0.01) {
					if (bin.charAt(i)=='0') {
						newV=newV+"1";
					}
					else {
						newV=newV+"0";
					}
				}
				else {
					if (bin.charAt(i)=='0') {
						newV=newV+"0";
					}
					else {
						newV=newV+"1";
					}
				}
			}
			for (int i=0; i<8; i++) {
					if (binary[i].equals(newV)) {
						this.setVengefulness(i);
					}
			}
		
		
		
		/*
		 * get the offspring's boldness in binary and flip each bit with 1% probability
		 */
		
			String newB = new String();
			int b = (int) this.getBoldness(); 
			bin = binary[b];
			for (int i=0; i<3; i++) {
				if (normspace.random.nextDouble() < 0.01) {
					if (bin.charAt(i)=='0') {
						newB=newB+"1";
					}
					else {
						newB=newB+"0";
					}
				}
				else {
					if (bin.charAt(i)=='0') {
						newB=newB+"0";
					}
					else {
						newB=newB+"1";
					}
				}
			}
			for (int i=0; i<8; i++) {
					if (binary[i].equals(newB)) {
						this.setBoldness(i);
					}
			}
		
	}
	
	/*
	 * calculates the mean payoffs of the agent's neighborhood
	 */
	public int calcMean() {
		int total = 0;
		for (Object obj : this.neighbors) {
			NormAgent neighbor = (NormAgent) obj;
			total=total+neighbor.getPayoffs(); 
		
			
		}
		int mean = total/neighbors.size();
		return mean;
	}
	
	/*
	 * calculates the standard deviation of  payoffs of the agent's neighborhood
	 */
	public double calcStDev(int mean) {
		
		double devs=0;
		for (Object obj : this.neighbors) {
			NormAgent neighbor = (NormAgent) obj;
			double dev=Math.pow(neighbor.getPayoffs()-mean,2); 
			devs=devs+dev;
			}
		
		double stdev = Math.sqrt(devs/neighbors.size());
		return stdev;
		
	}
	
}
