
export SPARQL_ENDPOINT=http://smw.coopaxis.fr/openrdf-sesame/repositories/smw-coopaxis
export SPARQL_QUERY_FILE=../gephi-smw-coopaxis-requete-sparql-v2.rq
export OUTPUT_FILE=$1
export GEXF_OUTPUT=smw-coopaxis.gexf

java -jar gephi-automate.jar $SPARQL_ENDPOINT $SPARQL_QUERY_FILE $OUTPUT_FILE $GEXF_OUTPUT