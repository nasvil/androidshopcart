<?php
    $tbl_name="guest_t";
    $link = mysql_connect('localhost','root','vertrigo') or die('Cannot connect to the DB');
    mysql_select_db('hotel_reservation_db',$link) or die('Cannot select the DB');
    $sql="INSERT INTO guest_t(name, address, age)VALUES('Teo','TP HCM ',1)";
    $result=mysql_query($sql);
    if($result){
    echo "Successful";
    echo "<BR>";
    //echo "<a href='insert.php'>Back to main page</a>";
    }

    else {
    echo "ERROR";
    }
?>

<?php
    // close connection
    mysql_close();
?>

