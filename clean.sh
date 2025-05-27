echo "cleaning up containers"

docker container rm documents-database-1
docker container rm documents-index-1
docker container rm documents-message-broker-1

echo "cleaning up db image"
docker image rm documents-database:latest

echo "cleaning up file stores"

CONTENT_PATH=`awk 'BEGIN { FS="=" } /documents.file.content-path/ {print $2}' src/main/resources/application.properties`
echo "cleaning up content path ${CONTENT_PATH}"

RENDITION_PATH=`awk 'BEGIN { FS="=" } /documents.file.rendition-path/ {print $2}' src/main/resources/application.properties`
echo "cleaning up transform path ${RENDITION_PATH}"

TRANSFORM_PATH=`awk 'BEGIN { FS="=" } /documents.file.transform-path/ {print $2}' src/main/resources/application.properties`
echo "cleaning up transform path ${TRANSFORM_PATH}"

if [ -f "${CONTENT_PATH}/.documents-filestore" ];
then
  rm ${CONTENT_PATH}/* -r
  echo "cleaned content filestore"
else
  echo "content file store path was invalid: ${CONTENT_PATH}"
fi

if [ -f "${RENDITION_PATH}/.documents-filestore" ];
then
  rm ${RENDITION_PATH}/* -r
  echo "cleaned content filestore"
else
  echo "content file store path was invalid: ${RENDITION_PATH}"
fi

if [ -f "${TRANSFORM_PATH}/.documents-filestore" ];
then
  rm ${TRANSFORM_PATH}/* -r
  echo "cleaned content filestore"
else
  echo "content file store path was invalid: ${TRANSFORM_PATH}"
fi