package br.com.biblioteca.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import br.com.biblioteca.enums.RiscoEnum;
import br.com.biblioteca.enums.StatusEnum;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "projeto")
public class ProjetoModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private LocalDate dataInicio;
    private LocalDate dataPrevisaoFim;
    private LocalDate dataFim;
    private String descricao;
    private float orcamento;

    @Enumerated(EnumType.STRING)
    private RiscoEnum risco;

    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    @ManyToOne
    @JoinColumn(name = "idgerente", referencedColumnName = "id")
    private PessoaModel gerente;

    @ManyToMany
    @JoinTable(name = "MEMBROS", joinColumns = @JoinColumn(name = "projeto_id"), inverseJoinColumns = @JoinColumn(name = "pessoa_id"))
    private Set<PessoaModel> membros = new HashSet<>();
}
