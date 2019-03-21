VERSION ?= 2.0.0

clean:
	rm -fr build

package-ssm:
	docker-compose -f infra/build/docker-compose.build-ssm.yml run package-chaincode