# SSM example session

## Initialize SSM and session

```
rm session*
echo -n "{\"ssm\": \"Car dealership\", \"session\": \"deal20181201\", \"public\": \"Used car for 100 dollars.\", \"roles\": {\"Bob\": \"Buyer\", \"Sam\": \"Seller\"}}" > session.txt
rsa_sign ../agents/adam session.txt
b64_encode session.txt.dgst
cat session.txt.dgst.b64 | tr -d "\n" | sed 's/"/\\"/g ; s/^/"/ ; s/$/"/'
```

```
rm session*
echo -n "Sell" > session.txt
echo -n "{\"session\": \"deal20181201\", \"iteration\": 0}" >> session.txt
rsa_sign ../agents/sam session.txt
b64_encode session.txt.dgst
cat session.txt.dgst.b64 | tr -d "\n" | sed 's/"/\\"/g ; s/^/"/ ; s/$/"/'
```

```
rm session*
echo -n "Buy" > session.txt
echo -n "{\"session\": \"deal20181201\", \"iteration\": 1}" >> session.txt
rsa_sign ../agents/bob session.txt
b64_encode session.txt.dgst
cat session.txt.dgst.b64 | tr -d "\n" | sed 's/"/\\"/g ; s/^/"/ ; s/$/"/'
```
