/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.sbapr.utils;

import cz.muni.fi.sbapr.Slide;
import java.util.ArrayList;

/**
 *
 * @author Adam
 */
public class SlideArrayList extends ArrayList<Slide> {

    /**
     *
     * @param from
     * @param to
     */
    public void swap(int from, int to) {
        Slide fromObj = super.get(from);
        Slide toObj = super.get(to);
        super.set(to, fromObj);
        super.set(from, toObj);
    }

    @Override
    public Slide set(int index, Slide element) {
        Slide slide = (Slide) get(index);
        slide.getElement().getParentNode().replaceChild(((Slide) element).getElement(), slide.getElement());
        return super.set(index, element);
    }

    @Override
    public boolean add(Slide element) {
        boolean res = super.add(element);
        int row = super.indexOf(element);
        if (res) {
            RGHelper.INSTANCE.getNodeListByName("slides").get(0).appendChild(((Slide) element).getElement());
        }
        return res;
    }

    @Override
    public boolean remove(Object element) {
        int row = super.indexOf(element);
        boolean res = super.remove(element);
        if (res) {
            RGHelper.INSTANCE.getNodeListByName("slides").get(0).removeChild(((Slide) element).getElement());
        }
        return res;
    }

    @Override
    public void clear() {
        super.clear();
        new IterableNodeList(RGHelper.INSTANCE.getNodeListByName("slides").get(0).getChildNodes()).forEach(node -> node.getParentNode().removeChild(node));
    }
}
