package br.com.biblioteca.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.biblioteca.model.PessoaModel;

@Repository
public interface PessoaRepository extends JpaRepository<PessoaModel, Long> {

    List<PessoaModel> findAllByOrderByIdDesc();

    List<PessoaModel> findByGerenteTrue();

    List<PessoaModel> findByFuncionarioTrue();

    @Query("select p from PessoaModel p where p.gerente = true and p.id in :id")
    Optional<PessoaModel> findByGerenteTrueById(@Param("id") Long id);

    @Query("select p from PessoaModel p where p.funcionario = true and p.id in :ids")
    List<PessoaModel> findByFuncionarioTrueByIds(@Param("ids") List<Long> membroIds);
}
