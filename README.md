# DHCP
Dynamic Host Configuration Protocol, client and server simulation in Java. Protocol generated from scratch, without many options. Works on UDP only in LAN. Main goal: correct Wireshark dump. Uses broadcast, does not support retries and partial timeouts. Sometimes configuration fails due to lack of automatic message resend. Does not use standard DHCP ports not to collide with real DHCP.

DHCP messages:
1) Discover
2) Offer
3) Request
4) Acknowledge
5) Release
