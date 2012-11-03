#include <openssl/rsa.h>
#include <openssl/pem.h>

const int kDefaultBits = 1024;
const int kDefaultExp = 65527;

int main(int argc, char *argv[]) {
	int bits = kDefaultBits;
	int exp = kDefaultExp;
	
	if (argc > 1)
		bits = atoi(argv[1]);
	if (argc > 2)
		exp = atoi(argv[2]);
	
	FILE *priv = fopen("id_rsa", "w");
	FILE *pub = fopen("id_rsa.pub", "w");
	
	RSA *rsa = RSA_generate_key(bits, exp, 0, 0);
	
	
	
	return 0;
}