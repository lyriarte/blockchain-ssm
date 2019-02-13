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

```
json_agent Bob bob.pub | jq . -cM | tr -d "\n" > bob.arg
rsa_sign adam bob.arg
b64_encode bob.arg.dgst
cat bob.arg.dgst.b64 | tr -d "\n" | sed 's/"/\\"/g ; s/^/"/ ; s/$/"/'
```

```
json_agent Sam sam.pub | jq . -cM | tr -d "\n" > sam.arg
rsa_sign adam sam.arg
b64_encode sam.arg.dgst
cat sam.arg.dgst.b64 | tr -d "\n" | sed 's/"/\\"/g ; s/^/"/ ; s/$/"/'
```
