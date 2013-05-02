<?php
 $client = new nusoap_client(HOSTNAME."ws_service/product_ws.php?wsdl", true);

$error = $client->getError();
if ($error) {
    echo $error ;
}

$result = $client->call("getProd", array("categoryID" => $_GET["categoryID"]));

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