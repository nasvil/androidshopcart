<?php
    include("../config.php");
    include("db.php");
    include("const/controllerDefinition.php");
    include("lib/ultility.php");
    require_once "lib/nusoap.php";
    
    $controller = $_GET["controller"];
    switch($controller){
        case BEST_SELLER:
            include(BEST_SELLER_FILE);
            break;
        case PRODUCT_DETAIL:
            include(PRODUCT_DETAIL_FILE);
            break;    
        case CATEGORY:
            include(CATEGORY_FILE);
            break;
        case PRODUCT_BY_CATEGORY:
            include(PRODUCT_BY_CATEGORY_FILE);
            break;
        case SHOPPINGCART:
            include(SHOPPINGCART_FILE);
            break;
    }
?>
