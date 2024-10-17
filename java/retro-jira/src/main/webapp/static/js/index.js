(function ($) {
  $(document).ready(function () {
    console.log(jQuery);

    associarBotoes();

    function associarBotoes() {
      $(".btn-editar").click(function () {
        let projetoId = $(this).data("id");
        abrirModalEditar(projetoId);
      });

      $(".btn-excluir").click(function () {
        let projetoId = $(this).data("id");
        excluirProjeto(projetoId);
      });
    }

    function abrirModalEditar(projetoId) {
      console.log("ID do projeto:", projetoId);

      // espera para carregar
      Promise.all([carregarSelectGerentes(), carregarSelectFuncionarios()])
        .then(function () {
          carregarModalEditar(projetoId);
        })
        .catch(function (error) {
          console.error("Erro ao carregar gerentes ou funcionários:", error);
        });
    }

    function mapearIdMembros(membros) {
      const membroIds = membros.map(function (membro) {
        return membro; //.id;
      });

      return membroIds;
    }

    function carregarModalEditar(projetoId) {
      $.ajax({
        url: "/projetos/buscar/" + projetoId,
        type: "GET",
        success: function (response) {
          console.log("Dados do projeto:", response);

          $("#id").val(response.id);
          $("#nome").val(response.nome);
          $("#dataInicio").val(response.dataInicio);
          $("#gerente").val(response.gerente.id).change();
          $("#status").val(response.status);
          $("#risco").val(response.risco);
          $("#dataPrevisaoFim").val(response.dataPrevisaoFim);
          $("#dataFim").val(response.dataFim);
          $("#orcamento").val(response.orcamento);
          $("#descricao").val(response.descricao);

          const membroIds = mapearIdMembros(response.membros);
          console.log(membroIds);
          $("#membros").val(membroIds).change();

          $("#projetoModalLabel").text("Editar Projeto");
          $("#projetoModal").modal("show");
        },
        error: function () {
          console.error("Erro ao buscar projeto");
        },
      });
    }

    function excluirProjeto(projetoId) {
      if (confirm("Tem certeza de que deseja excluir este projeto?")) {
        $.ajax({
          type: "POST",
          url: "/projetos/excluir/" + projetoId,
          data: { _method: "DELETE" },
          success: function (response) {
            console.log("Projeto excluído com sucesso:", response);
            // recarrega a página
            window.location.href = "/projetos";
          },
          error: function (xhr, status, error) {
            console.error("Erro ao excluir o projeto:", xhr.responseText);
            console.log(error);
          },
        });
      }
    }

    function carregarSelectGerentes() {
      return new Promise(function (resolve, reject) {
        $.ajax({
          url: "/api/v1/pessoas/gerentes/buscar",
          method: "GET",
          success: function (gerentes) { // NOSONAR
            carregarSelectGerentesConteudo(gerentes.data);
            resolve();
          },
          error: function (xhr, status, error) { // NOSONAR
            console.error("Erro ao buscar gerentes:", xhr.responseText);
            console.log(error);
            reject(error);
          },
        });
      });
    }

    function carregarSelectGerentesConteudo(gerentes) {
      const gerenteSelect = $("#gerente");
      gerenteSelect.empty();
      gerenteSelect.append('<option value="" selected>SELECIONE</option>');

      gerentes.forEach(function (gerente) {
        gerenteSelect.append(
          `<option value="${gerente.id}">${gerente.nome}</option>`
        );
      });
    }

    function carregarSelectFuncionarios() {
      return new Promise(function (resolve, reject) {
        $.ajax({
          url: "/api/v1/pessoas/funcionarios/buscar",
          method: "GET",
          success: function (funcionarios) { // NOSONAR
            carregarSelectFuncionariosConteudo(funcionarios.data);
            resolve();
          },
          error: function (xhr, status, error) { // NOSONAR
            console.error("Erro ao buscar funcionários:", xhr.responseText);
            console.log(error);
            reject(error);
          },
        });
      });
    }

    function carregarSelectFuncionariosConteudo(funcionarios) {
      const funcionarioSelect = $("#membros");
      funcionarioSelect.empty();

      funcionarios.forEach(function (funcionario) {
        funcionarioSelect.append(
          `<option value="${funcionario.id}">${funcionario.nome}</option>`
        );
      });
    }

    $(".btn-novo").click(function () {
      $("#projetoModalLabel").text("Novo Projeto");
      // reset form
      $("#projetoModal").find("form").trigger("reset");
      $("#projetoModal").find("form").find("#id").val(undefined);

      carregarSelectGerentes();
      carregarSelectFuncionarios();
    });

    $("#botaoPesquisar").click(function () {
      let nomeProjeto = $("#pesquisaNome").val();
      $.ajax({
        type: "GET",
        url: "/projetos/pesquisar",
        data: { nome: nomeProjeto },
        success: function (response) {
          console.log(response);
          // atualiza o conteúdo da tabela
          $("tbody").html(response);

          associarBotoes();
        },
        error: function (xhr, status, error) {
          console.error("Erro ao buscar projetos:", error);
        },
      });
    });
  });
})(jQuery);
