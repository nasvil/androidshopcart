<?php
class DB {
	private $driver;
	private $con;
	public function __construct($hostname, $username, $password, $database) {
        $this->con = mysql_connect($hostname,$username,$password);
        mysql_selectdb($database,$this->con);
        mysql_query("SET NAMES 'utf8'", $this->con);
        mysql_query("SET CHARACTER SET utf8", $this->con);
        mysql_query("SET CHARACTER_SET_CONNECTION=utf8", $this->con);
        mysql_query("SET SQL_MODE = ''", $this->con);
	}
		
  	public function query($sql) {
		$resource = mysql_query($sql, $this->con);
        if ($resource) {
            if (is_resource($resource)) {
                $i = 0;
        
                $data = array();
        
                while ($result = mysql_fetch_assoc($resource)) {
                    $data[$i] = $result;
        
                    $i++;
                }
                return $data;                
            } else {
                return true;
            } 
        } else {
            trigger_error('Error: ' . mysql_error($this->con) . '<br />Error No: ' . mysql_errno($this->con) . '<br />' . $sql);
        }   
  	}
	
	public function escape($value) {
        return mysql_real_escape_string($value, $this->con);
	}
	
  	public function countAffected() {
        return mysql_affected_rows($this->con);
  	}

  	public function getLastId() {
        return mysql_insert_id($this->con);  
  	}	
    public function destruct() {
        mysql_close($this->con);
    }
}
?>