# Iterates over every 6 digit pin possible and compares it with the goal SHA-256 hash

import itertools
import hashlib

g = "50b6b08d814a64c8c3f4951efc153235537f0801ae355d4c8c3a1bb92aef9fa4"

for c in itertools.product(range(10), repeat=6):
    x = ''.join(map(str, c))
    y = hashlib.sha256(x.encode()).hexdigest()
    if y == g:
        break

print(x)
