package com.doacoes.service;

import com.doacoes.dao.UsuarioDAO;
import com.doacoes.model.Usuario;

import javax.swing.JOptionPane;
import java.util.List;

public class UsuarioService {

    private final UsuarioDAO dao = new UsuarioDAO();

    public void inicializar() {
        dao.criarTabela();
    }

    public Usuario autenticar(String login, String senha) {
        if (login == null || login.isBlank()) {
            JOptionPane.showMessageDialog(null, "Informe o login!");
            return null;
        }

        if (senha == null || senha.isBlank()) {
            JOptionPane.showMessageDialog(null, "Informe a senha!");
            return null;
        }

        Usuario usuario = dao.buscarPorLoginESenha(login.trim(), senha.trim());

        if (usuario == null) {
            JOptionPane.showMessageDialog(null, "Usuário ou senha inválidos!");
            return null;
        }

        return usuario;
    }

    public boolean salvar(Usuario usuario) {
        if (!validar(usuario)) {
            return false;
        }

        usuario.setNome(usuario.getNome().trim().toUpperCase());
        usuario.setLogin(usuario.getLogin().trim());
        usuario.setSenha(usuario.getSenha().trim());
        usuario.setPerfil(usuario.getPerfil().trim().toUpperCase());

        try {
            dao.salvar(usuario);
            return true;
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(null, "Erro ao salvar usuário. Verifique se o login já existe.");
            return false;
        }
    }

    public List<Usuario> listar() {
        return dao.listar();
    }

    public Usuario buscarPorId(Long id) {
        return dao.buscarPorId(id);
    }

    public boolean atualizar(Usuario usuario) {
        if (!validar(usuario)) {
            return false;
        }

        usuario.setNome(usuario.getNome().trim().toUpperCase());
        usuario.setLogin(usuario.getLogin().trim());
        usuario.setSenha(usuario.getSenha().trim());
        usuario.setPerfil(usuario.getPerfil().trim().toUpperCase());

        try {
            dao.atualizar(usuario);
            return true;
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(null, "Erro ao atualizar usuário. Verifique se o login já existe.");
            return false;
        }
    }

    public void deletar(Long id) {
        dao.deletar(id);
    }

    private boolean validar(Usuario usuario) {
        if (usuario.getNome() == null || usuario.getNome().isBlank()) {
            JOptionPane.showMessageDialog(null, "Nome é obrigatório!");
            return false;
        }

        if (usuario.getLogin() == null || usuario.getLogin().isBlank()) {
            JOptionPane.showMessageDialog(null, "Login é obrigatório!");
            return false;
        }

        if (usuario.getSenha() == null || usuario.getSenha().isBlank()) {
            JOptionPane.showMessageDialog(null, "Senha é obrigatória!");
            return false;
        }

        if (usuario.getPerfil() == null || usuario.getPerfil().isBlank()) {
            JOptionPane.showMessageDialog(null, "Perfil é obrigatório!");
            return false;
        }

        return true;
    }
}