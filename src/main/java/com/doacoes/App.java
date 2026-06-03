package com.doacoes;

import com.doacoes.view.EstoqueView;

import javax.swing.SwingUtilities;

public class App {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            EstoqueView tela = new EstoqueView();
            tela.setVisible(true);
        });
    }
}