<?php
    include("../db.php"); 
    include("../../config.php"); 
    require_once "../lib/nusoap.php";
    
    $complexProduct = array('ID' => array('name' => 'ID','type' => 'xsd:string'),
                            'Name' => array('name' => 'Name','type' => 'xsd:string'),
                            'Image' => array('name' => 'Image','type' => 'xsd:string'),
                            'Price' => array('name' => 'Price','type' => 'xsd:string'));
    
    function getBestProd($content) {
                //Get database and fetch out to main_array
                $db = new DB(DB_HOSTNAME, DB_USERNAME, DB_PASSWORD, DB_DATABASE);
                $configDomain = 'http://flowercardvn.com/image/'; 
                //Config languageID in webservice
                $languageID = 1;
                $content_temp = $content;
                $query = $db->query("SELECT value FROM gc_setting WHERE gc_setting.key like '".$content."'");
                
                $main_array = array();
                $list_product_id = array();
                //$array = array("ID"=>$query[$i]['value'],"Name"=>$query[$i]['name'],"Image"=>$query[$i]['image']);
                $group_product_id  = $query[0]['value'];
                $list_product_id = explode(',',$group_product_id);
                for($i=0;$i<sizeof($list_product_id);$i++){
                    $query = $db->query("SELECT gc_product_description.product_id, name, image , price
                                    FROM gc_product_description, gc_product 
                                    WHERE gc_product_description.product_id = gc_product.product_id 
                                    AND gc_product_description.product_id = ".$list_product_id[$i]);
                    $product_info = array(  "ID"=>$query[0]['product_id'],
                                            "Name"=>$query[0]['name'],
                                            "Image"=>$configDomain.$query[0]['image'],
                                            "Price"=>$query[0]['price']);
                    $main_array[$i] = $product_info;
                                            
                }
                
                
            return $main_array;
    }

    $server = new soap_server();
    $server->configureWSDL("productlist", "urn:productlist");
    $server->wsdl->addComplexType('Product','complexType','struct','all','',$complexProduct);
    $server->wsdl->addComplexType(  'ProductList',
                                    'complexType',
                                    'array','','SOAP-ENC:Array',
                                    array(),array(
                                    array('ref'=>'SOAP-ENC:arrayType','wsdl:arrayType'=>'tns:Product[]')),'tns:Product');

    $server->register("getBestProd",
        array("type" => "xsd:string"),
        array("return" => "tns:ProductList"),
        "urn:productlist",
        "urn:productlist#getProd",
        "rpc",
        "encoded",
        "Get a listing of products by category");
    if ( !isset( $HTTP_RAW_POST_DATA ) ) $HTTP_RAW_POST_DATA =file_get_contents( 'php://input' );                                      
    $server->service($HTTP_RAW_POST_DATA);
?>
