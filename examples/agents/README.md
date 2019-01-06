# SSM example agents

## RSA keys generation

```
rsa_keygen adam
rsa_keygen bob
rsa_keygen sam
```

## Use in peer command-line

```
json_agent Adam adam.pub | mkarg
```

```
echo -n "Adam the admin, Sam the seller and Bob the buyer." > message.txt
rsa_sign adam message.txt
b64_encode message.txt.dgst
cat message.txt.dgst.b64 | tr -d "\n" | sed 's/"/\\"/g ; s/^/"/ ; s/$/"/'
```
