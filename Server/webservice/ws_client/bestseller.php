<?php
$client = new nusoap_client(HOSTNAME."ws_service/bestseller_ws.php?wsdl", true);

$error = $client->getError();
if ($error) {
    echo "<h2>Constructor error</h2><pre>" . $error . "</pre>";
}
$type = $_GET["type"];
$query_ws = array('content'=>'bestseller','type'=>$type);
$result = $client->call("getProd", array("query_ws" => $query_ws));

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
        echo "<h2>Best seller </h2><pre>";
        echo json_encode($result);
        echo "</pre>";
    }
}
?> 