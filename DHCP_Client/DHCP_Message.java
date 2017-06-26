

import java.net.*;
import java.util.Enumeration;
import java.util.Random;
import java.lang.Math;

public class DHCP_Message {

	public byte op;
	public byte htype;
	public byte hlen;
	public byte hops;
	public byte[] xid = new byte [4];
	public byte[] secs = new byte [2];
	public byte[] flags = new byte[2];
	public byte[] ciaddr = new byte[4];
	public byte[] yiaddr = new byte[4];                     
	public byte[] siaddr = new byte[4]; 
	public byte[] giaddr = new byte[4]; 
	public byte[] chaddr = new byte[16]; 
	public byte[] sname = new byte[64]; 
public static byte[] magic_cookie = new byte[4];
	public byte getOp() {
		return op;
	}


	public void setOp(byte op) {
		this.op = op;
	}


	public byte getHtype() {
		return htype;
	}


	public void setHtype(byte htype) {
		this.htype = htype;
	}


	public byte getHlen() {
		return hlen;
	}


	public void setHlen(byte hlen) {
		this.hlen = hlen;
	}


	public byte getHops() {
		return hops;
	}


	public void setHops(byte hops) {
		this.hops = hops;
	}


	public byte[] getXid() {
		return xid;
	}


	public void setXid(byte[] xid) {
		this.xid = xid;
	}


	public byte[] getSecs() {
		return secs;
	}


	public void setSecs(byte[] secs) {
		this.secs = secs;
	}


	public byte[] getFlags() {
		return flags;
	}


	public void setFlags(byte[] flags) {
		this.flags = flags;
	}


	public byte[] getCiaddr() {
		return ciaddr;
	}


	public void setCiaddr(byte[] ciaddr) {
		this.ciaddr = ciaddr;
	}


	public byte[] getYiaddr() {
		return yiaddr;
	}


	public void setYiaddr(byte[] yiaddr) {
		this.yiaddr = yiaddr;
	}


	public byte[] getSiaddr() {
		return siaddr;
	}


	public void setSiaddr(byte[] siaddr) {
		this.siaddr = siaddr;
	}


	public byte[] getGiaddr() {
		return giaddr;
	}


	public void setGiaddr(byte[] giaddr) {
		this.giaddr = giaddr;
	}


	public byte[] getChaddr() {
		return chaddr;
	}


	public void setChadrr(byte[] chaddr) {
		this.chaddr = chaddr;
	}


	public byte[] getSname() {
		return sname;
	}


	public void setSname(byte[] sname) {
		this.sname = sname;
	}


	public byte[] getFile() {
		return file;
	}


	public void setFile(byte[] file) {
		this.file = file;
	}


	public byte[] getOptions() {
		return options;
	}


	public void setOptions(byte[] options) {
		this.options = options;
	}


	public byte[] file = new byte[128]; 
	public byte[] options;
	
	
	public DHCP_Message()
	{
		
	}
	
	public static short interpret(byte[] buf)
	{
		
		int i = 236;
		
		for (; i<240;i++)
		{
			if (buf[i] != DHCP_Message.magic_cookie[i-236]) return 0;
		}
		
		while(buf[i]!= 53)
		{
			i++;
			i+=buf[i];
			i++;
		}
		
		i+=2;
	
		return buf[i];
		
	
		
	}

