/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.sbapr.utils;

import java.util.ArrayList;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Adam
 */
public class IterableNodeList extends ArrayList<Node>{
        
    public IterableNodeList(NodeList nodeList){
        super();
        for(int i=0;i<nodeList.getLength();i++){
            add(nodeList.item(i));
        }
    }
    
}
