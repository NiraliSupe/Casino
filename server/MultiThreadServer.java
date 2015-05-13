package server;
import java.io.*;
import java.net.*;

public class MultiThreadServer implements Runnable {
   private Login login;
   private ServerSocket srvr ;
   MultiThreadServer(ServerSocket srvr, Login login) {
	   this.srvr = srvr;
	   this.login = login;
   }

  public void run() {
	  String data = "";
      try {
    	    Socket skt = srvr.accept();
            PrintWriter out = new PrintWriter(skt.getOutputStream(), true);
            data = login.getEmail().getText();
            out.print(data);
            out.close();
      }
      catch(Exception e) {
            e.printStackTrace();
      }
   }
}