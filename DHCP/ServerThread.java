import java.net.DatagramSocket;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.Inet4Address;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ServerThread implements Runnable {


	DatagramSocket socket;
	DatagramPacket p;
	DatagramPacket p2;
	byte[] buf;
	
	

	public byte[] IP;
	public int numberofaddresses;
	public short NetworkPrefix;
	public boolean[] addressavailable;
	public byte[][] addressMAC;
	public byte[] subnet;
	private byte[] sent_ip;
	
    public DHCP_Message dhcp;
	
	
	public ServerThread() throws SocketException, UnknownHostException 
	{
		numberofaddresses = 5;
		sent_ip = new byte[4];
		subnet = new byte[4];
		addressMAC = new byte[numberofaddresses][6];
		dhcp = new DHCP_Message();
		IP = new byte[4];
		IP[0]=(byte)192;
		IP[1] = (byte)168;
		IP[2] = (byte)1;
		IP[3] = (byte) 1;
		
		NetworkPrefix = 24;
		addressavailable = new boolean[numberofaddresses];
		for (int i=0;i<numberofaddresses;i++)
		{
			addressavailable[i]=true;
			for (int j=0;j<6;j++)
			{
				addressMAC[i][j]=0;
			}
		}
		
		for (int i=0; i<NetworkPrefix; i++)
		{
			subnet[i/8]+= Math.pow(2, 7-i%8);
		}
		
		
		
	
		
	 buf = new byte[548];
		
	
	}
	@Override
	public synchronized void run() {
		
		
			while(true){
			
				try {
					socket = new DatagramSocket(10206);
					 p = new DatagramPacket(buf, buf.length);
					socket.receive(p);
					socket.close();
					
					socket = new DatagramSocket(10206);
					
					switch (dhcp.interpret(buf))
					{
					case 1:
						if( dhcpdiscover(buf))
						{
							
							 p2 = new DatagramPacket(buf,buf.length, Inet4Address.getByName("255.255.255.255"), 10205);
							for (int i=0;i<100000;i++);
							 socket.send(p2);
							
						}
						break;
					case 3:
					
						if(dhcprequest(buf))
						{
							 p2 = new DatagramPacket(buf,buf.length, Inet4Address.getByName("255.255.255.255"), 10205);
							 for (int i=0;i<100000;i++);
							 socket.send(p2);
							
						}
						break;
					case 7:
						dhcprelease(buf);
						break;
						
					}
					socket.close(); 
					
					
					
					
					
					
					
				
					
					
					
					
					
					
					
		
		
		
	
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
			}
}
	
	
	
	
	
	
	
	
	private synchronized boolean dhcpdiscover(byte[] buf)
	{
		
		int i=0;
		while(!addressavailable[i])
		{
			i+=1;
			if (i==numberofaddresses)
				return false;
		}
		
		sent_ip = IP.clone();
		sent_ip[3] += i +1;
		addressavailable[i]=false;
		for ( int j=0; j<6;j++)
		{
		addressMAC[i][j] = buf[j+28];
		}
		this.buf=dhcp.DHCPOFFER(buf, sent_ip, IP);
		
		return true;
	}
	
	private synchronized boolean dhcprequest(byte[] buf)
	{
		boolean x = false;
		for (int i=0; i<numberofaddresses; i++)
		{
			if(!addressavailable[i]){
			x= true;
			for (int j=0; j<6;j++)
			{
				if(buf[j+28]!=addressMAC[i][j]) x=false;
			}
			if (x) break;
			}
		}
		
		if (x)
		{
			this.buf=dhcp.DHCPACK(buf, subnet);
		}
		return x;
	}
	
	private synchronized void dhcprelease(byte[] buf)
	{
		boolean x = false;
		int i=0;
		for (; i<numberofaddresses; i++)
		{
			if(!addressavailable[i]){
			x= true;
			for (int j=0; j<6;j++)
			{
				if(buf[j+28]!=addressMAC[i][j]) x=false;
			}
			if (x) break;
			}
		}
		
		if (x)
		{
			addressavailable[i]=true;
		}
		
	}
	
}
