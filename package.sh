#!/bin/bash -e
THIS_DIR="$(basename $( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd ))"
TARBALL=${THIS_DIR}/${THIS_DIR}.tgz

echo "Executing tests and creating archive ${TARBALL}"

mvn -q test
mvn -q clean
pushd .. > /dev/null
tar zcf ${TARBALL} ${THIS_DIR}/pom.xml ${THIS_DIR}/src ${THIS_DIR}/package.sh ${THIS_DIR}/run-ws.sh ${THIS_DIR}/assignment.md
popd > /dev/null
