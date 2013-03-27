<?php
    include("../db.php"); 
    include("../../config.php"); 
    require_once "../lib/nusoap.php";
        
        $optionItem = array('option' => array('name' => 'option','type' => 'xsd:string'));
        $detailProduct =  array('ID' => array('name' => 'ID','type' => 'xsd:string'),
                                'name' => array('name' => 'name','type' => 'xsd:string'),
                                'image' => array('name' => 'image','type' => 'xsd:string'),
                                'description' => array('name' => 'description','type' => 'xsd:string'),
                                $option
                            );
        //quatangkem;image;uncheck$imageUrl$0_card$imageUrl$3_chocolate$imageUrl$5_bear$imageUrl$7&&
//        choncachgiaohang;radio;normal$giaohangthongthuong$5_exactlyday$giaochinhxacngay$10_exactlyhour$giaochinhxacgio$15&&
//        timeDelivery;datetimePicker&&
//        addressAndMsg;textField
        function getDetailProduct($productID){
             if ($productID == 'GD1') {
                    //Connect database and assign to main_array
                    //$array1 = array("ID"=>"1","Name"=>"Hoa tinh yeu");
                    //$array2 = array("ID"=>"2","Name"=>"Ngay cua mum");
                    $oD = array('normal' => '1','exactlyday' => '11/11/2012','exactlyhour' =>'12/11/2012');       
                    $oG = array('uncheck' => '1','card' => '0','bear' =>'0');
                    $g = array('name'=>'qu? t??ng k?m','type'=>'/image',$oG);
                    $d = array('name'=>'chon c?ch giao h?ng','type'=>'/image',$od);
                    $t = array('name'=>'th??i ?i??m giao h?ng','type'=>'9:00 AM');
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
