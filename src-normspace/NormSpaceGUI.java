
import sim.engine.*;


import sim.portrayal.*;
import sim.display.*;
import sim.portrayal.grid.*;
import sim.portrayal.simple.RectanglePortrayal2D;

import java.awt.*;

import javax.swing.*;

import ec.util.MersenneTwisterFast;

/*
 * class responsible for simulation visualization:
 */

public class NormSpaceGUI extends GUIState {

	public Display2D display_coop;
	public JFrame displayFrame_coop;
	public Display2D display_bold;
	public JFrame displayFrame_bold;
	public Display2D display_vengeful;
	public JFrame displayFrame_vengeful;
	public Display2D display_example;
	public JFrame displayFrame_example;
	
	MersenneTwisterFast random = new MersenneTwisterFast();
	
	
	private final static double INITIAL_WIDTH = 300;
    private final static double INITIAL_HEIGHT = 300;
	
	public NormSpaceGUI() { super(new NormSpace(System.currentTimeMillis())); }
    
    public NormSpaceGUI(SimState state) { super(state); }
    
    //initialize portrayals and attach grids to them:
    FastValueGridPortrayal2D coopGridPortrayal = new FastValueGridPortrayal2D();
    FastValueGridPortrayal2D boldGridPortrayal = new FastValueGridPortrayal2D();
    FastValueGridPortrayal2D vengefulGridPortrayal = new FastValueGridPortrayal2D();
    FastValueGridPortrayal2D exampleGridPortrayal = new FastValueGridPortrayal2D();
    public void setupPortrayals() {
    	coopGridPortrayal.setField(((NormSpace)state).coopGrid );
    	coopGridPortrayal.setMap(new sim.util.gui.SimpleColorMap( new Color[] {Color.gray, Color.black}));
    	exampleGridPortrayal.setField(((NormSpace)state).exampleGrid );
    	exampleGridPortrayal.setMap(new sim.util.gui.SimpleColorMap( new Color[] {Color.blue, Color.red, Color.green}));

    	boldGridPortrayal.setField(((NormSpace)state).boldGrid);
    	boldGridPortrayal.setMap(new sim.util.gui.SimpleColorMap(0, 7,Color.green, Color.yellow));
    	vengefulGridPortrayal.setField(((NormSpace)state).vengefulGrid);
    	vengefulGridPortrayal.setMap(new sim.util.gui.SimpleColorMap(0, 7,Color.blue, Color.red));
    	
    	
    	display_coop.attach(coopGridPortrayal, "Cooperation");
    	display_bold.attach(boldGridPortrayal, "Boldness");
    	display_vengeful.attach(vengefulGridPortrayal, "Vengefulness");
    	display_example.attach(exampleGridPortrayal, "Neighborhood Portrayal");
    	display_example.reset();
    	display_example.repaint();
    	display_coop.reset();
        display_coop.repaint();
        display_bold.reset();
        display_bold.repaint();
        display_vengeful.reset();
        display_vengeful.repaint();
    }	
    
    public void start() {
		super.start();
		setupPortrayals();
		
	}
	
	public void load() {
		super.start();
		setupPortrayals();
	}
	
	@Override
	//initialize controllers for the displays:
	public void init(Controller c)
    {
        super.init(c);
        
   
        
        display_coop = new Display2D(INITIAL_WIDTH, INITIAL_HEIGHT, this);
        displayFrame_coop = display_coop.createFrame();
        c.registerFrame(displayFrame_coop);
        displayFrame_coop.setVisible(true);
        displayFrame_coop.setTitle("Cooperation Display");
        
        display_example = new Display2D(INITIAL_WIDTH, INITIAL_HEIGHT, this);
        displayFrame_example = display_example.createFrame();
        c.registerFrame(displayFrame_example);
        displayFrame_example.setVisible(true);
        displayFrame_example.setTitle("Neighborhood Display");
        
        display_bold = new Display2D(INITIAL_WIDTH, INITIAL_HEIGHT, this);
        displayFrame_bold = display_bold.createFrame();
        c.registerFrame(displayFrame_bold);
        displayFrame_bold.setVisible(true);
        displayFrame_bold.setTitle("Boldness Display");
        
        display_vengeful = new Display2D(INITIAL_WIDTH, INITIAL_HEIGHT, this);
        displayFrame_vengeful = display_vengeful.createFrame();
        c.registerFrame(displayFrame_vengeful);
        displayFrame_vengeful.setVisible(true);
        displayFrame_vengeful.setTitle("Vengefulness Display");
        
        
        

    }
	
	public Object getSimulationInspectedObject() { return state; }
	
	public Inspector getInspector()
	{
	Inspector i = super.getInspector();
	i.setVolatile(true);
	return i;
	}
    
    public static void main( String[] args) {
    	NormSpaceGUI vid = new NormSpaceGUI();
    	Console c = new Console(vid);
    	c.setVisible(true);
    }
}
