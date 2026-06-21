package com.doacoes.service;

import com.doacoes.dao.UsuarioDAO;
import com.doacoes.model.Usuario;

import javax.swing.JOptionPane;
import java.util.ArrayList;
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

        prepararUsuarioParaSalvar(usuario);

        try {
            dao.salvar(usuario);
            return true;
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Erro ao salvar usuário. Verifique se o login já existe.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
            return false;
        }
    }

    public List<Usuario> listar() {
        return dao.listar();
    }

    public Usuario buscarPorId(Long id) {
        if (id == null) {
            return null;
        }

        return dao.buscarPorId(id);
    }

    public boolean atualizar(Usuario usuario) {
        if (!validar(usuario)) {
            return false;
        }

        prepararUsuarioParaSalvar(usuario);

        try {
            dao.atualizar(usuario);
            return true;
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Erro ao atualizar usuário. Verifique se o login já existe.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
            return false;
        }
    }

    public void deletar(Long id) {
        if (id == null) {
            JOptionPane.showMessageDialog(
                    null,
                    "Usuário inválido para exclusão.",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        dao.deletar(id);
    }

    private void prepararUsuarioParaSalvar(Usuario usuario) {
        usuario.setNome(usuario.getNome().trim().toUpperCase());
        usuario.setLogin(usuario.getLogin().trim());
        usuario.setSenha(usuario.getSenha().trim());
        usuario.setPerfil(usuario.getPerfil().trim().toUpperCase());

        if (usuario.getPermissoes() == null) {
            usuario.setPermissoes(new ArrayList<>());
        }

        if ("ADMIN".equalsIgnoreCase(usuario.getPerfil())) {
            usuario.getPermissoes().clear();
        }
    }

    private boolean validar(Usuario usuario) {
        if (usuario == null) {
            JOptionPane.showMessageDialog(null, "Usuário inválido!");
            return false;
        }

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

        String perfil = usuario.getPerfil().trim().toUpperCase();

        if (!"ADMIN".equals(perfil) && !"USUARIO".equals(perfil)) {
            JOptionPane.showMessageDialog(null, "Perfil inválido!");
            return false;
        }

        return true;
    }
}