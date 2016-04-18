<?php

 error_reporting(E_ALL);
ini_set('display_errors', TRUE);
ini_set('display_startup_errors', TRUE);
include('functions.php');
/*
	exemple de données devant être renvoyées par le json
{
	edges: [
		{"source":"465","target":"79","id":"951"} 
	],
	"nodes": [
		{"label":"Sciences De La Terre","x":1412.2230224609375,"y":-2.055976390838623,"id":"262","color":"rgb(255,204,102)","size":8.540210723876953}
	],
}
	
*/
//$data_url = http_build_query (array('query' =>$_GET['query']));
$var = get_sparql_result('PREFIX owl:   <http://www.w3.org/2002/07/owl#>
PREFIX swivt:   <http://semantic-mediawiki.org/swivt/1.0#>
PREFIX wiki:  <http://smw.coopaxis.fr/id/>
PREFIX rdfs:  <http://www.w3.org/2000/01/rdf-schema#>
PREFIX schema:  <http://schema.org/>

SELECT ?x ?y
WHERE {
 ?x schema:memberOf ?y .
}');
$organisations_a  = get_sparql_result('PREFIX owl:   <http://www.w3.org/2002/07/owl#>
PREFIX swivt:   <http://semantic-mediawiki.org/swivt/1.0#>
PREFIX wiki:  <http://smw.coopaxis.fr/id/>
PREFIX rdfs:  <http://www.w3.org/2000/01/rdf-schema#>
PREFIX schema:  <http://schema.org/>

SELECT ?organisation ?label
WHERE {
 ?organisation rdf:type schema:Organization . 
 ?organisation rdfs:label ?label .
}');
$personnes_a  = get_sparql_result('PREFIX owl:   <http://www.w3.org/2002/07/owl#>
PREFIX swivt:   <http://semantic-mediawiki.org/swivt/1.0#>
PREFIX wiki:  <http://smw.coopaxis.fr/id/>
PREFIX rdfs:  <http://www.w3.org/2000/01/rdf-schema#>
PREFIX schema:  <http://schema.org/>

SELECT ?person ?label
WHERE {
 ?person rdf:type schema:Person . 
 ?person rdfs:label ?label .
}');
$elements = $var['results']['bindings'];
$edges = array();
$id= 1 ;
//var_dump($elements);
foreach ($elements as $element) {
	$edge = array(
		'source' => $element['x']['value'],
		'target' =>$element['y']['value'],
		'id' =>	(string) $id
	);
	$id++;
	$edges[] = $edge ;
}
//var_dump($organistations_a);

$nodes = array() ;
$organisations = $organisations_a['results']['bindings'];
//var_dump($organisations_a) ;
//die();
$ids = array();
foreach ($organisations as $organisation) {
	if (!in_array($organisation['organisation']['value'], $ids)) {
	//var_dump($organistation);
	$node = array(
		'label' => $organisation['label']['value'],
		'nb_members' => get_number_of_member_for_node($organisation['organisation']['value'], $edges),
		'nb_is_members' => get_number_for_node_is_member($organisation['organisation']['value'], $edges),
		'type' => 'organisation',
		'id' =>	$organisation['organisation']['value']
	);
	$ids[] = $organisation['organisation']['value'];
	$id++;
	$nodes[] = $node ;
	}
}
//var_dump($personnes_a);
$personnes = $personnes_a['results']['bindings'];
foreach ($personnes as $personne) {
	if (!in_array($personne['person']['value'], $ids)) {
	$node = array(
		'label' => $personne['label']['value'],
		'nb_members' => get_number_of_member_for_node($personne['person']['value'], $edges),
		'nb_is_members' => get_number_for_node_is_member($personne['person']['value'], $edges),
		'type' => 'person',
		'id' =>	$personne['person']['value']
	);
	$id++;
	$ids[] = $personne['person']['value'];
	$nodes[] = $node ;
	}
}
/*
verification de target ou source non incluse dans les nodes
*/
foreach ($elements as $element) {
	if (!in_array($element['x']['value'], $ids)) {
		$node = array(
			'label' => $element['x']['value'],
			'nb_members' => get_number_of_member_for_node($element['x']['value'], $edges),
			'nb_is_members' => get_number_for_node_is_member($element['x']['value'], $edges),
			'type' => 'non-declare',
			'id' =>	$element['x']['value']
		);
		$nodes[] = $node ;
		$id++;
		$ids[] = $element['x']['value'];
	}
	if (!in_array($element['y']['value'], $ids)) {
		$node = array(
			'label' => $element['y']['value'],
			'nb_members' => get_number_of_member_for_node($element['y']['value'], $edges),
			'nb_is_members' => get_number_for_node_is_member($element['y']['value'], $edges),
			'type' => 'non-declare',
			'id' =>	$element['y']['value']
		);
		$nodes[] = $node ;
		$id++;
		$ids[] = $element['y']['value'];
	}
}




$array_global = array(
	'edges' => $edges,
	'nodes' => $nodes

);
$return_json = json_encode($array_global );
$file = json_decode($return_json, false) ;
echo json_encode($file ) ;

//var_dump($var) ;
//var_dump($elements) ;

//$file = json_decode($file) ;
//var_dump($file);
//echo json_encode($file );
?>