<?php
require_once "lib/nusoap.php"; 


        $optionDelivery = array('normal' => array('name' => 'normal','type' => 'xsd:string'),
                                'exactlyday' => array('name' => 'exactlyday','type' => 'xsd:string'),
                                'exactlyhour' => array('name' => 'exactlyhour','type' => 'xsd:string'));
        
        $optionGift = array(    'uncheck' => array('name' => 'uncheck','type' => 'xsd:string'),
                                'card' => array('name' => 'card','type' => 'xsd:string'),
                                'bear' => array('name' => 'bear','type' => 'xsd:string'));
                                
        $gift = array(          'name' => array('name' => 'name','type' => 'xsd:string'),
                                'type' => array('name' => 'type','type' => 'xsd:string'),
                                );
                                
        $delivery = array(      'name' => array('name' => 'name','type' => 'xsd:string'),
                                'type' => array('name' => 'type','type' => 'xsd:string'),
                                $optionDelivery);
                                
        $timeDelivery = array(  'name' => array('name' => 'name','type' => 'xsd:string'),
                                'type' => array('name' => 'type','type' => 'xsd:string'));
                                
        $addressAndMessage = array(     'name' => array('name' => 'name','type' => 'xsd:string'),
                                        'type' => array('name' => 'type','type' => 'xsd:string'));
                                        
        $option = array($gift,$delivery,$timeDelivery,$addressAndMessage);


        $detailProduct =  array('ID' => array('name' => 'ID','type' => 'xsd:string'),
                                'name' => array('name' => 'name','type' => 'xsd:string'),
                                'image' => array('name' => 'image','type' => 'xsd:string'),
                                'description' => array('name' => 'description','type' => 'xsd:string'),
                                $option
                            );
                            
        function getDetailProduct($productID){
             if ($productID == 'GD1') {
                    //Connect database and assign to main_array
                    //$array1 = array("ID"=>"1","Name"=>"Hoa tinh yeu");
                    //$array2 = array("ID"=>"2","Name"=>"Ngay cua mum");
                    $oD = array('normal' => '1','exactlyday' => '11/11/2012','exactlyhour' =>'12/11/2012');       
                    $oG = array('uncheck' => '1','card' => '0','bear' =>'0');
                    $g = array('name'=>'quà tặng kèm','type'=>'/image',$oG);
                    $d = array('name'=>'chon cách giao hàng','type'=>'/image',$od);
                    $t = array('name'=>'thời điểm giao hàng','type'=>'9:00 AM');
                    $a = array('name'=>'address','type'=>'I love you so much');
                    $dP = array($oD,$oG,$g,$d,$t,$a,$dP);
                    
                    //$main_array[0]= $array1;
                    //$main_array[1]= $array2;
                return $db;
            }

        }
        $server = new soap_server();
        $server->configureWSDL("detailProduct", "urn:detailProduct");
        $server->wsdl->addComplexType('optionGift','complexType','struct','all','',$optionGift);
        $server->wsdl->addComplexType('optionDelivery','complexType','struct','all','',$optionDelivery);
        $server->wsdl->addComplexType('gift','complexType','struct','all','',$gift);
        $server->wsdl->addComplexType('delivery','complexType','struct','all','',$delivery);
        $server->wsdl->addComplexType('addressAndMessage','complexType','struct','all','',$addressAndMessage);
        $server->wsdl->addComplexType('option','complexType','struct','all','',$option);
        $server->wsdl->addComplexType('detailProduct','complexType','struct','all','',$detailProduct);
        //$server->wsdl->addComplexType(  'CategoryList',
//                                        'complexType',
//                                        'array','','SOAP-ENC:Array',
//                                         array(),array(
//                                         array('ref'=>'SOAP-ENC:arrayType','wsdl:arrayType'=>'tns:Category[]')),'tns:Category');
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
