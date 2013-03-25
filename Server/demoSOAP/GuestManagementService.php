<?php
require_once "lib/nusoap.php";
    
//    $complexGuessDetail = array('name' => array('name' => 'name','type' => 'xsd:string'),
//                            'address' => array('name' => 'address','type' => 'xsd:string'),
//                            'age'=> array('name' => 'age','type' => 'xsd:int'));
    function addGuest($content){
        if($content=='addGuest'){
          $link = mysql_connect('localhost','root','vertrigo') or die('Cannot connect to the DB');
          mysql_select_db('hotel_reservation_db',$link) or die('Cannot select the DB');
          $sql="INSERT INTO guest_t(name, address, age)VALUES('Ti','TP HCM',24)";
          $result=mysql_query($sql); 
          if($result){
                return "Insert Guess successfully";
          }
          else {
                return "The guest is existed in database";
          }
          mysql_close(); 
        }
    }
    
    function getGuestDetails($name) {

    }
    function deleteGuest($name){
        
    }
    $server = new soap_server();
    $server->configureWSDL("guestManagement", "urn:guestManagement");
    //$server->wsdl->addComplexType('Guess','complexType','struct','all','',$complexProduct);
    $server->register("addGuest",
        array("content" => "xsd:string"),
        array("return" => "xsd:string"),
        "urn:productlist",
        "urn:productlist#getProd",
        "rpc",
        "encoded",
        "Get a listing of products by category");
        
    if ( !isset( $HTTP_RAW_POST_DATA ) ) $HTTP_RAW_POST_DATA =file_get_contents( 'php://input' );                                      
    $server->service($HTTP_RAW_POST_DATA);
?>
