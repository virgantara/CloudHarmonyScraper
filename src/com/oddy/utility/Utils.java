/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oddy.utility;

import com.oddy.skyline.SkylineObject;

/**
 *
 * @author ASUS
 */
public class Utils {

    public static boolean isDominate(SkylineObject p, SkylineObject q) {

        double x1 = p.getCost();
        double y1 = p.getAvailability();
        double x2 = q.getCost();
        double y2 = q.getAvailability();

        return x1 <= x2 && y1 >= y2;

    }
}
