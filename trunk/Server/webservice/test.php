<?php
    include("db.php");
    include("../config.php");
    include("lib/encryption.php");
    function getOptionValue($resource){
        $query = $db->query("SELECT cart FROM gc_customer");
        $decryption = new Encryption("65cd2235dcbfc045a4a65fd476bdab70");
        
        
        
    }
    $db = new DB(DB_HOSTNAME, DB_USERNAME, DB_PASSWORD, DB_DATABASE);
    //$query = $db->query("CALL getProduct(270);");
    
     a:2:{s:56:"273:YToyOntpOjUwMjtzOjM6IjUwMCI7aTo1MDQ7czo0OiJ0dHJyIjt9";i:1;
          s:76:"270:YTozOntpOjQyNTtzOjM6IjM2MSI7aTo0MjY7czozOiIzNjUiO2k6NDI4O3M6MToiQSI7fQ==";i:1;}    //$decryption = new Encryption("65cd2235dcbfc045a4a65fd476bdab70");
//    $temp = $decryption->decrypt("YToyOntpOjUwMjtzOjM6IjUwMCI7aTo1MDQ7czo0OiJ0dHJyIjt9");
     //use this function to decrypt option value info
    $optionValue  = getOptionValue($query[$cart]);
    $temp = unserialize(base64_decode($optionValue));
    echo "<pre>";
    //this is the result after decrypt
    //guy, wwhere query return 
    
    var_dump($temp);
    echo "<pre>";
?>
