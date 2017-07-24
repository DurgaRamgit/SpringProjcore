import java.io.DataInputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;

public class Server
{
    public static void main(String[] args)
    {
       ArrayList<Socket> sockets=new ArrayList<Socket>();
       ArrayList<SocketAddress> str= new ArrayList<SocketAddress>();
       ServerSocket s=null;
        try
        {
        	 s=new ServerSocket(4481);
        	System.out.println("Server Start Waiting for Client Request....");
            while(true)
            {
                Socket sock=s.accept();
                sockets.add(sock);
            //  System.out.println("sockets:"+sockets);
              // int i= sockets.size();
               
               SocketAddress sa=sock.getRemoteSocketAddress();
                 
                 str.add(sa);
                 //int t=str.size();
                // System.out.println(str);
                SThread sendt=new SThread(sockets);
                //System.out.println("S:"+sendt);
                RThread recvt=new RThread(sock,sockets,str);
                //System.out.println("S:"+recvt);
                sendt.start();
                recvt.start();
                
            }
        }
             
        catch(Exception e)
        {
            System.out.println("connectin not established:server");
        }
        finally{
        	try{        	
			if(s!=null)
        	{
        		s.close();
        	}
        	}
        	catch(Exception e){}
        }
         
    }
}

class SThread extends Thread
{
    Socket sock;
    ArrayList <Socket> sockets;
    DataInputStream in=new DataInputStream(System.in);
   // BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
     SThread(ArrayList <Socket> sockets)
    {
    	this.sockets=sockets;
    	
    }
    @Override
    public void run()
    {
        try
        {
            while(true)
           {
               @SuppressWarnings("deprecation")
               String line=in.readLine();
                if(!(line.equals("QUIT")))
                {
                    for(Socket s:sockets)
                    {
                    	PrintStream ps1=new PrintStream(s.getOutputStream());
                    	ps1.println("Server:"+line);
                    	//System.out.println("send for");
                    }
                }
                else
                    break; 
           }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }         
    }     
}
class RThread extends Thread
{
    Socket sock;
    ArrayList <Socket> sockets;
    ArrayList <SocketAddress> str;
    DataInputStream inp;
    RThread(Socket sock,ArrayList <Socket> sockets,ArrayList <SocketAddress> str)
    {
    	this.sock=sock;
    	this.sockets=sockets;
    	this.str=str;
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
            	@SuppressWarnings("deprecation")
				String line1=inp.readLine();
                if(!(line1.toUpperCase().equals("QUIT")))
                {
                	System.out.println(line1);
                	SocketAddress sa=sock.getRemoteSocketAddress();
                	
                    int in=str.indexOf(sa);
       				 
       				@SuppressWarnings("unchecked")
					ArrayList <Socket> sockets1=(ArrayList<Socket>) sockets.clone();
       				
       				sockets1.remove(in);
       				
      				for(Socket s:sockets1)
       				{
                    	PrintStream ps1=new PrintStream(s.getOutputStream());
                    	ps1.println(line1);
                    	
                    }
           
                }
                else
                {
                	System.out.println(line1);
            	SocketAddress sa=sock.getRemoteSocketAddress();
            	
                int in=str.indexOf(sa);
   				 
   				@SuppressWarnings("unchecked")
				ArrayList <Socket> sockets1=(ArrayList<Socket>) sockets.clone();
   				
   				sockets1.remove(in);
   				
  				for(Socket s:sockets1)
   				{
                	PrintStream ps1=new PrintStream(s.getOutputStream());
                	ps1.println(line1);
                	
                }
                	//break;
                	System.exit(0);

                }
            }
        //inp.close();
       // sock.close();
          
        }
        catch(Exception e)
        {
        	System.out.println("client socket closed");
        }
        
    }
}
 
