package com.sourcetip.fiestas.fiestaspatronales;

/**
 * Created by willy on 4/05/18.
 */

public class Language
{
    public String code = "";
    public String name = "";

        // A simple constructor for populating our member variables for this tutorial.
    public Language( String _code, String _name )
    {
        code = _code;
        name = _name;
    }

    public String toString()
    {
        return( name + " (" + code + ")" );
    }

    public String getCode()
    {
        return code;
    }
}
