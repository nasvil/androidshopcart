<?php
    include("../db.php"); 
    include("../../config.php"); 
    require_once "../lib/nusoap.php";
    
    $complexItem = array('ID' => array('name' => 'ID','type' => 'xsd:string'),
                            'Name' => array('name' => 'Name','type' => 'xsd:string'),
                            'Image' => array('name' => 'Image','type' => 'xsd:string'),
                            'ProductModel' => array('name' => 'ProductModel','type' => 'xsd:string'),
                            'ProductOption' => array('name' => 'ProductOption','type' => 'xsd:string'),
                            'Quantity' => array('name' => 'Quantity','type' => 'xsd:string'),
                            'Unitprice' => array('name' => 'Unitprice','type' => 'xsd:string'),
                            'Total' => array('name' => 'Total','type' => 'xsd:string'));

    function getBasket($para) {
                //Get database and fetch out to main_array
                $db = new DB(DB_HOSTNAME, DB_USERNAME, DB_PASSWORD, DB_DATABASE);
                //Config languageID in webservice
                $main_array = array();
                for($i = 0; $i < 3;$i++){
                $main_array[$i] = array('ID'=>'001',
                                    'Name'=>'CustomerName',
                                    'Image'=>'ImageURL',
                                    'ProductModel'=>'Model',
                                    'ProductOption'=>'OptionString',
                                    'Quantity'=>'quatity',
                                    'ProductModel'=>'Model',
                                    'Unitprice'=>'001',
                                    'Total'=>'total');
                }
                
                return $main_array;
    }

    $server = new soap_server();
    $server->configureWSDL("shoppingcart", "urn:shoppingcart");
    $server->wsdl->addComplexType(  'Item','complexType','struct','all','',$complexItem);
    $server->wsdl->addComplexType(  'ItemList',
                                    'complexType',
                                    'array','','SOAP-ENC:Array',
                                    array(),array(
                                    array('ref'=>'SOAP-ENC:arrayType','wsdl:arrayType'=>'tns:Item[]')),'tns:Item');

    $server->register("getBasket",
    array("shoppingcart" => "xsd:string"),
    array("return" => "tns:ItemList"),
    "urn:shoppingcart",
    "urn:shoppingcart#getBasket",
    "rpc",
    "encoded",
    "Get a listing of products by category");
    if ( !isset( $HTTP_RAW_POST_DATA ) ) $HTTP_RAW_POST_DATA =file_get_contents( 'php://input' );                                      
    $server->service($HTTP_RAW_POST_DATA);
?>
