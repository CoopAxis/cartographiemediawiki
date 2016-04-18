<?php

 error_reporting(E_ALL);
ini_set('display_errors', TRUE);
ini_set('display_startup_errors', TRUE);
include('functions.php');

$color_link = array  (
	'normal' => array('#8A0808','#886A08','#4B8A08','#088A29','#088A85','#08298A','#4B088A','#8A0868','#CC2900','#888888','#888888','#888888','#888888'),
	'hilight' => array('#DF0101','#DBA901','#74DF00','#01DF3A','#01DFD7','#01DFD7','#7401DF','#DF01A5','#FF3300','#666666','#666666','#666666','#666666'),
	);
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

# Le DISTINCT est important ! sinon à cause de la clause "?end ?aumoins ?untriplet" les lignes sont doublonnées
SELECT DISTINCT ?start ?link ?end ?labelStart ?labelEnd ?labelLink
WHERE {
  ?start rdfs:label ?labelStart .
  ?start ?link ?end .
  ?end ?aumoins ?untriplet .
  FILTER(
    (
    STRSTARTS(STR(?link), "http://schema.org/")
    ||
    STRSTARTS(STR(?link), "http://smw.coopaxis.fr/id/")
    )
    &&
    ?link != wiki:Attribut-3ADate_de_modification-23aux
    &&
    ?link != schema:url
    &&
    !isLiteral(?end)
  )
  OPTIONAL {
    ?end rdfs:label ?labelEnd .
  }
  OPTIONAL {
    ?link rdfs:label ?labelLink .
  }
}

');
//var_dump($var);
//die();
/*
$organisations_a  = get_sparql_result('PREFIX owl:   <http://www.w3.org/2002/07/owl#>
PREFIX swivt:   <http://semantic-mediawiki.org/swivt/1.0#>
PREFIX wiki:  <http://smw.coopaxis.fr/id/>
PREFIX rdfs:  <http://www.w3.org/2000/01/rdf-schema#>
PREFIX schema:  <http://schema.org/>

SELECT ?organisation ?label
WHERE {
 ?organisation rdf:type schema:Organization . 
 ?organisation rdfs:label ?label .
}');*/
$projets_a  = get_sparql_result('PREFIX owl:   <http://www.w3.org/2002/07/owl#>
PREFIX swivt:   <http://semantic-mediawiki.org/swivt/1.0#>
PREFIX wiki:  <http://smw.coopaxis.fr/id/>
PREFIX rdfs:  <http://www.w3.org/2000/01/rdf-schema#>
PREFIX schema:  <http://schema.org/>

SELECT ?fiche ?label ?type
WHERE {
  ?fiche a ?type .
  FILTER(
    (
    STRSTARTS(STR(?type), "http://schema.org/")
    ||
    STRSTARTS(STR(?type), "http://smw.coopaxis.fr/id/")
    )
  )
  OPTIONAL {
    ?fiche rdfs:label ?label .
  }
}');
//var_dump($var) ;
/*
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
*/


$elements = $var['results']['bindings'];
$edges = array();
$id= 1 ;
$types_edges = array() ;
$types_nodes = array() ;
//var_dump($elements);
foreach ($elements as $element) {
	if (isset($element['labelLink'])) {
		

	

	if (!in_array($element['link']['value'] ,$types_edges)) {
		$types_edges[] = $element['link']['value'] ;
		$label_edges[] = $element['labelLink']['value'] ;
		
	}
	$index_type_edge = 0 ;
	foreach ($types_edges as $type) {
		if ($element['link']['value'] == $type) {
			break ;
		} else {
			$index_type_edge++ ;
		}
	}
	$edge = array(
		'labelStart' => $element['labelStart']['value'],
		'start' => $element['start']['value'],
		'labelEnd' => $element['labelEnd']['value'],
		'end' => $element['end']['value'],
		'link' => $element['link']['value'],
		'link_machine_name' => get_en_url($element['link']['value']),
		'labelLink' => $element['labelLink']['value'],
		'color' => $color_link['normal'][$index_type_edge] ,
		'hilight' => $color_link['hilight'][$index_type_edge] ,
		'id' =>	(string) $id
	);
	
	
		$id++;
		$edges[] = $edge ;
	}

}
//var_dump($organistations_a);
$ids = array();
$nodes = array() ;
/*
$organisations = $organisations_a['results']['bindings'];
//var_dump($organisations_a) ;
//die();

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
$types_nodes[] = 'organisation' ;
*/
//var_dump($personnes_a);

/*
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
$types_nodes[] = 'person' ;
*/
//var_dump($projets_a);
$projets = $projets_a['results']['bindings'];
foreach ($projets as $projet) {
	if (!in_array($projet['fiche']['value'], $ids)) {
	
	$type_url_arr = explode('/', $projet['type']['value']) ;
	$type = $type_url_arr[sizeof($type_url_arr)-1] ;
	$node = array(
		'label' => $projet['label']['value'],
		'nb_members' => get_number_of_member_for_node($projet['fiche']['value'], $edges),
		'nb_is_members' => get_number_for_node_is_member($projet['fiche']['value'], $edges),
		'type' => $type,
		'id' =>	$projet['fiche']['value']
	);
	$id++;
	$ids[] = $projet['fiche']['value'];
	if (!in_array($type, $types_nodes)) {
		$types_nodes[] = $type ;
	}
	$nodes[] = $node ;
	}
}




/*
verification de target ou source non incluse dans les nodes
*/
foreach ($elements as $element) {
	if (!in_array($element['start']['value'], $ids)) {
		$node = array(
			'label' => $element['start']['value'],
			'nb_members' => get_number_of_member_for_node($element['start']['value'], $edges),
			'nb_is_members' => get_number_for_node_is_member($element['start']['value'], $edges),
			'type' => 'non-declare',
			'id' =>	$element['start']['value']
		);
		$nodes[] = $node ;
		$id++;
		$ids[] = $element['start']['value'];
	}
	if (!in_array($element['end']['value'], $ids)) {
		$node = array(
			'label' => $element['end']['value'],
			'nb_members' => get_number_of_member_for_node($element['end']['value'], $edges),
			'nb_is_members' => get_number_for_node_is_member($element['end']['value'], $edges),
			'type' => 'non-declare',
			'id' =>	$element['end']['value']
		);
		$nodes[] = $node ;
		$id++;
		$ids[] = $element['end']['value'];
	}
}
$types_nodes[] = 'non-declare' ;
$i_col = 0;
foreach($types_edges as &$type) {
	
	$ex = explode('/', $type) ;
	$nb_ex = sizeof($ex) -1 ;
	
	$type = array('id' => $type, 
		'machine_name' => $ex[$nb_ex], 
		'name' => $label_edges[$i_col], 
		'nb' => $nb_ex, 
		'color' => $color_link['normal'][$i_col], 
		'color_hilight' => $color_link['hilight'][$i_col] 
	) ;
	
	
	$i_col++;

}


$array_global = array(
	'edges' => $edges,
	'nodes' => $nodes,
	'types_edges' => $types_edges,
	'types_nodes' => $types_nodes

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