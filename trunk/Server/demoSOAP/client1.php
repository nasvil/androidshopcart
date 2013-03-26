<?php
    require_once "lib/nusoap.php";
    
    $client = new nusoap_client("http://localhost:8284/GuestManagementService.php?wsdl", true);

    $error = $client->getError();
    if ($error) {
        echo "<h2>Constructor error</h2><pre>" . $error . "</pre>";
    }

    //$query_ws = array('name'=>'categoryList','address'=>'X','age'=>'6');
    //$result = $client->call("addGuest", array("content" => $query_ws)); 
    //$result = $client->call("deleteGuest", array("content" => "Chuong Nguyen")); 
    $result = $client->call("getGuestDetails", array("name" => "Ta")); 

    if ($client->fault) {
        echo "<h2>Fault</h2><pre>";
        print_r($result);
        echo "</pre>";
    }
    else {
        $error = $client->getError();
        if ($error) {
            echo "<h2>Error</h2><pre>" . $error . "</pre>";
        }
        else {
            echo "<h2>CategoryList</h2><pre>";
            echo json_encode($result);
            echo "</pre>";
        }
    }
?> 

