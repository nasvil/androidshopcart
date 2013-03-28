<?php
require_once "lib/nusoap.php";
    
   $complexrRoomDetail = array('room_number' => array('name' => 'room_number','type' => 'xsd:int'),
                            'room_type' => array('name' => 'room_type','type' => 'xsd:string'),
                            'room_size'=> array('name' => 'room_size','type' => 'xsd:string'));
    function addRoom($content){
  
          $link = mysql_connect('localhost','root','vertrigo') or die('Cannot connect to the DB');
          mysql_select_db('hotel_reservation_db',$link) or die('Cannot select the DB');
          $sql="INSERT INTO ROOM_T(room_number, room_type, room_size)VALUES(".$content['room_number'].",'".$content['room_type']."','".$content['room_size']."')";
          $result=mysql_query($sql); 
          if($result){
                return "Insert Room successfully";
          }
          else {
                return "The Room is existed in database";
          }
          mysql_close(); 
    }
    
    function getRoomDetails($name) {
          $link = mysql_connect('localhost','root','vertrigo') or die('Cannot connect to the DB');
          mysql_select_db('hotel_reservation_db',$link) or die('Cannot select the DB');
          $sql= "SELECT * FROM ROOM_T WHERE room_number = ".$name. ""; 
          $result=mysql_query($sql);
          $row = mysql_fetch_row($result);
          
          return array( "room_number"=>$row[0],
                        "room_type"=>$row[1],
                        "room_size"=>$row[2]);
          
          mysql_close(); 
            
    }
    function deleteRoom($name){
            $link = mysql_connect('localhost','root','vertrigo') or die('Cannot connect to the DB');
            mysql_select_db('hotel_reservation_db',$link) or die('Cannot select the DB');
            $sql="DELETE FROM ROOM_T WHERE room_number = ".$name."";
            $result=mysql_query($sql); 
            if($result){
                return "Delete Room successfully";
            }
            else {
                return "The guest is existed in database";
            }
            mysql_close();     
    }
    $server = new soap_server();
    $server->configureWSDL("roomManagement", "urn:roomManagement");
    $server->wsdl->addComplexType('Room','complexType','struct','all','',$complexrRoomDetail);
    
    //Insert Room
    $server->register("addRoom",
        array("content" => "tns:Room"),
        array("return" => "xsd:string"),
        "urn:roomManagement",
        "urn:roomManagement#addRoom",
        "rpc",
        "encoded",
        "Insert Room to database");
    //deleteRoom
    $server->register("deleteRoom",
        array("roomNo" => "xsd:int"),
        array("return" => "xsd:string"),
        "urn:roomManagement",
        "urn:roomManagement#deleteRoom",
        "rpc",
        "encoded",
        "Delete Room from database");
    //getRoomDetails
    $server->register("getRoomDetails",
        array("roomNo" => "xsd:int"),
        array("return" => "tns:Room"),
        "urn:roomManagement",
        "urn:roomManagement#getRoomDetails",
        "rpc",
        "encoded",
        "Get detail room from database");
        
    if ( !isset( $HTTP_RAW_POST_DATA ) ) $HTTP_RAW_POST_DATA =file_get_contents( 'php://input' );                                      
    $server->service($HTTP_RAW_POST_DATA);
?>
