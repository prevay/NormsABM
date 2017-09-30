

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
			if (normspace.schedule.getSteps()%100==1) {
			if (normspace.job()<4){
				//writeToFile(normspace.getNumDefect(), "normspace-0.2-small-world-numdefect-adjpayoffs.txt");
				writeToFile(normspace.getAvgBoldness(), "normspace-0.2-scale-free-bold-extremes2.txt");
				writeToFile(normspace.getAvgVengefulness(), "normspace-0.2-scale-free-veng-extremes2.txt");
				//writeToFile(normspace.getStdVengefulness(), "normspace-0.2-small-world-stdveng-adjpayoffs.txt");
				//writeToFile(normspace.getStdBoldness(), "normspace-0.2-small-world-stdbold-adjpayoffs.txt");
			}
			if (normspace.job()>=4 && normspace.job()<8){
				//writeToFile(normspace.getNumDefect(), "normspace-0.1-small-world-numdefect-adjpayoffs.txt");
				writeToFile(normspace.getAvgBoldness(), "normspace-0.1-scale-free-bold-extremes2.txt");
				writeToFile(normspace.getAvgVengefulness(), "normspace-0.1-scale-free-veng-extremes2.txt");
				//writeToFile(normspace.getStdVengefulness(), "normspace-0.1-small-world-stdveng-adjpayoffs.txt");
				//writeToFile(normspace.getStdBoldness(), "normspace-0.1-small-world-stdbold-adjpayoffs.txt");
			}
			if (normspace.job()>=8 && normspace.job()<12){
				//writeToFile(normspace.getNumDefect(), "normspace-0.01small-world-numdefect-adjpayoffs.txt");
				writeToFile(normspace.getAvgBoldness(), "normspace-0.01-scale-free-bold-extremes2.txt");
				writeToFile(normspace.getAvgVengefulness(), "normspace-0.01-scale-free-veng-extremes2.txt");
				//writeToFile(normspace.getStdVengefulness(), "normspace-0.01-small-world-stdveng-adjpayoffs.txt");
				//writeToFile(normspace.getStdBoldness(), "normspace-0.01-small-world-stdbold-adjpayoffs.txt");
			}
			if (normspace.job()>=12 && normspace.job()<16){
				//writeToFile(normspace.getNumDefect(), "normspace-0.001-small-world-numdefect-adjpayoffs.txt");
				writeToFile(normspace.getAvgBoldness(), "normspace-0.001-scale-free-bold-extremes2.txt");
				writeToFile(normspace.getAvgVengefulness(), "normspace-0.001-scale-free-veng-extremes2.txt");
				//writeToFile(normspace.getStdVengefulness(), "normspace-0.001-small-world-stdveng-adjpayoffs.txt");
				//writeToFile(normspace.getStdBoldness(), "normspace-0.001-small-world-stdbold-adjpayoffs.txt");
			}
			}
		}
}
