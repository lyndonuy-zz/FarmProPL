/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author Nyker
 */
public class Attributes {
    public String id_type;      // Values: program_name or variable
    public String data_type;    // Values: bool, dec, int, string
    public String value;        // Values: [bool_const], [dec_const], [int_const], [string_const]
    
    public Attributes(String a) {
        this.id_type = "program_name";
    }
    public Attributes(String a, String b, String c) {
        this.id_type = "variable";
        this.data_type = b;
        this.value = c;
    }
    
    public String getIDType() {
        return id_type;
    }
    public String getDataType() {
        return data_type;
    }
    public String getValue() {
        return value;
    }
}
