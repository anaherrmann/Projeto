package Lamport;
import org.simgrid.msg.Task;

/**
 *
 * @author herrmann
 */
public class RequestTask extends Task {
    private int timestamp;
    
    public RequestTask(){ //super("request", 0, 0);
        super();
    }
	
    public RequestTask(int timestamp){
        super("Request", 10000, 50);
        this.timestamp = timestamp;
    }
    
    public int getTimestamp(){
        return this.timestamp;
    }
}
