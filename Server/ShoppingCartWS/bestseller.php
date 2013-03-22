<?php
require_once "lib/nusoap.php";
$client = new nusoap_client("http://127.0.0.1:8284/bestseller_ws.php?wsdl", true);

$error = $client->getError();
if ($error) {
    echo "<h2>Constructor error</h2><pre>" . $error . "</pre>";
}

$query_ws = array('content'=>'categoryList','type'=>'X');
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
        echo "<h2>Product List by Category</h2><pre>";
        echo json_encode($result);
        echo "</pre>";
    }
}
?> 