<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html lang="en">

<head>
  <meta charset="UTF-8" />
  <title>Gerenciamento de Projetos</title>

  <link href="../../static/css/bootstrap.min.css" rel="stylesheet" />

  <script src="../../static/js/jquery-3.3.1.min.js"></script>
  <script src="../../static/js/bootstrap.min.js"></script>
  <script src="../../static/js/index.js"></script>
</head>

<body>
  <div class="container">
    <c:if test="${not empty erroMensagem}">
      <div class="alert alert-danger">${erroMensagem}</div>
    </c:if>

    <h1 class="my-4">Gerenciamento de Projetos</h1>

    <button type="button" class="btn btn-primary btn-novo" data-toggle="modal" data-target="#projetoModal">
      Novo Projeto
    </button>

    <table class="table mt-3">
      <thead>
        <tr>
          <th>ID</th>
          <th>Nome</th>
          <th>Data de Início</th>
          <th>Gerente</th>
          <th>Status</th>
          <th>Ações</th>
        </tr>
      </thead>
      <tbody>
        <%@ include file="fragments/projetos.jsp" %>
      </tbody>
    </table>
  </div>

  <%@ include file="fragments/modal.jsp" %>
</body>

</html>