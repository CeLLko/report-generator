/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.sbapr.exceptions;

import java.io.IOException;

/**
 *
 * @author Adam
 */
public class GeneratorException extends IOException{
    public GeneratorException(String msg){
        super(msg);
    }
}
