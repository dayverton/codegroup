<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:forEach var="projeto" items="${projetos}">
  <tr>
    <td>${projeto.id}</td>
    <td>${projeto.nome}</td>
    <td>${projeto.dataInicio}</td>
    <td>${projeto.gerente.nome}</td>
    <td>${projeto.status}</td>
    <td>
      <c:if test="${projeto.status != 'INICIADO' && projeto.status != 'EM_ANDAMENTO' && projeto.status != 'ENCERRADO'}">
        Excluir
      </c:if>
    </td>
  </tr>
</c:forEach>