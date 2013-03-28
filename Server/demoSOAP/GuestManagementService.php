<?php
require_once "lib/nusoap.php";
    
    $complexGuessDetail = array('name' => array('name' => 'name','type' => 'xsd:string'),
                            'address' => array('name' => 'address','type' => 'xsd:string'),
                            'age'=> array('name' => 'age','type' => 'xsd:int'));
    function addGuest($content){
  
          $link = mysql_connect('localhost','root','vertrigo') or die('Cannot connect to the DB');
          mysql_select_db('hotel_reservation_db',$link) or die('Cannot select the DB');
          $sql="INSERT INTO guest_t(name, address, age)VALUES('".$content['name']."','".$content['address']."',".$content['age'].")";
          $result=mysql_query($sql); 
          if($result){
                return "Insert Guess successfully";
          }
          else {
                return "The guest is existed in database";
          }
          mysql_close(); 
    }
    
    function getGuestDetails($name) {
          $link = mysql_connect('localhost','root','vertrigo') or die('Cannot connect to the DB');
          mysql_select_db('hotel_reservation_db',$link) or die('Cannot select the DB');
          $sql= "SELECT * FROM GUEST_T WHERE name = '".$name. "'"; 
          $result=mysql_query($sql);
          $row = mysql_fetch_row($result);
          
          return array( "name"=>$row[0],
                        "address"=>$row[1],
                        "age"=>$row[2]);
          
          mysql_close(); 
            
    }
    function deleteGuest($name){
            $link = mysql_connect('localhost','root','vertrigo') or die('Cannot connect to the DB');
            mysql_select_db('hotel_reservation_db',$link) or die('Cannot select the DB');
            $sql="DELETE FROM GUEST_T WHERE name = '".$name."'";
            $result=mysql_query($sql); 
            if($result){
                return "Delete Guest successfully";
            }
            else {
                return "The guest is existed in database";
            }
            mysql_close();     
    }
    $server = new soap_server();
    $server->configureWSDL("guestManagement", "urn:guestManagement");
    $server->wsdl->addComplexType('Guess','complexType','struct','all','',$complexGuessDetail);
    
    //Insert Guest
    $server->register("addGuest",
        array("content" => "tns:Guess"),
        array("return" => "xsd:string"),
        "urn:guestManagement",
        "urn:guestManagement#addGuest",
        "rpc",
        "encoded",
        "Insert Guest to database");
    //DeleteGuest
    $server->register("deleteGuest",
        array("guestName" => "xsd:string"),
        array("return" => "xsd:string"),
        "urn:guestManagement",
        "urn:guestManagement#deleteGuest",
        "rpc",
        "encoded",
        "Delete Guest from database");
    //getGuestDetails
    $server->register("getGuestDetails",
        array("guestName" => "xsd:string"),
        array("return" => "tns:Guess"),
        "urn:guestManagement",
        "urn:guestManagement#getGuestDetails",
        "rpc",
        "encoded",
        "Get detail guest from database");
        
    if ( !isset( $HTTP_RAW_POST_DATA ) ) $HTTP_RAW_POST_DATA =file_get_contents( 'php://input' );                                      
    $server->service($HTTP_RAW_POST_DATA);
?>
