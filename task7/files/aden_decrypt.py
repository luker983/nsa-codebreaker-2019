from Crypto.PublicKey import RSA
from Crypto.Cipher import AES
from Crypto.Cipher import PKCS1_v1_5
import base64
import sys
from Crypto import Random

priv_path = 'backdoor/users/aden/priv.pem'

# (key, message, iv)
msg = [
("MmIIV+Wfi7q/xQJiqbzHAe4+sEIyqAT0Y2KrSQ6ncY1ZcfU2c42l8DDX6krip2CsL77poyKWWccq6yh6ttNtM6fpnWcvRzRyIxdj5hSfFA3HcsRBkK/FTjpYrgb48PQbJZD4cKochPH/a5sm6d+M79LFA4h5X3EiSpG8vCE7ucBbxRRVVvQjsVGTxNZJLaLdyREmyZbPFpLiGEgdqFwybvYD8hojeswumgDtpICU9VGiLqkBp60vyBjArUY9ceklfNnb80fcJH3sW/3+CUnLbKCUV0/pmSuVmV0WVc4j7PEvUPaZfjGCnj8QGHfEA6rEb7VtTvhmNRK/h10vHQrAZw==", "ZDPQ00LJpj4QD5gCI87wDADo/klY86dBvR810oE4cYUI4znFQi89yD4aG08tUhxEX5cAxHn7BiiUlJFGDvROhcn/2ZnQjo0z3M3BLB12FPlg+H1Djw8YmBjMKwVs2M7qD9fzxi9aKSQu8FWObdoroIcWkIRFcW+L+Iz11rXs/SXoEvBafdjRcSIfMOA6hN7d4Y6N+Y3bd/iHniMCQOO/4NmmUeJ3YrJ3hkg/Mx/tsgxwmHs72yVRmm5St1IcrESVrvgfTyqAsIONxag2a6doUEpFRuRT5a9LCkrTVbZh/f385SVn0Ps2gh1QSGYL5FSw", "CXcCNPZjI1Bf3pDbKqXfWQ=="),
("D6ihRS4q1EWagjQj7+LwvZTQM4LPOcWv3phyrzVjrt6k7qpCoLgNlk21ktKwQGyNJx7Zgc+aaT6EntgcPjzi/mWzcwpi9O2x99QGYlcubWwmZoMQJABI9iMjPBsx2Z+RO6ODOzg/YlugBAflf2C/wHyoIGy7mkJPXg54piVoA0HQ1WTUwg1DNj+RW1OhnFB0fpMcPZI+5ZXrdOXz3yv24AOkjV7aUNe6zj4Swy9j/ajZ8qm5jINGOf6k452j73bDuvOZ8d1plhf3b38uP4gqDRB07Xi8q4WpDNsyaz2P2cY7evNr37jLH25TZK+fHSjlOJ6yAgeUXrJCmgy8PYOW6g==", "VitUGNGwlmcXMulqD+niueHgNTiu5ED0o6B49oTazgr8EGVl6/nlWGpOzXBENA4oJ+whXeanEvmiuJgqqfG8Pc2gJbS+v9zzHbWOZ6DdKz7Vva4jWz5YAdulGc2dpIlsGkNYLW/n7fdmh0lSEm/f+3GkhL5Nb9/yYNoa/W4rWpHdOBbbjkEidW/Tw4zWH0NAOuZ8pfRsbRO7aLhV2xXIqLpMzPkcaxGD3WYmVtt6Vx4SOtpuRGR/yLwCvsHbpCAMK0DU7w8/J3qeUDMxfHVWrQ==", "+hCrHXnhbk41bX7VR+6KYw=="),
("FImDB4C6+gpoPfMKgR1pGTeZQALMqmgE8OXLztAjHv2ihor0CcfTeLhywwCLmWaCts4IlVnsoSc+vXkNMxC41/w031P9/bgaj/8OcjVPh1XgLt/bB0GpkGiWKOnaWAbcu3SLwK+5w3AsCRtc3YL0s+LHZ29i708EX8SIU4vcUqEzh5rsSU9LCvBPtMEFzrPAvEal2u9CXrCiZupSbsCKzpSac6Jdiv7bgbLDHOHRfxpVuHG9HHXnykeVp1JXkIKp9nAsN/s8letu5OEaKiKldrjmwtvEh24Hx6wN0eNYl3qPulQRn5f65g/jIhZC5/jnqzfeJRWAeFKdGIUp0PLauA==", "gBQ0iJh8+nN0+D+zJjLkhvWkP2GdPYL/OSMJ0CnwTMLz+Rc4xbT5GHKk3vGkEOIIwNbpuqxoTlwFD/BKg5i/JObiD/sqtnR9p1jJ3Q1j/+QnrKwafZGhxl7BiTS1ND15iLUmNn+UYEhOPikGKxverVdJVO6Z5xthdoXr336Ofvkat2w+gqiSXk39DXuRyfkmKxWIkLZX8TUS4LONwmATrA1/6VKqWXQvG3/0xs+j7dz7NIs6StTEcWXNsXfoPpjlikLiCUWCDsPda07bO/N99w==", "cRVKnqqAImAjmCCbQ9cP7A==")
]

with open(priv_path, 'rb') as priv:
    priv = RSA.importKey(priv.read())

for m in msg:
    key = base64.b64decode(m[0])
    msg = base64.b64decode(m[1])
    iv = base64.b64decode(m[2])

    cipher = PKCS1_v1_5.new(priv)
    sentinel = b'fail'
    key = cipher.decrypt(key, "Error")
    # print("AES key:", base64.b64encode(key).decode("utf-8"))

    aes = AES.new(key, AES.MODE_CBC, iv)
    x = aes.decrypt(msg)
    print(x.decode("utf-8"))
#p = aes.decrypt(msg)
#print(p)
