

import java.util.Collections;

import sim.engine.SimState;
import sim.engine.Steppable;

/*
 * Helper class responsible for resetting the defection count every time step, and keeping track of average boldness
 * and vengefulness levels in the system.
 */


public class CoopCA implements Steppable {
	
	
	public void step(SimState state) {
		NormSpace normspace = (NormSpace) state;
		
		normspace.setNumDefect(0); //reset number of defectors to zero at beginning of round
		double totalB=0;
		double totalV=0;
		double diffsB=0;
		double diffsV=0;
		/*
		 * calculate average population boldness and vengefulness from last round:
		 */
		for (NormAgent agent : normspace.agents) {
			totalB=totalB+agent.getBoldness();
			totalV=totalV+agent.getVengefulness();
		}
		
		normspace.setAvgBoldness(totalB/normspace.agents.size());
		normspace.setAvgVengefulness(totalV/normspace.agents.size());
		
		for (NormAgent agent : normspace.agents) {
			diffsB=diffsB+Math.pow(agent.getBoldness()-normspace.getAvgBoldness(),2);
			diffsV=diffsV+Math.pow(agent.getVengefulness()-normspace.getAvgVengefulness(),2);
		}
		
		normspace.setStdBoldness(Math.sqrt(diffsB/normspace.agents.size()));
		normspace.setStdVengefulness(Math.sqrt(diffsV/normspace.agents.size()));
		
	}
}
