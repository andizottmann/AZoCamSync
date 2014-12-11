telnetd -l  /bin/sh &
tcpsvd -E 0.0.0.0 21 ftpd -w /mnt/sd/ &