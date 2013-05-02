<?php
        include("../db.php"); 
        include("../../config.php"); 
        require_once "../lib/nusoap.php";
    
        $detailProduct =  array('idCategory' => array('name' => 'ID','type' => 'xsd:string'),
                                'name' => array('name' => 'name','type' => 'xsd:string'),
                                'price' => array('name' => 'price','type' => 'xsd:string'),
                                'image' => array('name' => 'image','type' => 'xsd:string'),
                                'description' => array('name' => 'description','type' => 'xsd:string'),
                                'shortDescription'=> array('name' => 'shortDescription','type' => 'xsd:string'),
                                'option' => array('name' => 'option','type' => 'xsd:string')
                            );
        
        //quatangkem;image;uncheck$imageUrl$0_card$imageUrl$3_chocolate$imageUrl$5_bear$imageUrl$7&&
        //choncachgiaohang;radio;normal$giaohangthongthuong$5_exactlyday$giaochinhxacngay$10_exactlyhour$giaochinhxacgio$15&&
        //timeDelivery;datetimePicker&&
        //addressAndMsg;textField
        function getDetailProduct($content){
                
                $db = new DB(DB_HOSTNAME, DB_USERNAME, DB_PASSWORD, DB_DATABASE);
                $configDomain = 'http://flowercardvn.com/image/'; 
                $query = $db->query("SELECT gc_product_description.product_id, name, price, description, image 
                                    FROM gc_product_description, gc_product 
                                    WHERE gc_product_description.product_id = gc_product.product_id 
                                    AND gc_product_description.product_id = ".$content.";");
                //config in webservice.php file;
                $optionGift = 13;
                $language = 1;
                //getSupOptionList_Gift
                $supOptionList_Gift= array();
                $query_supOptionList_Gift = $db->query("SELECT Distinct(govd.name),gov.image 
                                                    FROM gc_option_value_description govd,gc_option_value gov 
                                                    WHERE govd.option_id = ".$optionGift." 
                                                    AND govd.language_id = ".$language." 
                                                    AND govd.option_id = gov.option_id 
                                                    AND govd.option_value_id = gov.option_value_id;");
                for($i = 0; $i < sizeof($query_supOptionList_Gift);$i++){
                    $array = array("name"=>$query_supOptionList_Gift[$i]['name'],"image"=>$configDomain.$query_supOptionList_Gift[$i]['image']);
                    $supOptionList_Gift[$i] = $array;
                }    
                //getOption_Gift
                $supOption_Gift = array(); 
                $query_SupOption_Gift = $db->query("SELECT gd.option_id,gd.name,go.type  
                                                    FROM gc_option_description gd,gc_option go 
                                                    WHERE gd.option_id = ".$optionGift." 
                                                    AND gd.language_id = ".$language." and gd.option_id = go.option_id");
                for($i = 0; $i < sizeof($query_SupOption_Gift);$i++){
                    $array = array("name"=>$query_SupOption_Gift[$i]['name'],
                    "type"=>$query_SupOption_Gift[$i]['type'],
                    "option"=>$supOptionList_Gift);
                    $supOption_Gift[$i] = $array;
                }
                //declareOptionShipping;
                $optionShipping = 14;
                //getSupOptionList_ShippingMethod
                $supOptionList_Shipping= array();
                $query_supOptionList_Shipping = $db->query("SELECT Distinct(govd.name),gov.image 
                                                    FROM gc_option_value_description govd,gc_option_value gov 
                                                    WHERE govd.option_id = ".$optionShipping." 
                                                    AND govd.language_id = ".$language." 
                                                    AND govd.option_id = gov.option_id 
                                                    AND govd.option_value_id = gov.option_value_id;");
                for($i = 0; $i < sizeof($query_supOptionList_Shipping);$i++){
                    $array = array("name"=>$query_supOptionList_Shipping[$i]['name'],
                    "image"=>$configDomain.$query_supOptionList_Shipping[$i]['image']);
                    $supOptionList_Shipping[$i] = $array;
                }
                    
                //getOption_ShippingMethod
                $supOption_Shipping = array(); 
                $query_SupOption_Shipping = $db->query("SELECT gd.option_id,gd.name,go.type  
                                                    FROM gc_option_description gd,gc_option go 
                                                    WHERE gd.option_id = ".$optionShipping." 
                                                    AND gd.language_id = ".$language." and gd.option_id = go.option_id");
                for($i = 0; $i < sizeof($query_SupOption_Shipping);$i++){
                    $array = array( "name"=>$query_SupOption_Shipping[$i]['name'],
                                    "type"=>$query_SupOption_Shipping[$i]['type'],
                                    "option"=>$supOptionList_Shipping);
                    $supOption_Shipping[$i] = $array;
                }
                $optionTime = 12;
                //getOption_TimeDelivery
                $supOption_Time = array(); 
                $query_SupOption_Time = $db->query("SELECT gd.option_id,gd.name,go.type  
                                                    FROM gc_option_description gd,gc_option go 
                                                    WHERE gd.option_id = ".$optionTime." 
                                                    AND gd.language_id = ".$language." 
                                                    AND gd.option_id = go.option_id");
                for($i = 0; $i < sizeof($query_SupOption_Time);$i++){
                    $array = array( "name"=>$query_SupOption_Time[$i]['name'],"type"=>$query_SupOption_Time[$i]['type']);
                    $supOption_Time[$i] = $array;
                }
                $optionAddressMessage = 6; 
                //getOption_AddressAndMessage
                $supOption_AddressMessage = array(); 
                $query_SupOption_AddressMessage = $db->query("SELECT gd.option_id,gd.name,go.type  
                                                    FROM gc_option_description gd,gc_option go 
                                                    WHERE gd.option_id = ".$optionAddressMessage." 
                                                    AND gd.language_id = ".$language." 
                                                    AND gd.option_id = go.option_id");
                for($i = 0; $i < sizeof($query_SupOption_AddressMessage);$i++){
                    $array = array("name"=>$query_SupOption_AddressMessage[$i]['name'],
                                    "type"=>$query_SupOption_AddressMessage[$i]['type']);
                    $supOption_AddressMessage[$i] = $array;
                }
                $optionArray = array("Gift"=>$supOption_Gift,
                "ShippingMethod"=>$supOption_Shipping,
                "TimetoDelivery"=>$supOption_Time,"AddressAndMessage"=>$supOption_AddressMessage); 
                //return Option
                
                $array = array("ID"=>$query[0]['product_id'],
                                "name"=>$query[0]['name'],
                                "price"=>$query[0]['price'],
                                "image"=>$configDomain.$query[0]['image'],
                                "description"=>$query[0]['description'],
                                "shortDescription"=>$query[0]['description'],
                                "option"=> json_encode($optionArray));
                return $array;
        }
        $server = new soap_server();
        $server->configureWSDL("detailProduct", "urn:detailProduct");
        $server->wsdl->addComplexType('detailProduct','complexType','struct','all','',$detailProduct);
        
        $server->register("getDetailProduct",
        array("content" => "xsd:string"),
        array("return" => "tns:detailProduct"),
        "urn:detailProduct",
        "urn:detailProduct#getDetailProduct",
        "rpc",
        "encoded",
        "Get detail Product");
        
        if ( !isset( $HTTP_RAW_POST_DATA ) ) $HTTP_RAW_POST_DATA =file_get_contents( 'php://input' ); 
        $server->service($HTTP_RAW_POST_DATA);
    
?>
