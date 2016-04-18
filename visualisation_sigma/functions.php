<?php
//Here, change the url of your Sesame Sparql endpoint
define("SPAQLURL", 'http://yourdomaine/endpoint-path') ;

function get_sparql_result($requete) {

	$data_url = http_build_query (array('query' => $requete));

	$opts = array(
	  'http'=>array(
		'method'=>"GET",
		'header'=>"Accept: application/sparql-results+json\r\n"
	  )
	);

	$context = stream_context_create($opts);
	$file = file_get_contents(SPAQLURL.'?'.$data_url, false, $context);
	//echo utf8_decode($file);
	return  json_decode($file, true);
}

function get_number_of_member_for_node($id, $edges) {
	$i = 0;
	foreach ($edges as $value) {
		if ($value['end'] == $id) {
			$i++;
		}
	
	}
	return $i;
}
function get_number_for_node_is_member($id, $edges) {
	$i = 0;
	foreach ($edges as $value) {
		if ($value['start'] == $id) {
			$i++;
		}
	
	}
	return $i;
}
function get_en_url($url) {
	$ex = explode('/', $url) ;
	$nb_ex = sizeof($ex) -1 ;
	return $ex[$nb_ex] ; 
}