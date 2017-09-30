

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import sim.engine.SimState;
import sim.engine.Steppable;

/*
 * This class is responsible for writing out the model output into text files. 
 */

public class WriterAgent implements Steppable {
	
		public void writeToFile(double input, String filename) {
			
			//initializes a new writer and appends data to given file
			
			PrintWriter out = null;
			
			try {
			    out = new PrintWriter(new BufferedWriter(new FileWriter(filename, true)));
			    out.println(input);
			}catch (IOException e) {
			    System.err.println(e);
			}finally{
			    if(out != null){
			        out.close();
			    }
			}
			
		}

		public void step(SimState state) {
			
			NormSpace normspace= (NormSpace) state;
			/*
			 * This is an example of writing different outputs to files for a batch of simulation runs:
			 */
			
			
			
			if (normspace.job()<100){
				//writeToFile(normspace.getNumDefect(), "normspace-0.2-boxes-numdefect-wnoise.txt");
				writeToFile(normspace.getAvgBoldness(), "normspace-0.2-boxes-bold-adjpayoffs.txt");
				writeToFile(normspace.getAvgVengefulness(), "normspace-0.2-boxes-veng-adjpayoffs.txt");
				//writeToFile(normspace.getStdVengefulness(), "normspace-0.2-boxes-stdveng-wnoise.txt");
				//writeToFile(normspace.getStdBoldness(), "normspace-0.2-small-boxes-stdbold-wnoise.txt");
			}
			if (normspace.job()>100 && normspace.job()<200){
				//writeToFile(normspace.getNumDefect(), "normspace-0.1-boxes-numdefect-wnoise.txt");
				writeToFile(normspace.getAvgBoldness(), "normspace-0.1-boxes-bold-adjpayoffs.txt");
				writeToFile(normspace.getAvgVengefulness(), "normspace-0.1-boxes-veng-adjpayoffs.txt");
				///writeToFile(normspace.getStdVengefulness(), "normspace-0.1-boxes-stdveng-wnoise.txt");
				//writeToFile(normspace.getStdBoldness(), "normspace-0.1-boxesd-stdbold-wnoise.txt");
			}
			if (normspace.job()>200 && normspace.job()<300){
				//writeToFile(normspace.getNumDefect(), "normspace-0.01-boxes-numdefect-wnoise.txt");
				writeToFile(normspace.getAvgBoldness(), "normspace-0.01-boxes-bold-adjpayoffs.txt");
				writeToFile(normspace.getAvgVengefulness(), "normspace-0.01-boxes-veng-adjpayoffs.txt");
				//writeToFile(normspace.getStdVengefulness(), "normspace-0.01-boxes-stdveng-wnoise.txt");
				//writeToFile(normspace.getStdBoldness(), "normspace-0.01-boxes-stdbold-wnoise.txt");
			}
			if (normspace.job()>300 && normspace.job()<400){
				//writeToFile(normspace.getNumDefect(), "normspace-0.001-boxes-numdefect-wnoise.txt");
				writeToFile(normspace.getAvgBoldness(), "normspace-0.001-boxes-bold-wnoise.txt");
				writeToFile(normspace.getAvgVengefulness(), "normspace-0.001-boxes-veng-wnoise.txt");
				//writeToFile(normspace.getStdVengefulness(), "normspace-0.001-boxes-stdveng-wnoise.txt");
				//writeToFile(normspace.getStdBoldness(), "normspace-0.001-boxes-stdbold-wnoise.txt");
			} 
		}
}
