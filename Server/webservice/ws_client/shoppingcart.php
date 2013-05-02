<?php

$client = new nusoap_client(HOSTNAME."ws_service/shoppingcart_ws.php?wsdl", true);

$error = $client->getError();
if ($error) {
    echo  $error ;
}

$method =  $_GET["method"];
$shoppingcart =  $_GET["customername"];

$result = $client->call($method, array("shoppingcart" => $shoppingcart));

if ($client->fault) {
    echo $result;
}
else {
    $error = $client->getError();
    if ($error) {
        echo $error;
    }
    else {
        echo $_GET['callback']."".json_encode($result)."";
    }
}
?> 

