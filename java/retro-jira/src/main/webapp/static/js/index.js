(function ($) {
  $(document).ready(function () {
    console.log(jQuery);

    function carregarSelectGerentes() {
      $.ajax({
        url: "/projetos/gerentes/buscar",
        method: "GET",
        success: function (gerentes) {
          carregarSelectGerentesConteudo(gerentes);
        },
        error: function (xhr) {
          console.error("Erro ao buscar gerentes:", xhr.responseText);
        },
      });
    }

    function carregarSelectGerentesConteudo(gerentes) {
      const gerenteSelect = $("#gerente");
      // limpa o dropdown
      gerenteSelect.empty();

      gerenteSelect.append('<option value="" selected>SELECIONE</option>');
      gerentes.forEach(function (gerente) {
        gerenteSelect.append(
          `<option value=" ${gerente.id} "> ${gerente.nome} </option>`
        );
      });
    }

    function carregarSelectFuncionarios() {
      $.ajax({
        url: "/projetos/funcionarios/buscar",
        method: "GET",
        success: function (funcionarios) {
          carregarSelectFuncionariosConteudo(funcionarios);
        },
        error: function (xhr) {
          console.error("Erro ao buscar funcionários:", xhr.responseText);
        },
      });
    }

    function carregarSelectFuncionariosConteudo(funcionarios) {
      const funcionarioSelect = $("#membros");
      // limpa o dropdown
      funcionarioSelect.empty();

      funcionarios.forEach(function (funcionario) {
        funcionarioSelect.append(
          `<option value=" ${funcionario.id} "> ${funcionario.nome} </option>`
        );
      });
    }

    $(".btn-novo").click(function () {
      $("#projetoModalLabel").text("Novo Projeto");
      $("#projetoModal").find("form").trigger("reset");

      carregarSelectGerentes();
      carregarSelectFuncionarios();
    });
  });
})(jQuery);
