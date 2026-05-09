package com.doacoes;

import com.doacoes.dao.TipoDoacaoDAO;
import com.doacoes.model.TipoDoacao;

public class App {

    public static void main(String[] args) {

        TipoDoacaoDAO dao = new TipoDoacaoDAO();

        dao.criarTabela();

        dao.salvar(new TipoDoacao("Roupas"));
        dao.salvar(new TipoDoacao("Alimentos"));
        dao.salvar(new TipoDoacao("Brinquedos"));

        System.out.println("\nLISTANDO TIPOS:\n");

        dao.listar().forEach(System.out::println);
    }
}