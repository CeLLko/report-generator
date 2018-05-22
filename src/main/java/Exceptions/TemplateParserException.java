/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Exceptions;

import java.io.IOException;

/**
 *
 * @author Adam
 */
public class TemplateParserException extends IOException{
    public TemplateParserException(String msg){
        super(msg);
    }
}
