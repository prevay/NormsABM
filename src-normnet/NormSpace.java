


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import sim.engine.SimState;
import sim.field.grid.DoubleGrid2D;
import sim.field.grid.IntGrid2D;
import sim.field.grid.ObjectGrid2D;
import sim.util.Bag;

//the main simulation class:


public class NormSpace extends SimState {
	
	private double seenProb=0.01; //mean witnessing probability
    public int randseed =1;
    public int numagents=1000;
    public double avgBoldness;
    public double avgVengefulness;
    public double stdBoldness;
    public double stdVengefulness;
    public int numdefect;
    public double noiseLevel = 0;
    public boolean adjustPayoffs = false;
    public ArrayList<NormAgent> agents = new ArrayList<NormAgent>(); //list of agents
    
    
  
	
	public int getNumDefect() {
		return numdefect;
	}
	
	public void  setNumDefect(int val){
		numdefect = val;
	}
	
	public void setAvgBoldness(double val) {
		avgBoldness = val;
	}
	
	public double getAvgBoldness() {
		return avgBoldness;
	}
	
	
	public void setStdBoldness(double val) {
		stdBoldness = val;
	}
	
	public double getStdBoldness() {
		return stdBoldness;
	}
	
	public void setAvgVengefulness(double val) {
		avgVengefulness = val;
	}
	
	
	public double getAvgVengefulness() {
		return avgVengefulness;
	}
	
	public void setStdVengefulness(double val) {
		stdVengefulness = val;
	}
	
	
	public double getStdVengefulness() {
		return stdVengefulness;
	}
	
	public double getSeenProb() { return seenProb ;}
	public void setSeenProb(double p) {if (p >0)  seenProb = p ;}
	
	/*
	 * method for reading in adjacency matrices of networks. 
	 * the adjacency matrix defines the interaction network of the agents
	 * and is returned as a 2d array.
	 */
	
	public int[][] readMat() { 
		
		int adjMat[][] = new int[agents.size()][agents.size()];
		
		/*
		 * reads in the specified file with the adjacency matrix:
		 */
		String csvFile=String.format("scale-free-%d-1", agents.size(), (this.job()%100)+1); 
		BufferedReader br = null;
		String line = "";
		String splitBy = ",";
		
		try {
			 
			br = new BufferedReader(new FileReader(csvFile));
			int i=0;
			while ((line = br.readLine()) != null) {
	 
			        // use comma as separator
				String[] row = line.split(splitBy);
				
				for (int j=0; j<agents.size();j++) {
					adjMat[i][j]=Integer.valueOf(row[j]);
				}
				i++;
	 
			}
	 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return adjMat;
	}
	
	/*
	 * initializes the agent's neighborhoods based on the adjacency matrix:
	 */
	void initNeighborhoods(int adjMat[][]) {
		int i = 0;
		
		for (NormAgent agent : agents) {
			Bag neighbors = new Bag();
			/*
			 * go through current row of adj. matrix and all the connections to the list of neighbors
			 */
			for (int j=0; j<adjMat.length;j++){
				if (adjMat[i][j]==1) {
					neighbors.add(agents.get(j));
				}
			}
			agent.setNeighbors(neighbors);
			i++;
		}
		
	}
	

	public NormSpace(long seed) {
		super(seed);
	}
	
	/*
	 * run initialization:
	 */
	public void start()
    {
		super.start();
		
		if (this.job()<4) {this.seenProb=0.2;}
	    if (this.job()>=4 && this.job()<8) {this.seenProb=0.1;}
	    if (this.job()>=8 && this.job()<12) {this.seenProb=0.01;}
	    if (this.job()>=12 && this.job()<16) {this.seenProb=0.001;}
		
		
		/*
		 * initialize the agents
		 */
		for (int i=0; i<numagents; i++) {

			agents.add(new NormAgent(randseed,this));
			randseed++;
		}
		
		/*
		 * read in the adjacency matrix
		 */
		int adjMat[][]=readMat();
		initNeighborhoods(adjMat);
		
		/*
		 * schedule an object responsible for keeping track of run statistics
		 */
		schedule.scheduleRepeating(new CoopCA(),1,1);
		for (NormAgent agent : agents) {
			
			schedule.scheduleRepeating(agent,2,1); //schedule the agents
		}
		
		schedule.scheduleRepeating(new WriterAgent()); //schedule the writer agent
		
		
    }
	
	//main simulation loop:
	public static void main(String[] args)
    {
		doLoop(NormSpace.class, args);
		System.exit(0); 
    }
	
	
	

}
