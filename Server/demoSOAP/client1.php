<?php
    require_once "lib/nusoap.php";
    
    $client = new nusoap_client("http://localhost:8284/GuestManagementService.php?wsdl", true);

    $error = $client->getError();
    if ($error) {
        echo "<h2>Constructor error</h2><pre>" . $error . "</pre>";
    }

    $result = $client->call("addGuest", array("content" => "addGuest"));

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

