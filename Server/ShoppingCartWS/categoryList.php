<?php
require_once "lib/nusoap.php"; 

$arrayComplexType =  array( 'ID' => array('name' => 'ID','type' => 'xsd:string'),
                                'Name' => array('name' => 'Name','type' => 'xsd:string'));
function getCategoryList($content){

     if ($content == "categoryList") {
            //Connect database and assign to main_array
            $array1 = array("ID"=>"1","Name"=>"Hoa tinh yeu");
            $array2 = array("ID"=>"2","Name"=>"Ngay cua mum");
            //$main_array[0]= $array1;
            //$main_array[1]= $array2;
        return $array1;
    }
    else {
        return "No category is listed";
    }
}
    $server = new soap_server();
    $server->configureWSDL("categoryList", "urn:categoryList");
    $server->wsdl->addComplexType('categoryComplexType','complexType','struct','all','',$arrayComplexType);
    $server->register("getCategoryList",
    array("content" => "xsd:string"),
    array("return" => "tns:categoryComplexType"),
    "urn:categoryList",
    "urn:categoryList#getCategoryList",
    "rpc",
    "encoded",
    "Get category list");
    $server->service($HTTP_RAW_POST_DATA);
    if ( !isset( $HTTP_RAW_POST_DATA ) ) $HTTP_RAW_POST_DATA =file_get_contents( 'php://input' );
?>
