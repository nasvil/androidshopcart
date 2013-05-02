<?php
    $client = new nusoap_client(HOSTNAME."ws_service/category_ws.php?wsdl", true);

    $error = $client->getError();
    if ($error) {
        echo $error;
    }

    $result = $client->call("getCategoryList", array("content" => "categoryList"));
    if ($client->fault) {
        echo $result;

    }
    else {
        $error = $client->getError();
        if ($error) {
            echo $error;
        }
        else {
            echo $_GET['callback'].json_encode(escapeJsonString($result));
        }
    }
?> 