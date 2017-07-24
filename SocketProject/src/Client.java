
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketAddress;

public class Client
{
	static String name;
	static DataInputStream din;
	static DataOutputStream dout;
	
    public static void main(String[] args)throws Exception
    {
    	
    	String name;
    	System.out.print("Enter Client Name :");
    	BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
    	name=br.readLine();
    	  	
    
        try{
            Socket s=new Socket("localhost",4481);
           dout=new DataOutputStream(s.getOutputStream());
            //sending a message for login
            dout.writeBytes(name+":has Logged in \n");
            
            SocketAddress sa1=s.getRemoteSocketAddress();
          //  System.out.println("Socket address:"+sa1);
            SendThread sendt=new SendThread(s,name);
           // System.out.println("CS:"+sendt);
            RecvThread recv=new RecvThread(s);
          //  System.out.println("CR:"+recv);
            sendt.start();
            recv.start();
        }
        catch(Exception e)
        {
            System.out.println("connection not established:client");
        }
    }
}

class SendThread extends Thread
{
    
    String name;
   Socket s;
    BufferedReader kb= new BufferedReader(new InputStreamReader(System.in));
    DataOutputStream outp;
    SendThread(Socket s,String name)
    {       
        this.s=s;
        this.name=name;
    }
    @Override
    public void run()
    {
        try
        {
            while(true)
           {
               String line=kb.readLine();
           	   outp=new DataOutputStream(s.getOutputStream());
                if(!(line.toUpperCase().equals("QUIT")))
                {
                    	outp.writeBytes(name+" :"+line+"\n");
                }
                else 
                {
                	outp=new DataOutputStream(s.getOutputStream());
                	outp.writeBytes(name+": Loggedout");	
                	outp.close();
                	System.exit(1);
                	
                }
                
           }
            //outp.close();
           
        }
        catch(Exception e)
        {
            System.out.println("logout");
        }
         
    }
     
}
class RecvThread extends Thread
{
    Socket sock;
    DataInputStream inp;
    RecvThread(Socket sock)
    {
        this.sock=sock;
        try
        {
           inp=new DataInputStream(sock.getInputStream());
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
    @Override
    public void run()
    {
        try
        {
            while(true)
            {
                String line1=inp.readLine();
                if(line1==null)
                {
                        break;//System.exit(1);
                }
                else
                    System.out.println(line1);
                
                
            }
            inp.close();
            sock.close();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
}
