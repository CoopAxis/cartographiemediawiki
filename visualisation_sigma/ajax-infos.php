<?php

 error_reporting(E_ALL);
ini_set('display_errors', TRUE);
ini_set('display_startup_errors', TRUE);
include('functions.php');

$var = get_sparql_result('PREFIX owl:   <http://www.w3.org/2002/07/owl#>
PREFIX swivt:   <http://semantic-mediawiki.org/swivt/1.0#>
PREFIX wiki:  <http://smw.coopaxis.fr/id/>
PREFIX rdfs:  <http://www.w3.org/2000/01/rdf-schema#>
PREFIX schema:  <http://schema.org/>

SELECT ?propriete ?label
WHERE {
 <'.$_GET['query'].'> ?propriete ?label .
  FILTER(
  	!STRSTARTS(STR(?propriete), "http://semantic-mediawiki.org/swivt/1.0#")
  )
}');
//var_dump($var);
$elements = $var['results']['bindings'];
$neded_attributes = array(
'http://www.w3.org/1999/02/22-rdf-syntax-ns#type',
'http://www.w3.org/2000/01/rdf-schema#label',
'http://www.w3.org/2000/01/rdf-schema#isDefinedBy',
'http://schema.org/memberOf',
'http://schema.org/address',
'http://smw.coopaxis.fr/id/Attribut-3ACode_APE',
'http://schema.org/contactPoint',
'http://schema.org/legalName',
'http://smw.coopaxis.fr/id/Attribut-3ASiren',
'http://smw.coopaxis.fr/id/Attribut-3AStatus',
'http://smw.coopaxis.fr/id/Attribut-3AType',
'http://schema.org/url',
) ;
$infos = array();
foreach ($elements as $element) {
	
	if (in_array($element["propriete"]['value'], $neded_attributes) ) {
		$info = array(
			'propriete' => $element["propriete"]['value'],
			'label' => $element["label"]['value'],
			'type' => $element["label"]['type'],
		);
		$infos[] = $info ;
	}
}
$infos[] = array(
	'propriete' => 'fiche',
	'label' => $_GET['query'],
	'type' => 'url',
) ;
$array_global = array(
	'infos' => $infos
);
$return_json = json_encode($array_global );
$file = json_decode($return_json, false) ;
echo json_encode($file ) ;


?>