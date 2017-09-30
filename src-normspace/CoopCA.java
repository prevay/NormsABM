

import sim.engine.SimState;
import sim.engine.Steppable;

/*
 * Book-keeping class responsible for resetting the cooperation grid every time step, and keeping track of average boldness
 * and vengefulness levels in the system.
 */


public class CoopCA implements Steppable {
	
	
	public void step(SimState state) {
		NormSpace normspace = (NormSpace) state;
		normspace.coopGrid.setTo(0); //reset everything at the beginning of every time step
		normspace.setNumDefect(0);
		normspace.setAvgBoldness(normspace.boldGrid.mean());
		normspace.setAvgVengefulness(normspace.vengefulGrid.mean());
		
		double diffsB=0;
		double diffsV=0;
		
		for (Object obj : normspace.agentGrid.elements()) {
			NormAgent agent = (NormAgent) obj;
			diffsB=diffsB+Math.pow(agent.getBoldness()-normspace.getAvgBoldness(),2);
			diffsV=diffsV+Math.pow(agent.getVengefulness()-normspace.getAvgVengefulness(),2);
		}
		
		normspace.setStdBoldness(Math.sqrt(diffsB/(normspace.agentGrid.getHeight()*normspace.agentGrid.getWidth())));
		normspace.setStdVengefulness(Math.sqrt(diffsV/(normspace.agentGrid.getHeight()*normspace.agentGrid.getWidth())));
	}
}
