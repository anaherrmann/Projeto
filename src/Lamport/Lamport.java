package Lamport;
import java.util.ArrayList;
import org.simgrid.msg.*;
/**
 *
 * @author herrmann
 */
public class Lamport extends org.simgrid.msg.Process {
    private ArrayList<Integer> requestList;
    private int numProcessos; //preciso desse cara?
    int timestamp = 0;
    

    public Lamport(Host host, String name, String [] args){
        super(host, name, args);
        requestList = new ArrayList<>(numProcessos);
    }
    
    @Override
    public void main(String[] strings) throws MsgException {
         if(getPID() == 1){
            timestamp++;
            
            for(int i = 1; i <= getCount(); i++) {
                if(i != getPID()){
                    Task t = new RequestTask(timestamp);
                    t.isend("node-" + i);                    
                }
            }
            
            for(int i = 1; i < getCount(); i++){
                ReplyTask t1;
                //System.out.println(Msg.getClock()+ "entrou no for");
                try {
                    t1 = (ReplyTask) Task.receive("node-1");
                    
                    try {
                        t1.execute();
                    } catch (TaskCancelledException ex) {
                        System.out.println("Erro na execução do REPLY: "+ex.getMessage());
                    }
                } catch (TransferFailureException | HostFailureException | TimeoutException ex) {
                    System.out.println("Erro no recebimento do REPLY: "+ex.getMessage());
                }
                
            }
           
        } else {
            RequestTask t2;
            try {
                t2 = (RequestTask) Task.receive("node-" + getPID());
               
                try {
                    t2.execute();
                } catch (TaskCancelledException ex) {
                    System.out.println("Erro na execução do REQUEST: "+ex.getMessage());
                }
                ReplyTask t = new ReplyTask();
                t.send("node-" + t2.getSender().getPID());
               
                                 
            } catch (TransferFailureException | HostFailureException | TimeoutException | NativeException ex) {
              
            }            
        } 
    }
    
}
