#include <sys/types.h>
#include <sys/socket.h>
#include <sys/time.h>
#include <netinet/in.h>
#include <strings.h>
#include <netdb.h>
#include <stdlib.h>
#include <stdio.h>
#include <arpa/inet.h>

struct socket_t {
	int fd;
	struct sockaddr_in addr;
};

void error(const char *);
void setup(struct socket_t *, const char *, unsigned short);
void serve(struct socket_t *);

void out(const char *msg) {
	printf(msg);
}

void error(const char *msg) {
	perror(msg);
	exit(1);
}

int main(int argc, char *argv[]) {
	struct socket_t sock;
	setup(&sock, argv[1], 61803);
	serve(&sock);
	close(sock.fd);
	return 0;
}

void setup(struct socket_t *sock, const char *hostname, unsigned short port) {
	sock->fd = socket(AF_INET, SOCK_DGRAM, 0);
	if (sock->fd < 0)
		error("Socket failed\n");
	
	struct sockaddr_in addr;
	addr.sin_family = AF_INET;
	addr.sin_port = port;
	
	struct hostent *host;
	host = gethostbyname(hostname);
	if (!host)
		error("hostname lookup failed");
	
	memcpy(&addr.sin_addr.s_addr, host->h_addr, host->h_length);
	
	int res = bind(sock->fd, (struct sockaddr *)&addr, sizeof(addr));
	if (res < 0)
		error("bind failed");
	
	int len = sizeof(sock->addr);
	res = getsockname(sock->fd, (struct sockaddr *)&sock->addr, &len);
	if ( res < 0 )
		error("failed to get socket name");
}

void serve(struct socket_t *sock) {
	char buffer[256];
	do {
		int len = sizeof(sock->addr);
		int bytes = recvfrom(sock->fd, buffer, sizeof(buffer)-1, 0, &(sock->addr), &len);
		buffer[bytes] = '\0';
		printf("%s: %s\n", inet_ntoa(sock->addr.sin_addr), buffer);
	} while (strcmp(buffer, "logoff"));
}
