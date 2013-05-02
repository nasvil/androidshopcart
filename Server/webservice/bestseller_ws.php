<?php
require_once "lib/nusoap.php";
    
    $complexProduct = array('ID' => array('name' => 'ID','type' => 'xsd:string'),
                            'Name' => array('name' => 'Name','type' => 'xsd:string'),
                            'Image' => array('name' => 'Image','type' => 'xsd:string'),
                            'ShortDes'=> array('name' => 'Name','type' => 'xsd:string'));
    

    function getProd($query_ws) {
        if (($query_ws['content']=='bestseller') && ($query_ws['type']=='X' )) {    
            //Get database and fetch out to main_array
            $array1 = array("ID"=>"1","Name"=>"Hoa Hong","Image"=>"image\image.jpg","ShortDes"=>"Description");
            $array2 = array("ID"=>"2","Name"=>"Hoa Cuc","Image"=>"image\image.jpg","ShortDes"=>"Description");
            $main_array[0] = $array1;
            $main_array[1] = $array2;
            return $main_array;
        }
    }

    $server = new soap_server();
    $server->configureWSDL("productlist", "urn:productlist");
    $server->wsdl->addComplexType(  'Query_ws',
                                    'complexType',
                                    'struct',
                                    'all',
                                    '',
                                    array(
                                    'content' => array('name' => 'content', 'type' => 'xsd:string'),
                                    'type' => array('name' => 'type', 'type' => 'xsd:string')
                                    )
                                    );
    $server->wsdl->addComplexType('Product','complexType','struct','all','',$complexProduct);
    $server->wsdl->addComplexType(  'ProductList',
                                    'complexType',
                                    'array','','SOAP-ENC:Array',
                                    array(),array(
                                    array('ref'=>'SOAP-ENC:arrayType','wsdl:arrayType'=>'tns:Product[]')),'tns:Product');

    $server->register("getProd",
        array("query_ws" => "tns:Query_ws"),
        array("return" => "tns:ProductList"),
        "urn:productlist",
        "urn:productlist#getProd",
        "rpc",
        "encoded",
        "Get a listing of products by category");
if ( !isset( $HTTP_RAW_POST_DATA ) ) $HTTP_RAW_POST_DATA =file_get_contents( 'php://input' );                                      
$server->service($HTTP_RAW_POST_DATA);
?>
