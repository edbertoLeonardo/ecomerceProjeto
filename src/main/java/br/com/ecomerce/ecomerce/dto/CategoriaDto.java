package br.com.ecomerce.ecomerce.dto;


import br.com.ecomerce.ecomerce.model.Categoria;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;

public class CategoriaDto {

    private Integer id;
    @NotEmpty(message = "Preenchimento obrigatório")
    @Length(min = 3, max = 100, message = "O tamanho deve ser enre 3 e 100 caracteres")
    private String nome;

    public CategoriaDto() {

    }

    public CategoriaDto(Categoria categoria) {
        id = categoria.getId();
        nome = categoria.getNome();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
