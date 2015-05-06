/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.guisehn.teclado;

import com.guisehn.db.PalavrasProvider;
import java.awt.AWTException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

/**
 *
 * @author guilhermesehn
 */
public class Captura implements NativeKeyListener {
    
    private Keyboard keyboard;
    private boolean ligado = false;
    private String tema;
    private final Deque<Character> letrasDigitadas;
    private final List<String> palavrasBuscadas;
    
    public Captura() {
        this.letrasDigitadas = new ArrayDeque<>();
        this.palavrasBuscadas = new ArrayList<>();
        
        try {
            keyboard = new Keyboard();
        } catch (AWTException ex) {
            Logger.getLogger(Captura.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void setTema(String tema) {
        this.tema = tema;
    }
    
    public void desligar() {
        ligado = false;
        letrasDigitadas.clear();
    }
    
    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == NativeKeyEvent.VK_PERIOD) {
            if (!ligado) {
                ligado = true;
            } else {
                ligado = false;
                
                for (int i = 0, len = letrasDigitadas.size() + 2; i < len; i++) {
                    keyboard.backspace();
                }
                
                while (!letrasDigitadas.isEmpty()) {
                    char caractere = letrasDigitadas.pop();
                    String palavra = PalavrasProvider.buscarPalavraAdedanha(tema, caractere,
                            palavrasBuscadas);
                    
                    if (palavra != null && !palavra.isEmpty()) {
                        keyboard.type(palavra.toLowerCase());
                        keyboard.type(' ');
                        palavrasBuscadas.add(palavra);
                    }
                }
                
                palavrasBuscadas.clear();
            }
        } else if (ligado) {
            if (keyCode == NativeKeyEvent.VK_BACK_SPACE) {
                if (letrasDigitadas.isEmpty()) {
                    ligado = false;
                } else {
                    letrasDigitadas.removeLast();
                }
            } else {
                char caractere = getCharacter(keyCode);
                if (caractere != 0) {
                    letrasDigitadas.add(caractere);
                }
            }
        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent nke) {
        
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent nke) {
        
    }
    
    public char getCharacter(int keyCode) {
        switch (keyCode) {
            case NativeKeyEvent.VK_A: return 'a';
            case NativeKeyEvent.VK_B: return 'b';
            case NativeKeyEvent.VK_C: return 'c';
            case NativeKeyEvent.VK_D: return 'd';
            case NativeKeyEvent.VK_E: return 'e';
            case NativeKeyEvent.VK_F: return 'f';
            case NativeKeyEvent.VK_G: return 'g';
            case NativeKeyEvent.VK_H: return 'h';
            case NativeKeyEvent.VK_I: return 'i';
            case NativeKeyEvent.VK_J: return 'j';
            case NativeKeyEvent.VK_K: return 'k';
            case NativeKeyEvent.VK_L: return 'l';
            case NativeKeyEvent.VK_M: return 'm';
            case NativeKeyEvent.VK_N: return 'n';
            case NativeKeyEvent.VK_O: return 'o';
            case NativeKeyEvent.VK_P: return 'p';
            case NativeKeyEvent.VK_Q: return 'q';
            case NativeKeyEvent.VK_R: return 'r';
            case NativeKeyEvent.VK_S: return 's';
            case NativeKeyEvent.VK_T: return 't';
            case NativeKeyEvent.VK_U: return 'u';
            case NativeKeyEvent.VK_V: return 'v';
            case NativeKeyEvent.VK_W: return 'w';
            case NativeKeyEvent.VK_X: return 'x';
            case NativeKeyEvent.VK_Y: return 'y';
            case NativeKeyEvent.VK_Z: return 'z';
            //case NativeKeyEvent.VK_DEAD_CEDILLA: return 'ç';
        }
        
        return 0;
    }
    
}