	public byte[] DHCPRELEASE(byte[] clientaddr, byte[] servaddr, byte[] chaddr)
	{
		op = (byte)1;
		this.htype = (byte)1;
		this.hlen = (byte)6;
		hops= (byte)0;
		Random x= new Random();
		x.nextBytes(xid);
		for (int i=0; i<2 ; i++)
		{
			this.secs[i] = (byte)0;
		}
		
		
		for (int i=0; i<2 ; i++)
		{
			flags[i] =(byte)0;
		}
		
		
		ciaddr = clientaddr;
		
		for (int i=0; i<4 ; i++)
		{
			yiaddr[i]=(byte)0;
		}
		
		
			siaddr = servaddr;
		
		
		for (int i=0; i<4 ; i++)
		{
			giaddr[i]=(byte)0;
		}
		
		for (int i=0; i<64 ; i++)
		{
			sname[i]=(byte)0;
		}
		
		for (int i=0; i<128 ; i++)
		{
			file[i]=(byte)0;
		}
		
		
	
		
		options = new byte[4];
		options[0] =(byte) 53;
		options[1] = (byte)1;
		options[2] = (byte)7;
	options[3] = (byte) 255;
	
			
		byte[] ret = new byte[244];
		ret[0] = op;
		ret[1] = htype;
		ret[2] = hlen;
		ret[3] = hops;
		int j=4;
		for (int i = 0; i<4 ; i++, j++)
		{
			ret[j] = xid[i];
		}
		for (int i = 0; i<2 ; i++, j++)
		{
			ret[j] = secs[i];
		}
		
		for (int i = 0; i<2 ; i++, j++)
		{
			ret[j] = flags[i];
		}	
		
		for (int i = 0; i<4 ; i++, j++)
		{
			ret[j] = ciaddr[i];
		}	
		
		for (int i = 0; i<4 ; i++, j++)
		{
			ret[j] = yiaddr[i];
		}	
		for (int i = 0; i<4 ; i++, j++)
		{
			ret[j] = siaddr[i];
		}	
		for (int i = 0; i<4 ; i++, j++)
		{
			ret[j] = giaddr[i];
		}	
		
		for (int i = 0; i<16 ; i++, j++)
		{
			
			if ( i<hlen)
			ret[j] = chaddr[i];
			else ret[j] = 0;
		}	
		for (int i = 0; i<64 ; i++, j++)
		{
			ret[j] = sname[i];
		}	
		for (int i = 0; i<128 ; i++, j++)
		{
			ret[j] = file[i];
		}	
		for (int i = 0; i<4 ; i++, j++)
		{
			ret[j] = magic_cookie[i];
		}	
		
		
		for (int i = 0; i<options.length ; i++, j++)
		{
			ret[j] = options[i];
		}	
		return ret;
	}
	public byte[] DHCPDISCOVER(byte[] MAC)
	{
		op = (byte)1;
		this.htype = (byte)1;
		this.hlen = (byte)6;
		hops= (byte)0;
		Random x= new Random();
		x.nextBytes(xid);
		for (int i=0; i<2 ; i++)
		{
			this.secs[i] = (byte)0;
		}
		
		
		for (int i=0; i<2 ; i++)
		{
			flags[i] =(byte)0;
		}
		
		
		for (int i=0; i<4 ; i++)
		{
			ciaddr[i]=(byte)0;
		}
		
		for (int i=0; i<4 ; i++)
		{
			yiaddr[i]=(byte)0;
		}
		
		for (int i=0; i<4 ; i++)
		{
			siaddr[i]=(byte)0;
		}
		
		for (int i=0; i<4 ; i++)
		{
			giaddr[i]=(byte)0;
		}
		
		for (int i=0; i<64 ; i++)
		{
			sname[i]=(byte)0;
		}
		
		for (int i=0; i<128 ; i++)
		{
			file[i]=(byte)0;
		}
		
		
	
		
		options = new byte[4];
		options[0] =(byte) 53;
		options[1] = (byte)1;
		options[2] = (byte)1;
	options[3] = (byte) 255;
	
			
		byte[] ret = new byte[244];
		ret[0] = op;
		ret[1] = htype;
		ret[2] = hlen;
		ret[3] = hops;
		int j=4;
		for (int i = 0; i<4 ; i++, j++)
		{
			ret[j] = xid[i];
		}
		for (int i = 0; i<2 ; i++, j++)
		{
			ret[j] = secs[i];
		}
		
		for (int i = 0; i<2 ; i++, j++)
		{
			ret[j] = flags[i];
		}	
		
		for (int i = 0; i<4 ; i++, j++)
		{
			ret[j] = ciaddr[i];
		}	
		
		for (int i = 0; i<4 ; i++, j++)
		{
			ret[j] = yiaddr[i];
		}	
		for (int i = 0; i<4 ; i++, j++)
		{
			ret[j] = siaddr[i];
		}	
		for (int i = 0; i<4 ; i++, j++)
		{
			ret[j] = giaddr[i];
		}	
		
		for (int i = 0; i<16 ; i++, j++)
		{
			
			if ( i<hlen)
			ret[j] = MAC[i];
			else ret[j] = 0;
		}	
		for (int i = 0; i<64 ; i++, j++)
		{
			ret[j] = sname[i];
		}	
		for (int i = 0; i<128 ; i++, j++)
		{
			ret[j] = file[i];
		}	
		for (int i = 0; i<4 ; i++, j++)
		{
			ret[j] = magic_cookie[i];
		}	
		
		
		for (int i = 0; i<options.length ; i++, j++)
		{
			ret[j] = options[i];
		}	
		return ret;
	}
	
	
	public byte[] DHCPACK(byte[] p, byte[] subnet)
	{
		op = (byte)2;
		byte[] ret = new byte[250];
			
		
	
			
		
		ret[0] = op;
		ret[1] = p[1];
		ret[2] = p[2];
		ret[3] = p[3];
		int j=4;
		for (int i = 0; i<4 ; i++, j++)
		{
			ret[j] = p[j];
		}
		for (int i = 0; i<2 ; i++, j++)
		{
			ret[j] = p[j];
		}
		
		for (int i = 0; i<2 ; i++, j++)
		{
			ret[j] = p[j];
		}	
		
		for (int i = 0; i<4 ; i++, j++)
		{
			ret[j] = p[j];
		}	
		
		for (int i = 0; i<4 ; i++, j++)
		{
			ret[j] = p[j];
		}	
		for (int i = 0; i<4 ; i++, j++)
		{
			ret[j] = p[j];
		}	
		for (int i = 0; i<4 ; i++, j++)
		{
			ret[j] = p[j];
		}	
		
		for (int i = 0; i<16 ; i++, j++)
		{
			ret[j] = p[j];
		}	
		
		
		for (int i = 0; i<64 ; i++, j++)
		{
			ret[j] = p[j];
		}	
		for (int i = 0; i<128 ; i++, j++)
		{
			ret[j] = p[j];
		}	
		for (int i = 0; i<4 ; i++, j++)
		{
			ret[j] = magic_cookie[i];
		}	
		for (int i = 0; i< 2  ; i++, j++)
		{
			ret[j] = p[j];
		}	
		ret[j]= (byte) 5;
		j++;
		ret[j]= (byte)1;
		j++;
		ret[j]= (byte)4;
		j++;
		for (int i =0;i<4; i++, j++)
		{
			ret[j]= subnet[i];
		}
		
		
		
		ret[j] = (byte) 255;
		
		
		return ret;
}
	
	
	
	
	
	
	public byte[] DHCPREQUEST(byte[] p, byte[] secs)
	{
		op = (byte)1;
		byte[] ret = new byte[246];
			
		
	
			
		
		ret[0] = op;
		ret[1] = p[1];
		ret[2] = p[2];
		ret[3] = p[3];
		int j=4;
		for (int i = 0; i<4 ; i++, j++)
		{
			ret[j] = p[j];
		}
		for (int i = 0; i<2 ; i++, j++)
		{
			ret[j] = secs[i];
		}
		
		for (int i = 0; i<2 ; i++, j++)
		{
			ret[j] = p[j];
		}	
		
		for (int i = 0; i<4 ; i++, j++)
		{
			ret[j] = p[j];
		}	
		
		for (int i = 0; i<4 ; i++, j++)
		{
			ret[j] = p[j];
		}	
		for (int i = 0; i<4 ; i++, j++)
		{
			ret[j] = p[j];
		}	
		for (int i = 0; i<4 ; i++, j++)
		{
			ret[j] = p[j];
		}	
		
		for (int i = 0; i<16 ; i++, j++)
		{
			ret[j] = p[j];
		}	
		
		
		for (int i = 0; i<64 ; i++, j++)
		{
			ret[j] = p[j];
		}	
		for (int i = 0; i<128 ; i++, j++)
		{
			ret[j] = p[j];
		}	
		for (int i = 0; i<4 ; i++, j++)
		{
			ret[j] = magic_cookie[i];
		}	
		for (int i = 0; i< 2  ; i++, j++)
		{
			ret[j] = p[j];
		}	
		ret[j]= (byte) 3;
		j++;
		
		ret[j] = (byte) 255;
		
		
		return ret;
}
	
	
	
