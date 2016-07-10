
package RicardAgrawala;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.simgrid.msg.*;
/**
 *
 * @author herrmann
 */


public class RA extends org.simgrid.msg.Process {
    private int timestamp = -1;  
    private ArrayList<Integer> requestList;
    boolean requesting = false;
    private int numProcessos;
    
    public RA(Host host, String name, String[] args) { //construtor
	super(host, name, args); //herança de .process
        this.requestList = new ArrayList<>(numProcessos);
        try {
            System.setOut(new PrintStream("ra.log"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(RA.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void log(int from, int to, char evento, String msg) {
        System.out.printf("%.3f %c %d %d %s\n", Msg.getClock(), evento, from, to, msg);
    }
    
    private void logGeral(int p, String msg) {
        System.out.printf("%.3f %d %s\n", Msg.getClock(), p, msg);
    }
    
    @Override
    public void main(String[] arg0) { 
        if(getPID() == 1){
            timestamp++;
            
            for(int i = 1; i <= getCount(); i++) {
                if(i != getPID()){
                    Task t = new RequestTask(timestamp);
                    t.isend("node-" + i);
                    log(i, this.getPID(), 's', t.getName());
                }
            }
            
            for(int i = 1; i < getCount(); i++){
                ReplyTask t1;
                //System.out.println(Msg.getClock()+ "entrou no for");
                try {
                    t1 = (ReplyTask) Task.receive("node-1");
                    log(t1.getSender().getPID(), getPID(), 'r', t1.getName());
                    try {
                        t1.execute();
                    } catch (TaskCancelledException ex) {
                        System.out.println("Erro na execução do REPLY: "+ex.getMessage());
                    }
                } catch (TransferFailureException | HostFailureException | TimeoutException ex) {
                    System.out.println("Erro no recebimento do REPLY: "+ex.getMessage());
                }
                
            }
           logGeral(this.getPID(), "acabou");
        } else {
            RequestTask t2;
            try {
                t2 = (RequestTask) Task.receive("node-" + getPID());
                log(t2.getSender().getPID(), getPID(), 'r', t2.getName());
                try {
                    t2.execute();
                } catch (TaskCancelledException ex) {
                    System.out.println("Erro na execução do REQUEST: "+ex.getMessage());
                }
                ReplyTask t = new ReplyTask();
                t.send("node-" + t2.getSender().getPID());
                log(getPID(), t2.getSender().getPID(), 's', t.getName());
                                 
            } catch (TransferFailureException | HostFailureException | TimeoutException | NativeException ex) {
                Logger.getLogger(RA.class.getName()).log(Level.SEVERE, null, ex);
            }            
        } 
    }
}
    
