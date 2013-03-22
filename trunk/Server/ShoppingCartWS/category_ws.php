<?php
require_once "lib/nusoap.php"; 

$arrayCategory =  array('ID' => array('name' => 'ID','type' => 'xsd:string'),
                        'Name' => array('name' => 'Name','type' => 'xsd:string'));
                        
    function getCategoryList($content){
         if ($content == 'categoryList') {
                //Connect database and assign to main_array
                $array1 = array("ID"=>"1","Name"=>"Hoa tinh yeu");
                $array2 = array("ID"=>"2","Name"=>"Ngay cua mum");
                $main_array[0]= $array1;
                $main_array[1]= $array2;
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
