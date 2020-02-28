package softwareProfession;

import java.applet.Applet;
import java.awt.AWTEvent;
import java.awt.Button;
import java.awt.Component;
import java.awt.Event;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.util.Random;


public class Tsp extends Applet
//Contains a plot of the graph and the path,
//and several buttons.
{	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Plot Plotarea;
	Button Clear;
	Button Randomize;
	Button LocalOptimize;
	GridBagLayout gridbag=new GridBagLayout();
	int nrandom=9,niter=9;
	Random R=new Random();
	TextField NRandom; // To get number of random points 
	TextField NIterations; // To get number of iterations
	public void init ()
	// layout the applet
	{	Plotarea=new Plot();
		Panel panel1=new Panel();
		Panel panel2=new Panel();
		Clear=new Button("   Clear   ");
		Randomize=new Button("  Random  ");
		NRandom=new TextField("",4);
		LocalOptimize=new Button("Local Optimum");
		NIterations=new TextField("",4);
		panel1.add(Clear);
		panel1.add(Randomize);
		panel1.add(NRandom);
		panel2.add(LocalOptimize);
		panel2.add(new Label(" Iterations:"));
		panel2.add(NIterations);
		setLayout(gridbag);
		setConstrain(Plotarea,0,0,1,1,
			GridBagConstraints.BOTH,GridBagConstraints.CENTER,
			1.0,1.0);
		add(Plotarea);
		setConstrain(panel1,0,2,1,1,
			GridBagConstraints.NONE,GridBagConstraints.CENTER,
			1.0,0.0);
		add(panel1);
		setConstrain(panel2,0,3,1,1,
			GridBagConstraints.NONE,GridBagConstraints.CENTER,
			1.0,0.0);
		add(panel2);
	}
	int setParam (String s, int d)
	// read a parameter named s with default value d
	{	String h = s ;
		int i;
		try { i=Integer.parseInt(h); }
			catch (NumberFormatException e) { return d; }
		return i;
	}
	void setConstrain (Component c, int gx, int gy, int gw, int gh,
		int fill, int anchor, double wx, double wy)
	// helper function to handle constrains
	{	GridBagConstraints g=new GridBagConstraints();
		g.gridx=gx; g.gridy=gy; g.gridwidth=gw; g.gridheight=gh;
		g.fill=fill; g.anchor=anchor;
		g.weightx=wx; g.weighty=wy;
		gridbag.setConstraints(c,g);
	}
	public boolean action (Event e, Object a)
	// react to buttons
	{	if (e.target==Randomize)
		// create random points
		{	try
			{	nrandom=Integer.parseInt(NRandom.getText());
				if(nrandom > 9) {
					showStatus("Please enter values between 4 and 9");
					nrandom=setParam("points",9);
				}
			}
			catch (Exception ex)
			{	nrandom=setParam("points",9);
			}
			Points p=new Points(nrandom);
			
			p.random(R);
			Plotarea.clear();
			Plotarea.set(p);
			Plotarea.repaint();
			return true;
		}
		
		if (e.target==Clear)
		// clear all points
		{	Plotarea.clear();
			Plotarea.repaint();
			return true;
		}
		if (e.target==LocalOptimize)
		// find a local optimum
		{	if (Plotarea.G==null) return true;
		
			Path pa=new Path(Plotarea.G);
			pa.random(R);
			pa.localoptimize();
			Plotarea.set(pa);
			Plotarea.repaint();
			
			return true;
		}
	
		return false;
	}
	public void getiter ()
	{	try
	
		{	//Get user input for no.of iterations to solve the problem.
			niter=Integer.parseInt(NIterations.getText());
		}
		catch (Exception ex)
		{	//If user inputs anything else then integers, set the niter back to 10.
			niter=setParam("iterations",10);
		}
	}

}