	public byte[] DHCPOFFER(byte[] p, byte[] yiaddr, byte[] siaddr)
	{
		op = (byte)2;
		byte[] ret = new byte[246];
			
		
	
			
		
		ret[0] = op;
		ret[1] = p[1];
		ret[2] = p[2];
		ret[3] = p[3];
		int j=4;
		for (int i = 0; i<4 ; i++, j++)
		{
			ret[j] = p[j];
		}
		for (int i = 0; i<2 ; i++, j++)
		{
			ret[j] = p[j];
		}
		
		for (int i = 0; i<2 ; i++, j++)
		{
			ret[j] = p[j];
		}	
		
		for (int i = 0; i<4 ; i++, j++)
		{
			ret[j] = p[j];
		}	
		
		for (int i = 0; i<4 ; i++, j++)
		{
			ret[j] = yiaddr[i];
		}	
		for (int i = 0; i<4 ; i++, j++)
		{
			ret[j] = siaddr[i];
		}	
		for (int i = 0; i<4 ; i++, j++)
		{
			ret[j] = p[j];
		}	
		
		for (int i = 0; i<16 ; i++, j++)
		{
			ret[j] = p[j];
		}	
		
		
		for (int i = 0; i<64 ; i++, j++)
		{
			ret[j] = p[j];
		}	
		for (int i = 0; i<128 ; i++, j++)
		{
			ret[j] = p[j];
		}	
		for (int i = 0; i<4 ; i++, j++)
		{
			ret[j] = magic_cookie[i];
		}	
		for (int i = 0; i< 2  ; i++, j++)
		{
			ret[j] = p[j];
		}	
		ret[j]= (byte) 2;
		j++;
		
		ret[j] = (byte) 255;
		
		
		return ret;
}
	static
	{
		magic_cookie[0]=(byte)99;
		magic_cookie[1]=(byte) 130;
		magic_cookie[2]=(byte)83;
		magic_cookie[3]=(byte)99;
	}
	
}
