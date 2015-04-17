# MusicFileSharing
File Sharing Application to share filenames among a distributed network using UDP and RPC.
Step 1 - run the bootstrap server.
	compile using gcc P2PRegistry.c
	run using ./a.out
Step 2 - run the file sharing system.
	for UDP mode type the following
	java -jar FileSharingSystem.jar serverip serverport nodeip nodeport username mode
	example
	java -jar FileSharingSystem.jar 192.168.1.2 2000 192.168.1.3 7000 abcdef12 0
	for RPC mode
	java -jar FileSharingSystem.jar 192.168.1.2 2000 192.168.1.3 7000 abcdef12 1
Step 3 - By completing step 2 a GUI will open. Follow actions on the GUI and close GUI 
	to close the application.
