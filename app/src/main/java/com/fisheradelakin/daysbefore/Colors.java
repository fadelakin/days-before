package com.fisheradelakin.daysbefore;

import android.graphics.Color;

import java.util.Random;

/**
 * Created by temidayo on 3/30/16.
 *
 * most colors from flatuicolors.com and other custom colors
 */
public class Colors {

    public String[] mColors = {
            "#1abc9c", // turquoise
            "#2ecc71", // emerald
            "#3498db", // peter river
            "#9b59b6", // amethyst
            "#34495e", // wet asphalt
            "#16a085", // green sea
            "#27ae60", // nephritis
            "#2980b9", // belize hole
            "#8e44ad", // wisteria
            "#2c3e50", // midnight blue
            "#e67e22", // carrot
            "#e74c3c", // alizarin
            "#f39c12", // orange
            "#d35400", // pumpkin
            "#c0392b", // pomegranate
            "#39add1", // light blue
            "#3079ab", // dark blue
            "#e15258", // red
            "#838cc7", // lavender
            "#7d669e", // purple
            "#51b46d", // green
            "#ee6677",
            "#9955aa",
            "#cc0033",
            "#3399aa",
            "#cc2255",
            "#883388"
    };

    // Randomly select a color
    static Random rand = new Random();

    public int getColor() {
        return Color.parseColor(mColors[rand.nextInt(mColors.length)]);
    }
}
