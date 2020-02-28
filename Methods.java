package softwareProfession;
import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.applet.*;

class Graph
// contains a Matrix of distances for a graph.
{	protected int N;
	double M[][];
	final static double INFTY=1e50;
	public Graph (int n)
	{	N=n;
		M=new double[n][n];
		int i,j;
		// initially disconnect all points
		for (i=0; i<n; i++)
			for (j=0; j<n; j++)
				{	if (i!=j) connect(i,j,INFTY);
					else connect(i,j,0);
				}
	}
	void connect (int i, int j, double x) 
	{ 
		M[i][j]=x; 
	}
	final double distance (int i, int j) 
	{ 
		return M[i][j]; 
	}
	final int size () 
	{ 
		return N; 
	}
}

class Points
// contains a vector of points with x-coordinate and y-coordinate
{   int N;
	public double X[],Y[];
	public Points (int n)
	{	X=new double[n];
		Y=new double[n];
		N=n;
	}
	public void random (Random r)
	// generate random points
	{	int i;
		for (i=0; i<N; i++)
		{	X[i]=r.nextDouble();
			Y[i]=r.nextDouble();
		}
	}
	public void square (int n, int m)
	// generate points in a n x m grid
	{	int i,j,k=0;
		for (i=0; i<n; i++)
			for (j=0; j<m; j++)
			{	X[k]=i/(double)(n-1);
				Y[k]=j/(double)(m-1);
				k++;
			}
	}
	public final int size () { return N; }
	public void extend (double xn, double yn)
	// add another point to the vector
	{	double x[]=new double[N+1];
		double y[]=new double[N+1];
		int i;
		for (i=0; i<N; i++) { x[i]=X[i]; y[i]=Y[i]; }
		x[N]=xn; y[N]=yn;
		X=x; Y=y; N++;
	}
}

class planeGraph extends Graph
// A graph, which can be initialized with points.
{	final double sqr (double x) { return x*x; }
	public planeGraph (Points p)
	{	super(p.size());
		int i,j;
		for (i=0; i<N; i++)
			for (j=0; j<N; j++)
				connect(i,j,
				Math.sqrt(sqr(p.X[i]-p.X[j])+sqr(p.Y[i]-p.Y[j])));
	}		
}

