NAME   	:= civisblockchain/ssm
IMG    	:= ${NAME}:${VERSION}
LATEST  := ${NAME}:latest

clean:
	rm -fr build

package:
	@docker-compose -f infra/build/docker-compose.build-ssm.yml run package-chaincode

build:
	docker build --build-arg VERSION=${VERSION} -f infra/build/Ssm_Dockerfile -t ${IMG} .

tag-latest:
	@docker tag ${IMG} ${LATEST}

push:
	@docker push ${NAME}

inspect:
	docker run -it ${IMG} sh

login:
	@docker log -u ${DOCKER_USER} -p ${DOCKER_PASS}