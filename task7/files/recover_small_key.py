# use factors of 256 bit RSA key to generate private key

import rsa
import math
from Crypto.PublicKey import RSA

with open('backdoor/rsa256/rsa256.pem', 'rb') as pub:
    k = pub.read()
    k = rsa.PublicKey.load_pkcs1_openssl_pem(k)

print("{0:#0{1}x}".format(k.n,  66))
print(k.n)

n = k.n
e = k.e
p = 308585696759553472827425867257848567119
q = 319664591303122965348737822041986095747
output_file = 'backdoor/rsa256/rsa256_priv.pem'

def egcd(a, b):
	    x,y, u,v = 0,1, 1,0
	    while a != 0:
	        q, r = b//a, b%a
	        m, n = x-u*q, y-v*q
	        b,a, x,y, u,v = a,r, u,v, m,n
	    gcd = b
	    return gcd, x, y

def modinv(a, m):
    gcd, x, y = egcd(a, m)
    if gcd != 1:
        return None  # modular inverse does not exist
    else:
        return x % m

phi = (p-1) * (q-1)
d = modinv(e, phi)
dp = modinv(e, (p-1))
dq = modinv(e, (q-1))
qi = modinv(q, p)

params = (n, e, d, p, q)
key = RSA.construct(params)

with open(output_file, 'wb') as priv:
    priv.write(key.exportKey())
