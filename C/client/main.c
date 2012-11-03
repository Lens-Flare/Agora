#include <sys/types.h>
#include <sys/socket.h>
#include <sys/time.h>
#include <netinet/in.h>
#include <netdb.h>
#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>

void error(const char *msg) {
	perror(msg);
	exit(1);
}

int main(int argc, char *argv[]) {
	int fd = socket(AF_INET, SOCK_DGRAM, 0);
	if (fd < 0)
		error("socket failed");
	
	struct sockaddr_in dest;
	dest.sin_family = AF_INET;
	dest.sin_port = 61803;
	
	struct hostent *host;
	host = gethostbyname("chat.firelizzard.com");
	if (!host)
		error("hostname lookup failed");
	
	memcpy(&dest.sin_addr, host->h_addr, host->h_length);
	
	char buffer[256];
	do {
		int bytes;
		scanf("%255s%n", buffer, &bytes);
		buffer[bytes] = '\0';
		
		int sent = sendto(fd, buffer, bytes, 0, (struct sockaddr*)&dest, sizeof(dest));
	} while (strcmp(buffer, "logoff"));
	
	close(fd);
	
	return 0;
}
