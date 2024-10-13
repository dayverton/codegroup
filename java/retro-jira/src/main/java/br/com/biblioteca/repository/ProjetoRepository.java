package br.com.biblioteca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.biblioteca.model.ProjetoModel;

@Repository
public interface ProjetoRepository extends JpaRepository<ProjetoModel, Long> {

    List<ProjetoModel> findAllByOrderByIdAsc();
}
