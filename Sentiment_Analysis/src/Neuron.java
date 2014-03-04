
public class Neuron 
{
	public int id;
    public static int count;
    public double output;
    public double error; //for back propagation
    public Link[] to;   //links which come to the neuron
    
    public Neuron()
    {
        this.id=count++;
    }
}
