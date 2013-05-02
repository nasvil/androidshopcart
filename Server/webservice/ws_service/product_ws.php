<?php
    include("../db.php"); 
    include("../../config.php"); 
    require_once "../lib/nusoap.php";
    
    $complexProduct = array('ID' => array('name' => 'ID','type' => 'xsd:string'),
                            'Name' => array('name' => 'Name','type' => 'xsd:string'),
                            'Image' => array('name' => 'Image','type' => 'xsd:string'),
                            'Price' => array('name' => 'Price','type' => 'xsd:string'));
    

    function getProd($categoryID) {
                //Get database and fetch out to main_array
                $db = new DB(DB_HOSTNAME, DB_USERNAME, DB_PASSWORD, DB_DATABASE);
                $configDomain = 'http://flowercardvn.com/image/'; 
                //Config languageID in webservice
                $languageID = 1;
                $categoryID_temp = $categoryID;
                $query = $db->query("   SELECT gp.product_id,name,image,price 
                                        FROM gc_product gp inner join gc_product_to_category gptc on (gp.product_id = gptc.product_id)inner join gc_product_description gpd on(gp.product_id = gpd.product_id) 
                                        WHERE gptc.category_id = ".$categoryID_temp." and language_id = ".$languageID);
                $main_array = array();
                for($i = 0; $i < sizeof($query);$i++){
                    $array = array("ID"=>$query[$i]['product_id'],"Name"=>$query[$i]['name'],"Image"=>$configDomain.$query[$i]['image'],"Price"=>$query[$i]['price']);
                    $main_array[$i] = $array;
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

    $server->register("getProd",
        array("categoryID" => "xsd:string"),
        array("return" => "tns:ProductList"),
        "urn:productlist",
        "urn:productlist#getProd",
        "rpc",
        "encoded",
        "Get a listing of products by category");
if ( !isset( $HTTP_RAW_POST_DATA ) ) $HTTP_RAW_POST_DATA =file_get_contents( 'php://input' );                                      
$server->service($HTTP_RAW_POST_DATA);
?>
