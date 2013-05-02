<?php
    include("../db.php"); 
    include("../../config.php"); 
    require_once "../lib/nusoap.php";

$arrayCategory =  array('ID' => array('name' => 'ID','type' => 'xsd:string'),
                        'Name' => array('name' => 'Name','type' => 'xsd:string'));
                        
    function getCategoryList($content){
         if ($content == 'categoryList') {
                //Connect database and assign to main_array
                $db = new DB(DB_HOSTNAME, DB_USERNAME, DB_PASSWORD, DB_DATABASE);
                //Config languageID in webservice
                $languageID = 1;
                $query = $db->query("SELECT category_id,name FROM gc_category_description WHERE language_id =".$languageID."");
                //$query = $db->query("CALL getCategory();");
                $main_array = array();
                
                for($i = 0; $i < sizeof($query);$i++){
                    $array = array("ID"=>$query[$i]['category_id'],"Name"=>$query[$i]['name']);
                    $main_array[$i] = $array;
                }
            return $main_array;
        }

    }
    $server = new soap_server();
    $server->configureWSDL("categoryList", "urn:categoryList");
    $server->wsdl->addComplexType(  'Category','complexType','struct','all','',$arrayCategory);
    $server->wsdl->addComplexType(  'CategoryList',
                                    'complexType',
                                    'array','','SOAP-ENC:Array',
                                     array(),array(
                                     array('ref'=>'SOAP-ENC:arrayType','wsdl:arrayType'=>'tns:Category[]')),'tns:Category');
    $server->register("getCategoryList",
    array("content" => "xsd:string"),
    array("return" => "tns:CategoryList"),
    "urn:categoryList",
    "urn:categoryList#getCategoryList",
    "rpc",
    "encoded",
    "Get category list");
    
    if ( !isset( $HTTP_RAW_POST_DATA ) ) $HTTP_RAW_POST_DATA =file_get_contents( 'php://input' ); 
    $server->service($HTTP_RAW_POST_DATA);
    
?>
