/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kennyloggins.clarence;

import javax.swing.JTextField;

/**
 * 
 * @author Bill
 */
public class NumericTextField extends JTextField {
	private static final long serialVersionUID = 624000422898989225L;

	@Override
    public void replaceSelection(String text) {
        if(isNumeric(text)) 
            super.replaceSelection(text);
    }
    
    public boolean isNumeric(String text) { return text.matches("-?[0-9]*(?:\\.[0-9]*)?"); }
}
