package com.doacoes;

import com.doacoes.view.DoacaoView;

import javax.swing.SwingUtilities;

public class App {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DoacaoView tela = new DoacaoView();
            tela.setVisible(true);
        });
    }
}