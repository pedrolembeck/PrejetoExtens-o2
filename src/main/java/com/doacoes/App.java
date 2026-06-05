package com.doacoes;

import com.doacoes.view.EstoqueView;
import com.doacoes.view.MenuView;

import javax.swing.SwingUtilities;

public class App {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MenuView tela = new MenuView();
            tela.setVisible(true);
        });
    }
}