class Path
// A path in a graph.
// From[i] is the index of the point leading to i.
// To[i] the index of the point after i.
// The path can optimize itself in a graph.
{   Graph G;
	int N;
	double L;
	public int From[],To[];
	public Path (Graph g)
	{	N=g.size();
		G=g;
		From=new int[N];
		To=new int[N];
	}
	public Object clone ()
	// return a clone path
	{	Path p=new Path(G);
		p.L=L;
		int i;
		for (i=0; i<N; i++)
		{	p.From[i]=From[i];
			p.To[i]=To[i];
		}
		return p;
	}
	public void random (Random r)
	// random path.
	{	int i,j,i0,j0,k;
		for (i=0; i<N; i++) To[i]=-1;
		for (i0=i=0; i<N-1; i++)
		{	j=(int)(r.nextLong()%(N-i));
			To[i0]=0;
			for (j0=k=0; k<j; k++)
			{	j0++;
				while (To[j0]!=-1) j0++;
			}
			while (To[j0]!=-1) j0++;
			To[i0]=j0; From[j0]=i0;
			i0=j0;
		}
		To[i0]=0; From[0]=i0;
		getlength();
	}
	public double length () { return L; }
	public boolean improve ()
	// try to find another path with shorter length
	// using removals of points j and inserting i,j,i+1
	{   int i,j,h;
		double d1,d2;
		double H[]=new double[N];
		for (i=0; i<N; i++)
			H[i]=-G.distance(From[i],i)-G.distance(i,To[i])
				+G.distance(From[i],To[i]);
		for (i=0; i<N; i++)
		{	d1=-G.distance(i,To[i]);
			j=To[To[i]];
			while (j!=i)
			{   d2=H[j]+G.distance(i,j)+G.distance(j,To[i])+d1;
				if (d2<-1e-10)
				{	h=From[j];
					To[h]=To[j]; From[To[j]]=h;
					h=To[i]; To[i]=j; To[j]=h;
					From[h]=j; From[j]=i;
					getlength();
					return true;
				}
	            j=To[j];
			}
		}
		return false;
	}
	public boolean improvecross ()
	// improve the path locally, using replacements
	// of i,i+1 and j,j+1 with i,j and i+1,j+1
	{   int i,j,h,h1,hj;
		double d1,d2,d;
		for (i=0; i<N; i++)
		{	d1=-G.distance(i,To[i]);
			j=To[To[i]];
			d2=0;
	        d=0;
			while (To[j]!=i)
			{   d+=G.distance(j,From[j])-G.distance(From[j],j);
				d2=d1+G.distance(i,j)+d+G.distance(To[i],To[j])
					-G.distance(j,To[j]);
				if (d2<-1e-10)
				{   h=To[i]; h1=To[j];
					To[i]=j;
					To[h]=h1; From[h1]=h;
					hj=i;
					while (j!=h)
					{   h1=From[j];
						To[j]=h1;
						From[j]=hj;
						hj=j;
						j=h1;
					}
	                From[j]=hj;
					getlength();
					return true;
				}
				j=To[j];
			}
		}
		return false;
	}
	void getlength ()
	// compute the length of the path
	{	L=0;
		int i;
		for (i=0; i<N; i++)
		{	L+=G.distance(i,To[i]);
		}
	}
	void localoptimize ()
	// find a local optimum starting from this path
	{	do
		{	while (improve());
		} while (improvecross());
	}
}

class Plot extends Panel
// Show a plane graph, a path and its length.
{	Points P=null;
	Path Pa=null;
	public planeGraph G;
	String Message=new String("");
	final int col (double x) { return (int)(x*200+10); }
	final int row (double y) { return (int)(y*200+10); }
	void drawpoints (Graphics g)
	// draw the points
	{	if (P==null) return;
		int i,c,r;
		g.setColor(Color.red);
		for (i=0; i<P.size(); i++)
		{	c=col(P.X[i]); r=row(P.Y[i]);
			g.drawRect(c-1,r-1,3,3);
		}
	}
	void frame (Graphics graphics)
	// delete background and draw a frame
	{	graphics.setColor(getBackground());
	graphics.fillRect(0,0,size().width,size().height);
	graphics.setColor(Color.green);
	graphics.drawRect(10,10,216,216);
	}
	void drawmessage (Graphics g)
	// draw a message to the right of the frame
	{	g.setColor(Color.black);
		if (Pa!=null)
		{	Double l=new Double(Pa.length());
			g.drawString(Message+", Length="+l.toString(),240,20);
		}
		else g.drawString(Message,240,20);
	}
	void drawpath (Graphics g)
	// connect the points of the graph, following the path.
	{	if (Pa==null || P==null) return;
		g.setColor(Color.black);
		int i=0,j=Pa.To[i];
		while (j!=0)
		{   g.drawLine(col(P.X[i]),row(P.Y[i]),
				col(P.X[j]),row(P.Y[j]));
			j=Pa.To[j]; i=Pa.From[j];
		}
		g.drawLine(col(P.X[i]),row(P.Y[i]),
			col(P.X[j]),row(P.Y[j]));
	}
	public void paint (Graphics g)
	// paint everything
	{	frame(g);
		drawpath(g);
		drawpoints(g);
		drawmessage(g);
	}
	public void set (Points p)
	{	P=p;
		G=new planeGraph(p);
		Integer I = new Integer(p.size());
		Message=new String(I.toString()+" Points");
	}
	public void set (Path pa)
	{	Pa=pa;
	}
	public void clear ()
	// remove the points and the graph.
	{	P=null;
		Pa=null;
		G=null;
	}
	
}