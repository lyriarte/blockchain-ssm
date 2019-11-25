NAME   	:= civisblockchain/ssm
IMG    	:= ${NAME}:${VERSION}
LATEST  := ${NAME}:latest

clean: clean-ssm

build: build-ssm

tag-latest: tag-latest-ssm

push: push-ssm

clean-ssm:
	rm -fr build

package-ssm:
	@docker-compose -f infra/build/docker-compose.build-ssm.yml run package-chaincode

build-ssm:
	docker build --build-arg VERSION=${VERSION} -f infra/build/Ssm_Dockerfile -t ${IMG} .

tag-latest-ssm:
	@docker tag ${IMG} ${LATEST}

push-ssm:
	@docker push ${NAME}

inspect-ssm:
	docker run -it ${IMG} sh

login-ssm:
	@docker log -u ${DOCKER_USER} -p ${DOCKER_PASS}