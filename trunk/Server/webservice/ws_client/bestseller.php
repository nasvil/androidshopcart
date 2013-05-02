<?php
$client = new nusoap_client(HOSTNAME."ws_service/bestseller_ws.php?wsdl", true);
$error = $client->getError();
if ($error) {
    echo $error;
}
$type = $_GET["type"];
$result = $client->call("getBestProd", array("type" => $type));

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