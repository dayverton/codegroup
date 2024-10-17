<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="modal fade" id="projetoModal" tabindex="-1" aria-labelledby="projetoModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <form action="/projetos/salvar" method="post">
        <div class="modal-header">
          <h5 class="modal-title" id="projetoModalLabel">.</h5>
          <button type="button" class="close" data-dismiss="modal" aria-label="Close">
            <span aria-hidden="true">&times;</span>
          </button>
        </div>
        <div class="modal-body">
          <input type="hidden" id="id" name="id" value="${projeto.id}" />

          <div class="form-group">
            <label for="nome">Nome do Projeto</label>
            <input type="text" class="form-control" id="nome" name="nome" value="${projeto.nome}" required />
          </div>
          <div class="form-group">
            <label for="dataInicio">Data de Início</label>
            <input type="date" class="form-control" id="dataInicio" name="dataInicio" value="${projeto.dataInicio}"
              required />
          </div>
          <div class="form-group">
            <label for="gerente">Gerente</label>
            <select class="form-control" id="gerente" name="gerente" required></select>
          </div>
          <div class="form-group">
            <label for="status">Status</label>
            <select class="form-control" id="status" name="status" required>
              <option value="" selected>SELECIONE</option>
              <c:forEach var="statusOption" items="${statusOptions}">
                <option value="${statusOption}">${statusOption}</option>
              </c:forEach>
            </select>
          </div>
          <div class="form-group">
            <label for="risco">Risco</label>
            <select class="form-control" id="risco" name="risco" required>
              <option value="" selected>SELECIONE</option>
              <c:forEach var="riscoOption" items="${riscoOptions}">
                <option value="${riscoOption}">${riscoOption}</option>
              </c:forEach>
            </select>
          </div>
          <div class="form-group">
            <label for="dataPrevisaoFim">Previsão de Término</label>
            <input type="date" class="form-control" id="dataPrevisaoFim" name="dataPrevisaoFim"
              value="${projeto.dataPrevisaoFim}" />
          </div>
          <div class="form-group">
            <label for="dataFim">Data Real de Término</label>
            <input type="date" class="form-control" id="dataFim" name="dataFim" value="${projeto.dataFim}" />
          </div>
          <div class="form-group">
            <label for="orcamento">Orçamento Total</label>
            <input type="number" step="0.01" class="form-control" id="orcamento" name="orcamento"
              value="${projeto.orcamento}" required />
          </div>
          <div class="form-group">
            <label for="descricao">Descrição</label>
            <textarea class="form-control" id="descricao" name="descricao" rows="3">
              ${projeto.descricao}
            </textarea>
          </div>
          <div class="form-group">
            <label for="membros">Funcionários (Membros)</label>
            <select class="form-control" id="membros" name="membros" multiple required></select>
            <small class="form-text text-muted">
              Segure Ctrl para selecionar vários funcionários.
            </small>
          </div>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" data-dismiss="modal">
            Cancelar
          </button>
          <button type="submit" class="btn btn-primary">Salvar</button>
        </div>
      </form>
    </div>
  </div>
</div>