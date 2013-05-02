<?php
$client = new nusoap_client(HOSTNAME."ws_service/detailProduct_ws.php?wsdl", true); 

$error = $client->getError();
if ($error) {
    echo $error;
}

$result = $client->call("getDetailProduct", array("content" =>  $_GET["productID"]));

if ($client->fault) {
    echo $result;
}
else {
    $error = $client->getError();
    if ($error) {
        echo $error;
    }
    else {
        $option = $result["option"];
        $result["option"] = json_decode($option);
        echo $_GET['callback']."".escapeJsonString(json_encode($result)."");
    }
}
?> 