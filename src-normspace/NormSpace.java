


import sim.engine.SimState;
import sim.field.grid.DoubleGrid2D;
import sim.field.grid.IntGrid2D;
import sim.field.grid.ObjectGrid2D;
import sim.util.Bag;

//the main simulation class:


public class NormSpace extends SimState {
	
	private double seenProb=0.9; //mean witnessing probability
	public ObjectGrid2D agentGrid;
	public DoubleGrid2D vengefulGrid;
	public DoubleGrid2D boldGrid;
	public IntGrid2D coopGrid;
	public IntGrid2D exampleGrid;
	public int gridWidth = 100;
    public int gridHeight = 100;
    public int randseed =1;
    public int nSize=3;
    public double avgBoldness;
    public double avgVengefulness;
    public double stdBoldness;
    public double stdVengefulness;
    public int numdefect;
    public double noiseLevel = 0;
    public boolean adjustPayoffs = true;
    public int neighborhoodType=1; //1==little boxes, 2==glocalization, 3==networked individualism I, 4==networked individualism II
    
    
    public int getNtype() { return neighborhoodType ;}
	public void setNtype(int p) {if (p==1 || p==2 || p==3)  neighborhoodType = p ;}
	public Object domNtype() { return new sim.util.Interval(1,3);}
    
    public double getSeenProb() { return seenProb ;}
	public void setSeenProb(double p) {if (p >0)  seenProb = p ;}
	public Object domSeenProb() { return new sim.util.Interval(0,1.0);}
	
	public int getWidth() { return gridWidth ;}
	public void setWidth(int width) {if (width >0)  gridWidth = width ;}
	public Object domWidth() { return new sim.util.Interval(1,10000);}
    
	public int getHeight() { return gridHeight ;}
	public void setHeight(int height) {if (height >0)  gridHeight = height ;}
	public Object domHeight() { return new sim.util.Interval(1,10000);}
	
	public int getNsize() { return nSize ;}
	public void setNsize(int n) {if (n >0)  nSize = n ;}
	public Object domNsize() { return new sim.util.Interval(1,20);}
	
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
	
	public void setStdBoldness(double val) {
		stdBoldness = val;
	}
	
	public double getStdBoldness() {
		return stdBoldness;
	}
	
	
	//create agents on the grid
	void seedAgentGrid() {
		for (int x=0; x<agentGrid.getWidth(); x++){
			for (int y=0; y<agentGrid.getHeight(); y++){
				
				agentGrid.set(x, y, new NormAgent(x,y,randseed,this));
				randseed++;
			}
		}
	}
	
	//initialize the agents' neighborhoods:
	void initNeighborhoods() {
		for (Object obj: agentGrid.elements()) {
			NormAgent agent = (NormAgent) obj;
			
			if (neighborhoodType==1) {
			agent.setNeighbors(agent.getLittleBox(this,agent));
			}
			if (neighborhoodType==2) {
			agent.setNeighbors(agent.getGlocalizedNetwork(this,agent));
			}
			if (neighborhoodType==3) {
				agent.setNeighbors(agent.getIndividualizedNetwork(this,agent));
			}
			if (neighborhoodType==4) {
			agent.setNeighbors(agent.getIndividualizedNetwork2(this));
			}
		}
		
	}
	
	//initialize the vengefulness grid
	void seedVengefulGrid() {
		for (int x=0; x<vengefulGrid.getWidth(); x++){
			for (int y=0; y<vengefulGrid.getHeight(); y++){
				
				vengefulGrid.field[x][y] = ((NormAgent) agentGrid.get(x,y)).getVengefulness();
			}
		}
	}

	//initialize the boldness grid:
	void seedBoldGrid() {
		for (int x=0; x<boldGrid.getWidth(); x++){
			for (int y=0; y<boldGrid.getHeight(); y++){
				
				boldGrid.field[x][y] = ((NormAgent) agentGrid.get(x,y)).getBoldness();
			}
		}
	}
	
	//utility method used to create a grid  for illustrating the different types of neighborhoods:
	void seedExampleGrid() {
		exampleGrid.setTo(0);
		int x = random.nextInt(gridWidth);
		int y = random.nextInt(gridHeight);
		exampleGrid.field[x][y]=1;
		NormAgent agent = (NormAgent) agentGrid.get(x, y);
		Bag neighbors = agent.getNeighbors();
		for (Object obj : neighbors) {
			NormAgent neighbor = (NormAgent) obj;
			exampleGrid.field[neighbor.getxLoc()][neighbor.getyLoc()]=2;
		}
		
			
	}

	public NormSpace(long seed) {
		super(seed);
	}
	
	public void start()
    {
		super.start();
		
		//you can set parameter settings for different jobs:
		if (this.job()<100) {this.seenProb=0.2;}
	    if (this.job()>=100 && this.job()<200) {this.seenProb=0.1;}
	    if (this.job()>=200 && this.job()<300) {this.seenProb=0.01;}
	    if (this.job()>=300 && this.job()<400) {this.seenProb=0.001;}
	    //if (this.job()>=40 && this.job()<50) {this.setSeenProb(0.0001);}
	    //if (this.job()>=50 && this.job()<60) {this.setSeenProb(0.00001);}
	    
		//initialize and populate all of the grids:
		agentGrid = new ObjectGrid2D(gridWidth,gridHeight);
		vengefulGrid = new DoubleGrid2D(gridWidth,gridHeight);
		boldGrid = new DoubleGrid2D(gridWidth,gridHeight);
		coopGrid = new IntGrid2D(gridWidth,gridHeight);
		exampleGrid = new IntGrid2D(gridWidth,gridHeight);
		seedAgentGrid();
		seedVengefulGrid();
		seedBoldGrid();
		initNeighborhoods();
		seedExampleGrid();
		coopGrid.setTo(0);
		//schedule the agents:
		schedule.scheduleRepeating(new WriterAgent());
		schedule.scheduleRepeating(new CoopCA(),1,1);
		for (Object obj : agentGrid.elements()) {
			NormAgent agent = (NormAgent) obj;
			schedule.scheduleRepeating(agent,2,1);
		
		}
		
		
    }
	
	//main simulation loop:
	public static void main(String[] args)
    {
		doLoop(NormSpace.class, args);
		System.exit(0); 
    }
	
	

}
