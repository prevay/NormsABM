


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

	private Bag neighbors;
	
	private double seenProb;
	MersenneTwisterFast random;
	
	public NormAgent(int seed, NormSpace normspace) {
		/*
		 * initialize agent with random boldness and vengefulness levels
		 */
		this.random = new MersenneTwisterFast(seed);
		
		
		
		int[] v = {0,5,0,5};
		int[] b = {0,0,5,5};
		
		for (int i=0; i<4; i++ ) {
		 if (normspace.job()%4==i) {
			this.vengefulness=random.nextInt(3)+v[i];
			this.boldness=random.nextInt(3)+b[i]; 
		 }
		}
		
	
		
		
		this.seenProb=normspace.getSeenProb();//+(1/5)*normspace.getSeenProb()*random.nextGaussian(); //get witnessing probability (normally distributed around the mean witnessing probability 
		
	}
	
	
	
	
	public double getProb() {
		return seenProb;
	}
	
	public void setProb(double p) {
		this.seenProb = p;
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
	
	
		
	
	
	
	
	public void step(SimState state) {
		
		
		//every four steps invoke the selection mechanism:
		if (state.schedule.getTime() % 4 == 0 && state.schedule.getTime()>0 && neighbors.size()>0 /*or 1?*/) {
			evolve(state);
		}
		normspace = (NormSpace) state;

		//check whether agent defects and if so update it's payoffs and the neighbor's payoffs
		if (isDefected()) {
			
			calculatePayoffs(normspace);
		}
		
		
		
    }
	
	
	
	public boolean isDefected() {
		//check if boldness higher than perceived probability of being seen:
		if ((this.boldness/7) > 1 - Math.pow((1-normspace.getSeenProb()+normspace.noiseLevel*normspace.getSeenProb()*random.nextGaussian()), this.neighbors.size())) {
			normspace.setNumDefect(normspace.getNumDefect()+1);
			return true;
		}
		else {
			return false;
		}
		
	}
	
	public void calculatePayoffs(NormSpace normspace) {
		/*
		 * if defected, distribute payoffs accordingly:
		 */
		if (!normspace.adjustPayoffs){this.payoffs=this.payoffs+3;}
		else {this.payoffs=this.payoffs+(int)(0.15*this.neighbors.numObjs);}
		for (Object obj : this.neighbors) {
			NormAgent neighbor = (NormAgent) obj;
			neighbor.setPayoffs(neighbor.getPayoffs()-1); //cost of agent's defection on the neighbor
			/*
			 * check if any neighbors see and punish the defection:
			 */
			if (random.nextDouble() < neighbor.getProb()+normspace.noiseLevel*neighbor.getProb()*random.nextGaussian() && random.nextDouble() < (neighbor.getVengefulness()/7)) {
				neighbor.setPayoffs(neighbor.getPayoffs()-2); //enforcement cost for punishing the defection;
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
		
		//this.payoffs=0;
		
		
	}
	
	public void mutate(SimState state){
		
		



if (state.random.nextDouble() <= 0.01) {
    if (this.boldness==7) {
        this.boldness=6;
    }
    else if (this.boldness==0) {
        this.boldness=1;
    }
    else {
        if (state.random.nextBoolean()==true) {
            this.boldness++;
        }
        else {
            this.boldness--;
        }
    }
}
 
if (state.random.nextDouble() <= 0.01) {
    if (this.vengefulness==7) {
        this.vengefulness=6;
    }
    else if (this.vengefulness==0) {
        this.vengefulness=1;
    }
    else {
        if (state.random.nextBoolean()==true) {
            this.vengefulness++;
        }
        else {
            this.vengefulness--;
        }
    }
}
		
		/*
		 * get the offspring's vengefulnes in binary and flip each bit with 1% probability
		 */
	/*	String binary[] = {"000","001","010","011","100","101","110","111"};
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
		
		*/
		
		/*
		 * get the offspring's boldness in binary and flip each bit with 1% probability
		 */
		/*
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
		
	*/	
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
			double dev=Math.pow(neighbor.getPayoffs()-mean,2); //cost of agent's defection on the neighbor
			devs=devs+dev;
			}
		
		double stdev = Math.sqrt(devs/neighbors.size());
		return stdev;
		
	}
	
}
