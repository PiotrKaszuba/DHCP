import java.io.BufferedReader;
import java.util.Scanner;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Random;
public class Client_Proc implements Runnable {
DatagramSocket socket;
DatagramPacket p;
DatagramPacket p2;
byte[] buf;

public byte[] broadcast;
public byte[] IP;
public byte[] subnet;
public byte[] MAC;
public byte[] secs;
public byte[] servaddr;
public long time;
public DHCP_Message dhcp;


public Client_Proc() throws UnknownHostException, SocketException
{
	

	

	
	IP = new byte[4];
    servaddr = new byte[4];
	subnet = new byte[4];
	secs = new byte[2];
	MAC = new byte[6];
	broadcast = new byte[4];
	for (int i =0; i <4 ; i++)
	{
		broadcast[i] = (byte) 0xFF;
	}
	Random x = new Random();
	x.nextBytes(MAC);
	
	
	dhcp = new DHCP_Message();
	 buf = new byte[548];
	
	
	 
	 
	 
}
	@Override
	public synchronized  void run() {
		
		
			
			
				
				
					try {
						buf = dhcp.DHCPDISCOVER(MAC);
						time = System.currentTimeMillis();
						socket = new DatagramSocket(10205);
						 p2 = new DatagramPacket(buf,buf.length, Inet4Address.getByAddress(broadcast), 10206);
						socket.send(p2);
						socket.close();
						boolean mymessage = false;
						while(!mymessage){
						socket = new DatagramSocket(10205);
						socket.setSoTimeout(3000);
						 p = new DatagramPacket(buf, buf.length);
						socket.receive(p);
						
					
						switch (dhcp.interpret(buf))
						{
						case 2:
							if(dhcpoffer(buf))
								mymessage=true;
							break;
						case 5:
							if(dhcpack(buf))
								mymessage=true;
							break;
						}
						socket.close();
						}
						socket = new DatagramSocket(10205); 
						p2 = new DatagramPacket(buf,buf.length, Inet4Address.getByAddress(broadcast), 10206);
						socket.send(p2);
						socket.close();
						 mymessage = false;
						while(!mymessage){
						socket = new DatagramSocket(10205);
						socket.setSoTimeout(3000);
						buf = new byte[250];
						 p = new DatagramPacket(buf, buf.length);
						
						socket.receive(p);
						socket.close();
						switch (dhcp.interpret(buf))
						{
						case 2:
							
							if(dhcpoffer(buf))
								mymessage=true;
							break;
						case 5:
							if(dhcpack(buf))
								mymessage=true;
							break;
						}
						
						
						socket.close();
						}
						
						
						
						
						Scanner reader = new Scanner(System.in);
						System.out.println("\nType /release to relinquish IP");
						
						
						while(!reader.nextLine().equals("/release"))
						{
							 
						}
						buf=dhcp.DHCPRELEASE(IP, servaddr, MAC);
						socket = new DatagramSocket(10205);
						
						
						p2 = new DatagramPacket(buf,buf.length,Inet4Address.getByAddress(broadcast), 10206);
						socket.send(p2);
					    socket.close();
					   
						
						
					}
						catch (SocketTimeoutException e) {
							// TODO Auto-generated catch block
							System.out.println("Serwer nie odpowiada");
						}
					 catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
				
				
				
			
			
			
	
	}
	
	
	
	private synchronized boolean dhcpoffer(byte[] buf)
	{
		for (int i=0;i<6;i++)
		{
			if(buf[i+28]!=MAC[i]) return false;
		}
		
		
		
		
		secs[0] = (byte) (((System.currentTimeMillis()-time)/1000)/256);
		secs[1] = (byte) (((System.currentTimeMillis()-time)/1000)%256);
		
		this.buf = dhcp.DHCPREQUEST(buf, secs );
		return true;
	}
	
	private synchronized boolean dhcpack(byte[] buf)
	{
		for (int i=0;i<6;i++)
		{
			if(buf[i+28]!=MAC[i]) return false;
		}
		
		
		char[] h = new char[4];
		for (int i =0; i<4; i++)
		{
			IP[i] = buf[i+16];
			h[i]= (char) IP[i];
		}
		
		for (int i =0; i<4; i++)
		{
			subnet[i] = buf[i+245];
		}
		
		for (int i =0; i<4; i++)
		{
			servaddr[i] = buf[i+20];
		}
		
		
		for (int i=0;i<4;i++)
		{
			
			broadcast[i] = (byte) (IP[i] | (~subnet[i]));
			System.out.println((int) broadcast[i]);
		}
		System.out.println("Your IP: ");
		for (int i =0; i<4; i++){
			System.out.print( h[i] & 0xFF );
			if (i<3)
			{
				System.out.print(".");
			}
		}
		return true;
	}
	
}
