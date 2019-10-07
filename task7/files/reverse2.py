# Exploit backdoor in TerrorTime
import rsa
import math
import sys
from Crypto.PublicKey import RSA
from Crypto.Cipher import PKCS1_OAEP
import base64
from math import sqrt; from itertools import count, islice
import random

priv_path = 'backdoor/rsa1024/'
pub_path = 'backdoor/users/%s/pub.pem'%(sys.argv[1])
bits = 2048
half = int(bits) // 2

def is_Prime(n):
    """
    Miller-Rabin primality test.
 
    A return value of False means n is certainly not prime. A return value of
    True means n is very likely a prime.
    """
    if n!=int(n):
        return False
    n=int(n)
    #Miller-Rabin test for prime
    if n==0 or n==1 or n==4 or n==6 or n==8 or n==9:
        return False
 
    if n==2 or n==3 or n==5 or n==7:
        return True
    s = 0
    d = n-1
    while d%2==0:
        d>>=1
        s+=1
    assert(2**s * d == n-1)
 
    def trial_composite(a):
        if pow(a, d, n) == 1:
            return False
        for i in range(s):
            if pow(a, 2**i * d, n) == n-1:
                return False
        return True  
 
    for i in range(8):#number of trials 
        a = random.randrange(2, n)
        if trial_composite(a):
            return False
 
    return True  

def rsa_convert(c, n, d):
    c = int.from_bytes(c, sys.byteorder)
    return pow(c, d, n)

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

# Open up the public key we wanna reverse
with open(pub_path, 'rb') as pub:
    pub = RSA.importKey(pub.read())

# Open up our known private keys
with open(priv_path + 'rsa%d_priv.pem'%(half), 'rb') as priv:
    priv = RSA.importKey(priv.read())

with open(priv_path + 'rsa%s_k1.txt'%(half), 'r') as k1:
    k1 = base64.b64decode(k1.read())

with open(priv_path + 'rsa%s_k2.txt'%(half), 'r') as k2:
    k2 = base64.b64decode(k2.read())

k2 = k2.hex()
k1 = k1.hex() 
n = hex(pub.n)[2:]
ch = half // 4
by = half // 8
hp = n[:-ch]
found = False

print("Searching for N:", pub.n)

print("KEY1 (Hex):", k1)
print("KEY2 (Hex):", k2)
print("ENCP (Hex):", hp)
print()

for i in range(0, 1000):
    x = (int(k2, 16) + i) ^ int(hp, 16)
    x = rsa_convert(x.to_bytes(by, sys.byteorder), priv.n, priv.d)
    x = x ^ int(k1, 16)
    if is_Prime(x):
        p = x
        q = pub.n // p
        if is_Prime(q):
            found = True
            print("i:", i)
            print("P:", p)
            print("Q:", q)
            print("N:", p * q)
            break

if found:
    n = p * q
    e = pub.e
    output = 'backdoor/users/%s/priv.pem'%(sys.argv[1])
    phi = (p-1) * (q-1)
    d = modinv(e, phi)
    params = (n, e, d, p, q)
    key = RSA.construct(params)
    with open(output, 'wb') as priv:
        priv.write(key.exportKey())
else:
    print("Not found")
