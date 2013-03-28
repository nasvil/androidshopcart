<?php
require_once "lib/nusoap.php";
    
    $complexReservationDetail = array('res_id' => array('name' => 'res_id','type' => 'xsd:int'),
                            'guest_name' => array('name' => 'guest_name','type' => 'xsd:string'),
                            'room_no'=> array('name' => 'room_no','type' => 'xsd:int'),
                            'reserved_from' => array('name' => 'reserved_from','type' => 'xsd:string'),
                            'reserved_to' => array('name' => 'reserved_to','type' => 'xsd:string'));
                            
    function addReservation($content){
  
          $link = mysql_connect('localhost','root','vertrigo') or die('Cannot connect to the DB');
          mysql_select_db('hotel_reservation_db',$link) or die('Cannot select the DB');
          $sql="INSERT INTO RESERVATION_T(res_id, guest_name, room_no,reserved_from,reserved_to)VALUES(".$content['res_id'].",'".$content['guest_name']."',".$content['room_no'].",'".$content['reserved_from']."','".$content['reserved_to']."')";
          $result=mysql_query($sql); 
          if($result){
                return "Insert Reservation successfully";
          }
          else {
                return "Can not insert";
          }
          mysql_close(); 
    }
    
    function getReservationDetails($roomNo) {
          $link = mysql_connect('localhost','root','vertrigo') or die('Cannot connect to the DB');
          mysql_select_db('hotel_reservation_db',$link) or die('Cannot select the DB');
          $sql= "SELECT * FROM RESERVATION_T WHERE room_no = ".$roomNo. ""; 
          $result=mysql_query($sql);
          $row = mysql_fetch_row($result);
          
          return array( "res_id"=>$row[0],
                        "guest_name"=>$row[1],
                        "room_no"=>$row[2],
                        "reserved_from"=>$row[3],
                        "reserved_to"=>$row[4]);
          
          mysql_close(); 
            
    }
    
    function removeReservation($reservationID){
            $link = mysql_connect('localhost','root','vertrigo') or die('Cannot connect to the DB');
            mysql_select_db('hotel_reservation_db',$link) or die('Cannot select the DB');
            $sql="DELETE FROM RESERVATION_T WHERE res_id = ".$reservationID."";
            $result=mysql_query($sql); 
            if($result){
                return "Delete Reservation successfully";
            }
            else {
                return "The Reservation is not existed";
            }
            mysql_close();     
    }
    $server = new soap_server();
    $server->configureWSDL("reservationService", "urn:reservationService");
    $server->wsdl->addComplexType('Reservation','complexType','struct','all','',$complexReservationDetail);
    
    //addReservation
    $server->register("addReservation",
        array("content" => "tns:Reservation"),
        array("return" => "xsd:string"),
        "urn:reservationService",
        "urn:reservationService#addGuest",
        "rpc",
        "encoded",
        "Insert Guest to database");
    //removeReservation
    $server->register("removeReservation",
        array("reservationID" => "xsd:int"),
        array("return" => "xsd:string"),
        "urn:reservationService",
        "urn:reservationService#deleteGuest",
        "rpc",
        "encoded",
        "Delete Reservation from database");
    //getReservationDetails
    $server->register("getReservationDetails",
        array("roomNo" => "xsd:int"),
        array("return" => "tns:Reservation"),
        "urn:reservationService",
        "urn:reservationService#getGuestDetails",
        "rpc",
        "encoded",
        "Get detail Reservation from database");
        
    if ( !isset( $HTTP_RAW_POST_DATA ) ) $HTTP_RAW_POST_DATA =file_get_contents( 'php://input' );                                      
    $server->service($HTTP_RAW_POST_DATA);
?>
